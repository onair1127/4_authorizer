package com.anseong.authorization.exception;

import com.anseong.authorization.security.auth.exceptions.AuthorizationFailedException;

public class AuthorizationNotValidException extends AuthorizationFailedException {

     public AuthorizationNotValidException(String message, Throwable cause) {
          super(message, cause);
     }
}
