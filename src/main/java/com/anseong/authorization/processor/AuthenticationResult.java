package com.anseong.authorization.processor;

public interface AuthenticationResult {
     boolean isAuthenticated();
     AuthenticationContext context();
}
