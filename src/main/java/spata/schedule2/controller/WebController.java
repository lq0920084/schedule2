package spata.schedule2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spata.schedule2.dto.*;
import spata.schedule2.service.CommentService;
import spata.schedule2.service.ScheduleService;
import spata.schedule2.service.UserService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebController {
    private final UserService userService;
    private final ScheduleService scheduleService;

    //회원가입 페이지 표시.
    @GetMapping("/signup")
    public String singupView(Model model) {

        return "signup";
    }

    @PostMapping("/signup")
    //비어있지 않은지, 이메일은 형식에 맞는지 검사합니다.
    public String signupPostView(@ModelAttribute @Validated UserRequestDto request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                if (fieldError.getCode().equals("EmailVerification")) {
                    model.addAttribute("message", fieldError.getDefaultMessage());
                } else {
                    model.addAttribute("message2", fieldError.getDefaultMessage());
                }
            }
        } else {
            if (userService.signUp(request)) {
                model.addAttribute("message", "회원가입에 성공했습니다.");
                model.addAttribute("chekSignUp", true);
                model.addAttribute("signUpUrl", "/web/login");
            } else {
                model.addAttribute("message", "이메일이 중복됩니다. 다시 확인해주세요.");
            }
        }
        return "signup";
    }

    //로그인 페이지 표시.
    @GetMapping("/login")
    public String loginView(Model model) {
        return "login";
    }

    //사용자로부터 이메일과 비밀번호를 받아 로그인 세션 생성.
    @PostMapping("/login")
    public String loginPostView( Model model, HttpServletRequest request) {
        HttpSession session;
        session = request.getSession(false);
        if (session!=null) {
            model.addAttribute("message", "로그인에 성공했습니다.");
            model.addAttribute("checkSignIn", true);
            model.addAttribute("signInUrl", "/web/schedule");
        }
        return "login";
    }


    //만들어진 세션을 삭제 후 로그인 페이지로 리다이렉트.
    @GetMapping("/logout")
    public String logout(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/web/login";
    }

    @GetMapping("/changepassword")
    public String changePasswordView(Model model) {
        return "changepassword";
    }

    @PostMapping("/changepassword")
    public String changePassword(UserPasswordRequestDto dto, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (userService.modifyUserPasswordById((String) session.getAttribute("userid"), dto)) {
            model.addAttribute("message", "비밀번호가 정상적으로 수정되었습니다. 로그인페이지로 이동합니다.");
            model.addAttribute("chekModify", true);
            model.addAttribute("scheduleUrl", "/web/logout");
        } else {
            model.addAttribute("message", "현재 비밀번호가 다릅니다.");
            model.addAttribute("chekModify", false);
        }
        return "/changepassword";
    }

    @GetMapping("/changeuserdata")
    public String changeUserDataView(Model model) {
        return "changeuserdata";
    }

    @PostMapping("/changeuserdata")
    public String changeUserData(UserRequestDto dto, BindingResult bindingResult, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                if (fieldError.getCode().equals("UsernameVerification")) {
                    model.addAttribute("message", fieldError.getDefaultMessage());
                }
            }
        } else {
            if (userService.modifyUserById((String) session.getAttribute("userid"), dto)) {
                model.addAttribute("message", "이름과 이메일이 정상적으로 수정되었습니다. 로그인페이지로 이동합니다.");
                model.addAttribute("chekModify", true);
                model.addAttribute("scheduleUrl", "/web/logout");
            } else {
                model.addAttribute("message", "현재 비밀번호가 다르거나, 이메일이 중복됩니다.");
                model.addAttribute("chekModify", false);
            }
        }
        return "changeuserdata";
    }

    @GetMapping("/resignuser")
    public String resignUserview(Model model) {
        return "resign";
    }

    @PostMapping("/resignuser")
    public String resignUser(ResignUserRequestDto dto, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (userService.resignUser((String) session.getAttribute("userid"), dto)) {
            model.addAttribute("message", "그 동안 저희 사이트를 이용해주셔서 감사합니다. 다시 만나 뵙기를 기원하겠습니다.");
            model.addAttribute("checkresign", true);
            model.addAttribute("resignUrl", "/web/logout");
            return "resign";

        } else {
            model.addAttribute("message", "잘못된 요청입니다.");
            return "resign";
        }

    }



}
