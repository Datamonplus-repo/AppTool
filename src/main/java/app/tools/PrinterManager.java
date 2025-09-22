package app.tools;

import java.io.File;
import java.io.IOException;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;

public class PrinterManager {

    /**
     * Imprime um PDF configurando cópias, cor/P&B e orientação.
     *
     * @param pdfPath      caminho absoluto do PDF
     * @param printerName  nome exato da impressora (igual ao retornado por PrintService.getName())
     * @param copies       número de cópias (>=1)
     * @param color        true = colorido, false = preto-e-branco
     * @param landscape    true = horizontal (paisagem), false = vertical (retrato)
     * @return "SUCCESS" ou mensagem de erro
     */

    public String printto(String pdfPath, String printerName, int copies, boolean color, boolean landscape) {
        String xlog;

        try {
            File pdfFile = new File(pdfPath);
            if (!pdfFile.exists() || !pdfFile.isFile()) {
                return "Arquivo PDF não encontrado: " + pdfPath;
            }

            PrintService target = null;
            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
            for (PrintService ps : services) {
                if (ps.getName().equalsIgnoreCase(printerName)) {
                    target = ps;
                    break;
                }
            }
            if (target == null) {
                return "Impressora não encontrada: " + printerName;
            }

            try (PDDocument document = PDDocument.load(pdfFile)) {

                PDFPrintable printable = new PDFPrintable(
                        document,
                        Scaling.SHRINK_TO_FIT, // ajusta à página sem cortar
                        true,                   // showPageBorder
                        0                       // dpi (0 = padrão do driver)
                );


                PrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();

                if (copies < 1) copies = 1;
                attrs.add(new Copies(copies));

                if (target.isAttributeCategorySupported(Chromaticity.class)) {
                    attrs.add(color ? Chromaticity.COLOR : Chromaticity.MONOCHROME);
                }

                if (target.isAttributeCategorySupported(OrientationRequested.class)) {
                    attrs.add(landscape ? OrientationRequested.LANDSCAPE : OrientationRequested.PORTRAIT);
                }

                // (opcional) Qualidade e duplex – descomente se quiser
                // if (target.isAttributeCategorySupported(PrintQuality.class)) {
                //     attrs.add(PrintQuality.HIGH);
                // }
                // if (target.isAttributeCategorySupported(Sides.class)) {
                //     attrs.add(Sides.ONE_SIDED); // ou Sides.DUPLEX
                // }


                DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
                Doc doc = new SimpleDoc(printable, flavor, null);
                DocPrintJob job = target.createPrintJob();

                job.addPrintJobListener(new PrintJobAdapter() {
                    @Override public void printJobCompleted(PrintJobEvent pje) {
                        System.out.println("Job concluído: " + pdfPath);
                    }
                    @Override public void printJobFailed(PrintJobEvent pje) {
                        System.err.println("Job falhou: " + pdfPath);
                    }
                });

                job.print(doc, attrs);
                xlog = "SUCCESS";
            }

        } catch (PrintException | IOException e) {
            xlog = e.getClass().getSimpleName() + ": " + e.getMessage();
        }

        return xlog;
    }
}
