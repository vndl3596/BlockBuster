package com.example.demo.exception;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.Date;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ErrorMessage runErrorMessage(RuntimeException ex) {
        return new ErrorMessage(401, ex.getMessage(), new Date());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorMessage handleAllException(ConstraintViolationException ex, WebRequest request) {
        return new ErrorMessage(400, ex.getMessage(), new Date(), request.getDescription(false));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ErrorMessage UsernameNotFoundException(UsernameNotFoundException ex) {
        return new ErrorMessage(211, ex.getMessage(), new Date());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ErrorMessage BadCredentialsException() {
        return new ErrorMessage(404, "Wrong login or account not activated", new Date());
    }

    @ExceptionHandler(Exception.class)
    public ErrorMessage Exception(Exception ex) {
        return new ErrorMessage(ex.hashCode(), ex.getMessage(), new Date());
    }

    @ExceptionHandler(AccountExeption.class)
    public ErrorMessage AccountNotFoundException(AccountExeption ex) {
        return new ErrorMessage(404, ex.getMessage(), new Date());
    }

    @ExceptionHandler(MailException.class)
    public ErrorMessage EmailNotException(MailException ex) {
        return new ErrorMessage(1, ex.getMessage(), new Date());
    }

    @ExceptionHandler(UsernameException.class)
    public ErrorMessage UserExceptionNotFound(UsernameException ex) {
        return new ErrorMessage(2, ex.getMessage(), new Date());
    }

    @ExceptionHandler(UsernameExitException.class)
    public ErrorMessage UserExceptionEmail(UsernameExitException ex, WebRequest request) {
        return new ErrorMessage(3, ex.getMessage(), new Date());
    }

}
