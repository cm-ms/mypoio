package mypoio.infrastructure.poi.reader;

import mypoio.core.reader.ExcelRow;
import mypoio.core.reader.ExcelSheet;
import org.apache.poi.ss.usermodel.Sheet;

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
