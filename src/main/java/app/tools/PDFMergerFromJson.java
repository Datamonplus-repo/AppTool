package app.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.LayerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.util.Matrix;

import java.io.File;
import java.io.IOException;

public class PDFMergerFromJson {

    private static final PDRectangle TARGET_SIZE = PDRectangle.A4;
    private static final float MARGIN = 36f;

    public static void merge(String jsonContent, String outputPdfPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonContent);

        File outputFile = new File(outputPdfPath);
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
            throw new IOException("Unable to create output directory: " + parentDir.getAbsolutePath());
        }

        PDDocument outDoc = new PDDocument(MemoryUsageSetting.setupTempFileOnly());
        try {
            LayerUtility layerUtil = new LayerUtility(outDoc);

            for (JsonNode node : rootNode) {
                String realpath = node.get("realpath").asText();
                File file = new File(realpath);

                if (!file.exists() || !file.isFile()) {
                    System.err.println("Erro: Not found file or invalid -> " + realpath);
                    continue;
                }

                PDDocument srcDoc = PDDocument.load(file, MemoryUsageSetting.setupTempFileOnly());
                try {
                    int pageCount = srcDoc.getNumberOfPages();
                    for (int i = 0; i < pageCount; i++) {
                        PDPage srcPage = srcDoc.getPage(i);

                        PDPage targetPage = new PDPage(TARGET_SIZE);
                        outDoc.addPage(targetPage);

                        PDFormXObject form = layerUtil.importPageAsForm(srcDoc, srcPage);

                        float targetW = TARGET_SIZE.getWidth()  - 2 * MARGIN;
                        float targetH = TARGET_SIZE.getHeight() - 2 * MARGIN;

                        PDRectangle mb = srcPage.getMediaBox();
                        float srcW = mb.getWidth();
                        float srcH = mb.getHeight();

                        int rot = srcPage.getRotation();
                        boolean rotated90 = (rot == 90 || rot == 270);
                        float drawW = rotated90 ? srcH : srcW;
                        float drawH = rotated90 ? srcW : srcH;

                        float scale = Math.min(targetW / drawW, targetH / drawH);
                        float scaledW = drawW * scale;
                        float scaledH = drawH * scale;

                        float tx = MARGIN + (targetW - scaledW) / 2f;
                        float ty = MARGIN + (targetH - scaledH) / 2f;

                        PDPageContentStream cs = new PDPageContentStream(outDoc, targetPage);
                        try {
                            Matrix transform = Matrix.getTranslateInstance(tx, ty);
                            transform.scale(scale, scale);
                            if (rotated90) {
                                transform.rotate((float) Math.toRadians(90));
                                transform.translate(0, -srcH);
                            }
                            cs.transform(transform);
                            cs.drawForm(form);
                        } finally {
                            cs.close();
                        }
                    }
                } finally {
                    srcDoc.close();
                }
            }

            outDoc.save(outputFile);
            System.out.println("PDF normalize/merged in: " + outputFile.getAbsolutePath());

            for (JsonNode node : rootNode) {

                String realpath = node.get("realpath").asText();
                File file = new File(realpath);

                if (file.exists() && file.isFile()) {
                    if (file.delete()) {
                        System.out.println("Deleted: " + realpath);
                    } else {
                        System.err.println("Unable to delete file: " + realpath);
                    }
                }
            }

        } finally {
            outDoc.close();
        }
    }
}
