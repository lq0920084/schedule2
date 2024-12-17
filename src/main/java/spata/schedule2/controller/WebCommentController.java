package spata.schedule2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spata.schedule2.dto.*;
import spata.schedule2.service.CommentService;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/web")
public class WebCommentController {
    private final CommentService commentService;

    @GetMapping("/comment")
    public String commentView(@RequestParam(required = false) Long id, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (id == null) {
            id = (Long) session.getAttribute("scheduleid");
        } else {
            session.setAttribute("scheduleid", id);
        }
        List<CommentResponseDto> commentList = commentService.findCommentByAll(id);
        if (commentList.isEmpty()) {
            model.addAttribute("response", "NoData");
            model.addAttribute("id", id);
        } else {
            model.addAttribute("results", commentList);
            model.addAttribute("id", id);
        }
        return "comment";
    }

    @PostMapping("/comment")
    public String createComment(@ModelAttribute CreateCommentRequestDto dto, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (commentService.createComment((String) session.getAttribute("userid"), dto)) {
            model.addAttribute("message", "덧글이 추가되었습니다.");
            model.addAttribute("checkCommentList", true);
            model.addAttribute("CommentListUrl", "/web/comment");
        } else {
            model.addAttribute("message", "덧글 추가에 실패했습니다.");
        }
        return "comment";
    }

    @PostMapping("/comment/delete")
    public String removeComment(@ModelAttribute RemoveCommentRequestDto dto, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (commentService.removeCommentById((String) session.getAttribute("userid"), dto)) {
            model.addAttribute("message", "삭제되었습니다.");
            model.addAttribute("checkCommentList", true);
            model.addAttribute("CommentListUrl", "/web/comment");
        } else {
            model.addAttribute("message", "다른 유저가 작성한 덧글을 삭제할 수 없습니다.");
            model.addAttribute("checkCommentList", true);
            model.addAttribute("CommentListUrl", "/web/comment");
        }
        return "comment";
    }

    @PostMapping("/comment/modifyview")
    public String modifyCommentView(@ModelAttribute ModifyCommentViewRequestDto dto, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(commentService.findCommentByIdCheckUser((String)session.getAttribute("userid"),dto)){
            findCommentViewResponseDto findComment = commentService.findCommentViewById((String)session.getAttribute("userid"),dto);
            model.addAttribute("comment", findComment.getComment());
            model.addAttribute("commentid", findComment.getCommentid());
        } else {
            model.addAttribute("message", "다른 유저가 작성한 덧글을 수정할 수 없습니다.");
            model.addAttribute("checkModify", true);
            model.addAttribute("commentUrl", "/web/comment");
        }
        return "modifycomment";
    }

    @PostMapping("/comment/modify")
    public String modifyCommentById(@ModelAttribute ModifyCommentRequestDto dto, Model model,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(commentService.modifyCommentById((String)session.getAttribute("userid"),dto)){
            model.addAttribute("message", "덧글이 수정되었습니다.");
            model.addAttribute("checkModify", true);
            model.addAttribute("commentUrl", "/web/comment");
        } else {
            model.addAttribute("message", "다른 유저가 작성한 덧글을 수정할 수 없습니다.");
            model.addAttribute("checkModify", true);
            model.addAttribute("commentUrl", "/web/comment");
        }

        return "modifyComment";

    }

}
