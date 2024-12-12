package spata.schedule2.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spata.schedule2.dto.ScheduleRequestDto;
import spata.schedule2.dto.ScheduleResponseDto;
import spata.schedule2.service.ScheduleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
@Slf4j
public class ScheduleController {


    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto dto) {

        return new ResponseEntity<>(scheduleService.createSchedule(dto.getUsername(),dto.getTitle(),dto.getContents()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id) {

        return new ResponseEntity<>(scheduleService.findScheduleById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> modifyScheduleById(@PathVariable Long id,@RequestBody ScheduleRequestDto dto) {

        return new ResponseEntity<>(scheduleService.modifyScheduleById(id,dto.getUsername(),dto.getTitle(),dto.getContents()),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeScheduleById(@PathVariable Long id){
        scheduleService.removeScheduleById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
