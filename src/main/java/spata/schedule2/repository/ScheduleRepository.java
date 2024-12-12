package spata.schedule2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spata.schedule2.entity.Schedule;


public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

}
