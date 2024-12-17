package spata.schedule2.Validation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface EmailVerification {
    String message() default "이메일 형식이 유효하지 않습니다.";
    Class[] groups() default {};
    Class[] payload() default {};
}
