package com.vino.controller;

import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

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

    @RequestMapping(value = "/images",method = RequestMethod.POST)
    @ResponseBody
    public HttpServletResponse getImages(HttpServletRequest request, HttpServletResponse response) {
        try {

            File image = null;
            try {
                image = ResourceUtils.getFile("classpath:images/pikachu.jpg");
            } catch (FileNotFoundException e) {
                System.out.println("Error reading file");
            }

            InputStream fis = new BufferedInputStream(new FileInputStream(image));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Length", "" + image.length());
            response.setContentType("image/png");

            OutputStream toClient = new BufferedOutputStream(response.getOutputStream(),524000);
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }
}
