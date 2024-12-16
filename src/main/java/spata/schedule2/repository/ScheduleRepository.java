package spata.schedule2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spata.schedule2.entity.Schedule;
import spata.schedule2.entity.User;

import java.util.List;


public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    List<Schedule> findAllByUserid(User userid);

}
