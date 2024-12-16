package spata.schedule2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import spata.schedule2.dto.*;
import spata.schedule2.entity.User;
import spata.schedule2.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = new User(UUID.randomUUID().toString(),userRequestDto.getUsername(),
                                                        userRequestDto.getPassword(),
                                                        userRequestDto.getEmail());
        User savedUser = userRepository.save(user);
        return  new UserResponseDto(
                savedUser.getUserid(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getCreateAt());
    }

    @Override
    public UserResponseDto findUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND,"does not exist userid")));

        return new UserResponseDto(
                user.getUserid(),
                user.getUsername(),
                user.getEmail(),
                user.getCreateAt());
    }

    @Override
    public UserResponseDto modifyUserById(String id, UserModifyRequestDto userModifyRequestDto) {
        User user = userRepository.findById(id).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND,"does not exist userid")));
            user.setUsername(userModifyRequestDto.getUsername());
            user.setEmail(userModifyRequestDto.getEmail());

        return new UserResponseDto(
                user.getUserid(),
                user.getUsername(),
                user.getEmail(),
                user.getCreateAt());
    }

    @Override
    public void deleteUserById(String id) {
        userRepository.findById(id).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND,"does not exist userid")));
        userRepository.deleteById(id);
    }

    @Override
    public List<UserResponseDto> findUserByAll(){
            return UserList_To_UserResponseDto_list(userRepository.findAll());

    }

    @Override
    public boolean signUp(UserRequestDto userRequestDto) {
        if(checkEmail(userRequestDto.getEmail()).getEmail().equals(userRequestDto.getEmail())){
            return false;
        }else {
            createUser(userRequestDto);
        }
        return true;
    }

    @Override
    public LoginResponseDto userLogin(LoginRequestDto loginRequestDto) {
            User user = checkEmail(loginRequestDto.getEmail());
        if(user.getPassword().equals(loginRequestDto.getPassword())){
            return new LoginResponseDto(user.getUserid());
        }
        return new LoginResponseDto("LoginFailed");
    }

    @Transactional
    @Override
    public UserResponseDto modifyUserPasswordById(String id, UserPasswordRequestDto dto ) {
        User user = userRepository.findById(id).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND,"does not exist userid")));
        user.setPassword(dto.getPassword());
        return new UserResponseDto(
                user.getUserid(),
                user.getUsername(),
                user.getEmail(),
                user.getCreateAt());
    }

    public List<UserResponseDto> UserList_To_UserResponseDto_list(List<User> user_List){
        List<UserResponseDto> userResponseDto_List = new ArrayList<>();
        for(User user : user_List){
            userResponseDto_List.add(new UserResponseDto(
                    user.getUserid(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getCreateAt()));
        }
        return userResponseDto_List;
    }


    private User checkEmail(String email){
        return userRepository.findByEmail(email).orElse(new User("NoEmail"));
    }
}
