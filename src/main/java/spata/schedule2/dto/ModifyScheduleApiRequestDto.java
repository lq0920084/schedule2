package spata.schedule2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import spata.schedule2.Validation.ScheduleTitleVerification;

@Getter
@AllArgsConstructor
public class ModifyScheduleApiRequestDto {

    @ScheduleTitleVerification
    private String title;
    private String contents;




}