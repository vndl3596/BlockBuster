package com.example.blockbuster.controller;

import com.example.blockbuster.apicall.UserCall;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class ForgetPassController {
    @RequestMapping(method = RequestMethod.GET, path = "forget-pass")
    public ModelAndView forgetShow() {
        return new ModelAndView("forgetpass");
    }

    @RequestMapping(method = RequestMethod.POST, path = "forget-pass")
    public ModelAndView forget(@RequestParam(value = "email", required = true) String mail, Model model) {
        if (UserCall.forgetPassword(mail).equals("true")) {
            model.addAttribute("error", "Lấy lại mật khẩu thành công! Vui lòng check email!");
        } else model.addAttribute("error", "Lấy lại mật khẩu thất bại! Email không có trong kho dữ liệu!");

        return new ModelAndView("forgetpass");
    }
}
