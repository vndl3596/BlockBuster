package com.example.blockbuster.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class HelloController {
    @RequestMapping("/")
    public ModelAndView hello(HttpServletResponse response) throws IOException {
        response.sendRedirect("/home");
        return new ModelAndView("hello");
    }
}
