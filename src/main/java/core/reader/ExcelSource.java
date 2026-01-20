package core.reader;

/**
 * Contract representing a physical or virtual Excel workbook.
 * * <p>This interface abstracts the underlying engine (Apache POI, FastExcel, etc.),
 * allowing the library to read sheets and manage system resources regardless
 * of the file format or loading strategy.</p>
 * * <p>It extends {@link AutoCloseable} to ensure that file handles and memory
 * buffers are released properly after the mapping process.</p>
 */
public interface ExcelSource extends AutoCloseable {

    /**
     * Locates and opens a specific sheet based on the metadata provided in the DTO class.
     * * <p>The implementation should look for the annotation on the
     * provided class to determine whether to fetch the sheet by name or by index.</p>
     * * @param <T>   The type of the target POJO.
     * @param clazz The class representing the row model, annotated with {@code @ExcelModel}.
     * @return An implementation of {@link ExcelSheet} representing the target sheet.
     * @throws exceptions.ExcelPipelineException if the sheet cannot be found or accessed.
     */
    <T> ExcelSheet readSheet(Class<T> clazz);

    /**
     * Releases all resources associated with this source (e.g., file descriptors,
     * memory-mapped files, or internal POI workbooks).
     * * <p>This method is invoked automatically when using a try-with-resources block.
     * Implementation should be idempotent (calling it multiple times should not
     * cause errors).</p>
     */
    @Override
    void close();
}
