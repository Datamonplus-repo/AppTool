package app.tools;

import com.google.zxing.BarcodeFormat;
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
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class tools {

   public String qrcode(int width, int height, String data)  {

      BitMatrix matrix= null;
      String retorno = null;

      try {

         QRCodeWriter writer = new QRCodeWriter();
         BitMatrix qrBitMatrix = writer.encode(data, BarcodeFormat.QR_CODE,width,height);

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


}
