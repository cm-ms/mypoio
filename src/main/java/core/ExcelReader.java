package core;

import core.mapper.ExcelMapper;
import core.mapper.ReflectionExcelMapper;
import core.reader.ExcelSource;
import core.reader.ExcelSourceFactory;
import core.validator.AnnotationValidator;
import core.validator.ValidationEngine;
import domain.ExcelResult;
import infrastructure.poi.ExcelSourceFactoryPoiDefault;

import java.io.InputStream;
import java.lang.annotation.Annotation;

/**
 * Main entry point of the MyPoio library for reading and processing spreadsheets.
 *
 * <p>The {@code ExcelReader} orchestrates the data pipeline: opens the data source (Source),
 * executes validations, and maps rows to Java objects (POJOs) using Reflection.</p>
 *
 * <b>Usage examples:</b>
 * <pre>{@code
 * // Simple read using Apache POI (default)
 * ExcelResult<Person> result = new ExcelReader<>(Person.class)
 *         .initRead("data.xlsx");
 *
 * // Defining a custom Source provided by the user (e.g. FastExcel, CSV, etc)
 * ExcelResult<Person> result = new ExcelReader<>(Person.class)
 *         .withSource(new FastExcelSourceFactory())
 *         .initRead("data.xlsx");
 *
 * // Skipping validations when the data was previously validated and only reading is required
 * ExcelResult<Person> result = new ExcelReader<>(Person.class)
 *         .skipValidation()
 *         .initRead(inputStream);
 *
 * // Registering a user-provided validator mapped to a custom annotation
 * ExcelResult<Person> result = new ExcelReader<>(Person.class)
 *         .registerRules(MyAnnotation.class, new MyAnnotationValidator())
 *         .initRead("data.xlsx");
 * }</pre>
 */
public class ExcelReader<T> {
    private final Class<T> clazz;
    private final int startRow;
    private boolean skipValidation;

    private ExcelSourceFactory excelSourceFactory;
    private core.mapper.ExcelMapper<T> excelMapper;
    private ValidationEngine validationEngine;

    /**
     * @param clazz Classe que representa o modelo de dados (anotada com @ExcelModel).
     */
    public ExcelReader(Class<T> clazz) {
        this(clazz, 1);
    }

    /**
     * @param clazz    Classe de destino.
     * @param startRow Índice da linha inicial.
     */
    public ExcelReader(Class<T> clazz, int startRow) {
        this.clazz = clazz;
        this.startRow = startRow;
        this.excelSourceFactory = new ExcelSourceFactoryPoiDefault();
        this.validationEngine = new ValidationEngine();
        this.skipValidation = false;
    }

    // Permite ao usuário registrar novas anotações dele (Extensibilidade)
    public <A extends Annotation> ExcelReader<T> registerRules(Class<A> ann, AnnotationValidator<A> val) {
        this.validationEngine.registerValidator(ann, val);
        return this;
    }


    public ExcelReader<T> skipValidation() {
        this.skipValidation = true;
        return this;
    }

    /**
     * Define uma fábrica customizada para a criação da fonte de dados (Engine de leitura).
     *
     * <p>Por padrão, a biblioteca utiliza o Apache POI como motor de leitura.
     * Este método permite substituir o motor padrão por qualquer outra implementação
     * que siga o contrato {@link ExcelSourceFactory}.</p>
     *
     * <p>Isso é útil para suportar novos formatos de arquivos (como CSV) ou para
     * utilizar bibliotecas de alto desempenho (como FastExcel) sem alterar a lógica
     * de mapeamento e validação.</p>
     *
     * <p>Exemplo de uso alterando o motor de leitura:</p>
     *
     * <pre>{@code
     * new ExcelReader<>(UserDto.class, 1)
     * .withSource(new FastExcelSourceFactory()) // Substitui o Apache POI
     * .read("file.xlsx");
     * }</pre>
     *
     * @param factory implementação de {@link ExcelSourceFactory} responsável por
     *                abrir e gerenciar o arquivo.
     * @return a instância atual do {@link ExcelReader} para encadeamento fluente.
     */
    public ExcelReader<T> withSource(ExcelSourceFactory factory) {
        this.excelSourceFactory = factory;
        return this;
    }


    /**
     * Executa o processamento a partir de um arquivo físico.
     */
    public ExcelResult<T> initRead(String sourceFile) {
        try (ExcelSource excelSource = excelSourceFactory.create(sourceFile)) {
            return process(excelSource);
        }
    }

    /**
     * Executa o processamento a partir de um stream de dados.
     */
    public ExcelResult<T> initRead(InputStream inputStream) {
        try (ExcelSource excelSource = excelSourceFactory.create(inputStream)) {
            return process(excelSource);
        }
    }

    private ExcelResult<T> process(ExcelSource source) {
        return resolveMapper(source).mapperData();
    }


    private ExcelMapper<T> resolveMapper(ExcelSource excelSource) {
        ValidationEngine engineToUse = this.skipValidation ? null : this.validationEngine;
        return (excelMapper != null) ? excelMapper : new ReflectionExcelMapper<>(startRow, excelSource, clazz, engineToUse);
    }
}
