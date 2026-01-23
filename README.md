# 🐔 MyPoio

Dynamic Excel-to-POJO mapping and validation engine for Java.

MyPoio is a **lightweight, extensible library** built on top of Apache POI to remove the boilerplate of parsing Excel files. It turns rows into objects and validates them before they ever reach your business logic.

---

## 🐣 Why "MyPoio"?

The name is a pun. Apache POI is powerful, but using it directly is like handling raw chicken ("Pollo" or "Poio" in slang) — it's messy, requires a lot of preparation, and if you're not careful, it can make your code sick (NullPointers, Memory Leaks, Spaghetti code).

MyPoio cooks it for you. It takes the "raw" POI and serves you clean, validated, and ready-to-use Java Objects.

---

## General Features

- **Annotation-Driven**: Map columns and define validation rules directly in your DTOs (e.g., `@ExcelColumn`, `@ExcelRequired`).
- **Built-in Validation Engine**: Validates data during the read process. If a cell is invalid (e.g., invalid email, future date), you get a detailed error report instead of a crash.
- **Custom Validations**: Easily add your own validation rules by implementing the validation interfaces, without modifying the core engine.
- **Resource Safety**: Implements `AutoCloseable` to ensure file handles and workbooks are closed properly, preventing memory leaks.
- **Engine Agnostic**: The core logic is decoupled from the file reading mechanism. You can plug in different Excel readers (not limited to Apache POI) by implementing the reader interfaces.
- **Low Coupling by Design**: All components communicate through well-defined interfaces, making the system extensible, testable, and easy to maintain.
- **Zero Boilerplate**: No more `row.getCell(0).getStringCellValue()`.

---

## Installation

```xml
<dependency>
  <groupId>io.github.cm-ms</groupId>
  <artifactId>mypoio</artifactId>
  <version>1.0.1</version>
</dependency>
```

---

## Usage Examples

### 1. Define your DTO

```java
@ExcelModel(index = 0)
public class EmployeeDto {

    @ExcelColumn(index = 0)
    @ExcelRequired
    private String name;

    @ExcelColumn(index = 1)
    @ExcelEmail
    private String email;

    @ExcelColumn(index = 2)
    @ExcelAllowedValues({"IT", "HR", "SALES"})
    private String department;
}
```

### 2. Read the File

```java
import mypoio.ExcelReader;

@RestController
@RequestMapping
public class MyController {
  @PostMapping("/upload")
  public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) {
    try (InputStream is = file.getInputStream()) {

      ExcelReader<EmployeeDto> reader = new ExcelReader<>(EmployeeDto.class, 1); // 1 (starts reading data from the second row)

      ExcelResult<EmployeeDto> result = reader.initRead(is);

      if (result.hasErrors()) {
        return ResponseEntity.badRequest().body(result.getRowErrors());
      }

      return ResponseEntity.ok(result.getValidData());

    } catch (Exception e) {
      return ResponseEntity.internalServerError()
              .body("Erro ao processar o arquivo: " + e.getMessage());
    }
  }
}
```

---

## Customization

### Creating a Custom Validator

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelCpf {
    String message() default "Invalid CPF format";
}
```

```java
public class CpfValidator implements AnnotationValidator<ExcelCpf> {

    @Override
    public void validate(ExcelDocumentoBR annotation, ExcelCell excelCell, List<ExcelError> errorList) {
        if (excelCell.isBlank()) return;

        if (!isValidCpf(excelCell.getValue())) {
            errorList.add(ExcelError.of(ErrorCode.of("INVALID_DOCUMENT"), msg, excelCell.getAddress()));
        }
    }
}
```

```java
import mypoio.ExcelReader;

// 1. Define the class mapping
var reader = new ExcelReader<>(PersonCustomValidation.class, 1);

// 2. Register custom rules (Annotation -> Validator)
reader.

        registerValidator(ExcelCpf .class, new CpfValidator());

        // 3. Process the file
        var response = reader.initRead(source);
```


---
## Annotation Processing Rules

### `@ExcelModel`

Marks a class as an Excel Sheet model.

- Only classes annotated with `@ExcelModel` are considered during the read process.
- Defines which sheet should be read (by index).

```java
@ExcelModel(index = 0)
public class EmployeeDto {
}
```

Classes without `@ExcelModel` are not processed.

---

### `@ExcelColumn`

Defines a field-to-column mapping.

- Only fields annotated with `@ExcelColumn` are:
  - Read from the Excel sheet
  - Eligible for validation
- Fields without `@ExcelColumn` are ignored by the engine.

```java
@ExcelColumn(index = 1)
private String email;
```

---

### Validation Annotations

Validation annotations act as constraints applied to a column.

- Validation annotations are evaluated **only** on fields annotated with `@ExcelColumn`
- Validation annotations used **without** `@ExcelColumn` are **silently ignored** and will not be processed

Valid example:
```java
@ExcelColumn(index = 1)
@ExcelEmail
@ExcelRequired
private String email;
```

Ignored example:
```java
@ExcelEmail
private String email;
```
---
## Current Validations

| Validation | Description |
|-----------|-------------|
| `@ExcelAllowedValues` | Restricts the cell value to a predefined list of allowed values. |
| `@ExcelBoolean` | Validates that the cell value represents a boolean (`true/false`, `yes/no`, etc., depending on implementation). |
| `@ExcelEmail` | Validates that the cell value is a valid email format. |
| `@ExcelNumber` | Ensures the cell contains a numeric value. |
| `@ExcelFuture` | Ensures the date value is in the future. |
| `@ExcelPast` | Ensures the date value is in the past. |
| `@ExcelPatternDate` | Validates the date format against a predefined pattern. |
| `@ExcelPhone` | Validates that the cell value matches a phone number format. |
| `@ExcelRegex` | Validates the cell value using a custom regular expression. |
| `@ExcelRequired` | Ensures the cell is not null or blank. |
| `@ExcelSize` | Validates the length of a string (min and/or max size). |


---
## Project Roadmap

### Completed / Implemented

1. Dynamic Excel reading using annotations (values initially treated as `String`) ✅  
2. Dynamic validation engine inspired by Jakarta Validation ✅  
3. Support for custom validation rules beyond the built-in ones ✅  
4. Ability to skip validations when needed ✅  
5. Minimized coupling through dynamic initializers and interfaces ✅  
6. Reduced dependency on error-return structures (cleaner result model) ✅  
7. Decoupling from Apache POI (used as default implementation, not a hard dependency) ✅  

---

### Planned / In Progress

8. Column mapping by name (currently supported only by column index)  
9. Type conversion after validation (String → Integer, LocalDate, Enum, etc.)  
10. Data normalization layer  
   - Example: `Male/Female`, `M/F`, `1/0` → single canonical representation  
   - Especially useful for Data Warehousing and analytics use cases  
11. Multi-sheet processing  
   - Support reading multiple sheets (1..N) instead of a fixed sheet  
12. Direct Excel-to-Type processing  
   - Optional fast path that skips intermediate validation steps when desired  
13. Cross-column validation  
   - Example: if column A is filled, column B becomes required  

---

### Long-Term Goals

- Provide a fully configurable processing pipeline  
- Enable enterprise-grade data ingestion with strong validation and normalization  
- Keep the core lightweight, extensible, and framework-agnostic

---

## Contribution

1. Fork the project.
2. Create your Feature Branch.
3. Commit your changes.
4. Push to the branch.
5. Open a Pull Request.

---

Built with ❤️ (and Chicken) by **cmms**
