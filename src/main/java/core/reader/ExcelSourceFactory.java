package core.reader;

import java.io.InputStream;

/**
 * Factory responsible for instantiating the Excel data source.
 * * <p>Implementations of this interface define how the file is loaded into memory.
 * DOM-based engines (like traditional POI) will load the full file,
 * while Streaming engines will process data on-demand for memory efficiency.</p>
 */
public interface ExcelSourceFactory {

    /**
     * Creates a data source from a physical file path.
     * * @param filePath The absolute or relative path to the .xlsx or .xls file.
     * @return An instance of {@link ExcelSource} ready for reading.
     * @throws exceptions.ExcelPipelineException if the file is not found or inaccessible.
     */
    ExcelSource create(String filePath);

    /**
     * Creates a data source from an input stream.
     * * <p><b>Note:</b> The reading engine may behave differently (e.g., restricted
     * random access navigation) if the implementation uses Streaming techniques
     * for memory optimization.</p>
     * * @param inputStream Stream containing the spreadsheet bytes (common in multipart uploads).
     * @return An instance of {@link ExcelSource} ready for reading.
     * @throws exceptions.ExcelPipelineException if the stream is invalid or corrupted.
     */
    ExcelSource create(InputStream inputStream);
}
