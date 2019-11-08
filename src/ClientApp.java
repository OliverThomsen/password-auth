import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.naming.AuthenticationException;
import java.io.IOException;
import java.math.BigInteger;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

public class ClientApp {
    private static KeyFactory factory;
    private static BigInteger modulus, exponent;

    public ClientApp() throws NoSuchAlgorithmException {
    }

    public static void main(String[] args) throws IOException, NotBoundException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        factory = KeyFactory.getInstance("RSA");
        AuthService auth = (AuthService) Naming.lookup("rmi://localhost:5099/auth");

        modulus = auth.getModulus();
        exponent = auth.getExponent();

        SessionInterface session = auth.authenticate(
                rsaEncrypt("Alice".getBytes()),
                rsaEncrypt("Hello123".getBytes())
        );

        try {
            PrintService printer = session.getPrinter();
            System.out.println("Start: " +
                    printer.start()
            );
            System.out.println("Print: " +
                    printer.print("Hello", "2")
            );
            System.out.println("Queue: " +
                    printer.queue("2")
            );
            System.out.println("Set config: " +
                    printer.setConfig("param", "42")
            );
            System.out.println("Read config: " +
                    printer.readConfig("param")
            );
            System.out.println("Top queue: " +
                    printer.topQueue("2", 3)
            );
            System.out.println("Status: " +
                    printer.status("2")
            );
            System.out.println("Restart: " +
                    printer.restart()
            );

            session.logout();

            System.out.println("Print: " +
                    printer.print("This will never happen", "3")
            );
            System.out.println("Stop: " +
                    printer.stop()
            );
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
    }

    private static byte[] rsaEncrypt(byte[] data) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchPaddingException, NoSuchAlgorithmException {
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
        PublicKey pubKey = factory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

}
