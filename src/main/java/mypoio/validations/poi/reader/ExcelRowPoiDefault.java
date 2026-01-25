package mypoio.validations.poi.reader;

import mypoio.core.reader.ExcelCell;
import mypoio.core.reader.ExcelRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

public class ExcelRowPoiDefault implements ExcelRow {

    private final Row row;

    public ExcelRowPoiDefault(Row row) {
        this.row = row;
    }

    @Override
    public ExcelCell getExcelCell(int columnNumber) {
        Cell cell = row.getCell(columnNumber, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return new ExcelCellPoiDefault(cell);
    }

    public boolean rowIsNullOrEmpty() {
        return row == null || isRowEmpty();
    }

    private boolean isRowEmpty() {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}
