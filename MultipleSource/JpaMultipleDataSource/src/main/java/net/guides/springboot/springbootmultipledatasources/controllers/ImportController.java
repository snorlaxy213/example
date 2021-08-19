package net.guides.springboot.springbootmultipledatasources.controllers;

import net.guides.springboot.springbootmultipledatasources.security.entities.User;
import net.guides.springboot.springbootmultipledatasources.services.UserOrdersService;
import net.guides.springboot.springbootmultipledatasources.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;

@RestController
public class ImportController {

    @Autowired
    private UserOrdersService userOrdersService;

    @GetMapping("importData")
    public Integer importData() throws Exception {
        long start = System.currentTimeMillis();
        List<User> users = ExcelUtil.importExcel(new File("D:\\Space\\Java\\DevelopDemo\\MultipleSource\\JpaMultipleDataSource\\src\\main\\resources\\other\\进货清单.xlsx"));
        long end = System.currentTimeMillis();
        System.out.println("解析花费时间：" +(end - start) + "毫秒");

        long importStart = System.currentTimeMillis();
        userOrdersService.saveUser(users);
        long importEnd = System.currentTimeMillis();
        System.out.println("插入花费时间：" +(importEnd - importStart) + "毫秒");

        return 1;
    }
}
