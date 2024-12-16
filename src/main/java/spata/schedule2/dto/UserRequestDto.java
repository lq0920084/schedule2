package spata.schedule2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@AllArgsConstructor
public class UserRequestDto {
    @NotNull
    private String username;
    @Email
    private String email;
    @NotNull
    private String password;
}

