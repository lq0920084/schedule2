package spata.schedule2.service;

import spata.schedule2.dto.ScheduleResponseDto;

public interface ScheduleService {

    ScheduleResponseDto createSchedule(String username,String title,String contents);

}
