package test;

import model.User;
import parser.ExcelEventParser;
import util.CsvUtil;
import util.ExcelUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test {

    private static final String excelPath = "D:\\Space\\Java\\DevelopDemo\\MillionData\\src\\main\\resources\\other\\进货清单.xlsx";
    private static final String csvPath = "C:\\Users\\iso2e\\Desktop\\sys_user_202108312355.csv";

    public static void main(String[] args) {
//        csvRead();
//        excelRead();
        eventModeExcelRead();
    }

    private static void excelRead() {
        long start = System.currentTimeMillis();
        List<User> users = ExcelUtil.importExcel(new File(excelPath));
        long end = System.currentTimeMillis();
        System.out.println("解析Excel花费时间：" +(end - start) + "毫秒");
    }

    private static void eventModeExcelRead() {
        long start = System.currentTimeMillis();

        final List<List<String>> table = new ArrayList<>();
        new ExcelEventParser(excelPath).setHandler(new ExcelEventParser.SimpleSheetContentsHandler(){

            private List<String> fields;

            @Override
            public void endRow(int rowNum) {
                if(rowNum == 0){
                    // 第一行中文描述忽略
                }else if(rowNum == 1){
                    // 第二行字段名
                    fields = row;
                }else {
                    // 数据
                    table.add(row);
                }
            }
        }).parse();

        long end = System.currentTimeMillis();

        System.err.println(table.size());
        System.err.println(end - start);
    }

    private static void csvRead() {
        long start = System.currentTimeMillis();
        List<User> users = CsvUtil.parseCsv(new File(csvPath));
        long end = System.currentTimeMillis();
        System.out.println("解析Csv花费时间：" +(end - start) + "毫秒");
    }

}
