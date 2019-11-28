package server;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ServerApp {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        boolean useDatabase = false;
        int port = 5099;

        Storage storage = new Storage();
        Auth auth = useDatabase ? new Auth(new DataBaseConnection(), storage) : new Auth(storage);
        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind("auth", auth);
        System.out.println("Server running on port: "+port);
    }
}
