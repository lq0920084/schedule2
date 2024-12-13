package spata.schedule2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import spata.schedule2.dto.UserResponseDto;
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
    public UserResponseDto createUser(String username,String password,String email) {
        User user = new User(UUID.randomUUID().toString(),username,password,email);
        User savedUser = userRepository.save(user);
        return  new UserResponseDto(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getCreateAt());
    }

    @Override
    public UserResponseDto findUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND,"does not exist userid")));

        return new UserResponseDto(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreateAt());
    }

    @Override
    public UserResponseDto modifyUserById(String id, String username, String email) {
        User user = userRepository.findById(id).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND,"does not exist userid")));
            user.setUsername(username);
            user.setEmail(email);

        return new UserResponseDto(
                user.getUserId(),
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

    public List<UserResponseDto> UserList_To_UserResponseDto_list(List<User> user_List){
        List<UserResponseDto> userResponseDto_List = new ArrayList<>();
        for(User user : user_List){
            userResponseDto_List.add(new UserResponseDto(
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getCreateAt()));
        }
        return userResponseDto_List;
    }
}
