package app.logly.web.aop;

import app.logly.exception.InvalidPasswordException;
import app.logly.exception.UserNotFoundException;
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

    @Around("@annotation(returnOnError)")
    public Object validationAspect(ProceedingJoinPoint joinPoint, ReturnTemplateOnError returnOnError)
            throws Throwable {
        Object[] args = joinPoint.getArgs();
        String template = returnOnError.value();
        BindingResult bindingResult = findBindingResult(args);

        if (bindingResult != null && bindingResult.hasErrors()) {
            return template;
        }

        try {
            return joinPoint.proceed();
        } catch (InvalidPasswordException e) {
            bindingResult.rejectValue("password", InvalidPasswordException.ERROR_CODE);
        } catch (UserNotFoundException e) {
            bindingResult.rejectValue("username", UserNotFoundException.ERROR_CODE);
        }

        return template;
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
