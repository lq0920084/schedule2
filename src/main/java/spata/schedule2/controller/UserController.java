package spata.schedule2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import spata.schedule2.dto.*;
import spata.schedule2.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<Void> loginUser( HttpServletRequest request){
        if (request.getAttribute("statusCode")==null){
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            throw new ResponseStatusException((HttpStatusCode)request.getAttribute("statusCode"),(String)request.getAttribute("message"));
        }
    }
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        session.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

@PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Validated @RequestBody UserRequestDto dto, BindingResult bindingResult){
    if(bindingResult.hasErrors()){
        for(FieldError fieldError : bindingResult.getFieldErrors()){
            log.info(fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(userService.createUser(dto),HttpStatus.CREATED);

}

@GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable String id){

    return new ResponseEntity<>(userService.findUserById(id),HttpStatus.OK);
}

@PutMapping("/{id}")
    public ResponseEntity<Void> modifyUserById(@PathVariable String id,@Validated @RequestBody UserRequestDto userRequestDto,BindingResult bindingResult){
    if(bindingResult.hasErrors()){
        for(FieldError fieldError : bindingResult.getFieldErrors()){
            log.info(fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
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

@DeleteMapping("/{id}")
    public ResponseEntity<Void>  deleteUserById(@PathVariable String id, @RequestBody ResignUserRequestDto dto){


    if(userService.resignUser(id,dto)) {
        return new ResponseEntity<>(HttpStatus.OK);
    }else {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

@GetMapping
    public ResponseEntity<List<UserResponseDto>> findUserByAll(){
    return new ResponseEntity<>(userService.findUserByAll(),HttpStatus.OK);
}


}
