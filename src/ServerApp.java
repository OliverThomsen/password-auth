import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ServerApp {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        DataBaseConnection db = new DataBaseConnection();
        Registry registry = LocateRegistry.createRegistry(5099);
        registry.rebind("auth", new AuthServant(db));
    }
}
