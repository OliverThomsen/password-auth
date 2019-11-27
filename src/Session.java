import javax.naming.AuthenticationException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Session extends UnicastRemoteObject implements SessionInterface {
    private boolean isAuthenticated;
    private boolean useACL = false;
    private String userName;
    private HashMap<String, List<String>> ACL;
    private Storage storage;

    protected Session(String username, Storage storage) throws RemoteException {
        super();
        this.isAuthenticated = true;
        this.userName = username;
        this.storage = storage;
    }

    @Override
    public PrintServerInterface getPrinter() throws RemoteException, AuthenticationException {
        System.out.println("Returning new printer instance to "+userName);
        checkAuthentication();
        return new PrintServer(this);
    }

    @Override
    public void logout() throws RemoteException {
        System.out.println("Log out "+userName);
        this.isAuthenticated = false;
        this.userName = null;
    }

    public void checkAuthentication() throws AuthenticationException {
        if (!isAuthenticated) {
            throw new AuthenticationException("User is not authenticated");
        }
    }

    public void checkPermissions(String operation) throws AuthenticationException {
        if (useACL) {
            isAuthorisedWithACL(operation);
        } else {
            isAuthorisedWithRole(operation);
        }
    }

    private void isAuthorisedWithACL(String operation) throws AuthenticationException {
        ArrayList<String> operations = storage.getOperationsFromACL(userName);
        if (! operations.contains(operation)) {
            throw new AuthenticationException(userName + " does not have permission to perform \"" + operation + "\" operation");
        };
    }

    private void isAuthorisedWithRole(String operation) throws AuthenticationException {
        String userRole = storage.getRole(userName);
        ArrayList<String> operations = storage.getOperationsForRole(userRole);
        if (! operations.contains(operation)) {
            throw new AuthenticationException(userName + " does not have permission to perform \"" + operation + "\" operation");
        };
    }
}
