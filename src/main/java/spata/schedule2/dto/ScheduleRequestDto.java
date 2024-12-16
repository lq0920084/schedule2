package spata.schedule2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ScheduleRequestDto {

    private Long id;
    //이제 유저이름 대신 유저고유식별자를 받습니다.
    private String userid;

    //유저이름은 user테이블로 이동되었으므로 더 이상 여기서 수정할 수 없습니다.

    private String title;

    private String contents;

    //유저고유식별자는 수정할 수 없습니다. 다른 유저가 작성한 일정으로 속일 수 없어야 하기 때문입니다.
    public ScheduleRequestDto(String title,String contents){
        this.title=title;
        this.contents=contents;
    }



}
