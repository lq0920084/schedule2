package spata.schedule2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserModifyRequestDto {
    private String username;
    private String email;
}
