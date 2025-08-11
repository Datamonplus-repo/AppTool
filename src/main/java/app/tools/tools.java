package app.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import java.util.List;
import java.util.regex.*;

public class tools {

      static {
         if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
         }
      }

      public String qrcodeCM(int cm, String data, String Path) {

         String retorno = null;

         int dpi = 300;
         double cmToInches = cm * 1.0;
         int pixels = (int) Math.round((cmToInches * dpi) / 2.54);

         System.out.println(cm + " cm equivale a " + pixels + " pixels (a " + dpi + " DPI)");

         String appPath = Paths.get("").toAbsolutePath().toString();
         System.out.println("Path da aplicação: " + appPath);

         try {

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.MARGIN, 0); // Remove padding

            // Gerar o QR Code
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, pixels, pixels,hints);

            // Converter BitMatrix em BufferedImage
            BufferedImage image = new BufferedImage(pixels, pixels, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();

            graphics.setColor(Color.WHITE); // Fundo branco
            graphics.fillRect(0, 0, pixels, pixels);
            graphics.setColor(Color.BLACK); // QR Code preto

            for (int x = 0; x < pixels; x++) {
               for (int y = 0; y < pixels; y++) {
                  if (bitMatrix.get(x, y)) {
                     graphics.fillRect(x, y, 1, 1);
                  }
               }
            }
            graphics.dispose();

            // Salvar a imagem física (formato PNG)

            File outputFile = new File(Path);
            ImageIO.write(image, "PNG", outputFile);

            System.out.println("QR Code salvo com sucesso em: " + outputFile.getAbsolutePath());

            retorno = outputFile.getAbsolutePath();

         } catch (WriterException | IOException e) {
            e.printStackTrace();
         }

         return retorno;

      }

      public String qrcode(int width, int height, String data)  {

      String retorno = null;

      try {

         Map<EncodeHintType, Object> hints = new HashMap<>();
         hints.put(EncodeHintType.MARGIN, 0); // Remove padding

         QRCodeWriter writer = new QRCodeWriter();
         BitMatrix qrBitMatrix = writer.encode(data, BarcodeFormat.QR_CODE,width,height,hints);

         BufferedImage qrImage =  MatrixToImageWriter.toBufferedImage(qrBitMatrix);
         ByteArrayOutputStream os = new ByteArrayOutputStream();
         ImageIO.write(qrImage,"png",os);

         byte[] imageBytes = os.toByteArray();
         retorno = new String(Base64.encode(imageBytes));

      } catch (WriterException | IOException e) {
         retorno = e.getMessage();
      }

     return retorno;
   }

      public String hashAt(String pathPrivatePem, String value) {

      String hashEnc = null;

        try {

           RsaSigner.loadPrivateKey(pathPrivatePem);
           hashEnc = RsaSigner.signValue(value);

        } catch (Exception e) {
           hashEnc = e.getMessage();
        }

      return hashEnc;
   }

      public String hashAt_bck(String pathPrivatePem, String value)  {

      String hashEnc = null;

      try {

         FileReader fileReader = new FileReader(pathPrivatePem);
         PEMParser pemParser = new PEMParser(fileReader);

         Object pemObject = pemParser.readObject();

         if (pemObject instanceof PEMKeyPair) {

            if (Security.getProvider("BC") == null) {
               Security.addProvider(new BouncyCastleProvider());
            }

            PEMKeyPair pemKeyPair = (PEMKeyPair) pemObject;
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            PrivateKey privateKey = converter.getPrivateKey(pemKeyPair.getPrivateKeyInfo());

            Signature signature = Signature.getInstance("SHA1withRSA", "BC");
            signature.initSign(privateKey);
            signature.update(value.getBytes(StandardCharsets.UTF_8));

            byte[] assinatura = signature.sign();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            try {
               byteArrayOutputStream.write(assinatura);
               byte[] hastbase64  = byteArrayOutputStream.toByteArray();
               hashEnc = new String(Base64.encode(hastbase64));
            } catch ( Exception e) {
               hashEnc = e.getMessage();
            }

         } else {
            hashEnc = "O arquivo PEM não contém uma chave privada válida.";
         }

         fileReader.close();
         pemParser.close();

      } catch (Exception e) {
         hashEnc = "Erro ao validar o arquivo PEM: " + e.getMessage();
      }


      return hashEnc;
   }

      public String merge(String jsonInputPath, String outputMergedPdf) {

         String Log = null;

         try {

            PDFMergerFromJson.merge(jsonInputPath, outputMergedPdf);
            Log = outputMergedPdf;

         } catch (Exception e) {
            Log = "Erro ao agrupar PDFs: " + e.getMessage();
         }

         return Log;
      }

      public String servletInfo() {

            String basePath = System.getProperty("catalina.base");
            return basePath + java.io.File.separator;
      }

      public String listprinter() {

         // Imprimir como JSON formatado
         String jsonOutput = null;
         try {
         PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

         ObjectMapper mapper = new ObjectMapper();
         ArrayNode printerArray = mapper.createArrayNode();

         for (PrintService service : services) {
            ObjectNode printerJson = mapper.createObjectNode();
            printerJson.put("name", service.getName());
            printerJson.put("path", service.getName()); // No Java, não há um path real
            printerJson.put("status", "AVAILABLE");     // Status fixo por limitação da API Java
            printerArray.add(printerJson);
         }
          jsonOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(printerArray);

          } catch (JsonProcessingException e) {
              throw new RuntimeException(e);
          }

          System.out.println(jsonOutput);

         return jsonOutput;
      }

      public String printto(String pdfPath, String printerName) {

         System.out.println(pdfPath);
         System.out.println(printerName);



         String xlog = "";

         try {
         File pdfFile = new File(pdfPath);

         try (PDDocument document = PDDocument.load(pdfFile)) {

            PrinterJob job = PrinterJob.getPrinterJob();
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

            for (PrintService ps : printServices) {

               if (ps.getName().equalsIgnoreCase(printerName)) {
                       job.setPrintService(ps);
                   break;
               }
            }

            job.setJobName("Datamon Printer - WEB ");
            job.setPageable(new PDFPageable(document));
            job.print();
            xlog = "SUCCESS";
         }
         } catch (PrinterException | IOException e) {
            xlog = e.getMessage();
         }

         return xlog;
      }

      public String BuscarDatas(String texto) {
      StringBuilder resultado = new StringBuilder();

      String regex =
              "\\b\\d{2}[\\/\\.\\-]\\d{2}[\\/\\.\\-]\\d{4}\\b" +                        // 18/12/2024, 08.09.2021
                      "|\\b\\d{1,2} de [a-zç]+ de \\d{4}\\b" +                                  // 18 de outubro de 2024
                      "|\\b\\d{1,2} de [A-ZÇ][a-zç]+ de \\d{4}\\b" +                            // 05 de Outubro de 2022
                      "|\\b\\d{2}\\.\\d{2}\\.\\d{4}\\b";                                        // 08.09.2021

      Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
      Matcher matcher = pattern.matcher(texto);

      List<String> datasEncontradas = new ArrayList<>();
      while (matcher.find()) {
         datasEncontradas.add(matcher.group());
      }

      for (String dataOriginal : datasEncontradas) {
         Date dataConvertida = null;

         String[] formatosEntrada = {
                 "dd/MM/yyyy", "dd.MM.yyyy", "dd-MM-yyyy",
                 "d 'de' MMMM 'de' yyyy", "dd 'de' MMMM 'de' yyyy",
                 "d 'de' MMMMM 'de' yyyy", "dd.MM.yyyy"
         };

         for (String formato : formatosEntrada) {
            try {
               DateFormat entrada = new SimpleDateFormat(formato, new Locale("pt", "BR"));
               entrada.setLenient(false);
               dataConvertida = entrada.parse(dataOriginal);
               break;
            } catch (ParseException e) {
               // Tenta o próximo formato
            }
         }

         if (dataConvertida != null) {
            DateFormat saida = new SimpleDateFormat("dd/MM/yyyy");
            resultado.append("").append(saida.format(dataConvertida)).append(",");
         }
      }

      return resultado.toString().trim();
   }

      public String ExtratoBradesco(String path) {

         return null;
      }

      public String email(String json) {

         String log = null;

         try {
            ObjectMapper mapper = new ObjectMapper();
            EmailRequest request = mapper.readValue(json, EmailRequest.class);
            EmailSender.sendEmail(request);

            log = "success : ok ";

         } catch (Exception e) {
            log = "error : " + e.getMessage();
         }

         return log;
      }

}
