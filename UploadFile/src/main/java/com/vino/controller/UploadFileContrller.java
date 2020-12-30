package com.vino.controller;

import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class UploadFileContrller {

    public Long upload(@RequestParam(name = "file") MultipartFile file) {
        try {
            File localFile = ResourceUtils.getFile("classpath:file/");
            if (localFile.isFile()) {
                localFile.mkdir();
            }

            byte[] bytes = file.getBytes();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 1L;
    }
}