package app.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.IOException;

public class PDFMergerFromJson {

    /**
     * Mescla os arquivos PDF com base no JSON fornecido e deleta os arquivos originais.
     *
     * @param jsonContent    Conteúdo JSON com os caminhos dos PDFs
     * @param outputPdfPath  Caminho para o PDF de saída mesclado
     * @throws IOException Em caso de erro de leitura ou escrita
     */
    public static void merge(String jsonContent, String outputPdfPath) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonContent);

        File outputFile = new File(outputPdfPath);
        File parentDir = outputFile.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Não foi possível criar o diretório de saída: " + parentDir.getAbsolutePath());
            }
        }

        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        pdfMerger.setDestinationFileName(outputFile.getAbsolutePath());

        for (JsonNode node : rootNode) {
            String realpath = node.get("realpath").asText();
            File file = new File(realpath);

            if (file.exists() && file.isFile()) {
                System.out.println("Adicionando: " + realpath);
                pdfMerger.addSource(file);
            } else {
                System.err.println("Erro: Arquivo não encontrado ou inválido -> " + realpath);
            }
        }

        pdfMerger.mergeDocuments(null);
        System.out.println("PDF mesclado criado em: " + outputFile.getAbsolutePath());

        // Deletar os arquivos originais
        for (JsonNode node : rootNode) {
            String realpath = node.get("realpath").asText();
            File file = new File(realpath);

            if (file.exists() && file.isFile()) {
                if (file.delete()) {
                    System.out.println("Arquivo deletado: " + realpath);
                } else {
                    System.err.println("Não foi possível deletar o arquivo: " + realpath);
                }
            }
        }
    }
}