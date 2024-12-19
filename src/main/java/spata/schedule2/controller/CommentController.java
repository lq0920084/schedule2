package spata.schedule2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spata.schedule2.dto.*;
import spata.schedule2.service.CommentService;
import spata.schedule2.service.ScheduleService;
import spata.schedule2.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final ScheduleService scheduleService;
    private final UserService userService;
    private final CommentService commentService;

    @GetMapping("/{scheduleid}")
    public ResponseEntity<List<CommentResponseDto>> findAllComment(@PathVariable Long scheduleid){
        return new ResponseEntity<>(commentService.findCommentByAll(scheduleid),HttpStatus.OK);
    }

    @PostMapping("/{scheduleid}")
    public ResponseEntity<List<CommentResponseDto>> createComment(@PathVariable Long scheduleid, @RequestBody CreateCommentApiRequestDto dto, HttpServletRequest request){
           HttpSession session = request.getSession(false);
            if(commentService.createComment((String)session.getAttribute("userid"),new CreateCommentRequestDto(scheduleid,dto.getComment()))){
                return new ResponseEntity<>(commentService.findCommentByAll(scheduleid),HttpStatus.CREATED);
            }else {
                return new ResponseEntity<>(commentService.findCommentByAll(scheduleid),HttpStatus.BAD_REQUEST);
            }

    }

    @PutMapping("/{scheduleid}")
        public ResponseEntity<List<CommentResponseDto>> modifyComent(@PathVariable Long scheduleid, @RequestBody ModifyCommentRequestDto dto,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(commentService.modifyCommentById((String)session.getAttribute("userid"),dto)){
            return new ResponseEntity<>(commentService.findCommentByAll(scheduleid),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(commentService.findCommentByAll(scheduleid),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{scheduleid}")
        public ResponseEntity<List<CommentResponseDto>> removeComment(@PathVariable Long scheduleid, @RequestBody RemoveCommentRequestDto dto,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(commentService.removeCommentById((String)session.getAttribute("userid"),dto)){
            return new ResponseEntity<>(commentService.findCommentByAll(scheduleid),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(commentService.findCommentByAll(scheduleid),HttpStatus.BAD_REQUEST);
        }
    }
}
