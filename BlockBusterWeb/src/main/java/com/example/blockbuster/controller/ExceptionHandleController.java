package com.example.blockbuster.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class ExceptionHandleController implements ErrorController {
    @RequestMapping("error")
    public ModelAndView exceptionHandle(HttpServletResponse res){
        return new ModelAndView("error");
    }
}
