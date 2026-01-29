package mypoio.core.mapper;

import mypoio.annotations.ExcelColumn;
import mypoio.core.ExcelMappingContext;
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

public class ExcelMapperImpl implements ExcelMapper {

    @Override
    public <T> void mapperData(ExcelMappingContext<T> context, Consumer<List<ExcelResultItem<T>>> chunkConsumer) {
        ExcelSource source = context.getSource();
        Class<T> clazz = context.getClazz();

        ExcelSheet sheet = source.readSheet(clazz);
        List<MappedField> mappedFields = prepareMetadata(clazz);

        int lastRowInSheet = sheet.getLastRowNum();
        int start = context.getOffsetRow();
        int end = context.calculateEndRow(lastRowInSheet);
        int chunkSize = context.getChunkSize();

        List<ExcelResultItem<T>> currentChunk = new ArrayList<>(
                context.suggestInitialCapacity(chunkSize, lastRowInSheet)
        );

        for (int i = start; i <= end; i++) {
            try {
                ExcelRow excelRow = sheet.getExcelRow(i);

                if (excelRow.rowIsNullOrEmpty()) {
                    continue;
                }

                List<ExcelError> errors = new ArrayList<>();

                T object = mapRowToInstance(clazz, excelRow, mappedFields, errors, context.isSkipValidation());
                currentChunk.add(new ExcelResultItem<>(object, i, errors));

                if (currentChunk.size() >= chunkSize) {
                    chunkConsumer.accept(new ArrayList<>(currentChunk));
                    currentChunk.clear();
                }
            } catch (Exception e) {
                if (!currentChunk.isEmpty()) {
                    chunkConsumer.accept(currentChunk);
                }
                throw new ExcelPipelineException("Unexpected failure while processing line " + i + ": " + e.getMessage(), e);
            }
        }

        if (!currentChunk.isEmpty()) {
            chunkConsumer.accept(currentChunk);
        }
    }

    private <T> T mapRowToInstance(Class<T> clazz, ExcelRow excelRow, List<MappedField> mappedFields, List<ExcelError> errors, boolean skipValidation) throws Exception {
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

    private <T> List<MappedField> prepareMetadata(Class<T> clazz) {
        List<MappedField> mappedFields = new ArrayList<>();

        for (Field f : clazz.getDeclaredFields()) {
            ExcelColumn ann = f.getAnnotation(ExcelColumn.class);

            if (ann != null) {
                if (!f.getType().equals(String.class)) {
                    throw new ExcelPipelineException(String.format(
                            "Invalid field type: '%s' in class '%s' must be a String.",
                            f.getName(), clazz.getSimpleName()
                    ));
                }

                f.setAccessible(true);
                mappedFields.add(new MappedField(f, ann));
            }
        }
        return mappedFields;
    }


    private <T> T createInstance(Class<T> clazz) throws Exception {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new ExcelPipelineException("The class " + clazz.getSimpleName() + " needs a public constructor with no arguments.");
        }
    }

}
