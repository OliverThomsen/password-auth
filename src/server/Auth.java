package server;

import shared.AuthInterface;
import shared.SessionInterface;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;

public class Auth extends UnicastRemoteObject implements AuthInterface {
    private KeyFactory factory = KeyFactory.getInstance("RSA");
    private RSAPublicKeySpec publicKeySpec;
    private RSAPrivateKeySpec privateKeySpec;
    private DataBaseConnection db;
    private Storage storage;

    Auth(Storage storage) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        super();
        this.storage = storage;
        initRSAEncryption();
        byte[] salt = createRandomSalt();
        byte[] passwordHash = hash("Hello123".getBytes(), salt); // Hash password
        byte[] passwordHashSalt = Converter.concatBytes(passwordHash, salt); // Append salt to hashed password

        // Create new User
//        db.registerUser(new String(username), passwordHashSalt);
        System.out.println(Converter.byteArrayToHex(passwordHashSalt));
    }

    Auth(DataBaseConnection db, Storage storage) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        super();
        this.db = db;
        this.storage = storage;
        initRSAEncryption();
    }

    private void initRSAEncryption() throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair keyPair = kpg.generateKeyPair();

        publicKeySpec = factory.getKeySpec(keyPair.getPublic(), RSAPublicKeySpec.class);
        privateKeySpec = factory.getKeySpec(keyPair.getPrivate(), RSAPrivateKeySpec.class);
    }

    @Override
    public BigInteger getModulus() {
        return publicKeySpec.getModulus();
    }

    @Override
    public BigInteger getExponent() {
        return publicKeySpec.getPublicExponent();
    }

    @Override
    public SessionInterface authenticate(byte[] userNameEncrypted, byte[] passwordEncrypted) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, RemoteException {
        // Decrypt password and username
        byte[] passwordReceived = rsaDecrypt(passwordEncrypted);
        String userName = new String( rsaDecrypt(userNameEncrypted) );

        try {
            // Get password from Server.Storage
            byte[] passwordWithSaltFromStorage = storage.getPassword(userName);
            byte[] passwordFromStorage = extractPassword(passwordWithSaltFromStorage);
            byte[] salt = extractSalt(passwordWithSaltFromStorage);

            // Use salt from storage to Hash received password
            byte[] passwordReceivedHashed = hash(passwordReceived, salt);

            // Compare received password with password from storage
            if (Arrays.equals(passwordFromStorage, passwordReceivedHashed)) {
                System.out.println(userName + " authenticated");
                // Return a Server.Session to the user
                return new Session(userName, storage);
            }
        } catch (NullPointerException e) {
            System.out.println(userName + " does not have a user");
            return null;
        }
        System.out.println(userName + " failed authentication");
        return null;
    }

    @Override
    // Only worked with previous postgres DB implementation
    public boolean register(byte[] userNameEncrypted, byte[] passwordEncrypted) throws IOException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        if (this.db == null) {
            return false;
        }
        // Decrypt password and username
        byte[] password = rsaDecrypt(passwordEncrypted);
        byte[] username = rsaDecrypt(userNameEncrypted);

        // Create salt and hash password
        byte[] salt = createRandomSalt();
        byte[] passwordHash = hash(password, salt); // Hash password
        byte[] passwordHashSalt = Converter.concatBytes(passwordHash, salt); // Append salt to hashed password

        // Create new User
        db.registerUser(new String(username), passwordHashSalt);
        System.out.println("User "+new String(username)+" registered");

        return true;
    }

    private byte[] rsaDecrypt(byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        BigInteger modulus = privateKeySpec.getModulus();
        BigInteger exponent = privateKeySpec.getPrivateExponent();
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
        PrivateKey privateKey = factory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    private byte[] hash(byte[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        char[] charPassword = (new String(password)).toCharArray();
        KeySpec spec = new PBEKeySpec(charPassword, salt, 65536, 128);
        return factory.generateSecret(spec).getEncoded();
    }

    private byte[] extractPassword(byte[] passwordHash) {
        return Arrays.copyOfRange(passwordHash, 0, 16);
    }

    private byte[] extractSalt(byte[] passwordHash) {
        return Arrays.copyOfRange(passwordHash, 16, 32);
    }

    private byte[] createRandomSalt() {
        SecureRandom generator = new SecureRandom();
        byte[] salt = new byte[16];
        generator.nextBytes(salt);
        return salt;
    }
}
