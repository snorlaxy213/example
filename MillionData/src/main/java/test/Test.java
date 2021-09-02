package test;

import model.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import util.CsvUtil;
import util.ExcelUtil;

import java.io.*;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        csvRead();
    }

    private static void excelRead() {
        long start = System.currentTimeMillis();
        List<User> users = ExcelUtil.importExcel(new File("D:\\Space\\Java\\DevelopDemo\\MillionData\\src\\main\\resources\\other\\进货清单.xlsx"));
        long end = System.currentTimeMillis();
        System.out.println("解析Excel花费时间：" +(end - start) + "毫秒");
    }

    private static void csvRead() {
        long start = System.currentTimeMillis();
        List<User> users = CsvUtil.parseCsv(new File("C:\\Users\\iso2e\\Desktop\\sys_user_202108312355.csv"));
        long end = System.currentTimeMillis();
        System.out.println("解析Csv花费时间：" +(end - start) + "毫秒");
    }

}
