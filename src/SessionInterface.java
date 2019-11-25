import javax.naming.AuthenticationException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SessionInterface extends Remote {
    PrintServerInterface getPrinter() throws RemoteException, AuthenticationException;
    void logout() throws RemoteException;
    void checkAuthentication() throws RemoteException, AuthenticationException;
    void checkPermissions(String operation) throws RemoteException, AuthenticationException;
}
