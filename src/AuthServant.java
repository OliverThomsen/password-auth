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

public class AuthServant extends UnicastRemoteObject implements AuthService {
    private final byte[] password;
    private KeyFactory factory = KeyFactory.getInstance("RSA");
    private RSAPublicKeySpec pub;
    private RSAPrivateKeySpec priv;


    protected AuthServant() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
        super();

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair keyPair = kpg.generateKeyPair();

        this.pub = factory.getKeySpec(keyPair.getPublic(), RSAPublicKeySpec.class);
        this.priv = factory.getKeySpec(keyPair.getPrivate(), RSAPrivateKeySpec.class);
        byte[] salt = getRandomSalt();
        this.password = concatBytes( hash("Hello123".getBytes(), salt), salt );
    }

    @Override
    public String echo(String input) throws RemoteException {
        return "From server: " + input;
    }

    @Override
    public BigInteger getModulus() throws RemoteException {
        return pub.getModulus();
    }

    @Override
    public BigInteger getExponent() throws RemoteException {
        return pub.getPublicExponent();
    }

    @Override
    public boolean authenticate(byte[] passwordEncrypted) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        byte[] password = rsaDecrypt(passwordEncrypted);
        byte[] passwordWithSaltFromDB = getPasswordFromDB();
        byte[] passwordFromDB = extractPassword(passwordWithSaltFromDB);
        byte[] salt = extractSalt(passwordWithSaltFromDB);
        byte[] passwordHashed = hash(password, salt);

        return Arrays.equals(passwordFromDB, passwordHashed);
    }

    @Override
    public boolean register(byte[] userNameEncrypted, byte[] passwordEncrypted) throws IOException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] password = rsaDecrypt(passwordEncrypted);
        byte[] username = rsaDecrypt(userNameEncrypted);
        byte[] salt = getRandomSalt();
        byte[] passwordHashed = hash(password, salt);
        byte[] passwordHashedWithSalt = concatBytes(passwordHashed, salt);
        // TODO: savePassword(username, passwordHashedWithSalt);

        return true;
    }

    private byte[] rsaDecrypt(byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        BigInteger modulus = priv.getModulus();
        BigInteger exponent = priv.getPrivateExponent();
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
        PrivateKey privKey = factory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        return cipher.doFinal(data);
    }

    private byte[] hash(byte[] password, byte[] salt) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");

        // Create PBKDF2 cipher
        char[] charPassword = (new String(password)).toCharArray();
        KeySpec spec = new PBEKeySpec(charPassword, salt, 65536, 128);

        byte[] hash = factory.generateSecret(spec).getEncoded();
        System.out.println("hash length: "+hash.length);
        for (byte b : hash) {
            System.out.print(b+" ");
        }

        return hash;
    }


    private byte[] extractPassword(byte[] passwordHash) {
        return Arrays.copyOfRange(passwordHash, 0, 16);
    }

    private byte[] extractSalt(byte[] passwordHash) {
        System.out.println(passwordHash.length);
        return Arrays.copyOfRange(passwordHash, 16, 32);
    }

    private byte[] getRandomSalt() {
        SecureRandom generator = new SecureRandom();
        byte[] salt = new byte[16];
        generator.nextBytes(salt);
        System.out.println("salt length: "+salt.length);
        for (byte b : salt) {
            System.out.print(b+" ");
        }
        System.out.println();
        return salt;
    }

    private byte[] getPasswordFromDB() {
        return this.password;
    }

    private byte[] concatBytes(byte[] a, byte[] b) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write( a );
        outputStream.write( b );
        return outputStream.toByteArray();
    }


}
