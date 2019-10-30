public class PrintServant implements PrintService {
    @Override
    public boolean print(String filename, String printer) {
        return true;
    }

    @Override
    public String queue(String printer) {
        return "001 fileOne, 002 fileTwo, 003 fileThree";
    }

    @Override
    public boolean topQueue(String printer, int job) {
        return true;
    }

    @Override
    public boolean start() {
        return true;
    }

    @Override
    public boolean stop() {
        return true;
    }

    @Override
    public boolean restart() {
        return true;
    }

    @Override
    public String status(String printer) {
        return "This is the status of the printer: " + printer;
    }

    @Override
    public String readConfig(String parameter) {
        return "This is the value of the parameter: " + parameter;
    }

    @Override
    public boolean setConfig(String parameter, String value) {
        return true;
    }
}
