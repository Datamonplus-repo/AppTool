package app.tools;

import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

public class RsaSigner {

    private static final AtomicReference<PrivateKey> cachedPrivateKey = new AtomicReference<>();
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

    static {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    public static void loadPrivateKey(String pathPrivatePem) throws Exception {
        if (cachedPrivateKey.get() != null) {
            return; // já carregada
        }

        try (FileReader fileReader = new FileReader(pathPrivatePem);
             PEMParser pemParser = new PEMParser(fileReader)) {

            Object pemObject = pemParser.readObject();

            if (!(pemObject instanceof PEMKeyPair)) {
                throw new IllegalArgumentException("PEM inválido ou não contém chave privada.");
            }

            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            PrivateKey privateKey = converter.getPrivateKey(((PEMKeyPair) pemObject).getPrivateKeyInfo());

            cachedPrivateKey.set(privateKey);
        }
    }

    /**
     * Assina o valor informado usando a chave privada previamente carregada.
     */
    public static String signValue(String value) throws Exception {
        PrivateKey privateKey = cachedPrivateKey.get();
        if (privateKey == null) {
            throw new IllegalStateException("Chave privada ainda não carregada. Use loadPrivateKey() antes.");
        }

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM, "BC");
        signature.initSign(privateKey);
        signature.update(value.getBytes(StandardCharsets.UTF_8));

        byte[] signed = signature.sign();
        return Base64.getEncoder().encodeToString(signed);
    }
}