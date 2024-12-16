package spata.schedule2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spata.schedule2.dto.*;
import spata.schedule2.service.ScheduleService;
import spata.schedule2.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/web")
public class LoginController {
    private final UserService userService;
    private final ScheduleService scheduleService;

    @GetMapping("/signup")
    public String singupView(Model model) {

        return "signup";
    }

    @PostMapping("/signup")
    //비어있지 않은지, 이메일은 형식에 맞는지 검사합니다.
    public String signupPostView(@ModelAttribute @Validated UserRequestDto request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {

            model.addAttribute("message", "입력된 값이 유효한 형식이 아닙니다.");
        } else {
            if (userService.signUp(
                    request.getUsername(),
                    request.getPassword(),
                    request.getEmail()
            )) {
                model.addAttribute("message", "회원가입에 성공했습니다.");
                model.addAttribute("chekSignUp", true);
                model.addAttribute("signUpUrl", "/user/login");
            } else {
                model.addAttribute("message", "이메일이 중복됩니다. 다시 확인해주세요.");
            }
        }

        return "signup";
    }


    @GetMapping("/login")
    public String loginView(Model model) {
        return "login";
    }


    @PostMapping("/login")
    public String loginPostView(@ModelAttribute LoginRequestDto dto, Model model, HttpServletRequest request) {
        HttpSession session;
        LoginResponseDto loginResponseDto = userService.userLogin(dto.getEmail(), dto.getPassword());
        if (loginResponseDto.getUserId().equals("LoginFailed")) {
            model.addAttribute("message", "로그인에 실패했습니다. 다시 확인해주세요.");
        } else {
            model.addAttribute("message", "로그인에 성공했습니다.");
            model.addAttribute("checkSignIn", true);
            model.addAttribute("signInUrl", "/user/schedule");
            session = request.getSession();
            session.setAttribute("userid", loginResponseDto.getUserId());
        }
        return "login";
    }

    @GetMapping("/schedule")
    public String scheduleList(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        List<ScheduleResponseDto> scheduleList = scheduleService.findScheduleByUserid((String) session.getAttribute("userid"));
        if (scheduleList.isEmpty()) {
            model.addAttribute("response", "NoData");
        } else {
            model.addAttribute("results", scheduleList);
        }
        return "schedule";
    }

    @GetMapping("/newSchedule")
    public String createScheduleView(Model model) {
        return "newschedule";
    }

    @PostMapping("/createSchedule")
    public String createSchedule(ScheduleRequestDto dto, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
       scheduleService.createSchedule((String) session.getAttribute("userid"), dto.getTitle(), dto.getContents());
        model.addAttribute("message","일정이 추가되었습니다.");
        model.addAttribute("chekCreate",true);
        model.addAttribute("scheduleUrl","/user/schedule");

    return "newschedule";
    }

    @PostMapping("/delete")
    public String removeSchedule(ScheduleRequestDto dto,Model model,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(scheduleService.removeScheduleByIdCheckUser((String)session.getAttribute("userid"),dto.getId())){
            model.addAttribute("message","삭제되었습니다.");
            model.addAttribute("checkScheduleList", true);
            model.addAttribute("ScheduleListUrl","/user/schedule");
        }else{
            model.addAttribute("message","해당하는 일정이 없습니다..");
        }

        return "schedule";
    }
    @PostMapping("/modify")
    public String modifyView(ScheduleRequestDto dto,Model model,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(scheduleService.findScheduleByIdCheckUser((String)session.getAttribute("userid"),dto.getId())){
            ScheduleResponseDto scheduleResponseDto = scheduleService.findScheduleById(dto.getId());
            model.addAttribute("title",scheduleResponseDto.getTitle());
            model.addAttribute("contents",scheduleResponseDto.getContents());
            model.addAttribute("id",scheduleResponseDto.getId());
        }

        return "modifyschedule";


    }
    @PostMapping("/modifySchedule")
    public String modifySchedule(ScheduleRequestDto dto,Model model,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        System.out.println(dto.getId());
        System.out.println(dto.getTitle());
        System.out.println(dto.getContents());
        System.out.println(session.getAttribute("userid"));
        ScheduleResponseDto scheduleResponseDto = scheduleService.modifyScheduleByIdCheckUser(
                (String)session.getAttribute("userid"),
                dto.getId(),
                dto.getTitle(),
                dto.getContents());
        if(scheduleResponseDto.getId()!=0){
            scheduleService.modifyScheduleById(dto.getId(),dto.getTitle(),dto.getContents());
            model.addAttribute("message","수정되었습니다.");
            model.addAttribute("chekModify",true);
            model.addAttribute("scheduleUrl","/user/schedule");

        }else {
            model.addAttribute("message","수정에 실패했습니다. 데이터 변조가 감지되었씁니다..");
        }

        return "modifyschedule";
    }
    @GetMapping("logout")
        public String logout(Model model,HttpServletRequest request){
        HttpSession session = request.getSession();
            session.invalidate();
            return "redirect:/user/login";

        }

}
