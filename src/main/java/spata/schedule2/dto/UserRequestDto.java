package spata.schedule2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spata.schedule2.Validation.EmailVerification;
import spata.schedule2.Validation.UsernameVerification;


@Getter
@AllArgsConstructor
public class UserRequestDto {
    @NotNull
    @UsernameVerification
    private String username;
    @EmailVerification
    private String email;
    @NotNull
    private String password;
}

