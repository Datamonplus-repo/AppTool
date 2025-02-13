Generador de QR Code en Java

Este es un programa en Java que genera un código QR según un tamaño especificado en centímetros. Convierte las dimensiones en cm a píxeles en función de la resolución (DPI) y genera un archivo de imagen QR.

Características

Convierte las dimensiones de centímetros a píxeles según un DPI especificado.

Genera un código QR basado en una URL proporcionada.

Guarda el QR Code como una imagen PNG.

Requisitos

Java 8 o superior.

Biblioteca ZXing (para la generación del QR Code).

Instalación

Clona o descarga el repositorio.

Asegúrate de tener la biblioteca ZXing en tu proyecto. Si usas Maven, agrégala en tu pom.xml:

<dependencies>
    <dependency>
        <groupId>com.google.zxing</groupId>
        <artifactId>core</artifactId>
        <version>3.4.1</version>
    </dependency>
    <dependency>
        <groupId>com.google.zxing</groupId>
        <artifactId>javase</artifactId>
        <version>3.4.1</version>
    </dependency>
</dependencies>

Uso

Ejecuta el programa pasando los siguientes parámetros:

URL: El contenido del QR Code.

Tamaño en cm: Dimensiones del QR Code en centímetros.

DPI: Resolución deseada (300 DPI recomendado para impresión).

Ejemplo de Uso

java -jar GeneradorQRCode.jar "https://ejemplo.com" 3 300

Código Principal

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QRCodeGenerator {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Uso: java -jar GeneradorQRCode.jar <URL> <Tamaño en cm> <DPI>");
            return;
        }
        
        String data = args[0];
        double cmSize = Double.parseDouble(args[1]);
        int dpi = Integer.parseInt(args[2]);
        
        // Convertir cm a píxeles
        int sizeInPixels = (int) Math.round((cmSize * dpi) / 2.54);
        
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, sizeInPixels, sizeInPixels);
            
            BufferedImage image = new BufferedImage(sizeInPixels, sizeInPixels, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, sizeInPixels, sizeInPixels);
            graphics.setColor(Color.BLACK);
            
            for (int x = 0; x < sizeInPixels; x++) {
                for (int y = 0; y < sizeInPixels; y++) {
                    if (bitMatrix.get(x, y)) {
                        graphics.fillRect(x, y, 1, 1);
                    }
                }
            }
            graphics.dispose();
            
            File outputFile = new File("qrcode_" + cmSize + "cm.png");
            ImageIO.write(image, "PNG", outputFile);
            
            System.out.println("QR Code generado y guardado en: " + outputFile.getAbsolutePath());
            System.out.println("Tamaño: " + cmSize + " cm (" + sizeInPixels + " píxeles)");
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }
}

Salida esperada

El programa generará un archivo PNG con el código QR en el directorio de ejecución.

Ejemplo de salida:

QR Code generado y guardado en: /home/usuario/qrcode_3cm.png
Tamaño: 3.0 cm (354 píxeles)

Notas

Usa 300 DPI para impresión precisa.

Para visualización en pantalla, 96 DPI es suficiente.

Asegúrate de instalar ZXing si usas un entorno sin Maven.

Licencia

Este proyecto está bajo la licencia MIT.

