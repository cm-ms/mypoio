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

        ExcelSheet sheet = source.readSheet(clazz); // get sheet with Iterable
        List<MappedField> mappedFields = prepareMetadata(clazz);

        int offset = context.getOffsetRow();
        int chunkSize = context.getChunkSize();
        int limit = context.getLimit();

        List<ExcelResultItem<T>> currentChunk = context.createChunkBuffer();

        // controllers Loop
        int physicalRowIndex = 0;
        int processedItemsCount = 0;

        for (ExcelRow excelRow: sheet) {

            if(physicalRowIndex < offset) {
                physicalRowIndex ++;
                continue;
            }

            if(limit > 0 && processedItemsCount >= limit) {
                break;
            }

            try {
                if (excelRow.rowIsNullOrEmpty()) {
                    physicalRowIndex++;
                    continue;
                }

                List<ExcelError> errors = new ArrayList<>();

                T object = mapRowToInstance(clazz, excelRow, mappedFields, errors, context.isSkipValidation());

                currentChunk.add(new ExcelResultItem<>(object, physicalRowIndex, errors));
                processedItemsCount++;

                if (currentChunk.size() >= chunkSize) {
                    chunkConsumer.accept(new ArrayList<>(currentChunk));
                    currentChunk.clear();
                }
            } catch (Exception e) {
                if (!currentChunk.isEmpty()) {
                    chunkConsumer.accept(currentChunk);
                }
                throw new ExcelPipelineException("Unexpected failure while processing line " + physicalRowIndex + ": " + e.getMessage(), e);
            }

            physicalRowIndex++;
        }

        // alguns casos, para o currentChunk.size < chunkSize, garante que os dados sejam consumidos
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
                            "Invalid field type: '%s' in class '%s' must be a String. Type conversion is not yet supported.",
                            f.getName(), clazz.getSimpleName()
                    ));
                }

                resolveAndValidateIndex(ann, f);

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
            throw new ExcelPipelineException("Class '" + clazz.getSimpleName() + "' must have a public no-arguments constructor.");
        }
    }

    private void resolveAndValidateIndex(ExcelColumn ann, Field f) {
        String ref = ann.reference().trim();
        int idx = ann.index();

        if (!ref.isEmpty()) {
            if (!ref.matches("(?i)^[A-Z]+$")) {
                throw new ExcelPipelineException(String.format(
                        "Field '%s' has an invalid reference: '%s'. Please use letters only (e.g., A, B, AA).",
                        f.getName(), ref
                ));
            }
            return;
        }

        if (idx < 0) {
            throw new ExcelPipelineException(String.format(
                    "Field '%s' must define a valid 'index' (>= 0) or a 'reference' (e.g., \"A\").",
                    f.getName()
            ));
        }
    }

}
