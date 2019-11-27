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
import java.util.Scanner;

public class ClientApp {
    private static KeyFactory factory;
    private static BigInteger modulus, exponent;
    private static PrintServerInterface printer;
    private static SessionInterface session;
    private static AuthInterface auth;

    public static void main(String[] args) throws IOException, NotBoundException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        factory = KeyFactory.getInstance("RSA");
        auth = (AuthInterface) Naming.lookup("rmi://localhost:5099/auth");
        System.out.println("Connected to server");
        modulus = auth.getModulus();
        exponent = auth.getExponent();

        Scanner scanner = new Scanner(System.in);

        /*
         * Asks the user for login details until correct details are provided
         * The authenticate function here also returns the session object that is saved globally
         * The session object is used to keep the users session when the are logged in
         * and will also provide the print server instance.
         */
        SessionInterface session = null;
        while (session == null) {
            System.out.println("Enter username:");
            String username = scanner.next();
            System.out.println("Enter password:");
            String password = scanner.next();
            session = auth.authenticate(
                    rsaEncrypt(username.getBytes()),
                    rsaEncrypt(password.getBytes())
            );
            if (session == null) System.out.println("Password or username wrong");
        }

        /*
         * Present user with printer options
         * And ask keep asking the user for an option until they log out
         */
        try {
            printer = session.getPrinter();
            System.out.println("1 : print");
            System.out.println("2 : queue");
            System.out.println("3 : topQueue");
            System.out.println("4 : start");
            System.out.println("5 : stop");
            System.out.println("6 : restart");
            System.out.println("7 : status");
            System.out.println("8 : readConfig");
            System.out.println("9 : setConfig");
            System.out.println("0 : logout");

            int action = -1;
            while (action != 0) {

                if (scanner.hasNextInt()) {
                    action = scanner.nextInt();

                    String result = "";
                    switch (action) {
                        case 1:
                            result = printer.print("filename", "printer");
                            break;
                        case 2:
                            result = printer.queue("printer");
                            break;
                        case 3:
                            result = printer.topQueue("job", 1);
                            break;
                        case 4:
                            result = printer.start();
                            break;
                        case 5:
                            result = printer.stop();
                            break;
                        case 6:
                            result = printer.restart();
                            break;
                        case 7:
                            result = printer.status("printer");
                            break;
                        case 8:
                            result = printer.readConfig("param");
                            break;
                        case 9:
                            result = printer.setConfig("param", "value");
                            break;
                        case 0:
                            session.logout();
                            result = "User logged out";
                            break;
                        default:
                            break;
                    }
                    System.out.println(result);
                } else {
                    scanner.next();
                }
            }

        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }


    private static byte[] rsaEncrypt(byte[] data) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchPaddingException, NoSuchAlgorithmException {
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
        PublicKey pubKey = factory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }
}
