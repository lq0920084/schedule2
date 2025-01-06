package spata.schedule2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import spata.schedule2.dto.*;
import spata.schedule2.entity.Comment;
import spata.schedule2.entity.Schedule;
import spata.schedule2.entity.User;
import spata.schedule2.repository.CommentRepository;
import spata.schedule2.repository.ScheduleRepository;
import spata.schedule2.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;

    @Override
    public boolean createComment(String userid, CreateCommentRequestDto createCommentRequestDto) {
        User user = findUserID_to_User(userid);
        Schedule schedule = findScheduleID_to_Schedule(createCommentRequestDto.getScheduleid());
        Comment comment = new Comment(schedule, user, createCommentRequestDto.getComment());
        Comment savedComment = commentRepository.save(comment);
        if (savedComment == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<CommentResponseDto> findCommentByAll(Long scheduleid) {

        return Comment_to_CommentResponseDto(commentRepository.findAllByScheduleid(findScheduleID_to_Schedule(scheduleid)));
    }

    @Override
    public boolean removeCommentById(String userid, RemoveCommentRequestDto dto) {
        Comment comment = commentRepository.findById(dto.getCommentid()).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 덧글이 없습니다.")));
        if(comment.getUserid().getUserid().equals(userid)){
            commentRepository.deleteById(dto.getCommentid());
            return true;
        }else {
            return false;
        }
        }

    @Override
    public boolean findCommentByIdCheckUser(String userid, ModifyCommentViewRequestDto dto) {
        Comment comment = commentRepository.findById(dto.getCommentid()).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다.")));
        if(comment.getUserid().getUserid().equals(userid)){
            return true;
        }
        return false;
    }


    @Override
    public boolean modifyCommentById(String userid, ModifyCommentRequestDto dto) {
        Comment comment = commentRepository.findById(dto.getCommentid()).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다.")));
        if(comment.getUserid().getUserid().equals(userid)){
            comment.updateComment(dto.getComment());
            commentRepository.save(comment);
            return true;
        }
        return false;
    }

    @Override
    public findCommentViewResponseDto findCommentViewById(String userid, ModifyCommentViewRequestDto dto) {
        Comment comment = commentRepository.findById(dto.getCommentid()).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "does not exist userid")));
        if(comment.getUserid().getUserid().equals(userid)){
            return new findCommentViewResponseDto(comment.getId(),comment.getComment());
        }
        return new findCommentViewResponseDto(0L,"NoData");
    }

    private User findUserID_to_User(String id) {
        return userRepository.findById(id).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "does not exist userid")));
    }

    private Schedule findScheduleID_to_Schedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "does not exist userid")));
    }


    private List<CommentResponseDto> Comment_to_CommentResponseDto(List<Comment> comment_list) {
        List<CommentResponseDto> commentResponseDto_list = new ArrayList<>();
        for (Comment comment : comment_list) {
            User user = findUserID_to_User(comment.getUserid().getUserid());
            commentResponseDto_list.add(new CommentResponseDto(
                    comment.getId(),
                    user.getUsername(),
                    comment.getComment(),
                    comment.getCreateAt(),
                    comment.getModifiedAt()));
        }
        return commentResponseDto_list;
    }
}
