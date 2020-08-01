package com.thoughtworks.springbootemployee.common;

import com.thoughtworks.springbootemployee.exception.IllegalOperationException;
import com.thoughtworks.springbootemployee.exception.NoSuchDataException;
import org.springframework.beans.BeansException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchDataException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String handleNoSuchDataException() {
        return ExceptionMessage.NO_SUCH_DATA.getErrorMsg();
    }

    @ExceptionHandler(IllegalOperationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String handleIllegalOperationException() {
        return ExceptionMessage.ILLEGAL_OPERATION.getErrorMsg();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_EXTENDED)
    String handleIllegalArgumentException() {
        return ExceptionMessage.IllegalArgumentException.getErrorMsg();
    }

    @ExceptionHandler(BeansException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    String handleTransBeansException() {
        return HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();
    }
}
