import javax.naming.AuthenticationException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrintServant extends UnicastRemoteObject implements PrintService {

    private final SessionInterface session;

    protected PrintServant(SessionInterface session) throws RemoteException {
        super();
        this.session = session;
    }

    @Override
    public boolean print(String filename, String printer) throws RemoteException, AuthenticationException {
        session.checkAuthentication();
        System.out.println("Print " + filename + " on " + printer);
        return true;
    }

    @Override
    public String queue(String printer) throws RemoteException, AuthenticationException {
        session.checkAuthentication();
        System.out.println("Display printer queue");
        return "001 fileOne, 002 fileTwo, 003 fileThree";
    }

    @Override
    public boolean topQueue(String printer, int job) throws RemoteException, AuthenticationException {
        session.checkAuthentication();
        System.out.println("Move " + job + " to top of que on " + printer);
        return true;
    }

    @Override
    public boolean start() throws RemoteException, AuthenticationException {
        session.checkAuthentication();
        System.out.println("Start print server");
        return true;
    }

    @Override
    public boolean stop() throws RemoteException, AuthenticationException {
        session.checkAuthentication();
        System.out.println("Stop print server");
        return true;
    }

    @Override
    public boolean restart() throws RemoteException, AuthenticationException {
        session.checkAuthentication();
        System.out.println("Restart print server");
        return true;
    }

    @Override
    public String status(String printer) throws RemoteException, AuthenticationException {
        session.checkAuthentication();
        System.out.println("Print status of " + printer);
        return "This is the status of the printer: " + printer;
    }

    @Override
    public String readConfig(String parameter) throws RemoteException, AuthenticationException {
        session.checkAuthentication();
        System.out.println("Read config");
        return "This is the value of the parameter: " + parameter;
    }

    @Override
    public boolean setConfig(String parameter, String value) throws RemoteException, AuthenticationException {
        session.checkAuthentication();
        System.out.println("Set config");
        return true;
    }
}
