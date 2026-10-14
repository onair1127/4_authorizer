package com.anseong.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent.RequestContext.Http;
import com.anseong.authorization.processor.Authentication;
import com.anseong.authorization.processor.AuthenticationProcessor;
import com.anseong.authorization.processor.AuthenticationResult;
import com.anseong.authorization.processor.Credential;
import com.anseong.authorization.processor.HttpRequestCredential;
import com.anseong.authorization.web.result.AuthorizationResult;

public class AuthorizationHandler implements RequestHandler<APIGatewayV2HTTPEvent, AuthorizationResult> {

     private static final Logger log = LoggerFactory.getLogger(AuthorizationHandler.class);
     private static final Authentication authentication = AuthenticationProcessor.JWT();

     @Override
     public AuthorizationResult handleRequest(APIGatewayV2HTTPEvent event, Context context) {

          /**
           * 인증 프로세스 시작.
           */
          final Http request = event.getRequestContext().getHttp();
          final Credential credential = new HttpRequestCredential(
               event.getHeaders(),
               event.getCookies()
          );

          final AuthenticationResult authenticationResult = authentication.process(credential);

          /**
           * 인증 프로세스 결과물에 대한 로깅(?).
           */
          if (authenticationResult.isAuthenticated()) {
               log.info("{}\t | 인증 성공. [ID: {}, message: {}]",
                    request.getSourceIp(),
                    authenticationResult.context().userId(),
                    authenticationResult.context().message()
               );

          } else {
               log.warn("{}\t | 인증 실패. [{}]",
                    request.getSourceIp(),
                    authenticationResult.context().message()
               );
          }

          return AuthorizationResult.builder()
                    .isAuthorized(authenticationResult.isAuthenticated())
                    .context(authenticationResult.context())
                    .build();
     }
}
