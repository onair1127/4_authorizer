package com.anseong.authorization.processor;

import com.anseong.authorization.processor.impl.JwtAuthentication;

public class AuthenticationProcessor {
     public static Authentication JWT() {
          return JwtAuthentication.INSTANCE;
     }
}
