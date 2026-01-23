package mypoio.infrastructure.poi.reader;

import mypoio.annotations.ExcelModel;
import mypoio.core.reader.ExcelSheet;
import mypoio.core.reader.ExcelSource;
import mypoio.exceptions.ExcelPipelineException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ExcelSourcePoiDefault implements ExcelSource {

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

        Sheet sheet = (excelModel != null)
                ? workbook.getSheetAt(excelModel.index())
                : workbook.getSheetAt(0); // Todo: using 0 default

        return new ExcelSheetPoiDefault(sheet);
    }

    @Override
    public void close() {
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException e) {
                throw new ExcelPipelineException("Error closing workbook. Check if the file is open. Message: {}", e.getMessage());
            }
        }
    }
}
