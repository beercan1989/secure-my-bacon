package uk.co.baconi.secure.base.cipher.symmetric;

import org.springframework.stereotype.Service;

@Service
public class SymmetricEngine {

    public byte[] encrypt(final SymmetricCipher type, final byte[] key, final String plainText) {
        // TODO - Implement
        return new byte[] { 5, 6, 7, 8 };
    }

    public String decrypt(final SymmetricCipher type, final byte[] key, final byte[] cipherText) {
        // TODO - Implement
        return "fake-string";
    }
}
