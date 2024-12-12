package spata.schedule2.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import spata.schedule2.dto.ScheduleResponseDto;
import spata.schedule2.entity.Schedule;
import spata.schedule2.repository.ScheduleRepository;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;


    @Override
    public ScheduleResponseDto createSchedule(String username, String title, String contents) {
        Schedule schedule = new Schedule(username, title, contents);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new ScheduleResponseDto(
                savedSchedule.getId(),
                savedSchedule.getUsername(),
                savedSchedule.getTitle(),
                savedSchedule.getContents(),
                savedSchedule.getCreateAt(),
                savedSchedule.getModifiedAt());
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        Schedule findSchedule = scheduleRepository.findById(id).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "does not exist ID"))
        );
        return new ScheduleResponseDto(
                findSchedule.getId(),
                findSchedule.getUsername(),
                findSchedule.getTitle(),
                findSchedule.getContents(),
                findSchedule.getCreateAt(),
                findSchedule.getModifiedAt()
        );
    }

    @Transactional
    @Override
    public ScheduleResponseDto modifyScheduleById(Long id, String username, String title, String contents) {
        Schedule findSchedule = scheduleRepository.findById(id).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "does not exist ID")));
                    findSchedule.setUsername(username);
                    findSchedule.setTitle(title);
                    findSchedule.setContents(contents);

        return new ScheduleResponseDto(
                findSchedule.getId(),
                findSchedule.getUsername(),
                findSchedule.getTitle(),
                findSchedule.getContents(),
                findSchedule.getCreateAt(),
                findSchedule.getModifiedAt()
        );
    }

}
