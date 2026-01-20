package infrastructure.poi;

import core.reader.ExcelSource;
import core.reader.ExcelSourceFactory;
import infrastructure.poi.reader.ExcelSourcePoiDefault;

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
