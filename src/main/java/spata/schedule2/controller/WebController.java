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

            model.addAttribute("message", "입력된 값이 유효한 형식이 아닙니다.");
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
    public String loginPostView(@ModelAttribute LoginRequestDto dto, Model model, HttpServletRequest request) {
        HttpSession session;
        LoginResponseDto loginResponseDto = userService.userLogin(dto);
        if (loginResponseDto.getUserId().equals("LoginFailed")) {
            model.addAttribute("message", "로그인에 실패했습니다. 다시 확인해주세요.");
        } else {
            model.addAttribute("message", "로그인에 성공했습니다.");
            model.addAttribute("checkSignIn", true);
            model.addAttribute("signInUrl", "/web/schedule");
            session = request.getSession();
            session.setAttribute("userid", loginResponseDto.getUserId());
        }
        return "login";
    }

    //로그인된 세션에서 uuid로 된 userid를 받아와서 전체 일정목록표시.
    @GetMapping("/schedule")
    public String scheduleList(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        UserResponseDto user =  userService.findUserById((String)session.getAttribute("userid"));
        List<ScheduleResponseDto> scheduleList = scheduleService.findScheduleByUserid((String) session.getAttribute("userid"));
        if (scheduleList.isEmpty()) {
            model.addAttribute("name",user.getUsername());
            model.addAttribute("response", "NoData");
        } else {
            model.addAttribute("name",user.getUsername());
            model.addAttribute("results", scheduleList);
        }
        return "schedule";
    }

    //새일정 추가 페이지 표시.
    @GetMapping("/newSchedule")
    public String createScheduleView(Model model) {
        return "newschedule";
    }

    //새 일정을 추기하기 위해 세션으로부터 받은 userid를 기반으로  새일정 추가.
    @PostMapping("/createSchedule")
    public String createSchedule(CreateScheduleRequestDto dto, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
       scheduleService.createSchedule((String) session.getAttribute("userid"), dto.getTitle(), dto.getContents());
        model.addAttribute("message","일정이 추가되었습니다.");
        model.addAttribute("chekCreate",true);
        model.addAttribute("scheduleUrl","/web/schedule");

    return "newschedule";
    }

    //세션으로부터 받은 userid와 일정의 번호를 받아서 변조되지 않은 경우에만 일정을 삭제.
    @PostMapping("/delete")
    public String removeSchedule(ScheduleRequestDto dto,Model model,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(scheduleService.removeScheduleByIdCheckUser((String)session.getAttribute("userid"),dto.getId())){
            model.addAttribute("message","삭제되었습니다.");
            model.addAttribute("checkScheduleList", true);
            model.addAttribute("ScheduleListUrl","/web/schedule");
        }else{
            model.addAttribute("message","해당하는 일정이 없습니다..");
        }

        return "schedule";
    }

    //일정 수정페이지로 진입하기 전, 세션id  위변조 확인 후 진입.
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

    //일정id와 세션id 위변조 확인 후 일치하는 경우에만 일정 수정.
    @PostMapping("/modifySchedule")
    public String modifySchedule(ModifyScheduleRequestDto dto, Model model, HttpServletRequest request){
        HttpSession session = request.getSession(false);

        ScheduleResponseDto scheduleResponseDto = scheduleService.modifyScheduleByIdCheckUser(
                (String)session.getAttribute("userid"),
                dto.getId(),
                dto.getTitle(),
                dto.getContents());
        if(scheduleResponseDto.getId()!=0){
            scheduleService.modifyScheduleById(dto.getId(),dto.getTitle(),dto.getContents());
            model.addAttribute("message","수정되었습니다.");
            model.addAttribute("chekModify",true);
            model.addAttribute("scheduleUrl","/web/schedule");

        }else {
            model.addAttribute("message","수정에 실패했습니다. 데이터 변조가 감지되었씁니다..");
        }

        return "modifyschedule";
    }

    //만들어진 세션을 삭제 후 로그인 페이지로 리다이렉트.
    @GetMapping("/logout")
        public String logout(Model model,HttpServletRequest request){
        HttpSession session = request.getSession();
            session.invalidate();
            return "redirect:/web/login";
        }
    @GetMapping("/changepassword")
    public String changePasswordView(Model model){
        return "changepassword";
    }

    @PostMapping("/changepassword")
    public String changePassword(UserPasswordRequestDto dto,Model model,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(userService.modifyUserPasswordById((String)session.getAttribute("userid"),dto)){
            model.addAttribute("message", "비밀번호가 정상적으로 수정되었습니다. 로그인페이지로 이동합니다.");
            model.addAttribute("chekModify",true);
            model.addAttribute("scheduleUrl","/web/logout");
        }else {
            model.addAttribute("message", "현재 비밀번호가 다릅니다.");
            model.addAttribute("chekModify",false);
        }
        return "/changepassword";
    }

    @GetMapping("/changeuserdata")
    public String changeUserDataView(Model model){
        return "changeuserdata";
    }
    @PostMapping("/changeuserdata")
    public String changeUserData(UserRequestDto dto,Model model,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(userService.modifyUserById((String)session.getAttribute("userid"),dto)) {
            model.addAttribute("message", "이름과 이메일이 정상적으로 수정되었습니다. 로그인페이지로 이동합니다.");
            model.addAttribute("chekModify",true);
            model.addAttribute("scheduleUrl","/web/logout");
        }else {
            model.addAttribute("message", "현재 비밀번호가 다르거나, 이메일이 중복됩니다.");
            model.addAttribute("chekModify",false);
        }
        return "changeuserdata";
    }


}
