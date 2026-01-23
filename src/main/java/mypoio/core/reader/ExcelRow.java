package mypoio.core.reader;

import mypoio.exceptions.ExcelPipelineException;

/**
 * Represents a horizontal row of cells in a spreadsheet.
 * <p>This interface acts as a container for {@link ExcelCell} objects.
 * It provides the bridge between the physical spreadsheet row and
 * the object-mapping logic, allowing access to individual cells
 * by their column index.</p>
 */
public interface ExcelRow {

    /**
     * Checks if the row is physically null (does not exist in the file)
     * or if it contains no data cells.
     * <p>This is a safety check used by the iteration process to skip
     * empty lines and avoid unnecessary processing or mapping errors.</p>
     * @return {@code true} if the row is null or effectively empty;
     * {@code false} otherwise.
     */
    boolean rowIsNullOrEmpty();

    /**
     * Retrieves a specific cell from this row based on its column index.
     * @param columnNumber The zero-based index of the column (e.g., A=0, B=1).
     * @return An instance of {@link ExcelCell} representing the data at
     * the specified column.
     * @throws ExcelPipelineException if the cell cannot be
     * accessed or the index is invalid.
     */
    ExcelCell getExcelCell(int columnNumber);
}