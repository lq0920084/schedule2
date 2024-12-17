package spata.schedule2.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ScheduleTitleValidator implements ConstraintValidator<ScheduleTitleVerification, String> {
    @Override
    public boolean isValid(String title, ConstraintValidatorContext constraintValidatorContext) {
        if(title==null){
            return false;
        }
        return title.matches("^[가-힣]{1,10}+$");
    }
}
