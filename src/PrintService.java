import java.rmi.Remote;

public interface PrintService extends Remote {
    /*
     * prints file filename on the specified printer
     */
    public boolean print(String filename, String printer);

    /*
     * lists the print queue for a given printer on the user's display in lines of the form <job number>   <file name>
     */
    public String queue(String printer);

    /*
     * moves job to the top of the queue
     */
    public boolean topQueue(String printer, int job);

    /*
     * starts the print server
     */
    public boolean start();

    /*
     * stops the print server
     */
    public boolean stop();

    /*
     * stops the print server, clears the print queue and starts the print server again
     */
    public boolean restart();

    /*
     * prints status of printer on the user's display
     */
    public String status(String printer);

    /*
     * prints the value of the parameter on the user's display
     */
    public String readConfig(String parameter);

    /*
     * sets the parameter to value
     */
    public boolean  setConfig(String parameter, String value);
}
