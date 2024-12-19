package spata.schedule2.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModifyCommentRequestDto {
private Long commentid;
@NotNull(message="등록할 덧글은 비울 수 없습니다.")
private String comment;
}
