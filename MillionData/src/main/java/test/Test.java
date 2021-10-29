package test;

import com.alibaba.excel.EasyExcel;
import easyexcel.UserListener;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.CsvUtil;
import util.ExcelUtil;

import java.io.File;
import java.util.List;

/**
 * 读取Excel测试类
 *
 * @author 86135
 */
public class Test {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserListener.class);

    private static final String excelPath = "C:\\Users\\86135\\Desktop\\sys_user.xlsx";
    private static final String csvPath = "C:\\Users\\86135\\Desktop\\sys_user.csv";

    public static void main(String[] args) {
        simpleRead();
    }

    private static void csvRead() {
        long start = System.currentTimeMillis();
        List<User> users = CsvUtil.parseCsv(new File(csvPath));
        long end = System.currentTimeMillis();
        System.out.println("解析Csv花费时间：" + (end - start) + "毫秒");
    }

    private static void excelRead() {
        long start = System.currentTimeMillis();
        List<User> users = ExcelUtil.parseExcel(new File(excelPath));
        long end = System.currentTimeMillis();
        System.out.println("解析Excel花费时间：" + (end - start) + "毫秒");
    }

    private static void eventModeExcelRead() {
        long start = System.currentTimeMillis();
        List<User> users = ExcelUtil.parseExcelByEventMode(excelPath);
        long end = System.currentTimeMillis();
        System.out.println("使用POI事件驱动模式解析Excel花费时间：" + (end - start) + "毫秒");
    }

    public static void simpleRead() {
        long start = System.currentTimeMillis();
        EasyExcel.read(excelPath, User.class, new UserListener()).sheet().doRead();
        long end = System.currentTimeMillis();
        System.out.println("使用Easy Excel解析Excel花费时间：" + (end - start) + "毫秒");
    }

}
