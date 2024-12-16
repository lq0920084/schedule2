package spata.schedule2.service;

import spata.schedule2.dto.*;

import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto findUserById(String id);

    UserResponseDto modifyUserById(String id,UserModifyRequestDto userModifyRequestDto);

    void deleteUserById(String id);

    List<UserResponseDto> findUserByAll();

    boolean signUp(UserRequestDto userRequestDto);

    LoginResponseDto userLogin(LoginRequestDto loginRequestDto);

    UserResponseDto modifyUserPasswordById(String id, UserPasswordRequestDto dto);
}
