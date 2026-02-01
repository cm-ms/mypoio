package mypoio.core;

import mypoio.annotations.ExcelColumn;
import mypoio.core.mapper.MappedField;
import mypoio.exceptions.ExcelPipelineException;
import mypoio.utils.MsgBundle;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ExcelMetadataResolver {
    public static <T> T createInstance(Class<T> clazz) throws Exception {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new ExcelPipelineException(
                    MsgBundle.get("constructor.no_default", clazz.getSimpleName())
            );
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

        boolean hasRef = !ref.isEmpty();
        boolean hasIdx = idx >= 0;

        if (!hasRef && !hasIdx) {
            throw new ExcelPipelineException(
                    MsgBundle.get("excel.column.missing",
                            f.getName(),
                            f.getDeclaringClass().getSimpleName())
            );
        }

        if (hasRef) {
            if (!ref.matches("(?i)^[A-Z]+$")) {
                throw new ExcelPipelineException(
                        MsgBundle.get("excel.column.reference.invalid",
                                f.getName(),
                                f.getDeclaringClass().getSimpleName(),
                                ref)
                );
            }

            return columnReferenceToIndex(ref.toUpperCase());
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
            throw new ExcelPipelineException(
                    MsgBundle.get("field.type.invalid",
                            f.getName(),
                            clazz.getSimpleName())
            );
        }
    }
}
