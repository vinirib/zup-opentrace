package br.com.zup.opentrace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCepException extends RuntimeException {

    public InvalidCepException(String message){
        super(message);
    }
}
