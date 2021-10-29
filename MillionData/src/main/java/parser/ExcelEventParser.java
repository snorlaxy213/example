package parser;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * POI事件驱动模型解析器
 *
 * @author Chen Jiaying
 */
public class ExcelEventParser {

    private final String filename;
    private SheetContentsHandler handler;

    public ExcelEventParser(String filename) {
        this.filename = filename;
    }

    public ExcelEventParser setHandler(SheetContentsHandler handler) {
        this.handler = handler;
        return this;
    }

    public void parse() {
        OPCPackage pkg = null;
        InputStream sheetInputStream = null;

        try {
            pkg = OPCPackage.open(filename, PackageAccess.READ);
            XSSFReader xssfReader = new XSSFReader(pkg);

            StylesTable styles = xssfReader.getStylesTable();
            // ZipSecureFile$ThresholdInputStream cannot be cast to class ZipFile$ZipFileInputStream
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);
            sheetInputStream = xssfReader.getSheetsData().next();

            processSheet(styles, strings, sheetInputStream);
        } catch (Exception e) {
            System.out.println("Excel事件驱动模式解析失败");
            e.printStackTrace();
        } finally {
            if (sheetInputStream != null) {
                try {
                    sheetInputStream.close();
                } catch (IOException e) {
                    System.out.println("关闭InputStream资源失败");
                    e.printStackTrace();
                }
            }
            if (pkg != null) {
                try {
                    pkg.close();
                } catch (IOException e) {
                    System.out.println("关闭OPCPackage资源失败");
                    e.printStackTrace();
                }
            }
        }
    }

    private void processSheet(StylesTable styles, ReadOnlySharedStringsTable strings, InputStream sheetInputStream) throws SAXException, ParserConfigurationException, IOException {
        XMLReader sheetParser = XMLHelper.newXMLReader();
        sheetParser.setContentHandler(new XSSFSheetXMLHandler(styles, strings, Objects.requireNonNullElseGet(handler, SimpleSheetContentsHandler::new), false));
        sheetParser.parse(new InputSource(sheetInputStream));
    }

    public static class SimpleSheetContentsHandler implements SheetContentsHandler {
        protected List<String> row = new LinkedList<>();

        @Override
        public void startRow(int rowNum) {
            row.clear();
        }

        @Override
        public void endRow(int rowNum) {
            System.err.println(rowNum + " : " + row);
        }

        @Override
        public void cell(String cellReference, String formattedValue, XSSFComment comment) {
            row.add(formattedValue);
        }

        @Override
        public void headerFooter(String text, boolean isHeader, String tagName) {

        }
    }
}