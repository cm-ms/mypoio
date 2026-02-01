package mypoio.validations.poi.reader;

import mypoio.core.reader.ExcelRow;
import mypoio.core.reader.ExcelSheet;
import org.apache.poi.ss.usermodel.Row;
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

    @Override
    public Iterator<ExcelRow> iterator() {
        Iterator<Row> poiIterator = sheet.iterator();

        return new Iterator<ExcelRow>() {
            @Override
            public boolean hasNext() {
                return poiIterator.hasNext();
            }

            @Override
            public ExcelRow next() {
                return new ExcelRowPoiDefault(poiIterator.next());
            }
        };
    }
}
