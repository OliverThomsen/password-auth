import javax.swing.text.IconView;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Storage {
    private HashMap<String, ArrayList<String>> accessControlList;
    private HashMap<String, ArrayList<String>> roles;
    private HashMap<String, ArrayList<String>> users;

    Storage() {
        accessControlList = getHashMapFromFile("accessControlList");
        roles = getHashMapFromFile("roles");
        users = getHashMapFromFile("users");
    }

    public String getRole(String userName) {
        return users.get(userName).get(1);
    }

    public byte[] getPassword(String userName) {
        String hexPass = users.get(userName).get(0);
        return Converter.hexToByteArray(hexPass);
    }

    public ArrayList<String> getOperationsFromACL(String userName) {
        return accessControlList.get(userName);
    }

    public ArrayList<String> getOperationsForRole(String role) {
        return roles.get(role);
    }


    private HashMap<String, ArrayList<String>> getHashMapFromFile(String fileName) {
        HashMap<String, ArrayList<String>> hashMap = new HashMap<>();
        String absolutePath = System.getProperty("user.dir")+"/src/"+fileName+".txt";
        try {
            List<String> allLines = Files.readAllLines(Paths.get(absolutePath));
            for (String line : allLines) {
                String[] array = (line.split(" ", 0));
                ArrayList<String> list = new ArrayList<>(Arrays.asList(array));
                String key = array[0];
                list.remove(0); // remove key from list
                hashMap.put(key, list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashMap;
    }
}
