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

    private String getValueCel(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    return sdf.format(cell.getDateCellValue());
                }
                return NumberToTextConverter.toText(cell.getNumericCellValue()).trim();
            case STRING:
                return cell.getStringCellValue().replace("\u00A0", " ").trim();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue().trim();
                } catch (Exception e) {
                    return NumberToTextConverter.toText(cell.getNumericCellValue()).trim();
                }
            case BLANK:
            case _NONE:
                return "";
            default:
                return cell.toString().trim();
        }
    }
}
