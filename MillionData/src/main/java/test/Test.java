package test;

import model.User;
import parser.ExcelEventParser;
import util.CsvUtil;
import util.ExcelUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test {

    private static final String excelPath = "C:\\Users\\86135\\Desktop\\sys_user.xlsx";
    private static final String csvPath = "C:\\Users\\86135\\Desktop\\sys_user.csv";

    public static void main(String[] args) {
//        csvRead();
//        excelRead();
        eventModeExcelRead();
    }

    private static void csvRead() {
        long start = System.currentTimeMillis();
        List<User> users = CsvUtil.parseCsv(new File(csvPath));
        long end = System.currentTimeMillis();
        System.out.println("解析Csv花费时间：" +(end - start) + "毫秒");
    }

    private static void excelRead() {
        long start = System.currentTimeMillis();
        List<User> users = ExcelUtil.parseExcel(new File(excelPath));
        long end = System.currentTimeMillis();
        System.out.println("解析Excel花费时间：" +(end - start) + "毫秒");
    }

    private static void eventModeExcelRead() {
        long start = System.currentTimeMillis();
        List<User> users = ExcelUtil.parseExcelByEventMode(excelPath);
        long end = System.currentTimeMillis();
        System.out.println("使用POI事件驱动模式解析Excel花费时间：" +(end - start) + "毫秒");
    }

}
