package mypoio.core;

import mypoio.core.reader.ExcelSource;
import mypoio.domain.ExcelResultItem;

import java.util.ArrayList;
import java.util.List;

public class ExcelMappingContext<T> {
    private final Class<T> clazz;
    private ExcelSource source;
    private int offsetRow;
    private int limit;
    private int chunkSize;
    private boolean skipValidation;

    public ExcelMappingContext(Class<T> clazz) {
        this.clazz = clazz;
        this.offsetRow = 1;
        this.limit = 0;
        this.chunkSize = 1000;
        this.skipValidation = false;
    }

    public List<ExcelResultItem<T>> createChunkBuffer() {
        int capacity = (chunkSize > 0 && chunkSize <= 5000) ? chunkSize : 5000;
        return new ArrayList<>(capacity);
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public ExcelSource getSource() {
        return source;
    }

    public void setSource(ExcelSource source) {
        this.source = source;
    }

    public int getOffsetRow() {
        return offsetRow;
    }

    public void setOffsetRow(int offsetRow) {
        this.offsetRow = offsetRow;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public boolean isSkipValidation() {
        return skipValidation;
    }

    public void setSkipValidation(boolean skipValidation) {
        this.skipValidation = skipValidation;
    }
}
