package spata.schedule2.Validation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
public @interface UsernameVerification {
    String message() default "사용자 이름은 한글로 4글자까지만 입력할 수 있습니다. 다른 문자는 허용되지 않습니다.";
    Class[] groups() default {};
    Class[] payload() default {};
}
