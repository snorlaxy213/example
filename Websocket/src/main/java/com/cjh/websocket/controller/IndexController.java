package com.cjh.websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @RequestMapping(value = "toIndex")
    public ModelAndView toIndex(){
        return new ModelAndView("index");
    }
}
