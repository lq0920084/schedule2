package spata.schedule2.service;

import spata.schedule2.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {

    ScheduleResponseDto createSchedule(String userId,String title,String contents);

    ScheduleResponseDto findScheduleById(Long id);

    ScheduleResponseDto modifyScheduleById(Long id,String title,String contents);

    void removeScheduleById(Long id);
    List<ScheduleResponseDto> findScheduleByAll();
}
