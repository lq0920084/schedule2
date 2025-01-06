package spata.schedule2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Table(name = "comment")
public class Comment extends CommentDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "scheduleid", nullable = false, unique=false)
    private Schedule scheduleid;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "userid", nullable = false, unique=false)
    private User userid;

    @Column(name = "comment")
    private String comment;


    public Comment(Schedule scheduleid, User userid, String comment) {
        this.scheduleid = scheduleid;
        this.userid = userid;
        this.comment = comment;
    }

    public Comment() {

    }

    public void updateComment(String comment){
        this.comment = comment;
    }
}
