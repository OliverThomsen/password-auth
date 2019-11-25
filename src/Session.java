import javax.naming.AuthenticationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Session extends UnicastRemoteObject implements SessionInterface {
    private boolean isAuthenticated;
    private boolean useACL = true;
    private String userName;
    private HashMap<String, List<String>> ACL;

    protected Session(String username) throws RemoteException {
        super();
        this.isAuthenticated = true;
        this.userName = username;
    }

    @Override
    public PrintServerInterface getPrinter() throws RemoteException, AuthenticationException {
        checkAuthentication();
        return new PrintServer(this);
    }

    @Override
    public void logout() throws RemoteException {
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
        HashMap<String, List<String>> acl = getACL();
        if (! acl.get(userName).contains(operation)) {
            throw new AuthenticationException(userName + " does not have permission to perform \"" + operation + "\" operation");
        };
    }

    private void isAuthorisedWithRole(String operation) {
    }

    private HashMap<String, List<String>> getACL() {
        if (ACL == null) {
            ACL = new HashMap<String, List<String>>();
            String directory = System.getProperty("user.dir");
            String fileName = "/src/accessControlList.txt";
            String absolutePath = directory + fileName;
            try {
                List<String> allLines = Files.readAllLines(Paths.get(absolutePath));
                for (String line : allLines) {
                    String[] array = (line.split(", ", 0));
                    ArrayList<String> list = new ArrayList<String>(Arrays.asList(array));
                    list.remove(0);
                    ACL.put(array[0], list);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("ERROR: User " + userName + " not in database");
            }
        }

        System.out.println(ACL);

        return ACL;
    }

}
