package core.mapper;

import annotations.ExcelColumn;
import core.reader.ExcelCell;
import core.reader.ExcelRow;
import core.reader.ExcelSheet;
import core.reader.ExcelSource;
import core.validator.ValidationEngine;
import domain.ErrorCode;
import domain.ExcelError;
import domain.ExcelResult;
import domain.ExcelResultItem;
import exceptions.ExcelPipelineException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectionExcelMapper<T> implements ExcelMapper<T> {
    private final int startRow;
    private final ExcelSource source;
    private final Class<T> clazz;
    private final ValidationEngine validationEngine;

    public ReflectionExcelMapper(int startRow, ExcelSource source, Class<T> clazz, ValidationEngine validationEngine) {
        this.startRow = startRow;
        this.source = source;
        this.clazz = clazz;
        this.validationEngine = validationEngine;
    }

    public ExcelResult<T> mapperData() {
        ExcelResult<T> excelResult = new ExcelResult<>();

        ExcelSheet sheet = source.readSheet(clazz);

        Field[] fields = clazz.getDeclaredFields();

        for (Field f : fields) {
            f.setAccessible(true);
        }

        for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
            try {
                List<ExcelError> errors = new ArrayList<>();
                ExcelRow excelRow = sheet.getExcelRow(i);

                if (excelRow.rowIsNullOrEmpty()) {
                    continue;
                }

                T object = createInstance(clazz);

                for (Field field : fields) {
                    ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);

                    if (excelColumn == null) {
                        continue;
                    }

                    ExcelCell excelCell = excelRow.getExcelCell(excelColumn.index());
                    field.set(object, excelCell.getValue());

                    if (this.validationEngine != null) {
                        this.validationEngine.validate(field, excelCell, errors);
                    }
                }
                excelResult.addRow(new ExcelResultItem<>(object, i, errors));
            } catch (Exception e) {
                excelResult.addGeneralError(
                        ExcelError.of(ErrorCode.UNKNOWN, "Unexpected failure while processing line: " + e.getMessage())
                );
            }
        }

        return excelResult;
    }


    private T createInstance(Class<T> clazz) throws Exception {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new ExcelPipelineException("The class " + clazz.getSimpleName() + " needs a public constructor with no arguments.");
        }
    }
}
