package spata.schedule2.service;

import spata.schedule2.dto.LoginResponseDto;
import spata.schedule2.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto createUser(String username,String password,String email);

    UserResponseDto findUserById(String id);

    UserResponseDto modifyUserById(String id,String username,String email);

    void deleteUserById(String id);

    List<UserResponseDto> findUserByAll();

    boolean signUp(String username,String password,String email);

    LoginResponseDto userLogin(String email, String password);

    UserResponseDto modifyUserPasswordById(String id, String password);
}
