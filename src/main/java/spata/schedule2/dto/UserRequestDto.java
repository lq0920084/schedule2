package spata.schedule2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
public class UserRequestDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @Email
    private String email;
}

