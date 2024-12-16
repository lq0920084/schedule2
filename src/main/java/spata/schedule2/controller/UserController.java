package spata.schedule2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spata.schedule2.dto.UserPasswordRequestDto;
import spata.schedule2.dto.UserRequestDto;
import spata.schedule2.dto.UserResponseDto;
import spata.schedule2.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

@PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto dto){

    return new ResponseEntity<>(userService.createUser(dto),HttpStatus.CREATED);

}

@GetMapping("{id}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable String id){

    return new ResponseEntity<>(userService.findUserById(id),HttpStatus.OK);
}

@PutMapping("{id}")
    public ResponseEntity<Void> modifyUserById(@PathVariable String id,@RequestBody UserRequestDto userRequestDto){
    if(userService.modifyUserById(id,userRequestDto)) {
        return new ResponseEntity<>(HttpStatus.OK);
    }else {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
@PutMapping("/changepassword/{id}")
    public ResponseEntity<Void> modifyUserpasswordById(@PathVariable String id,@RequestBody UserPasswordRequestDto dto){
if(userService.modifyUserPasswordById(id,dto)) {

    return new ResponseEntity<>(HttpStatus.OK);
} else {
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
}
}

@DeleteMapping("{id}")
    public ResponseEntity<Void>  deleteUserById(@PathVariable String id){

    userService.deleteUserById(id);
    return new ResponseEntity<>(HttpStatus.OK);
}

@GetMapping
    public ResponseEntity<List<UserResponseDto>> findUserByAll(){
    return new ResponseEntity<>(userService.findUserByAll(),HttpStatus.OK);
}


}
