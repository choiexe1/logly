package app.logly.web.aop;

import app.logly.web.annotation.ReturnTemplateOnError;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Aspect
@Slf4j
@Component
public class BindingResultAspect {

    /**
     * 컨트롤러에서 BindingResult 관련 코드를 줄이기 위해 만들어진 AOP입니다.
     * <p>컨트롤러 내에서 비즈니스 로직이 작동하기 전에 BindingResult를 사전 검증하고
     * returnTempalteOnError에 지정된 템플릿으로 반환합니다.</p>
     *
     * @param returnTemplateOnError @ReturnTemplateOnError 어노테이션
     */
    @Around("@annotation(returnTemplateOnError)")
    public Object validationAspect(ProceedingJoinPoint joinPoint, ReturnTemplateOnError returnTemplateOnError)
            throws Throwable {
        Object[] args = joinPoint.getArgs();
        String template = returnTemplateOnError.value();
        BindingResult bindingResult = findBindingResult(args);

        if (bindingResult != null && bindingResult.hasErrors()) {
            return template;
        }

        Object result = joinPoint.proceed();

        if (bindingResult != null && bindingResult.hasErrors()) {
            return template;
        }

        return result;
    }

    private BindingResult findBindingResult(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                return (BindingResult) arg;
            }
        }
        return null;
    }
}
