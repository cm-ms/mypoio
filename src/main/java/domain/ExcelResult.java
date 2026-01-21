package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents the result of processing an Excel file.
 *
 * @param <T> the type of data stored in each row
 */
public class ExcelResult<T> {

    private List<ExcelResultItem<T>> rows;
    private List<ExcelError> generalErrors;
    private long errorCount;

    /**
     * Constructs an empty ExcelResult with no rows and no general errors.
     */
    public ExcelResult() {
        this.rows = new ArrayList<>();
        this.generalErrors = new ArrayList<>();
        this.errorCount = 0L;
    }

    /**
     * Adds a general error that is not tied to a specific row.
     *
     * @param excelError the error to add; ignored if null
     */
    public void addGeneralError(ExcelError excelError) {
        if (excelError != null) {
            this.generalErrors.add(excelError);
            errorCount++;
        }
    }

    /**
     * Adds a row to the result.
     *
     * @param item the ExcelResultItem to add; ignored if null
     */
    public void addRow(ExcelResultItem<T> item) {
        if (Objects.nonNull(item)) {
            rows.add(item);
            errorCount += item.getErrors().size();
        }
    }

    /**
     * Checks if the result contains any errors, either general or in the rows.
     *
     * @return true if there are any errors; false otherwise
     */
    public boolean hasErrors() {
        return !generalErrors.isEmpty() || rows.stream().anyMatch(row -> !row.isValid());
    }

    /**
     * Checks if the result is completely successful (no errors at all).
     *
     * @return true if there are no errors; false otherwise
     */
    public boolean isSuccess() {
        return !hasErrors();
    }

    /**
     * Checks if the result is partially successful, meaning there are valid rows
     * but also some errors.
     *
     * @return true if at least one row is valid and there are errors; false otherwise
     */
    public boolean isPartiallySuccess() {
        long validCount = rows.stream().filter(ExcelResultItem::isValid).count();
        return validCount > 0 && hasErrors();
    }

    /**
     * Returns all rows processed in the result.
     *
     * @return the list of ExcelResultItem objects
     */
    public List<ExcelResultItem<T>> getRows() {
        return rows;
    }

    /**
     * Returns all general errors that are not associated with specific rows.
     *
     * @return the list of general ExcelError objects
     */
    public List<ExcelError> getGeneralErrors() {
        return generalErrors;
    }

    /**
     * Returns only the errors found within the processed rows.
     *
     * @return the list of row-specific ExcelError objects
     */
    public List<ExcelError> getRowErrors() {
        return rows.stream()
                .flatMap(row -> row.getErrors().stream())
                .collect(Collectors.toList());
    }

    /**
     * Returns all errors, including both general errors and row-specific errors.
     *
     * @return the complete list of ExcelError objects
     */
    public List<ExcelError> getAllErrors() {
        List<ExcelError> all = new ArrayList<>(generalErrors);
        all.addAll(getRowErrors());
        return all;
    }

    /**
     * Returns the data objects from all valid rows.
     *
     * @return the list of valid data objects
     */
    public List<T> getValidData() {
        return rows.stream()
                .filter(ExcelResultItem::isValid)
                .map(ExcelResultItem::getData)
                .collect(Collectors.toList());
    }

    public long getErrorCount() {
        return errorCount;
    }

}
