package spata.schedule2.Exception;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
@Slf4j
@ControllerAdvice
public class GlobalException  {


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionDto> responseStatusException(ResponseStatusException e) {

        return new ResponseEntity<>(new ExceptionDto(e.getStatusCode(), e.getReason()), e.getStatusCode());
    }

    @ExceptionHandler(LoginWebException.class)
    public ModelAndView loginWebException( HttpServletRequest request, Exception e) {
        log.info(e.getMessage());
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("message", e.getMessage());
        mav.addObject("checkSignIn", true);
        mav.addObject("signInUrl", "/web/login");
        return mav;
    }

}
