package com.anseong.authorization.exception;

import com.anseong.authorization.security.auth.exceptions.AuthorizationFailedException;

public class ClaimsMismatchException extends AuthorizationFailedException {

     public ClaimsMismatchException(String message) {
          super(message);
     }

     public ClaimsMismatchException(String message, Throwable cause) {
          super(message, cause);
     }
}
