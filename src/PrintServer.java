import javax.naming.AuthenticationException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrintServer extends UnicastRemoteObject implements PrintServerInterface {

    private final SessionInterface session;

    protected PrintServer(SessionInterface session) throws RemoteException {
        super();
        this.session = session;
    }

    @Override
    public String print(String filename, String printer) throws RemoteException, AuthenticationException {
        session.checkAuthentication();
        session.checkPermissions("print");
        String result = "Print " + filename + " on " + printer;
        System.out.println(result);
        return result;
    }

    @Override
    public String queue(String printer) throws RemoteException, AuthenticationException {
        session.checkAuthentication();
        session.checkPermissions("queue");
        String result = "Display printer queue";
        System.out.println(result);
        return result;
    }

    @Override
    public String topQueue(String printer, int job) throws RemoteException, AuthenticationException {
        session.checkAuthentication();
        session.checkPermissions("topQueue");
        String result = "Move " + job + " to top of queue on " + printer;
        System.out.println(result);
        return result;
    }

    @Override
    public String start() throws RemoteException, AuthenticationException {
        session.checkAuthentication();
        session.checkPermissions("start");
        String result = "Start print server";
        System.out.println(result);
        return result;
    }

    @Override
    public String stop() throws RemoteException, AuthenticationException {
        session.checkAuthentication();
        session.checkPermissions("stop");
        String result = "Stop print server";
        System.out.println(result);
        return result;
    }

    @Override
    public String restart() throws RemoteException, AuthenticationException {
        session.checkAuthentication();
        session.checkPermissions("restart");
        String result = "Restart print server";
        System.out.println(result);
        return result;
    }

    @Override
    public String status(String printer) throws RemoteException, AuthenticationException {
        session.checkAuthentication();
        session.checkPermissions("status");
        String result = "Print status of " + printer;
        System.out.println(result);
        return result;
    }

    @Override
    public String readConfig(String parameter) throws RemoteException, AuthenticationException {
        session.checkAuthentication();
        session.checkPermissions("readConfig");
        String result = "Read config";
        System.out.println(result);
        return result;
    }

    @Override
    public String setConfig(String parameter, String value) throws RemoteException, AuthenticationException {
        session.checkAuthentication();
        session.checkPermissions("setConfig");
        String result = "Set config";
        System.out.println(result);
        return result;
    }
}
