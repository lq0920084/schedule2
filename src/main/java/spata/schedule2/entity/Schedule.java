package spata.schedule2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name="schedule")
@Getter
@Setter
public class Schedule extends ScheduleDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="userid",nullable=false)
    //ddl-auto값이 Create여야만 합니다.
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User userid;

    @Column(nullable=false)
    private String title;

    @Column(columnDefinition="longtext")
    private String contents;




    public Schedule() {
    }

    public Schedule(User userid,
                    String title,
                    String contents){
        this.userid=userid;
        this.title=title;
        this.contents=contents;
    }

    public void updateSchedule(String title,String contents){
        this.title=title;
        this.contents=contents;

    }

}
