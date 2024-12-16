package spata.schedule2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RemoveScheduleRequestDto {
    private String userid;
    private Long id;
}
