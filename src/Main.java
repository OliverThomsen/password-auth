import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.*;
import java.io.Console;

public class Main {

    public static void main(String[] args) {
        try {
            // runEas();
            runPassword();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /*
     * Password Based Encryption†
     */
    private static void runPassword() throws Exception {
        PBEKeySpec pbeKeySpec;
        PBEParameterSpec pbeParameterSpec;
        SecretKeyFactory secretKeyFactory;

        // Salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        // Iteration count
        int count = 1000;

        // Iteration count
        pbeParameterSpec = new PBEParameterSpec(salt, count);

        // Prompt user for encryption password.
        // Collect user password as char array, and convert
        // it into a SecretKey object, using a PBE key
        // factory.
        char[] password = System.console().readPassword("Enter password: ");
        pbeKeySpec = new PBEKeySpec(password);
        secretKeyFactory = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_256");
        SecretKey pbeKey = secretKeyFactory.generateSecret(pbeKeySpec);

        // Create PBE Cipher
        Cipher pbeCipher = Cipher.getInstance("PBEWithHmacSHA256AndAES_256");

        // Initialize PBE Cipher with key parameters
        pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParameterSpec);

        // Our clear text
        byte[] clearText = "Hello world!".getBytes();
        System.out.println(new String(clearText));

        // Encrypt clear text
        byte[] cipherText = pbeCipher.doFinal(clearText);
        System.out.println(new String(cipherText));

        // Decrypt cipher text
//        byte[] textDecrypt =textDecrypt
    }


    /*
     * AES Encryption
     */
    private static void runEas() throws Exception {
        // Generate key
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecretKey aesKey = keyGenerator.generateKey();
        System.out.println("Key: " + new String(aesKey.getEncoded()));

        // Create cipher
        Cipher aesCipher = Cipher.getInstance("AES");

        // Clear text
        byte[] clearText = "Hello world!".getBytes();
        System.out.println("Clear text: " + new String(clearText));

        // Encrypt
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] cipherText = aesCipher.doFinal(clearText);
        System.out.println("Cipher text: " + new String(cipherText));

        // Decrypt
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] clearText2 = aesCipher.doFinal(cipherText);
        System.out.println("New: " + new String(clearText2));

    }
}
