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
import java.security.spec.RSAPublicKeySpec;

public interface AuthService extends Remote {
    public String echo(String input) throws RemoteException;

    public BigInteger getModulus() throws RemoteException;

    public BigInteger getExponent() throws RemoteException;

    public boolean authenticate(byte[] password) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException;

    boolean register(byte[] userNameEncrypted, byte[] passwordEncrypted) throws IOException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException;
}
