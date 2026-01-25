package mypoio.validations.poi.reader;

import mypoio.core.reader.ExcelCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.text.SimpleDateFormat;

public class ExcelCellPoiDefault implements ExcelCell {

    private final Cell cell;

    public ExcelCellPoiDefault(Cell cell) {
        this.cell = cell;
    }

    @Override
    public String getAddress() {
        return cell.getAddress().toString();
    }

    @Override
    public String getValue() {
        return getValueCel(cell);
    }

    @Override
    public int getColumnNum() {
        return cell.getColumnIndex();
    }

    @Override
    public int getRowNum() {
        return cell.getRowIndex();
    }

    private String getValueCel(Cell cell) {
        if (cell == null) return "";

        if (CellType.NUMERIC.equals(cell.getCellType())) {
            if (DateUtil.isCellDateFormatted(cell)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                return sdf.format(cell.getDateCellValue());
            }

            return NumberToTextConverter.toText(cell.getNumericCellValue()).trim();
        }

        if (CellType.STRING.equals(cell.getCellType())) {
            return cell.getStringCellValue().replace("\u00A0", "").trim();
        }

        return cell.toString();
    }
}
