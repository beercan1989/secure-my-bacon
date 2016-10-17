package uk.co.baconi.secure.base.cipher.symmetric;

import org.springframework.stereotype.Service;

@Service
public class SymmetricGenerator {

    public byte[] generateKey(final SymmetricCipher type, final int bits) {
        // TODO - Implement
        return new byte[] { 1, 2, 3, 4 };
    }

}
