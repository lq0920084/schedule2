package spata.schedule2.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<UsernameVerification,String> {
    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if(username==null){
            return false;
        }
        return username.matches("^[가-힣]{1,4}+$");
    }
}
