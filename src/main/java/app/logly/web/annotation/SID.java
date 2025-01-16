package app.logly.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SID는 Session Member ID의 줄임말입니다.
 * <p>세션의 속성 값 중에 SessionManager.SESSION_MEMBER_ID를 통해서 세션 유저의 ID 값을 가져옵니다.</p>
 * <p>SIDArgumentResolver를 적용하는 어노테이션입니다.</p>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface SID {
}
