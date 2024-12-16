package spata.schedule2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {

    private Long id;

    private String username;

    private String title;

    private String contents;

    private LocalDateTime createAt;

    private LocalDateTime modifiedAt;


}
