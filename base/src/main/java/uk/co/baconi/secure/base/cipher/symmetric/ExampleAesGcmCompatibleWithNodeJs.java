package uk.co.baconi.secure.base.cipher.symmetric;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import uk.co.baconi.secure.base.cipher.DecryptionException;
import uk.co.baconi.secure.base.cipher.charset.CharsetCodec;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Security;

public class ExampleAesGcmCompatibleWithNodeJs {

    private ExampleAesGcmCompatibleWithNodeJs() {}

    public static void main(final String[] args) throws IOException, DecryptionException {

        Security.addProvider(new BouncyCastleProvider());

        final SymmetricEngine engine = new SymmetricEngine(new CharsetCodec());

        final byte[] cipherText = Base64.decode("SUTBvAf8mB1iqoDj2TpmRddtyR4GjhlyaZIdMvwbvi2+H1NQeeE2BxuGUFxXC8BhL3K3r89VNuSEkmU=");
        final byte[] authTag = Base64.decode("qSdiLNH8eTsXDDYKF0/1mw==");
        final byte[] iv = Base64.decode("1MmxNgwpU42Mi77Z");
        final byte[] key = Base64.decode("TIZTMfJvxTSAlyjMP76QWo9Src/+ALpcaI59Kx8M2e4=");

        final IvParameterSpec parameterSpec = new IvParameterSpec(iv);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(cipherText);
        outputStream.write(authTag);

        final SecretKey secretKey = new SecretKeySpec(key, "AES");

        final String result = engine.decrypt(SymmetricCipher.AES_GCM_NONE, secretKey, parameterSpec, outputStream.toByteArray());

        System.out.println("result: " + result);

    }

}
