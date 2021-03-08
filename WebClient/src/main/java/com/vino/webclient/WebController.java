package com.vino.webclient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @GetMapping("/get")
    public String get() {
        return "get method test successful";
    }

    @GetMapping("/getParam/{id}")
    public String getParam(@PathVariable("id") Long id,
                         @RequestParam("name") String name) {
        return "id: " + id + " " + "name: " + name;
    }

}
