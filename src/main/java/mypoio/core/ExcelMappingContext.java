package mypoio.core;

import mypoio.core.reader.ExcelSource;

public class ExcelMappingContext<T> {
    private final Class<T> clazz;
    private ExcelSource source;
    private int offsetRow;
    private int limit;
    private int chunkSize;
    private boolean skipValidation;

    public ExcelMappingContext(Class<T> clazz) {
        this.clazz = clazz;
        this.offsetRow = 1;
        this.limit = Integer.MAX_VALUE;
        this.chunkSize = Integer.MAX_VALUE; // Ajustado para processamento completo por padrão
        this.skipValidation = false;
    }

    /**
     * Calcula o índice da última linha a ser processada.
     *
     * @param lastRowInSheet O índice da última linha real da planilha (POI sheet.getLastRowNum())
     * @return O índice final respeitando o limit definido pelo usuário.
     */
    public int calculateEndRow(int lastRowInSheet) {
        if (limit == Integer.MAX_VALUE) {
            return lastRowInSheet;
        }
        // Exemplo: começa na 2, limite de 5. Deve ir até a 6 (2,3,4,5,6).
        return Math.min(lastRowInSheet, offsetRow + limit - 1);
    }

    /**
     * Sugere a capacidade inicial da ArrayList para evitar redimensionamentos (resize) caros na Heap.
     *
     * @param chunkSize      O tamanho do lote atual.
     * @param lastRowInSheet O total de linhas da planilha.
     * @return Um valor de capacidade seguro para a ArrayList.
     */
    public int suggestInitialCapacity(int chunkSize, int lastRowInSheet) {
        int endRow = calculateEndRow(lastRowInSheet);
        int totalToProcess = Math.max(0, endRow - offsetRow + 1);

        // A capacidade deve ser o menor entre o chunk e o total real de linhas
        int capacity = Math.min(chunkSize, totalToProcess);

        // Proteção contra valores absurdos ou negativos
        if (capacity <= 0) return 10;
        if (capacity > 10000) return 1000; // Cap de segurança para memória

        return capacity;
    }

    // --- Getters e Setters ---

    public Class<T> getClazz() {
        return clazz;
    }

    public ExcelSource getSource() {
        return source;
    }

    public void setSource(ExcelSource source) {
        this.source = source;
    }

    public int getOffsetRow() {
        return offsetRow;
    }

    public void setOffsetRow(int offsetRow) {
        this.offsetRow = offsetRow;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public boolean isSkipValidation() {
        return skipValidation;
    }

    public void setSkipValidation(boolean skipValidation) {
        this.skipValidation = skipValidation;
    }
}
