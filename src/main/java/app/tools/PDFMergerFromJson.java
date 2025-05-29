package app.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.IOException;

public class PDFMergerFromJson {

    /**
     * Mescla os arquivos PDF com base no JSON fornecido.
     *
     * @param jsonFilePath Caminho para o JSON contendo os caminhos dos PDFs
     * @param outputPdfPath Caminho para o PDF de saída mesclado
     * @throws IOException Em caso de erro de leitura
     */
    public static void merge(String jsonFilePath, String outputPdfPath) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(jsonFilePath));

        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        pdfMerger.setDestinationFileName(outputPdfPath);

        for (JsonNode node : rootNode) {
            String realpath = node.get("realpath").asText();
            File file = new File(realpath);
            if (file.exists() && file.isFile()) {
                System.out.println("Adicionando: " + realpath);
                pdfMerger.addSource(file);
            } else {
                System.out.println("Arquivo não encontrado: " + realpath);
            }
        }

        pdfMerger.mergeDocuments(null);
        System.out.println("PDF mesclado criado em: " + outputPdfPath);
    }
}