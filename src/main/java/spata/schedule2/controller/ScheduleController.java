package spata.schedule2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spata.schedule2.dto.ScheduleRequestDto;
import spata.schedule2.dto.ScheduleResponseDto;
import spata.schedule2.service.ScheduleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ScheduleController {


    private final ScheduleService scheduleService;

    @PostMapping("/create")
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto dto){

        return new ResponseEntity<>(scheduleService.createSchedule(dto.getUsername(),dto.getTitle(),dto.getContents()),HttpStatus.OK);
    }
}
