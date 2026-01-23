package mypoio.core.reader;


import mypoio.exceptions.ExcelPipelineException;

/**
 * Represents a specific worksheet within an Excel workbook.
 * <p>This interface provides the necessary methods to navigate through
 * the rows of a sheet. It abstracts engine-specific implementations
 * (such as Apache POI's {@code Sheet}) to maintain a consistent API
 * for the mapping process.</p>
 */
public interface ExcelSheet {

    /**
     * Gets the index of the last row contained in this sheet.
     * <p>This is typically used to determine the boundaries of the
     * iteration loop during the mapping process.</p>
     * @return The zero-based index of the last row, or -1 if the sheet is empty.
     */
    int getLastRowNum();

    /**
     * Retrieves a specific row from the sheet by its index.
     * @param rowNumber The zero-based index of the row to retrieve.
     * @return An instance of {@link ExcelRow} representing the data at the specified index.
     * @throws ExcelPipelineException if the row cannot be accessed or is out of bounds.
     */
    ExcelRow getExcelRow(int rowNumber);
}