package util;

import model.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import parser.ExcelEventParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * poi存在问题：解析Excel非常消耗内存，数据操作麻烦。
 *
 * 解析50万数据发生内存Java heap space OutOfMemoryError，堆内存溢出。（网上传：大于20万时即便Java内存加大至2048M也会堆溢出）
 *
 * 发生堆溢出：POI实现步骤为通过InputStream一行行读取到TreeMap类型的HSSFRow结构体中，因此当数据量大时就会造成内存溢出。
 */
public class ExcelUtil {

    public static List<User> parseExcel(File file) {
        try {
            List<User> list = new ArrayList<>();
            InputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            // 获取第几个Sheet
            Sheet sheet = workbook.getSheetAt(0);
            // 获取sheet的行数
            int rows = sheet.getPhysicalNumberOfRows();
            for (int i = 0; i < rows; i++) {
                //获取当前行的数据
                Row row = sheet.getRow(i);
                Object[] objects = new Object[row.getPhysicalNumberOfCells()];
                int index = 0;
                for (Cell cell : row) {
                    if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                        objects[index] = (int) cell.getNumericCellValue();
                    }
                    if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        objects[index] = cell.getStringCellValue();
                    }
                    if (cell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
                        objects[index] = cell.getBooleanCellValue();
                    }
                    if (cell.getCellType() == XSSFCell.CELL_TYPE_ERROR) {
                        objects[index] = cell.getErrorCellValue();
                    }
                    index++;
                }
                User user = new User();
                list.add(user);
            }

            return list;
        }catch (Exception e){
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

        return new ArrayList<>();
    }
}
