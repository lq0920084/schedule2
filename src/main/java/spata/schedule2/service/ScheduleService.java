package spata.schedule2.service;

import spata.schedule2.dto.RemoveScheduleRequestDto;
import spata.schedule2.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {

    ScheduleResponseDto createSchedule(String userid,String title,String contents);

    ScheduleResponseDto findScheduleById(Long id);

    ScheduleResponseDto modifyScheduleById(Long id,String title,String contents);

    void removeScheduleById(Long id);

    boolean removeScheduleByIdCheckUser(String userid, RemoveScheduleRequestDto dto);

    boolean findScheduleByIdCheckUser(String userid,Long id);

    ScheduleResponseDto modifyScheduleByIdCheckUser(String userid,Long id,String title,String contents);

    List<ScheduleResponseDto> findScheduleByAll();

}
