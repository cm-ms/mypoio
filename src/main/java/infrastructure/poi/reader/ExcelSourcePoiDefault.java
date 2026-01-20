package infrastructure.poi.reader;

import annotations.ExcelModel;
import core.reader.ExcelSheet;
import core.reader.ExcelSource;
import exceptions.ExcelPipelineException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ExcelSourcePoiDefault implements ExcelSource {
    // private static final Logger log = LoggerFactory.getLogger(ExcelSourcePoiDefault.class);

    private final Workbook workbook;

    public ExcelSourcePoiDefault(String fileName) {
        try {
            this.workbook = WorkbookFactory.create(new File(fileName));
        } catch (IOException e) {
            throw new ExcelPipelineException(e.getMessage());
        }
    }

    public ExcelSourcePoiDefault(InputStream inputStream) {
        try {
            this.workbook = WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            throw new ExcelPipelineException(e.getMessage());
        }
    }


    @Override
    public <T> ExcelSheet readSheet(Class<T> clazz) {
        ExcelModel excelModel = clazz.getAnnotation(ExcelModel.class);

        Sheet sheet = (excelModel != null && !excelModel.name().isBlank())
                ? workbook.getSheet(excelModel.name())
                : workbook.getSheetAt(excelModel.index());

        return new ExcelSheetPoiDefault(sheet);
    }

    @Override
    public void close() {
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException e) {
                //log.error("Warning: Failed to close Workbook: {}", e.getMessage());
            }
        }
    }
}
