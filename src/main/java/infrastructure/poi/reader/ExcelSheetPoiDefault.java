package infrastructure.poi.reader;

import core.reader.ExcelRow;
import core.reader.ExcelSheet;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Iterator;

public class ExcelSheetPoiDefault implements ExcelSheet {
    private final Sheet sheet;

    public ExcelSheetPoiDefault(Sheet sheet) {
        this.sheet = sheet;
    }


    @Override
    public int getLastRowNum() {
        return sheet.getLastRowNum();
    }

    @Override
    public ExcelRow getExcelRow(int rowNumber) {
        return new ExcelRowPoiDefault(sheet.getRow(rowNumber));
    }
}
