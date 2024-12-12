package spata.schedule2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private String userId;
    private String username;
    private String email;
    private LocalDateTime createAt;

}
