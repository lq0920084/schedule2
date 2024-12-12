package spata.schedule2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spata.schedule2.dto.ScheduleResponseDto;
import spata.schedule2.entity.Schedule;
import spata.schedule2.repository.ScheduleRepository;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{
    private final ScheduleRepository scheduleRepository;


    @Override
    public ScheduleResponseDto createSchedule(String username, String title, String contents) {
        Schedule schedule = new Schedule(username,title,contents);
         Schedule savedSchedule = scheduleRepository.save(schedule);
        return new ScheduleResponseDto(savedSchedule.getId(),
                savedSchedule.getUsername(),
                savedSchedule.getTitle(),savedSchedule.getContents(),savedSchedule.getCreateAt(),savedSchedule.getModifiedAt());
    }
}
