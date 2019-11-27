import javax.naming.AuthenticationException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrintServerInterface extends Remote {
    /*
     * prints file filename on the specified printer
     */
    public String print(String filename, String printer) throws RemoteException, AuthenticationException;

    /*
     * lists the print queue for a given printer on the user's display in lines of the form <job number>   <file name>
     */
    public String queue(String printer) throws RemoteException, AuthenticationException;

    /*
     * moves job to the top of the queue
     */
    public String topQueue(String printer, int job) throws RemoteException, AuthenticationException;

    /*
     * starts the print server
     */
    public String start() throws RemoteException, AuthenticationException;

    /*
     * stops the print server
     */
    public String stop() throws RemoteException, AuthenticationException;

    /*
     * stops the print server, clears the print queue and starts the print server again
     */
    public String restart() throws RemoteException, AuthenticationException;

    /*
     * prints status of printer on the user's display
     */
    public String status(String printer) throws RemoteException, AuthenticationException;

    /*
     * prints the value of the parameter on the user's display
     */
    public String readConfig(String parameter) throws RemoteException, AuthenticationException;

    /*
     * sets the parameter to value
     */
    public String  setConfig(String parameter, String value) throws RemoteException, AuthenticationException;
}
