package com.anseong.authorization.processor;

public interface AuthenticationContext {
     long userId();
     String token();
     String message();
     String accessTokenForClient();
     String refreshTokenForClient();
}
