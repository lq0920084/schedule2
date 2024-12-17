package spata.schedule2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CommentResponseDto {
    private Long commentid;
    private String username;
    private String comment;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
}
