import javax.naming.AuthenticationException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Session extends UnicastRemoteObject implements SessionInterface {
    private boolean isAuthenticated;

    protected Session() throws RemoteException {
        super();
        this.isAuthenticated = true;
    }

    @Override
    public PrintService getPrinter() throws RemoteException, AuthenticationException {
        checkAuthentication();
        return new PrintServant(this);
    }

    @Override
    public void logout() throws RemoteException {
        this.isAuthenticated = false;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void checkAuthentication() throws AuthenticationException {
        if (!isAuthenticated()) {
            throw new AuthenticationException("User not authenticated");
        }
    }
}
