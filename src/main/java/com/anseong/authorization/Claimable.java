package com.anseong.authorization;

import com.anseong.authorization.security.auth.token.exceptions.DefaultClaimsMethodNotFoundException;
import com.anseong.authorization.security.auth.token.exceptions.DefaultClaimsValueLoadFailedException;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * 인증 토큰 발급시 Body값을 정의하는 인터페이스 입니다.
 * <p>
 * 인증 토큰의 Body값으로 활용하고 싶으신 DTO Record에 해당 인터페이스를 상속해주시면 되며, 구현체가 Class인 경우 {@code AbstractBaseClaims}추상 클래스를 상속해 주시기 바랍니다.
 * </p>
 * <p>
 * Claims 값을 파싱 시 아래 Annotation 설정이 적용됩니다.
 * </p>
 *
 * <pre>{@code @JsonIgnoreProperties}</pre> : Body값의 민감 정보를 필터링하여 파싱합니다.
 * <pre>{@code @JsonAutoDetect}</pre> : Body값은 필드값으로만 파싱하기 때문에 getter/setter 메소드는 무시됩니다.
 * <p>
 * <b>[상속시 주의사항]</b>
 * 1. 만약 구현체의 Type이 Class인 경우 {@code AbstractBaseClaims}추상 클래스를 상속해 주시기 바랍니다.
 * <p>
 * 2. 모든 구현체(Class/Record)는 리플렉션을 통한 기본값 조회를 위해 반드시 아래와 같은 정적 팩토리 메서드를 포함해야 합니다.
 *
 * <pre>{@code
 * public static User getDefault() {
 *     return new User("null도", "가능하니", "원하시는", "기본값을", "반환해주시면", "됩니다.", "ㅎㅎ");
 * }
 * }</pre>
 *
 * @throws NoSuchMethodException getDefault() 메서드가 구현되지 않았을 경우 발생시킵니다.
 * @throws DefaultClaimsLoadFailedException 그 외 리플렉션 실패시 해당 예외로 래핑하여 발생시킵니다.
 */
@JsonIgnoreProperties(
     value = { "password" },
     ignoreUnknown = true
)
@JsonAutoDetect(
     fieldVisibility = Visibility.ANY,
     getterVisibility = Visibility.NONE,
     setterVisibility = Visibility.NONE,
     isGetterVisibility = Visibility.NONE
)
public interface Claimable {

     String getSubject();

     static <T extends Claimable> T getDefaultClaims(Class<T> castType) {
          try {
               Object defaultObject = castType.getDeclaredMethod("getDefault").invoke(null);
               return castType.cast(defaultObject);

          } catch (NoSuchMethodException e) {
               // clazz.getSimpleName() + " 레코드에 'public static User getDefaultClaims()' 메서드를 정의해야 합니다."
               throw new DefaultClaimsMethodNotFoundException(castType.getSimpleName());

          } catch (Exception e) {
               throw new DefaultClaimsValueLoadFailedException(e.getMessage(), e);
          }
     }
}
