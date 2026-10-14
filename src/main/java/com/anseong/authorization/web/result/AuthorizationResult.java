package com.anseong.authorization.web.result;

import com.anseong.authorization.processor.AuthenticationContext;

public record AuthorizationResult(
     boolean isAuthorized,
     AuthenticationContext context
) {
     public static Builder builder() {
          return new Builder();
     }

     public static class Builder {
          private boolean isAuthorized;
          private AuthenticationContext context;

          public Builder isAuthorized(boolean isAuthorized) {
               this.isAuthorized = isAuthorized;
               return this;
          }

          public Builder context(AuthenticationContext context) {
               this.context = context;
               return this;
          }

          public AuthorizationResult build() {
               return new AuthorizationResult(isAuthorized, context);
          }
     }
}
