package com.anseong.authorization.processor.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.anseong.authorization.User;
import com.anseong.authorization.exception.ClaimsMismatchException;
import com.anseong.authorization.processor.Authentication;
import com.anseong.authorization.processor.AuthenticationResult;
import com.anseong.authorization.processor.Credential;
import com.anseong.authorization.processor.HttpRequestCredential;
import com.anseong.authorization.processor.JwtAuthenticationContext;
import com.anseong.authorization.processor.JwtAuthenticationResult;
import com.anseong.authorization.security.auth.exceptions.AuthorizationFailedException;
import com.anseong.authorization.security.auth.token.Token;
import com.anseong.authorization.security.auth.token.TokenIssuer;
import com.anseong.authorization.security.auth.token.TokenVerifier;
import com.anseong.authorization.security.auth.token.provider.TokenIssuers;
import com.anseong.authorization.security.auth.token.provider.TokenVerifiers;
import com.anseong.authorization.security.auth.token.result.TokenClaimsResult;
import com.anseong.authorization.security.auth.token.result.TokenIssueResult;
import com.anseong.authorization.security.cipher.Decryptor;
import com.anseong.authorization.security.cipher.Encryptor;
import com.anseong.authorization.security.cipher.provider.Decryptors;
import com.anseong.authorization.security.cipher.provider.Encryptors;

import jakarta.servlet.http.Cookie;

public enum JwtAuthentication implements Authentication {

     INSTANCE;

     private final Encryptor cipherEncryptor = Encryptors.AES256();
     private final Decryptor cipherDecryptor = Decryptors.AES256();
     private final TokenIssuer tokenIssuer = TokenIssuers.JWT();
     private final TokenVerifier tokenResolver = TokenVerifiers.JWT();

     @Override
	public AuthenticationResult process(Credential credential) {

          /**
           * 인증 단계 1 : 블랙리스트 확인 및 복호화
           */
          try {
               Token token = Token.of(credential);

               if (token.getAccessToken().equals("refresh")) {
                    TokenIssueResult issueResult = tokenIssuer.reissue(
                         cipherDecryptor.decrypt(token.getRefreshToken()),
                         User.class
                    );


                    String encryptedAccessToken = cipherEncryptor.encrypt(issueResult.accessToken());
                    String encryptedRefreshToken = cipherEncryptor.encrypt(issueResult.refreshToken());
                    long expirationRefreshToken = issueResult.expirationDateMillisRefreshToken();

                    String accessTokenForClient = encryptedAccessToken;
                    String refreshTokenForClient = generateCookieValue(encryptedRefreshToken, expirationRefreshToken);

                    return JwtAuthenticationResult.builder()
                         .isAuthenticated(true)
                         .context(JwtAuthenticationContext.builder()
                              .userId(0)
                              .token(issueResult.accessToken())
                              .message(issueResult.message())
                              .accessTokenForClient(accessTokenForClient)
                              .refreshTokenForClient(refreshTokenForClient)
                              .build()
                         ).build();

               } else {
                    token.setAccessToken(cipherDecryptor.decrypt(token.getAccessToken()));
                    token.setRefreshToken(cipherDecryptor.decrypt(token.getRefreshToken()));

                    return JwtAuthenticationResult.builder()
                         .isAuthenticated(true)
                         .context(setContext(token))
                         .build();
               }

          } catch (Exception e) {
               String errorMessage = e instanceof AuthorizationFailedException
                    ? e.getMessage()
                    : "알 수 없는 에러 : " + e.getMessage();

               return JwtAuthenticationResult.builder()
                    .isAuthenticated(false)
                    .context(JwtAuthenticationContext.builder()
                         .message(errorMessage)
                         .build()
                    ).build();
          }
	}

     private JwtAuthenticationContext setContext(Token token) throws Exception {
          TokenClaimsResult<User> claims = tokenResolver.resolveClaims(token, User.class);
          TokenIssueResult reissueResult = claims.reissueResult();

          if (isClaimsMismatch(claims)) {
               throw new ClaimsMismatchException("Access Token과 Refresh Token의 ID값 불일치. [" + claims.accessTokenClaims().userId() + " : " + claims.refreshTokenClaims().userId() + "]");
          }

          if (isTokenWillExpired(claims)) {
               reissueResult = tokenIssuer.reissue(
                    token.getRefreshToken(),
                    User.class
               );
               token.setAccessToken(reissueResult.accessToken());
          }

          String accessTokenForClient;
          String refreshTokenForClient;

          if (reissueResult != null) {
               String encryptedAccessToken = cipherEncryptor.encrypt(reissueResult.accessToken());
               String encryptedRefreshToken = cipherEncryptor.encrypt(reissueResult.refreshToken());
               long expirationRefreshToken = reissueResult.expirationDateMillisRefreshToken();

               accessTokenForClient = encryptedAccessToken;
               refreshTokenForClient = generateCookieValue(encryptedRefreshToken, expirationRefreshToken);

          } else {
               accessTokenForClient = null;
               refreshTokenForClient = null;
          }

          return JwtAuthenticationContext.builder()
               .userId(claims.accessTokenClaims().userId())
               .message(claims.message())
               .token(token.getAccessToken())
               .accessTokenForClient(accessTokenForClient)
               .refreshTokenForClient(refreshTokenForClient)
               .build();
     }

     private boolean isClaimsMismatch(TokenClaimsResult<User> claims) {
          long accessTokenId = claims.accessTokenClaims().userId();
          long refreshTokenId = claims.refreshTokenClaims().userId();

          return accessTokenId != refreshTokenId;
     }

     private boolean isTokenWillExpired(TokenClaimsResult<User> claims) {
          long expirationTimeAccessToken = claims.expirationTimeAccessToken();
          long expirationTimeRefreshToken = claims.expirationTimeRefreshToken();
          long currentTimeMillis = System.currentTimeMillis();

          return (expirationTimeAccessToken - currentTimeMillis) <= Duration.ofMinutes(1).toMillis()
               || (expirationTimeRefreshToken - currentTimeMillis) <= Duration.ofDays(30).toMillis();
     }

     private String generateCookieValue(String refreshToken, long expirationTimeMillis) {
          Instant instant = Instant.ofEpochMilli(expirationTimeMillis);
          ZonedDateTime dateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("GMT"));
          DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
          Cookie cookie = new Cookie("_rt", refreshToken);

          cookie.setDomain(".anseongapp.com");
          cookie.setHttpOnly(true);
          cookie.setAttribute("SameSite", "Strict");
          cookie.setSecure(true);
          cookie.setPath("/api");

          return new StringBuilder()
                    .append(cookie.getName()).append("=").append(cookie.getValue())
                    .append("; Path=").append(cookie.getPath())
                    .append("; Domain=").append(cookie.getDomain())
                    .append("; Max-Age=").append((expirationTimeMillis - System.currentTimeMillis()) / 1000)
                    .append("; Expires=").append(dateTime.format(formatter))
                    .append("; SameSite=").append(cookie.getAttribute("SameSite"))
                    .append("; Secure; HttpOnly")
                    .toString();
     }
}
