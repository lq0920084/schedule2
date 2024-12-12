package spata.schedule2.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import spata.schedule2.dto.ScheduleResponseDto;
import spata.schedule2.entity.Schedule;
import spata.schedule2.entity.User;
import spata.schedule2.repository.ScheduleRepository;
import spata.schedule2.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;


    @Override
    public ScheduleResponseDto createSchedule(String userId, String title, String contents) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "does not exist userId")));
        Schedule schedule = new Schedule(user, title, contents);
        Schedule savedSchedule = scheduleRepository.save(schedule);

        return new ScheduleResponseDto(
                savedSchedule.getId(),
                user.getUserId(),
                user.getUsername(),
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
        User findUser = findSchedule.getUserId();
        User user = userRepository.findById(findUser.getUserId()).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "does not exist userId")));
        return new ScheduleResponseDto(
                findSchedule.getId(),
                findUser.getUserId(),
                user.getUsername(),
                findSchedule.getTitle(),
                findSchedule.getContents(),
                findSchedule.getCreateAt(),
                findSchedule.getModifiedAt()
        );
    }

    @Transactional
    @Override
    public ScheduleResponseDto modifyScheduleById(Long id, String title, String contents) {
        Schedule findSchedule = scheduleRepository.findById(id).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "does not exist ID")));
        User findUser = findSchedule.getUserId();
        User user = userRepository.findById(findUser.getUserId()).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "does not exist userId")));
        findSchedule.setTitle(title);
        findSchedule.setContents(contents);
        return new ScheduleResponseDto(
                findSchedule.getId(),
                findUser.getUserId(),
                user.getUsername(),
                findSchedule.getTitle(),
                findSchedule.getContents(),
                findSchedule.getCreateAt(),
                findSchedule.getModifiedAt()
        );
    }

    @Override
    public void removeScheduleById(Long id) {
        scheduleRepository.findById(id).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "does not exist ID")));
        scheduleRepository.deleteById(id);
    }

    @Override
    public List<ScheduleResponseDto> findScheduleByAll() {
        return scheduleList_To_ScheduleResponseDto(scheduleRepository.findAll());
    }

    private List<ScheduleResponseDto> scheduleList_To_ScheduleResponseDto(List<Schedule> schedule_list) {
        List<ScheduleResponseDto> scheduleResponseDto_list = new ArrayList<>();
        for (Schedule schedule : schedule_list) {
            User findUser = schedule.getUserId();
            User user = userRepository.findById(findUser.getUserId()).orElseThrow(() ->
                    (new ResponseStatusException(HttpStatus.NOT_FOUND, "does not exist userId")));
            scheduleResponseDto_list.add(new ScheduleResponseDto(
                    schedule.getId(),
                    findUser.getUserId(),
                    user.getUsername(),
                    schedule.getTitle(),
                    schedule.getContents(),
                    schedule.getCreateAt(),
                    schedule.getModifiedAt()));
        }
        return scheduleResponseDto_list;
    }

}
