package com.anseong.authorization.exception;

import com.anseong.authorization.security.auth.exceptions.AuthorizationFailedException;

public class UserNotFoundException extends AuthorizationFailedException {

     public UserNotFoundException(String message, Throwable cause) {
          super(message, cause);
     }
}
