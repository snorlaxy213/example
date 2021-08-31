package test;

import model.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import util.ExcelUtil;

import java.io.*;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        csvRead();
    }

    private static void excelRead() {
        long start = System.currentTimeMillis();
        List<User> users = ExcelUtil.importExcel(new File("D:\\Space\\Java\\DevelopDemo\\MillionData\\src\\main\\resources\\other\\进货清单.xlsx"));
        long end = System.currentTimeMillis();
        System.out.println("解析花费时间：" +(end - start) + "毫秒");
    }

    private static void csvRead() {
        CSVParser parser = null;

        try {
            InputStream is = new FileInputStream("C:\\Users\\iso2e\\Desktop\\sys_user_202108312355.csv");
            InputStreamReader isr = new InputStreamReader(is, "GBK");
            Reader reader = new BufferedReader(isr);
            parser = CSVFormat.EXCEL
                    .withHeader("user_id", "username", "name", "password", "gender", "birthday", "create_time", "create_user")
                    .parse(reader);
//            parser = CSVParser.parse(reader, CSVFormat.DEFAULT.withHeader("name", "age", "jia"));

            List<CSVRecord> list = parser.getRecords();
            for (CSVRecord record : list) {
                System.out.println(record.getRecordNumber()
                        + ":" + record.get("name")
                        + ":" + record.get("age")
                        + ":" + record.get("jia"));
            }
        } catch (FileNotFoundException e) {
            System.out.println("CSV文件找不到");
        } catch (UnsupportedEncodingException e) {
            System.out.println("CSV文件编码不正确");
        } catch (IOException e) {
            System.out.println("CSV文件解析出现错误");
        } finally {
            try {
                if (parser != null) {
                    parser.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
