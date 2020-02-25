package br.com.zup.opentrace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidOutputFormatException extends RuntimeException {

    public InvalidOutputFormatException(String message){
        super(message);
    }
}
