package com.anseong.authorization;

import com.anseong.authorization.processor.JwtAuthenticationContext;

public record AuthorizationContext(
     JwtAuthenticationContext jwtAuthenticationContext
) {
     // public static Builder builder() {
     //      return new Builder();
     // }

     // public static class Builder {
     //      private long id;
     //      private String accessToken;
     //      private String refreshToken;
     //      private String accessTokenForClient;
     //      private String refreshTokenForClient;

     //      public Builder id(long id) {
     //           this.id = id;
     //           return this;
     //      }

     //      public Builder accessToken(String accessToken) {
     //           this.accessToken = accessToken;
     //           return this;
     //      }

     //      public Builder refreshToken(String refreshToken) {
     //           this.refreshToken = refreshToken;
     //           return this;
     //      }

     //      public Builder accessTokenForClient(String accessTokenForClient) {
     //           this.accessTokenForClient = accessTokenForClient;
     //           return this;
     //      }

     //      public Builder refreshTokenForClient(String refreshTokenForClient) {
     //           this.refreshTokenForClient = refreshTokenForClient;
     //           return this;
     //      }

     //      public AuthorizationContext build() {
     //           return new AuthorizationContext(id, accessToken, refreshToken, accessTokenForClient, refreshTokenForClient);
     //      }
     // }
}
