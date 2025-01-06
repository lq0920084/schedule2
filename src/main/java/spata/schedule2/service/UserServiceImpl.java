package spata.schedule2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import spata.schedule2.Exception.LoginWebException;
import spata.schedule2.dto.*;
import spata.schedule2.entity.User;
import spata.schedule2.repository.UserRepository;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = new User(UUID.randomUUID().toString(), userRequestDto.getUsername(),
                encryptPassword(userRequestDto.getPassword()),
                userRequestDto.getEmail());
        User savedUser = userRepository.save(user);
        return new UserResponseDto(
                savedUser.getUserid(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getCreateAt());
    }

    @Override
    public UserResponseDto findUserById(String id) {
        User user = findUserID_to_User(id);
        return new UserResponseDto(
                user.getUserid(),
                user.getUsername(),
                user.getEmail(),
                user.getCreateAt());
    }

    @Override
    public boolean modifyUserById(String id, UserRequestDto userRequestDto) {
        User user = findUserID_to_User(id);
        if (user.getPassword().equals(encryptPassword(userRequestDto.getPassword()))) {
            if (checkEmail(userRequestDto.getEmail()).getEmail().equals("NoEmail")) {
                user.updateUser(userRequestDto.getUsername(),userRequestDto.getEmail());
                userRepository.save(user);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    @Override
    public void deleteUserById(String id) {
        userRepository.findById(id).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다.")));
        userRepository.deleteById(id);
    }

    @Override
    public List<UserResponseDto> findUserByAll() {
        return UserList_To_UserResponseDto_list(userRepository.findAll());

    }

    @Override
    public boolean signUp(UserRequestDto userRequestDto) {
        if (checkEmail(userRequestDto.getEmail()).getEmail().equals(userRequestDto.getEmail())) {
            return false;
        } else {
            createUser(userRequestDto);
        }
        return true;
    }

    @Override
    public LoginResponseDto userLogin(LoginRequestDto loginRequestDto)  {
        User user = checkEmail(loginRequestDto.getEmail());
        if (user.getPassword().equals(encryptPassword(loginRequestDto.getPassword()))) {
            return new LoginResponseDto(user.getUserid());
        }
        if(loginRequestDto.isUi()){
            throw new LoginWebException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"이메일 또는 비밀번호가 일치하지 않습니다.");
        }

    }

    @Transactional
    @Override
    public boolean modifyUserPasswordById(String id, UserPasswordRequestDto dto) {
        User user = findUserID_to_User(id);
        if (user.getPassword().equals(encryptPassword(dto.getCurrent_password()))) {
            user.updatePassword(encryptPassword(dto.getNew_password()));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean resignUser(String id, ResignUserRequestDto dto) {
        User user = findUserID_to_User(id);
        if(user.getPassword().equals(encryptPassword(dto.getPassword()))){
            deleteUserById(id);
            return true;
        }else {
            return false;
        }
    }

    public List<UserResponseDto> UserList_To_UserResponseDto_list(List<User> user_List) {
        List<UserResponseDto> userResponseDto_List = new ArrayList<>();
        for (User user : user_List) {
            userResponseDto_List.add(new UserResponseDto(
                    user.getUserid(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getCreateAt()));
        }
        return userResponseDto_List;
    }


    private User checkEmail(String email) {
        return userRepository.findByEmail(email).orElse(new User("nodata","nodata","nodata","NoEmail"));
    }

    private User findUserID_to_User(String id){
        return userRepository.findById(id).orElseThrow(() ->
                (new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다.")));
    }
    private String encryptPassword(String password){
        //암호로 레인보우 테이블을 생성할 수 없도록 Salt를 추가합니다.
        password = password+"sparta";
        try {
            MessageDigest encryptInstance = MessageDigest.getInstance("SHA-256");
            byte[] encryptedPasswordToByte = encryptInstance.digest(password.getBytes(Charset.forName("UTF-8")));
            return String.format("%032X",new BigInteger(1,encryptedPasswordToByte));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
