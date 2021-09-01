package test;

import model.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
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
        System.out.println("解析花费时间：" +(end - start) + "毫秒");
    }

    private static void csvRead() {
        final String[] FILE_HEADER = {"user_id", "username", "name", "password", "gender", "birthday", "create_time", "create_user"};

        // 这里显式地配置一下CSV文件的Header，然后设置跳过Header（要不然读的时候会把头也当成一条记录）
        CSVFormat format = CSVFormat.DEFAULT.builder().setHeader(FILE_HEADER).setSkipHeaderRecord(true).build();
        CSVParser parser = null;

        long start = System.currentTimeMillis();
        try {
            InputStream is = new FileInputStream("C:\\Users\\iso2e\\Desktop\\sys_user_202108312355.csv");
            InputStreamReader isr = new InputStreamReader(is, "GBK");
            Reader reader = new BufferedReader(isr);
            DateTimeFormatter birthdayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter createTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            parser = format.parse(reader);
            List<CSVRecord> list = parser.getRecords();
            for (CSVRecord record : list) {
                User temp = new User();
                temp.setUserId(Integer.valueOf(record.get("user_id")));
                temp.setUsername(record.get("username"));
                temp.setName(record.get("name"));
                temp.setPassword(record.get("password"));
                temp.setGender(record.get("gender"));
                temp.setBirthday(LocalDate.parse(record.get("birthday") ,birthdayFormatter));
                temp.setCreateTime(LocalDateTime.parse(record.get("create_time") ,createTimeFormatter));
                temp.setPassword(record.get("create_user"));

                System.out.println(temp);
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
        long end = System.currentTimeMillis();
        System.out.println("CSV解析完成时间：" + (end - start));

    }

}
