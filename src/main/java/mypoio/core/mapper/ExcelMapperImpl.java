package mypoio.core.mapper;

import mypoio.annotations.ExcelColumn;
import mypoio.core.reader.ExcelCell;
import mypoio.core.reader.ExcelRow;
import mypoio.core.reader.ExcelSheet;
import mypoio.core.reader.ExcelSource;
import mypoio.core.validators.ValidationEngine;
import mypoio.domain.ExcelError;
import mypoio.domain.ExcelResultItem;
import mypoio.exceptions.ExcelPipelineException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ExcelMapperImpl<T> implements ExcelMapper<T> {
    private final int startRow;
    private final ExcelSource source;
    private final Class<T> clazz;
    private final boolean skipValidation;

    public ExcelMapperImpl(int startRow, ExcelSource source, Class<T> clazz, boolean skipValidation) {
        this.startRow = startRow;
        this.source = source;
        this.clazz = clazz;
        this.skipValidation = skipValidation;
    }

    @Override
    public void mapperData(int chunkSize, Consumer<List<ExcelResultItem<T>>> chunkConsumer) {
        process(chunkSize, chunkConsumer);
    }

    private void process(int chunkSize, Consumer<List<ExcelResultItem<T>>> chunkConsumer) {
        ExcelSheet sheet = source.readSheet(clazz);
        List<MappedField> mappedFields = prepareMetadata();

        int totalRows = sheet.getLastRowNum() - startRow + 1;
        int initialCapacity = Math.min(chunkSize, Math.max(totalRows, 1000));
        List<ExcelResultItem<T>> currentChunk = new ArrayList<>(initialCapacity);

        for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
            try {
                ExcelRow excelRow = sheet.getExcelRow(i);

                if (excelRow.rowIsNullOrEmpty()) {
                    continue;
                }

                List<ExcelError> errors = new ArrayList<>();

                T object = mapRowToInstance(excelRow, mappedFields, errors);
                currentChunk.add(new ExcelResultItem<>(object, i, errors));

                if (currentChunk.size() >= chunkSize) {
                    chunkConsumer.accept(currentChunk);
                    currentChunk.clear();
                }
            } catch (Exception e) {
                if (!currentChunk.isEmpty()) {
                    chunkConsumer.accept(currentChunk);
                }
                throw new ExcelPipelineException("Unexpected failure while processing line: " + e.getMessage());
            }
        }

        if (!currentChunk.isEmpty()) {
            chunkConsumer.accept(currentChunk);
        }

    }

    private T mapRowToInstance(ExcelRow excelRow, List<MappedField> mappedFields, List<ExcelError> errors) throws Exception {
        T object = createInstance(clazz);

        for (MappedField mappedField : mappedFields) {

            ExcelColumn excelColumn = mappedField.getExcelColumn();
            Field field = mappedField.getField();

            ExcelCell excelCell = excelRow.getExcelCell(excelColumn.index());
            field.set(object, excelCell.getValue());

            if (!skipValidation) {
                ValidationEngine.validate(mappedField, excelCell, errors);
            }
        }

        return object;
    }

    private List<MappedField> prepareMetadata() {
        List<MappedField> mappedFields = new ArrayList<>();

        for (Field f : clazz.getDeclaredFields()) {
            ExcelColumn ann = f.getAnnotation(ExcelColumn.class);

            if (ann != null) {
                if (!f.getType().equals(String.class)) {
                    throw new ExcelPipelineException(String.format(
                            "Invalid field type: '%s' in class '%s' must be a String. " +
                                    "Currently, MyPoio only supports String mapping.",
                            f.getName(), clazz.getSimpleName()
                    ));
                }

                f.setAccessible(true);
                mappedFields.add(new MappedField(f, ann));
            }
        }
        return mappedFields;
    }


    private T createInstance(Class<T> clazz) throws Exception {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new ExcelPipelineException("The class " + clazz.getSimpleName() + " needs a public constructor with no arguments.");
        }
    }


}
