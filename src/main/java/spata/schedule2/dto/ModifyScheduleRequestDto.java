package spata.schedule2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModifyScheduleRequestDto {
    private Long id;
    private String title;
    private String contents;
}
