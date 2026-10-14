package com.anseong.authorization.processor;

import java.util.Map;
import java.util.Optional;


public interface Credential {
     Map<String, Object> getAttributes();

     // default <T> Optional<T> get(String key, Class<T> type) {
     //      Object value = getAttributes().get(key);

     //      // return type.isInstance(value)
     //      //      ? Optional.of(type.cast(value))
     //      //      : Optional.empty();

     //      return Optional.ofNullable(value)
     //           .filter(type::isInstance)
     //           .map(type::cast);
     // }
}
