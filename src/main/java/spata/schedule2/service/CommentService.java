package spata.schedule2.service;

import spata.schedule2.dto.*;

import java.util.List;

public interface CommentService {

    boolean createComment(String userid,CreateCommentRequestDto createCommentRequestDto);

    List<CommentResponseDto> findCommentByAll(Long scheduleid);

    boolean removeCommentById(String userid, RemoveCommentRequestDto dto);

    boolean findCommentByIdCheckUser(String userid,ModifyCommentViewRequestDto dto);

    findCommentViewResponseDto findCommentViewById(String userid, ModifyCommentViewRequestDto dto);

    boolean modifyCommentById(String userid,ModifyCommentRequestDto dto);



}
