import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

public class ClientApp {
    private static RSAPublicKeySpec publicKey;
    private static KeyFactory factory;
    private static BigInteger modulus, exponent;

    public ClientApp() throws NoSuchAlgorithmException {
    }

    public static void main(String[] args) throws IOException, NotBoundException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        factory = KeyFactory.getInstance("RSA");
        AuthService auth = (AuthService) Naming.lookup("rmi://localhost:5089/auth");
        modulus = auth.getModulus();
        exponent = auth.getExponent();

        String password = "Hello123";
        byte[] passwordBytes = password.getBytes();
        byte[] passwordEncrypt = rsaEncrypt(passwordBytes);
        boolean loginSuccess = auth.authenticate(passwordEncrypt);
        System.out.println("Login success: " + loginSuccess);
    }

    private static byte[] rsaEncrypt(byte[] data) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchPaddingException, NoSuchAlgorithmException {
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
        PublicKey pubKey = factory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

}
