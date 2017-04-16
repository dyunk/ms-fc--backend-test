package com.scmspain.handlers;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.scmspain.entities.ErrorMessage;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ErrorMessage invalidArgumentException(IllegalArgumentException ex) {
	LOGGER.error(ex.getMessage());
	return new ErrorMessage(ex.getMessage(), ex.getClass().getSimpleName());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    public ErrorMessage entityNotFoundException(EntityNotFoundException ex) {
	LOGGER.error(ex.getMessage());
	return new ErrorMessage(ex.getMessage(), ex.getClass().getSimpleName());
    }
}
