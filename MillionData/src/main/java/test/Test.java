package test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import easyexcel.UserListener;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.CsvUtil;
import util.ExcelUtil;

import java.io.File;
import java.util.List;

public class Test {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserListener.class);

    private static final String excelPath = "C:\\Users\\86135\\Desktop\\sys_user.xlsx";
    private static final String csvPath = "C:\\Users\\86135\\Desktop\\sys_user.csv";

    public static void main(String[] args) {
//        csvRead();
//        excelRead();
//        eventModeExcelRead();
        simpleRead();
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

    /**
     * 最简单的读
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link UserListener}
     * <p>
     * 3. 直接读即可
     */
    public static void simpleRead() {
        long start = System.currentTimeMillis();
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        // 这里每次会读取3000条数据 然后返回过来 直接调用使用数据就行
        EasyExcel.read(excelPath, User.class, new UserListener()).sheet().doRead();
        long end = System.currentTimeMillis();
        System.out.println("使用Easy Excel解析Excel花费时间：" +(end - start) + "毫秒");
    }

}
