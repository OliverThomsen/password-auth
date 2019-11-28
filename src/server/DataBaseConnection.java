package server;

import java.sql.*;

public class DataBaseConnection {
    private String driver = "org.postgresql.Driver";
    private String url = "jdbc:postgresql://localhost:5432/printer_auth";
    private String username = "postgres";
    private String password = "";
    private Connection connection;

    public DataBaseConnection() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found exception");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registerUser(String userName, byte[] password) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO auth (user_name, password) VALUES (?, ?)");
            statement.setString(1, userName);
            statement.setBytes(2, password);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public byte[] getPassword(String userName) {
        byte[] password = null;
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT password FROM auth WHERE user_name=?");
            stmt.setString(1, userName);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                password = resultSet.getBytes("password");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return password;
    }
}
