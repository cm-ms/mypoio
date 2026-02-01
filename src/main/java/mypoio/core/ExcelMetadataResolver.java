package mypoio.core;

import mypoio.annotations.ExcelColumn;
import mypoio.core.mapper.MappedField;
import mypoio.exceptions.ExcelPipelineException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ExcelMetadataResolver {

    public static <T> T createInstance(Class<T> clazz) throws Exception {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new ExcelPipelineException("Class '" + clazz.getSimpleName() + "' must have a public no-arguments constructor.");
        }
    }

    public static <T> List<MappedField> prepareMetadata(Class<T> clazz) {
        List<MappedField> mappedFields = new ArrayList<>();

        for (Field f : clazz.getDeclaredFields()) {
            ExcelColumn ann = f.getAnnotation(ExcelColumn.class);
            if (ann == null) continue;

            validateFieldType(f, clazz);
            int index = resolveAndValidateIndex(ann, f);

            f.setAccessible(true);
            mappedFields.add(new MappedField(f, index));
        }
        return mappedFields;
    }

    private static int resolveAndValidateIndex(ExcelColumn ann, Field f) {
        String ref = ann.reference().trim();
        int idx = ann.index();

        if (!ref.isEmpty()) {
            if (!ref.matches("(?i)^[A-Z]+$")) {
                throw new ExcelPipelineException(String.format(
                        "Field '%s' has an invalid reference: '%s'. Use letters only.", f.getName(), ref));
            }
            return columnReferenceToIndex(ref.toUpperCase());
        }

        if (idx < 0) {
            throw new ExcelPipelineException(String.format(
                    "Field '%s' must define a valid 'index' or 'reference'.", f.getName()));
        }
        return idx;
    }

    private static int columnReferenceToIndex(String ref) {
        int result = 0;
        for (int i = 0; i < ref.length(); i++) {
            result = result * 26 + (ref.charAt(i) - 'A' + 1);
        }
        return result - 1;
    }

    private static void validateFieldType(Field f, Class<?> clazz) {
        if (!f.getType().equals(String.class)) {
            throw new ExcelPipelineException(String.format(
                    "Invalid field type: '%s' in '%s' must be a String.", f.getName(), clazz.getSimpleName()));
        }
    }
}
