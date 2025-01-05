package spata.schedule2.Exception;

import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

public record ExceptionDto(HttpStatusCode statusCode, String message, LocalDateTime dateTime) {
    public ExceptionDto(HttpStatusCode statusCode,String message){
        this(statusCode,message,LocalDateTime.now());
    }
}
