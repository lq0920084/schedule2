package spata.schedule2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spata.schedule2.dto.*;
import spata.schedule2.service.ScheduleService;
import spata.schedule2.service.UserService;

import java.util.List;

@RequestMapping("/web")
@RequiredArgsConstructor
@Controller
public class WebScheduleController {
    private final UserService userService;
    private final ScheduleService scheduleService;

    //로그인된 세션에서 uuid로 된 userid를 받아와서 전체 일정목록표시.
    @GetMapping("/schedule")
    public String scheduleList(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        UserResponseDto user = userService.findUserById((String) session.getAttribute("userid"));
        List<ScheduleResponseDto> scheduleList = scheduleService.findScheduleByAll();
        if (scheduleList.isEmpty()) {
            model.addAttribute("name", user.getUsername());
            model.addAttribute("response", "NoData");
        } else {
            model.addAttribute("name", user.getUsername());
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
    public String createSchedule(@ModelAttribute @Validated CreateScheduleRequestDto dto, BindingResult bindingResult, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                if (fieldError.getCode().equals("ScheduleTitleVerification")) {
                    model.addAttribute("message", fieldError.getDefaultMessage());
                }
            }
        } else {
            scheduleService.createSchedule((String) session.getAttribute("userid"), dto.getTitle(), dto.getContents());
            model.addAttribute("message", "일정이 추가되었습니다.");
            model.addAttribute("chekCreate", true);
            model.addAttribute("scheduleUrl", "/web/schedule");
        }
        return "newschedule";
    }

    //세션으로부터 받은 userid와 일정의 번호를 받아서 변조되지 않은 경우에만 일정을 삭제.
    @PostMapping("/delete")
    public String removeSchedule(RemoveScheduleRequestDto dto, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (scheduleService.removeScheduleByIdCheckUser((String) session.getAttribute("userid"), dto)) {
            model.addAttribute("message", "삭제되었습니다.");
            model.addAttribute("checkScheduleList", true);
            model.addAttribute("ScheduleListUrl", "/web/schedule");
        } else {
            model.addAttribute("message", "삭제할 권한이 없습니다.");
            model.addAttribute("checkScheduleList", true);
            model.addAttribute("ScheduleListUrl", "/web/schedule");
        }

        return "schedule";
    }

    //일정 수정페이지로 진입하기 전, 세션id  위변조 확인 후 진입.
    @PostMapping("/modify")
    public String modifyView(ModifyScheduleViewRequestDto dto, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (scheduleService.findScheduleByIdCheckUser((String) session.getAttribute("userid"), dto.getId())) {
            ScheduleResponseDto scheduleResponseDto = scheduleService.findScheduleById(dto.getId());
            model.addAttribute("title", scheduleResponseDto.getTitle());
            model.addAttribute("contents", scheduleResponseDto.getContents());
            model.addAttribute("id", scheduleResponseDto.getId());
            return "modifyschedule";
        } else {
            model.addAttribute("message", "수정할 권한이 없습니다.");
            model.addAttribute("checkScheduleList", true);
            model.addAttribute("ScheduleListUrl", "/web/schedule");
            return "schedule";
        }


    }

    //일정id와 세션id 위변조 확인 후 일치하는 경우에만 일정 수정.
    @PostMapping("/modifySchedule")
    public String modifySchedule(@ModelAttribute @Validated ModifyScheduleRequestDto dto, BindingResult bindingResult, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                if (fieldError.getCode().equals("ScheduleTitleVerification")) {
                    model.addAttribute("message", fieldError.getDefaultMessage());
                }
            }
        } else {
            ScheduleResponseDto scheduleResponseDto = scheduleService.modifyScheduleByIdCheckUser(
                    (String) session.getAttribute("userid"),dto);
            if (scheduleResponseDto.getId() != 0) {
                scheduleService.modifyScheduleById(dto);
                model.addAttribute("message", "수정되었습니다.");
                model.addAttribute("checkModify", true);
                model.addAttribute("scheduleUrl", "/web/schedule");

            } else {
                model.addAttribute("message", "수정에 실패했습니다. 데이터 변조가 감지되었씁니다..");
            }
        }
        return "modifyschedule";
    }
}
