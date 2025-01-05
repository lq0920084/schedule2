package spata.schedule2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequestDto {
    private boolean ui;
    private String email;
    private String password;

}
