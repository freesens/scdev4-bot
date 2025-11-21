package kr.co.pionnet.scdev4.bot;

import org.assertj.core.api.Assertions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.ZeroSaltGenerator;
import org.junit.jupiter.api.Test;

public class EncryptorTest {
    private final String secretKey = "test-secret-key";

    @Test
    void encryptTest() {
        String originText = "This is target text.";
        String targetText = "fOtibkz6TSOJn3jcoqqekbzj4QSAN3p1";

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(secretKey);
        encryptor.setAlgorithm("PBEWithMD5AndDES");

        // only test!!
        encryptor.setSaltGenerator(new ZeroSaltGenerator());

        String encryptedText = encryptor.encrypt(originText);
        System.out.println("encryptedText = " + encryptedText);

        Assertions.assertThat(encryptedText).isEqualTo(targetText);
    }

    @Test
    void decryptTest() {
        String originText = "n73EKiR1wloUfPdONQFG+JEml8y1HvOVk3K8TypHjgM=";
        String targetText = "This is target text.";

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(secretKey);
        encryptor.setAlgorithm("PBEWithMD5AndDES");

        String decryptedText = encryptor.decrypt(originText);
        System.out.println("decryptedText = " + decryptedText);

        Assertions.assertThat(decryptedText).isEqualTo(targetText);
    }
}
