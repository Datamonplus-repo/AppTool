package app.tools;

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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class tools {

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

      public String hashAt(String pathPrivatePem, String value)  {

      String hashEnc = null;

      try {

         FileReader fileReader = new FileReader(pathPrivatePem);
         PEMParser pemParser = new PEMParser(fileReader);

         Object pemObject = pemParser.readObject();

         if (pemObject instanceof PEMKeyPair) {

            Security.addProvider(new BouncyCastleProvider());
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

         pemParser.close();
      } catch (Exception e) {
         hashEnc = "Erro ao validar o arquivo PEM: " + e.getMessage();
      }
      return hashEnc;
   }

      public String MergePdfFromJson(String jsonInputPath, String outputMergedPdf) {

         String Log = null;

         try {

            PDFMergerFromJson.merge(jsonInputPath, outputMergedPdf);
            Log = outputMergedPdf;

         } catch (Exception e) {
            Log = "Erro ao agrupar PDFs: " + e.getMessage();
         }

         return Log;
      }

}
