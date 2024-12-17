package spata.schedule2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="comment")
public class Comment extends CommentDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="scheduleid",nullable = false)
    private Schedule scheduleid;

    @OneToOne
    @JoinColumn(name="userid",nullable=false)
    private User userid;

    @Column(name="comment")
     private String comment;


}
