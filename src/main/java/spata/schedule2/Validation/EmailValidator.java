package spata.schedule2.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailVerification, String> {
    @Override
    public boolean isValid(String Email, ConstraintValidatorContext constraintValidatorContext) {
        if(Email==null){
            return false;
        }
        return Email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }
}
