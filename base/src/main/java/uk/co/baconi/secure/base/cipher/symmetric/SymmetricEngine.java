package uk.co.baconi.secure.base.cipher.symmetric;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.baconi.secure.base.cipher.DecryptionException;
import uk.co.baconi.secure.base.cipher.EncryptionException;
import uk.co.baconi.secure.base.cipher.UnsupportedCipherTypeException;
import uk.co.baconi.secure.base.cipher.charset.CharsetCodec;

import javax.crypto.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.function.Function;

@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class SymmetricEngine {

    private final CharsetCodec charsetCodec;

    public byte[] encrypt(final SymmetricCipher type, final SecretKey secretKey, final AlgorithmParameterSpec parameterSpec, final String plainText) throws EncryptionException {

        final Cipher engine = initCipher(Cipher.ENCRYPT_MODE, type, secretKey, parameterSpec);

        final byte[] dataToEncrypt = charsetCodec.encode(plainText);

        return doFinal(engine, dataToEncrypt, EncryptionException::new);
    }

    public String decrypt(final SymmetricCipher type, final SecretKey secretKey, final AlgorithmParameterSpec parameterSpec, final byte[] cipherText) throws DecryptionException {

        final Cipher engine = initCipher(Cipher.DECRYPT_MODE, type, secretKey, parameterSpec);

        final byte[] decryptedData = doFinal(engine, cipherText, DecryptionException::new);

        return charsetCodec.decode(decryptedData);
    }

    private <A extends Exception> byte[] doFinal(final Cipher engine, final byte[] cipherText, final Function<Exception, A> onError) throws A {
        try {
            return engine.doFinal(cipherText);
        } catch (final IllegalBlockSizeException | BadPaddingException exception) {
            throw onError.apply(exception); // TODO - Review exception wrapping
        }
    }

    /**
     * Setup Cipher engine with everything we need to perform our action
     *
     * @param mode          cipher mode
     * @param type          type of symmetric cipher
     * @param secretKey     symmetric cipher key
     * @param parameterSpec any extra parameters required by the symmetric cipher
     * @return the initialised cipher
     */
    private Cipher initCipher(final int mode, final SymmetricCipher type, final SecretKey secretKey, final AlgorithmParameterSpec parameterSpec) {

        // Create Cipher engine
        final Cipher engine;
        try {
            engine = Cipher.getInstance(type.getType());
        } catch (final NoSuchPaddingException | NoSuchAlgorithmException exception) {
            throw new UnsupportedCipherTypeException(type, "cipher-get-instance", exception); // TODO - Review exception wrapping
        }

        // Init engine with key and parameters
        try {
            if (parameterSpec != null) {
                engine.init(mode, secretKey, parameterSpec);
            } else {
                engine.init(mode, secretKey);
            }
        } catch (final InvalidKeyException | InvalidAlgorithmParameterException exception) {
            throw new UnsupportedCipherTypeException(type, "cipher-init", exception); // TODO - Review exception wrapping
        }

        return engine;
    }
}
