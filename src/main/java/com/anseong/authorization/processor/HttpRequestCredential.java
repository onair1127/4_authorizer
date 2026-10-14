package com.anseong.authorization.processor;

import java.lang.annotation.Native;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anseong.authorization.exception.AuthorizationNotValidException;

public record HttpRequestCredential(
     @Native Map<String, String> headers,
     List<String> cookies
) implements Credential {

     public HttpRequestCredential {
          // if (headers == null) throw new AuthorizationNotValidException("요청 헤더값이 null입니다.");
          // if (cookies == null) throw new AuthorizationNotValidException("요청 쿠키값이 null입니다.");
     }

     @Override
     public Map<String, Object> getAttributes() {
          Map<String, Object> attributes = new HashMap<>();

          attributes.put("headers", headers);
          attributes.put("cookies", cookies);

          return Collections.unmodifiableMap(attributes);
     }
}
