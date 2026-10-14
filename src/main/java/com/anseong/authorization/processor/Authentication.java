package com.anseong.authorization.processor;

public interface Authentication {
     AuthenticationResult process(Credential credential);
}
