package util;

import model.User;
import org.apache.poi.ss.usermodel.*;
import parser.ExcelEventParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Grio Vino
 */
public class ExcelUtil {

    /**
     * poi存在问题：解析Excel非常消耗内存，数据操作麻烦。
     * <p>
     * 解析50万数据发生内存Java heap space OutOfMemoryError，堆内存溢出。（网上传：大于20万时即便Java内存加大至2048M也会堆溢出）
     * <p>
     * 发生堆溢出：POI实现步骤为通过InputStream一行行读取到TreeMap类型的HSSFRow结构体中，因此当数据量大时就会造成内存溢出。
     * <p>
     * 测试30条数据解析要5S，效率很差
     */

    public static List<User> parseExcel(File file) {
        try {
            List<User> list = new ArrayList<>();
            InputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            // 获取第几个Sheet
            Sheet sheet = workbook.getSheetAt(0);
            // 获取sheet的行数
            int rows = sheet.getPhysicalNumberOfRows();
            // 从第一行开始， 不要Header
            for (int i = 1; i < rows; i++) {
                //获取当前行的数据
                Row row = sheet.getRow(i);
                Object[] objects = new Object[row.getPhysicalNumberOfCells()];
                int index = 0;
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.NUMERIC) {
                        objects[index] = (int) cell.getNumericCellValue();
                    }
                    if (cell.getCellType() == CellType.STRING) {
                        objects[index] = cell.getStringCellValue();
                    }
                    if (cell.getCellType() == CellType.BOOLEAN) {
                        objects[index] = cell.getBooleanCellValue();
                    }
                    if (cell.getCellType() == CellType.ERROR) {
                        objects[index] = cell.getErrorCellValue();
                    }
                    index++;
                }

                // 组装User信息, objects[0] != null意义在于防止最后一行空指针
                if (objects[0] != null) {
                    User user = new User();
                    user.setUserId((int) objects[0]);
                    user.setUsername((String) objects[1]);
                    user.setName((String) objects[2]);
                    user.setPassword((String) objects[3]);
                    user.setGender((String) objects[4]);
                    // todo 解析日期格式
//                user.setBirthday((LocalDate) objects[5]);
//                user.setCreateTime((LocalDateTime) objects[6]);
                    user.setCreateUser((String) objects[7]);
                    list.add(user);
                }
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * POI事件驱动模式需在Java8环境运行
     *
     * @param excelPath
     * @return
     */
    public static List<User> parseExcelByEventMode(String excelPath) {
        final List<List<String>> table = new ArrayList<>();
        new ExcelEventParser(excelPath).setHandler(new ExcelEventParser.SimpleSheetContentsHandler() {
            private List<String> fields;

            @Override
            public void endRow(int rowNum) {
                if (rowNum == 0) {
                    // 第一行中文描述忽略
                } else if (rowNum == 1) {
                    // 第二行字段名
                    fields = row;
                } else {
                    // 数据
                    table.add(row);
                }
            }
        }).parse();

        return new ArrayList<>();
    }
}
