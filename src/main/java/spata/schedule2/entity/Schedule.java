package spata.schedule2.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name="schedule")
@Getter
public class Schedule extends DateBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable=false)
    private String username;

    @Column(nullable=false)
    private String title;

    @Column(columnDefinition="longtext")
    private String contents;

    public Schedule() {
    }

    public Schedule(String username,
                    String title,
                    String contents){
        this.username=username;
        this.title=title;
        this.contents=contents;
    }
}
