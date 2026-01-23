package mypoio.infrastructure.poi;

import mypoio.core.reader.ExcelSource;
import mypoio.core.reader.ExcelSourceFactory;
import mypoio.infrastructure.poi.reader.ExcelSourcePoiDefault;

import java.io.InputStream;

public class ExcelSourceFactoryPoiDefault implements ExcelSourceFactory {
    @Override
    public ExcelSource create(String filePath) {
        return new ExcelSourcePoiDefault(filePath);
    }

    @Override
    public ExcelSource create(InputStream inputStream) {
        return new ExcelSourcePoiDefault(inputStream);
    }
}
