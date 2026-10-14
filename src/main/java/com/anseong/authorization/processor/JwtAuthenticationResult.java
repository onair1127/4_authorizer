package com.anseong.authorization.processor;

public record JwtAuthenticationResult(
     boolean isAuthenticated,
     JwtAuthenticationContext context
) implements AuthenticationResult {

     public static Builder builder() {
          return new Builder();
     }

     public static class Builder {
          private boolean isAuthenticated;
          private JwtAuthenticationContext context;

          public Builder isAuthenticated(boolean isAuthenticated) {
               this.isAuthenticated = isAuthenticated;
               return this;
          }

          public Builder context(JwtAuthenticationContext context) {
               this.context = context;
               return this;
          }

          public JwtAuthenticationResult build() {
               return new JwtAuthenticationResult(isAuthenticated, context);
          }
     }
}
