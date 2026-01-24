package mypoio;

import mypoio.core.mapper.ExcelMapper;
import mypoio.core.mapper.factory.ExcelMapperFactory;
import mypoio.core.mapper.factory.ExcelMapperFactoryDefault;
import mypoio.core.reader.ExcelSource;
import mypoio.core.reader.ExcelSourceFactory;
import mypoio.domain.ExcelResult;
import mypoio.domain.ExcelResultItem;
import mypoio.infrastructure.poi.ExcelSourceFactoryPoiDefault;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

/**
 * Main entry point of the MyPoio library for reading and processing spreadsheets.
 *
 * <p>The {@code core.ExcelReader} orchestrates the data pipeline: opens the data source (Source),
 * executes validations, and maps rows to Java objects (POJOs) using Reflection.</p>
 *
 * <b>Usage examples:</b>
 * <pre>{@code
 * // Simple read using Apache POI (default)
 * ExcelResult<Person> result = new core.ExcelReader<>(Person.class)
 *         .initRead("data.xlsx");
 *
 * // Defining a custom Source provided by the user (e.g. FastExcel, CSV, etc)
 * ExcelResult<Person> result = new core.ExcelReader<>(Person.class)
 *         .withSource(new FastExcelSourceFactory())
 *         .initRead("data.xlsx");
 *
 * // Skipping validations when the data was previously validated and only reading is required
 * ExcelResult<Person> result = new core.ExcelReader<>(Person.class)
 *         .skipValidation()
 *         .initRead(inputStream);
 *
 * // Streaming read using chunked processing, ideal for large files
 * // Rows are processed in batches and delivered incrementally
 * // allowing persistence or further processing per chunk
 *
 * new ExcelReader<>(PersonCustomValidation.class)
 *     .offsetRow(2) // defines the starting row for reading
 *     .withChunkSize(500) // processes data in chunks of 500
 *     .skipValidation()   // skips validations if desired
 *     .initRead(SOURCE, chunk -> {
 *         // called every 500 rows
 *         personRepository.saveAll(
 *             chunk.stream().flatMap(ExcelResultItem::getData)
 *         );
 *     });
 *
 * }</pre>
 */
public final class ExcelReader<T> {

    private final Class<T> clazz;
    private int offsetRow;
    private boolean skipValidation;
    private int chunkSize = Integer.MAX_VALUE;

    private ExcelSourceFactory excelSourceFactory;
    private ExcelMapperFactory mapperFactory;

    /**
     * @param clazz Classe que representa o modelo de dados (anotada com @ExcelModel).
     */
    public ExcelReader(Class<T> clazz) {
        this(clazz, 1);
    }

    /**
     * @param clazz    Classe de destino.
     * @param offsetRow Índice da linha inicial.
     */
    public ExcelReader(Class<T> clazz, int offsetRow) {
        this.clazz = clazz;
        this.offsetRow = offsetRow;
        this.skipValidation = false;
        this.excelSourceFactory = new ExcelSourceFactoryPoiDefault();
        this.mapperFactory = new ExcelMapperFactoryDefault();
    }

    public ExcelReader<T> skipValidation() {
        this.skipValidation = true;
        return this;
    }

    public ExcelReader<T> offsetRow(int startRow) {
        this.offsetRow = startRow;
        return this;
    }

    public ExcelReader<T> withChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
        return this;
    }

    public ExcelReader<T> withMapperFactory(ExcelMapperFactory mapperFactory) {
        this.mapperFactory = mapperFactory;
        return this;
    }


    public ExcelResult<T> initRead(String sourceFile) {
        ExcelResult<T> result = new ExcelResult<>();
        try (ExcelSource excelSource = excelSourceFactory.create(sourceFile)) {
            resolveMapper(excelSource)
                    .mapperData(this.chunkSize, chunk -> chunk.forEach(result::addRow));
        }
        return result;
    }

    public void initRead(String sourceFile, Consumer<List<ExcelResultItem<T>>> chunkConsumer) {
        try (ExcelSource excelSource = excelSourceFactory.create(sourceFile)) {
            resolveMapper(excelSource)
                    .mapperData(this.chunkSize, chunkConsumer);
        }
    }

    public ExcelResult<T> initRead(InputStream is) {
        ExcelResult<T> result = new ExcelResult<>();
        try (ExcelSource source = excelSourceFactory.create(is)) {
            resolveMapper(source)
                    .mapperData(this.chunkSize, chunk -> chunk.forEach(result::addRow));
        }
        return result;
    }

    public void initRead(InputStream is, Consumer<List<ExcelResultItem<T>>> chunkConsumer) {
        try (ExcelSource source = excelSourceFactory.create(is)) {
            resolveMapper(source)
                    .mapperData(this.chunkSize, chunkConsumer);
        }
    }

    private ExcelMapper<T> resolveMapper(ExcelSource excelSource) {
        return mapperFactory.create(offsetRow, excelSource, clazz, skipValidation);
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
     * new core.ExcelReader<>(UserDto.class, 1)
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
}
