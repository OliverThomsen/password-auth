import javax.naming.AuthenticationException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SessionInterface extends Remote {
    PrintService getPrinter() throws RemoteException, AuthenticationException;
    void logout() throws RemoteException;
    boolean isAuthenticated() throws RemoteException;
    void checkAuthentication() throws RemoteException, AuthenticationException;
}
