package com.anseong.authorization;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent.RequestContext;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent.RequestContext.Http;
import com.anseong.authorization.web.result.AuthorizationResult;
import com.fasterxml.jackson.databind.ObjectMapper;

class AuthorizationApplicationTests {

     private final AuthorizationHandler handler = new AuthorizationHandler();
     private APIGatewayV2HTTPEvent event;
     private RequestContext requestContext;
     private Http http;

     /**
      * 테스트 사전 준비.
      */
     public AuthorizationApplicationTests() {
          this.http = Http.builder()
               .withMethod("GET")
               .withPath("/api/test")
               .withSourceIp("1.234.567.8")
               .build();

          this.requestContext = RequestContext.builder()
               .withHttp(http)
               .build();

          this.event = APIGatewayV2HTTPEvent.builder()
               .withRequestContext(this.requestContext)
               .build();
     }

	@Test
     @DisplayName("1. JWT 토큰이 아무것도 없을 경우.")
	public void sex() {
          Map<String, String> headers = Map.of();
          List<String> cookies = List.of();

          event.setHeaders(headers);
          event.setCookies(cookies);

          handler.handleRequest(event, null);
     }

     @Test
     @DisplayName("2. Access Token이 refresh고 Refresh Token이 없는 경우(이 경우는 앱 최초 설치자이거나 1년만에 접속한 경우이다.)")
     public void fuck() {
          Map<String, String> headers = Map.of("authorization", "refresh");
          List<String> cookies = List.of("");

          event.setHeaders(headers);
          event.setCookies(cookies);

          handler.handleRequest(event, null);
     }

     @Test
     @DisplayName("3. Access Token이 refresh고 Refresh Token이 있는 경우(이 경우는 1년 이내 사용자가 앱을 접속한 경우이다.)")
     public void dildo() throws Exception {
          Map<String, String> headers = Map.of("authorization", "UPKaOusXI5pNbirJDswIkBgp889JCHiIwcZzAykDEATJ5VcRMVIYcyAwV7OdB2iEV1ss49XuoLG8VzYD6gBwbZpemnxLqaA4TxfdhYifjq/rmmd4z0fibPxMEWsaz1BdCiyc9SlnfTAwq6qYyJz0ZhR+jARjFKnesaAKtzpoppdTuTcakwVYIq/AVltc8jWM9JWgljJkr5pY90HJKJPqMGmx7uQWoqxRvI9Yt6kyOf6jvYdUBD5bmeoPNsr5//5JMZMlJiVWojZIofJ3MxZQHEQjd5d4y4Q/mCrgWnNIUCMSdJtov4X8jIHT2rmVcUCXEQcbmA0LliZBgH3xofnyH2wMOLRgNcZ+xP2O4afpxLeK9E4mA1iigv0suqxkW+WAyK9gbDv67ovTDeS6gPBn3CD6njSb4ZTIYyWj6oR9dQO4w69i25TIyqvlHkjtsvaL10cZ0UDrqpkAUruB/RO6N8W6YcTfwoejZUpli/aReTAJGvih9pHKJMDMbELgFLxzJfKlU94oYsKFaerImfu6ERhrcGZ0m8yezXrahC/rEGQ=");
          List<String> cookies = List.of("_rt=UPKaOusXI5pNbirJDswIkBgp889JCHiIwcZzAykDEATJ5VcRMVIYcyAwV7OdB2iEV1ss49XuoLG8VzYD6gBwbZpemnxLqaA4TxfdhYifjq/rmmd4z0fibPxMEWsaz1BdCiyc9SlnfTAwq6qYyJz0ZhR+jARjFKnesaAKtzpoppdTuTcakwVYIq/AVltc8jWM9JWgljJkr5pY90HJKJPqMGmx7uQWoqxRvI9Yt6kyOf6jvYdUBD5bmeoPNsr5//5JMZMlJiVWojZIofJ3MxZQHEQjd5d4y4Q/mCrgWnNIUCMSdJtov4X8jIHT2rmVcUCXEQcbmA0LliZBgH3xofnyH2wMOLRgNcZ+xP2O4afpxLeK9E4mA1iigv0suqxkW+WAyK9gbDv67ovTDeS6gPBn3CD6njSb4ZTIYyWj6oR9dQO4w69i25TIyqvlHkjtsvaL10cZ0UDrqpkAUruB/RO6N8W6YcTfwoejZUpli/aReTAJGvih9pHKJMDMbELgFLxzJfKlU94oYsKFaerImfu6ERhrcGZ0m8yezXrahC/rEGQ=");

          event.setHeaders(headers);
          event.setCookies(cookies);

          AuthorizationResult result = handler.handleRequest(event, null);
          ObjectMapper mapper = new ObjectMapper();
          System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
          // System.out.println(ReflectionToStringBuilder.toString(result, ToStringStyle.MULTI_LINE_STYLE));
     }

     /**
      * AT RT 둘다 없으면 기각
      * AT가 refresh고 RT가 없는 경우(앱 처음 설치한 사용자가 올 경우 or 1년만에 접속한 경우)
      * AT가 refresh고 RT가 있는 경우
      * 둘 다 있는 경우
      *
      * AT가 있는데 RT가 없는 경우
      * AT가 없는데 RT가 있는 경우
      */
}
