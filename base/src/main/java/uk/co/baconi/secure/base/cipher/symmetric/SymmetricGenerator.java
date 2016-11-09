package uk.co.baconi.secure.base.cipher.symmetric;

import org.springframework.stereotype.Service;
import uk.co.baconi.secure.base.cipher.UnsupportedCipherTypeException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

@Service
public class SymmetricGenerator {

    public SecretKey generateKey(final SymmetricCipher type, final int bits) {
        switch (type) {
            case AES_CBC_PKCS7:
            case AES_GCM_NONE: return generateSecretKey(type, bits);
            default:
                throw new UnsupportedCipherTypeException(type, "generate-key");
        }
    }

    /**
     * [WARNING!] This is experimental and signature may change or be replaced by something else entirely.
     */
    public AlgorithmParameterSpec generateParameters(final SymmetricCipher type) {
        switch (type) {
            case AES_CBC_PKCS7: return generateIv(16);
            case AES_GCM_NONE: return generateIv(12);
            default:
                throw new UnsupportedCipherTypeException(type, "generate-parameters");
        }
    }

    private IvParameterSpec generateIv(final int bytes) {

        final byte[] iv = new byte[bytes];

        final SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);

        return new IvParameterSpec(iv);
    }

    private SecretKey generateSecretKey(final SymmetricCipher type, final int bits) {

        final KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance(type.getKeyGeneratorType());
        } catch (final NoSuchAlgorithmException exception) {
            throw new UnsupportedCipherTypeException(type, "generate-key-impl", exception);
        }

        keyGen.init(bits);

        return keyGen.generateKey();
    }
}
