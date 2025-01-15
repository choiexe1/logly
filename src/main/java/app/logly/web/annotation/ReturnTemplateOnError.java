package app.logly.web.annotation;

import jakarta.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ReturnOnError 어노테이션은 BindingResultAspect와 함께 사용되어야 합니다.
 * <p>
 * 이 어노테이션은 오류 발생 시 반환할 템플릿 이름을 지정하는 데 사용됩니다.
 * </p>
 * BindingResultAspect에서 이 값을 활용하여 오류 처리 후 해당 템플릿으로 반환할 수 있습니다.
 * @value 템플릿 이름을 지정한다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReturnTemplateOnError {
    @NotNull String value();
}
