package spata.schedule2.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import spata.schedule2.dto.ScheduleResponseDto;
import spata.schedule2.entity.Schedule;
import spata.schedule2.entity.User;
import spata.schedule2.repository.ScheduleRepository;
import spata.schedule2.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;


    @Override
    public ScheduleResponseDto createSchedule(String userid, String title, String contents) {
        User user = userRepository.findById(userid).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "does not exist userId")));
        Schedule schedule = new Schedule(user, title, contents);
        Schedule savedSchedule = scheduleRepository.save(schedule);

        return new ScheduleResponseDto(
                savedSchedule.getId(),
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
        User findUser = findSchedule.getUserid();
        User user = userRepository.findById(findUser.getUserid()).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "does not exist userId")));
        return new ScheduleResponseDto(
                findSchedule.getId(),
                user.getUsername(),
                findSchedule.getTitle(),
                findSchedule.getContents(),
                findSchedule.getCreateAt(),
                findSchedule.getModifiedAt()
        );
    }


    @Override
    public ScheduleResponseDto modifyScheduleById(Long id, String title, String contents) {
        Schedule findSchedule = scheduleRepository.findById(id).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "does not exist ID")));
        User findUser = findSchedule.getUserid();
        User user = userRepository.findById(findUser.getUserid()).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "does not exist userId")));
        findSchedule.setTitle(title);
        findSchedule.setContents(contents);
        Schedule schedule =  scheduleRepository.save(findSchedule);
        return new ScheduleResponseDto(
                schedule.getId(),
                user.getUsername(),
                schedule.getTitle(),
                schedule.getContents(),
                schedule.getCreateAt(),
                schedule.getModifiedAt()
        );
    }

    @Override
    public void removeScheduleById(Long id) {
        scheduleRepository.findById(id).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "does not exist ID")));
        scheduleRepository.deleteById(id);
    }

    @Override
    public boolean removeScheduleByIdCheckUser(String userid, Long id) {
        if(findScheduleByIdCheckUser(userid,id)){
            scheduleRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
        }

    @Override
    public boolean findScheduleByIdCheckUser(String userid, Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "does not exist ID")));
        User user = schedule.getUserid();
        if(user.getUserid().equals(userid)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public ScheduleResponseDto modifyScheduleByIdCheckUser(String userid, Long id,String title,String contents) {
        if(findScheduleByIdCheckUser(userid,id)){
            return modifyScheduleById(id,title,contents);
        }
         LocalDateTime nodata = LocalDateTime.now();
        return new ScheduleResponseDto(0L,"NoData","NoData","Nodata",nodata,nodata) ;
    }

    @Override
    public List<ScheduleResponseDto> findScheduleByAll() {
        return scheduleList_To_ScheduleResponseDto(scheduleRepository.findAll());
    }

    @Override
    public List<ScheduleResponseDto> findScheduleByUserid(String userid) {
        User user = new User();
        user.setUserid(userid);
        return scheduleList_To_ScheduleResponseDto(scheduleRepository.findAllByUserid(user));
    }

    private List<ScheduleResponseDto> scheduleList_To_ScheduleResponseDto(List<Schedule> schedule_list) {
        List<ScheduleResponseDto> scheduleResponseDto_list = new ArrayList<>();
        for (Schedule schedule : schedule_list) {
            User findUser = schedule.getUserid();
            User user = userRepository.findById(findUser.getUserid()).orElseThrow(() ->
                    (new ResponseStatusException(HttpStatus.NOT_FOUND, "does not exist userId")));
            scheduleResponseDto_list.add(new ScheduleResponseDto(
                    schedule.getId(),
                    user.getUsername(),
                    schedule.getTitle(),
                    schedule.getContents(),
                    schedule.getCreateAt(),
                    schedule.getModifiedAt()));
        }
        return scheduleResponseDto_list;
    }

}
