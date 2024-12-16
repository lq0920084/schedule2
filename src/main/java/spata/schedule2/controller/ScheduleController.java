package spata.schedule2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spata.schedule2.dto.LoginRequestDto;
import spata.schedule2.dto.LoginResponseDto;
import spata.schedule2.dto.ScheduleRequestDto;
import spata.schedule2.dto.ScheduleResponseDto;
import spata.schedule2.service.ScheduleService;
import spata.schedule2.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
@Slf4j
public class ScheduleController {


    private final ScheduleService scheduleService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestBody LoginRequestDto dto,HttpServletRequest request){
        LoginResponseDto loginResponseDto = userService.userLogin(dto);
        if (loginResponseDto.getUserId().equals("LoginFailed")){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }else {
            HttpSession session = request.getSession();
            session.setAttribute("userid", loginResponseDto.getUserId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto dto, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return new ResponseEntity<>(scheduleService.createSchedule(
                (String)session.getAttribute("userid"),
                dto.getTitle(),
                dto.getContents()),HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id,HttpServletRequest request) {
        return new ResponseEntity<>(scheduleService.findScheduleById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> modifyScheduleById(@PathVariable Long id,@RequestBody ScheduleRequestDto dto) {

        return new ResponseEntity<>(scheduleService.modifyScheduleById(id,dto.getTitle(),dto.getContents()),HttpStatus.OK);
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
