package com.tanhua.sso.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ExceptionHandler
    public String exceptionHandler(Exception e) {
        System.out.println(e);
        log.error(e.getMessage());
        return e.getMessage();
    }
}
