package spata.schedule2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    return new ResponseEntity<>(userService.createUser(dto.getUsername(),dto.getPassword(),dto.getEmail()),HttpStatus.CREATED);

}

@GetMapping("{id}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable String id){

    return new ResponseEntity<>(userService.findUserById(id),HttpStatus.OK);
}

@PutMapping("{id}")
    public ResponseEntity<UserResponseDto> modifyUserById(@PathVariable String id,@RequestBody UserRequestDto dto){

    return new ResponseEntity<>(userService.modifyUserById(id,dto.getUsername(),dto.getEmail()),HttpStatus.OK);

}
@PutMapping("/changepassword/{id}")
    public ResponseEntity<UserResponseDto> modifyUserpasswordById(@PathVariable String id,@RequestBody UserRequestDto dto){

    return new ResponseEntity<>(userService.modifyUserPasswordById(id,dto.getPassword()),HttpStatus.OK);
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
