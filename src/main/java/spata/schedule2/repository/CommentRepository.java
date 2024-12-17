package spata.schedule2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spata.schedule2.entity.Comment;
import spata.schedule2.entity.Schedule;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllByScheduleid(Schedule scheduleid);
}
