package com.vino.controller;

import com.vino.model.DateTimeVo;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
public class DateTimeSerializeController {

    @GetMapping("/testDate")
    public String testDate(@RequestParam(value = "date") Date date) {
        return date.toString();
    }

    @GetMapping("/testLocalDateTime")
    public String testLocalDateTime(@RequestParam(value = "localDateTime") LocalDateTime localDateTime) {
        return localDateTime.toString();
    }

    @PostMapping("/testVo")
    public DateTimeVo testVo(@RequestBody DateTimeVo dateTimeVo) {
        return dateTimeVo;
    }
}
