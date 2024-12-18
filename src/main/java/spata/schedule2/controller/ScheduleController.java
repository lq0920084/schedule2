package spata.schedule2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spata.schedule2.dto.*;
import spata.schedule2.service.ScheduleService;
import spata.schedule2.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
@Slf4j
public class ScheduleController {


    private final ScheduleService scheduleService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestBody LoginRequestDto dto,HttpServletRequest request){
        LoginResponseDto loginResponseDto = userService.userLogin(dto);
        System.out.println(loginResponseDto.getUserId());
        if (loginResponseDto.getUserId().equals("LoginFailed")){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }else {
            HttpSession session = request.getSession();
            session.setAttribute("userid", loginResponseDto.getUserId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Validated @RequestBody CreateScheduleRequestDto dto, BindingResult bindingResult, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (bindingResult.hasErrors()) {
            for(FieldError fieldError : bindingResult.getFieldErrors()) {
                if(fieldError.getCode().equals("ScheduleTitleVerification")) {
                    log.info(fieldError.getDefaultMessage());
                }
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
        return new ResponseEntity<>(scheduleService.createSchedule(
                (String)session.getAttribute("userid"),
                dto.getTitle(),
                dto.getContents()),HttpStatus.CREATED);
    }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id,HttpServletRequest request) {
        return new ResponseEntity<>(scheduleService.findScheduleById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> modifyScheduleById(@PathVariable Long id, @Validated @RequestBody ModifyScheduleApiRequestDto dto, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                if (fieldError.getCode().equals("ScheduleTitleVerification")) {
                    log.info(fieldError.getDefaultMessage());
                }
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            HttpSession session = request.getSession(false);
            if(scheduleService.findScheduleByIdCheckUser((String)session.getAttribute("userid"),id)){
                return new ResponseEntity<>(scheduleService.modifyScheduleById(new ModifyScheduleRequestDto(id, dto.getTitle(), dto.getContents())), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeScheduleById(@PathVariable Long id){
        scheduleService.removeScheduleById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findScheduleByAll(){

        return new ResponseEntity<>(scheduleService.findScheduleByAll(),HttpStatus.OK);
    }
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        session.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
