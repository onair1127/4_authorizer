package com.anseong.authorization;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public record User(
     long userId,
     UUID ceoId,
     String username,
     String nickname,
     String password,
     int birth,
     int imageSort,
     boolean temporary,
     boolean notRegisteredCard,
     boolean adult,
     boolean storeCreated,
     String profile,
     String logo,
     String auth

) implements Claimable {

     public static User getDefault() {
          return new User(
               0,
               null,
               "__GUEST__",
               "__GUEST__",
               "password", 9999,
               3,
               false,
               false,
               false,
               false,
               null,
               null,
               "[ROLE_USER]"
          );
     }

     @Override
     public String getSubject() {
          return username;
     }

     public Set<String> getAuthorities() {
          return Collections.singleton(ceoId != null ? "ROLE_CEO" : "ROLE_USER");
     }
}
