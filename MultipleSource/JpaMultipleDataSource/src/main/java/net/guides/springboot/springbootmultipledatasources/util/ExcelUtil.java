package net.guides.springboot.springbootmultipledatasources.util;

import net.guides.springboot.springbootmultipledatasources.security.entities.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    public static List<User> importExcel(File file) {
        System.out.println("导入解析开始！");
        try {
            List<User> list = new ArrayList<>();
            InputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            //获取sheet的行数
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
                user.setId((int)objects[0]);
                user.setName((String) objects[1]);
                user.setEmail((String) objects[2]);
                list.add(user);
            }
            System.out.println("导入文件解析成功！");
            return list;
        }catch (Exception e){
            System.out.println("导入文件解析失败！");
            e.printStackTrace();
        }
        return null;
    }
}
