package com.vino.controller;

import com.vino.webclient.WebClientTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    WebClientTest webClientTest;

    @GetMapping("/testGet")
    public void testGet() {
        webClientTest.get();
    }

    @GetMapping("/testGetParam")
    public void testGetParam() {
        webClientTest.getRequestParam();
    }

    @GetMapping("/testPostFile")
    public void testPostFile() {
        webClientTest.postFile();
    }
}
