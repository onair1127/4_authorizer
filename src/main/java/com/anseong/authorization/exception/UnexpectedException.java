package com.anseong.authorization.exception;

import com.anseong.authorization.security.auth.exceptions.AuthorizationFailedException;

public class UnexpectedException extends AuthorizationFailedException {

     public UnexpectedException(String message, Throwable cause) {
          super("예상치 못한 오류 : " + message, cause);
     }
}
