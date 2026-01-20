package core.reader;

/**
 * Represents a single unit of data within an Excel spreadsheet.
 * * <p>This interface provides access to the cell's raw value and its
 * physical location. It also includes default utility methods to simplify
 * common validation tasks like blank checks and regex matching.</p>
 */
public interface ExcelCell {

    /**
     * Gets the alphanumeric address of the cell (e.g., "A1", "B5").
     * @return The cell address as a String.
     */
    String getAddress();

    /**
     * Retrieves the raw string content of the cell.
     * @return The cell value, or null if the cell is empty.
     */
    String getValue();

    /**
     * Returns the zero-based column index.
     * @return Column index (0 for A, 1 for B, etc.).
     */
    int getColumNum();

    /**
     * Returns the zero-based row index.
     * @return Row index.
     */
    int getRowNum();

    /**
     * Checks if the cell is null, empty, or contains only whitespace.
     * <p>This uses Java 11's {@code String.isBlank()} for memory efficiency.</p>
     * @return {@code true} if the cell lacks meaningful content.
     */
    default boolean isBlank() {
        String value = getValue();
        return value == null || value.isBlank();
    }

    /**
     * Validates the cell value against a regular expression.
     * * <p><b>Note:</b> Returns {@code false} (valid) if the cell is blank,
     * as format validation usually only applies to present values.
     * Mandatory checks should be handled by a separate required-field validator.</p>
     * * @param regex The regular expression to match against.
     * @return {@code true} if the value does NOT match the regex; {@code false} otherwise.
     */
    default boolean doesNotMatch(String regex) {
        if (isBlank() || regex == null || regex.isEmpty()) {
            return false;
        }
        return !getValue().matches(regex);
    }

    /**
     * Returns the character count of the cell's value.
     * @return The length of the string, or 0 if null.
     */
    default int valueLength() {
        String value = getValue();
        return value == null ? 0 : value.length();
    }
}
