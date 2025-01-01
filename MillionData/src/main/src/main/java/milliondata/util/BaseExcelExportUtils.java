package milliondata.util;

import com.alibaba.excel.util.StringUtils;
import milliondata.model.User;
import milliondata.parser.ExcelEventParser;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version 1.0.0
 * @description Excel通用导出类
 */
public class BaseExcelExportUtils<T> {
    
    // 2007 版本以上 最大支持1048576行
    public final static String EXCEl_FILE_2007 = "2007";
    // 2003 版本 最大支持65536 行
    public final static String EXCEL_FILE_2003 = "2003";
    
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    private static final String SERIAL_VERSION_UID = "serialVersionUID";
    
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
                    user.setUserId((String) objects[0]);
                    user.setUserCode((String) objects[1]);
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
    
    /**
     * <p>
     * 导出无头部标题行Excel <br>
     * 时间格式默认：yyyy-MM-dd HH:mm:ss(修改时间为24小时制) <br>
     * </p>
     *
     * @param title   表格标题
     * @param dataset 数据集合
     * @param out     输出流
     * @param version 2003 或者 2007，不传时默认生成2003版本
     */
    public void exportExcel(String title, Collection<T> dataset, OutputStream out, String version) {
        if (StringUtils.isEmpty(version) || EXCEL_FILE_2003.equals(version.trim())) {
            exportExcel2003(title, null, dataset, out, TIME_PATTERN);
        } else {
            exportExcel2007(title, null, dataset, out, TIME_PATTERN);
        }
    }
    
    /**
     * <p>
     * 导出带有头部标题行的Excel <br>
     * 时间格式默认：yyyy-MM-dd HH:mm:ss(修改时间为24小时制) <br>
     * </p>
     *
     * @param title   表格标题
     * @param headers 头部标题集合
     * @param dataset 数据集合
     * @param out     输出流
     * @param version 2003 或者 2007，不传时默认生成2003版本
     */
    public void exportExcel(String title, String[] headers, Collection<T> dataset, OutputStream out, String version) {
        if (StringUtils.isEmpty(version) || EXCEL_FILE_2003.equals(version.trim())) {
            exportExcel2003(title, headers, dataset, out, TIME_PATTERN);
        } else {
            exportExcel2007(title, headers, dataset, out, TIME_PATTERN);
        }
    }
    
    /**
     * <p>
     * 通用Excel导出方法,利用反射机制遍历对象的所有字段，将数据写入Excel文件中 <br>
     * 此版本生成2007以上版本的文件 (文件后缀：xlsx)
     * </p>
     *
     * @param title   表格标题名
     * @param headers 表格头部标题集合
     * @param dataset 需要显示的数据集合,集合中一定要放置符合JavaBean风格的类的对象。此方法支持的
     *                JavaBean属性的数据类型有基本数据类型及String,Date
     * @param out     与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern 如果有时间数据，设定输出格式。默认为"yyyy-MM-dd HH:mm:ss"
     */
    public void exportExcel2007(String title, String[] headers, Collection<T> dataset, OutputStream out, String pattern) {
        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(20);
        // 生成一个样式
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(new XSSFColor(java.awt.Color.gray));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        // 生成一个字体
        XSSFFont font = workbook.createFont();
        font.setBold(Boolean.TRUE);
        font.setFontName("宋体");
        font.setColor(new XSSFColor(java.awt.Color.BLACK));
        font.setFontHeightInPoints((short) 11);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        XSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(new XSSFColor(java.awt.Color.WHITE));
        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
//        style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        // 生成另一个字体
        XSSFFont font2 = workbook.createFont();
        font2.setBold(Boolean.FALSE);
        // 把字体应用到当前的样式
        style2.setFont(font2);
        
        // 产生表格标题行
        XSSFRow row = sheet.createRow(0);
        XSSFCell cellHeader;
        for (int i = 0; i < headers.length; i++) {
            cellHeader = row.createCell(i);
            cellHeader.setCellStyle(style);
            cellHeader.setCellValue(new XSSFRichTextString(headers[i]));
        }
        
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        T t;
        Field[] fields;
        Field field;
        XSSFRichTextString richString;
        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
        Matcher matcher;
        String fieldName;
        String getMethodName;
        XSSFCell cell;
        Class tCls;
        Method getMethod;
        Object value;
        String textValue;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            t = (T) it.next();
            // 利用反射，根据JavaBean属性的先后顺序，动态调用getXxx()方法得到属性值
            fields = t.getClass().getDeclaredFields();
            int k = 0;
            for (int i = 0; i < fields.length; i++) {
                field = fields[i];
                fieldName = field.getName();
                if (SERIAL_VERSION_UID.equals(fieldName)) {
                    continue;
                }
                cell = row.createCell(k);
                cell.setCellStyle(style2);
                getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                try {
                    tCls = t.getClass();
                    getMethod = tCls.getMethod(getMethodName, new Class[]{});
                    value = getMethod.invoke(t, new Object[]{});
                    // 判断值的类型后进行强制类型转换
                    textValue = null;
                    if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else if (value instanceof Float) {
                        textValue = String.valueOf((Float) value);
                        cell.setCellValue(textValue);
                    } else if (value instanceof Double) {
                        textValue = String.valueOf((Double) value);
                        cell.setCellValue(textValue);
                    } else if (value instanceof Long) {
                        cell.setCellValue((Long) value);
                    }
                    if (value instanceof Boolean) {
                        textValue = "是";
                        if (!(Boolean) value) {
                            textValue = "否";
                        }
                    } else if (value instanceof Date) {
                        textValue = sdf.format((Date) value);
                    } else {
                        // 其它数据类型都当作字符串简单处理
                        if (value != null) {
                            textValue = value.toString();
                        }
                    }
                    if (textValue != null) {
                        matcher = p.matcher(textValue);
                        if (matcher.matches()) {
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            richString = new XSSFRichTextString(textValue);
                            cell.setCellValue(richString);
                        }
                    }
                    k++;
                    if (k >= fields.length - 1) {
                        k = fields.length - 1;
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } finally {
                    // 清理资源
                }
            }
        }
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * <p>
     * 通用Excel导出方法,利用反射机制遍历对象的所有字段，将数据写入Excel文件中 <br>
     * 此方法生成2003版本的excel,文件名后缀：xls <br>
     * </p>
     *
     * @param title   表格标题名
     * @param headers 表格头部标题集合
     * @param dataset 需要显示的数据集合,集合中一定要放置符合JavaBean风格的类的对象。此方法支持的
     *                JavaBean属性的数据类型有基本数据类型及String,Date
     * @param out     与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     */
    @SuppressWarnings({"unchecked", "rawtypes", "resource"})
    public void exportExcel2003(String title, String[] headers, Collection<T> dataset, OutputStream out, String pattern) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        Sheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        //生成一个字体
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontName("宋体");
        font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        font.setFontHeightInPoints((short) 11);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        CellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成另一个字体
        Font font2 = workbook.createFont();
        font2.setBold(false);
        // 把字体应用到当前的样式
        style2.setFont(font2);
        
        // 产生表格标题行
        Row row = sheet.createRow(0);
        HSSFCell cellHeader;
        for (int i = 0; i < headers.length; i++) {
            cellHeader = (HSSFCell) row.createCell(i);
            cellHeader.setCellStyle(style);
            cellHeader.setCellValue(new HSSFRichTextString(headers[i]));
        }
        
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        T t;
        Field[] fields;
        Field field;
        HSSFRichTextString richString;
        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
        Matcher matcher;
        String fieldName;
        String getMethodName;
        HSSFCell cell;
        Class<? extends Object> tCls;
        Method getMethod;
        Object value;
        String textValue;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern == null ? "yyyy-MM-dd" : pattern);
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            t = (T) it.next();
            // 利用反射，根据JavaBean属性的先后顺序，动态调用getXxx()方法得到属性值
            fields = t.getClass().getDeclaredFields();
            //k的初始值为0
            int k = 0;
            for (int i = 0; i < fields.length; i++) {
                field = fields[i];
                fieldName = field.getName();
                //忽略此字段
                if (SERIAL_VERSION_UID.equals(fieldName)) {
                    continue;
                }
                cell = (HSSFCell) row.createCell(k);
                cell.setCellStyle(style2);
                getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                try {
                    tCls = t.getClass();
                    getMethod = tCls.getMethod(getMethodName, new Class[]{});
                    value = getMethod.invoke(t, new Object[]{});
                    // 判断值的类型后进行强制类型转换
                    textValue = null;
                    if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else if (value instanceof Float) {
                        textValue = String.valueOf((Float) value);
                        cell.setCellValue(textValue);
                    } else if (value instanceof Double) {
                        textValue = String.valueOf((Double) value);
                        cell.setCellValue(textValue);
                    } else if (value instanceof Long) {
                        cell.setCellValue((Long) value);
                    }
                    if (value instanceof Boolean) {
                        textValue = "是";
                        if (!(Boolean) value) {
                            textValue = "否";
                        }
                    } else if (value instanceof Date) {
                        textValue = sdf.format((Date) value);
                    } else {
                        // 其它数据类型都当作字符串简单处理
                        if (value != null) {
                            textValue = value.toString();
                        }
                    }
                    if (textValue != null) {
                        matcher = p.matcher(textValue);
                        if (matcher.matches()) {
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            richString = new HSSFRichTextString(textValue);
                            cell.setCellValue(richString);
                        }
                    }
                } catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException |
                         InvocationTargetException e) {
                    e.printStackTrace();
                } finally {
                    // 清理资源
                }
                
            }
        }
    }
}
