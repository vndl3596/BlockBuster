package com.example.blockbuster.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class ComingSoonController {
    @RequestMapping("comingsoon")
    public ModelAndView comingShow() {
        return new ModelAndView("comingsoon");
    }

    @RequestMapping("movie-detail/comingsoon")
    public ModelAndView moviecomingShow() {
        return new ModelAndView("comingsoon");
    }
}
