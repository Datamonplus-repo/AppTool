package app.tools;

import java.io.IOException;
import java.nio.file.*;
import java.util.Base64;

public class Base64FileSaver {

    /**
     * Salva um arquivo a partir de uma string base64 diretamente no caminho especificado.
     *
     * @param destFile Caminho completo do arquivo de destino (incluindo nome e extensão).
     * @param base64Str Conteúdo base64 (aceita também data URI: "data:...;base64,XXXX").
     * @throws IOException              se ocorrer erro de IO (permissão, disco, etc.).
     * @throws IllegalArgumentException se parâmetros forem inválidos.
     */
    public static void saveBase64File(String destFile, String base64Str) throws IOException {

        if (destFile == null || destFile.isBlank()) {
            throw new IllegalArgumentException("destFile (arquivo de destino) é obrigatório.");
        }
        if (base64Str == null || base64Str.isBlank()) {
            throw new IllegalArgumentException("base64Str é obrigatório.");
        }

        // remove cabeçalho data URI se existir
        String cleanBase64 = base64Str;
        int dataIdx = base64Str.indexOf("base64,");
        if (dataIdx >= 0) {
            cleanBase64 = base64Str.substring(dataIdx + "base64,".length());
        }

        cleanBase64 = cleanBase64.replaceAll("\\s+", "");

        byte[] bytes;
        try {
            bytes = Base64.getDecoder().decode(cleanBase64);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Conteúdo base64 inválido.", e);
        }

        Path finalFile = Paths.get(destFile).toAbsolutePath().normalize();
        Files.write(finalFile, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
