package spata.schedule2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="users")
@AllArgsConstructor
public class User extends UserDateEntity {

    @Id
    private String userId;

    @Column(nullable=false)
    private String username;

    @Column(nullable=false)
    private String password;

    //보통 회원가입의 경우 이메일을 생략하는 경우는 거의 없기에 nullable로 섫정하였습니다.
    @Column(nullable=false)
    private String email;

    public User() {
    }
}
