import com.alibaba.excel.EasyExcel;
import milliondata.easyexcel.UserListener;
import milliondata.model.User;
import milliondata.util.BaseExcelExportUtils;

import java.io.File;
import java.util.List;

public class MultipleExcelImportTest {
    
    //解析excelPath路径下的xlsx文件
    private static String excelPath = "E:\\User\\Desktop\\user.xlsx";
    
    public static void main(String[] args) {
        simpleRead();
    }

    private static void excelRead() {
        // 读取Excel
        long start = System.currentTimeMillis();
        List<User> users = BaseExcelExportUtils.parseExcel(new File(excelPath));
        long end = System.currentTimeMillis();
        System.out.println("解析Excel花费时间：" + (end - start) + "毫秒");
    }

    private static void eventModeExcelRead() {
        // 事件驱动模式读取Excel
        long start = System.currentTimeMillis();
        List<User> users = BaseExcelExportUtils.parseExcelByEventMode(excelPath);
        long end = System.currentTimeMillis();
        System.out.println("使用POI事件驱动模式解析Excel花费时间：" + (end - start) + "毫秒");
    }

    public static void simpleRead() {
        // 简单读取Excel
        long start = System.currentTimeMillis();
        EasyExcel.read(excelPath, User.class, new UserListener()).sheet().doRead();
        long end = System.currentTimeMillis();
        System.out.println("使用Easy Excel解析Excel花费时间：" + (end - start) + "毫秒");
    }

}
