package milliondata.util;

import milliondata.model.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析50万数据，花费时间4S左右
 */
public class CsvUtil {

    public static List<User> parseCsv(File file) {
        final String[] FILE_HEADER = {"user_id", "username", "name", "password", "gender", "birthday", "create_time", "create_user"};

        // 这里显式地配置一下CSV文件的Header，然后设置跳过Header（要不然读的时候会把头也当成一条记录）
        CSVFormat format = CSVFormat.DEFAULT.builder().setHeader(FILE_HEADER).setSkipHeaderRecord(true).build();
        CSVParser parser = null;

        try {
            InputStream is = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(is, "GBK");
            Reader reader = new BufferedReader(isr);
            DateTimeFormatter birthdayFormatter = DateTimeFormatter.ofPattern("yyyy-M-dd");
            DateTimeFormatter createTimeFormatter = DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm:ss");

            parser = format.parse(reader);
            List<CSVRecord> list = parser.getRecords();
            for (CSVRecord record : list) {
                User temp = new User();
                temp.setUserId(record.get("userId"));
                temp.setUsername(record.get("username"));
                temp.setUserCode(record.get("userCode"));
                temp.setGender(record.get("gender"));
                temp.setBirthday(LocalDate.parse(record.get("birthday"), birthdayFormatter));
                temp.setCreateTime(LocalDateTime.parse(record.get("create_time"), createTimeFormatter));

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
                System.out.println("关闭资源失败");
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
}
