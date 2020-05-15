package com.vino.controller;

import com.vino.model.DateTimeVo;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
public class DateTimeSerializeController {

    //https://www.cnblogs.com/childking/p/12154431.html

    @GetMapping("/testDate")
    public String testDate(@RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date) {
        return date.toString();
    }

    @GetMapping("/testLocalDateTime")
    public String testLocalDateTime(@RequestParam(value = "localDateTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime localDateTime) {
        return localDateTime.toString();
    }

    @PostMapping("/testVo")
    public String testVo(@RequestBody DateTimeVo dateTimeVo) {
        return dateTimeVo.toString();
    }
}
