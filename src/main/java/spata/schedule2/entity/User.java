package spata.schedule2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Getter
@Table(name="users")
@AllArgsConstructor
public class User extends UserDateEntity {

    @Id
    private String userid;

    @Column(nullable=false)
    private String username;

    @Column(nullable=false)
    private String password;

    //보통 회원가입의 경우 이메일을 생략하는 경우는 거의 없기에 nullable로 섫정하였습니다.
    @Column(nullable=false,unique=true)
    private String email;

    public User() {
    }
    public User(String email){
        this.email=email;
    }
    public void updateUser(String username,String email){
        this.username=username;
        this.email=email;
    }

    public void updatePassword(String password){
        this.password=password;
    }
}
