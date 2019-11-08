import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigInteger;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface AuthInterface extends Remote {
    BigInteger getModulus() throws RemoteException;

    BigInteger getExponent() throws RemoteException;

    SessionInterface authenticate(byte[] userName, byte[] password) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException;

    boolean register(byte[] userNameEncrypted, byte[] passwordEncrypted) throws IOException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException;
}
