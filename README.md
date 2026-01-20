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
  <version>1.0.0</version>
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
File file = new File("employees.xlsx");

try (ExcelReader<EmployeeDto> reader = new ExcelReader<>(EmployeeDto.class)) {

    ExcelResult<EmployeeDto> result = reader.read(file.getAbsolutePath());

    result.getSuccessList().forEach(emp ->
        System.out.println("Importing: " + emp.getName())
    );

    result.getErrorList().forEach(error ->
        System.out.println("Error at " + error.getAddress() + ": " + error.getMessage())
    );

} catch (Exception e) {
    e.printStackTrace();
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
public class CpfValidator implements ExcelRule<ExcelCpf> {

    @Override
    public void validate(ExcelCell cell, ExcelCpf annotation, List<ExcelError> errors) {
        if (cell.isBlank()) return;

        if (!isValidCpf(cell.getValue())) {
            errors.add(new ExcelError(
                cell.getAddress(),
                annotation.message(),
                cell.getValue()
            ));
        }
    }
}
```

---

## Contribution

1. Fork the project.
2. Create your Feature Branch.
3. Commit your changes.
4. Push to the branch.
5. Open a Pull Request.

---

Built with ❤️ (and Chicken) by **cmms**
