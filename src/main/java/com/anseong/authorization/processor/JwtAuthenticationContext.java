package com.anseong.authorization.processor;

public record JwtAuthenticationContext(
     long userId,
     String token,
     String message,
     String accessTokenForClient,
     String refreshTokenForClient
) implements AuthenticationContext {

     public static Builder builder() {
          return new Builder();
     }

     public static class Builder {
          private long userId;
          private String token;
          private String message;
          private String accessTokenForClient;
          private String refreshTokenForClient;

          public Builder userId(long userId) {
               this.userId = userId;
               return this;
          }

          public Builder token(String token) {
               this.token = token;
               return this;
          }

          public Builder message(String message) {
               this.message = message;
               return this;
          }

          public Builder accessTokenForClient(String accessTokenForClient) {
               this.accessTokenForClient = accessTokenForClient;
               return this;
          }

          public Builder refreshTokenForClient(String refreshTokenForClient) {
               this.refreshTokenForClient = refreshTokenForClient;
               return this;
          }

          public JwtAuthenticationContext build() {
               return new JwtAuthenticationContext(userId, token, message, accessTokenForClient, refreshTokenForClient);
          }
     }
}
