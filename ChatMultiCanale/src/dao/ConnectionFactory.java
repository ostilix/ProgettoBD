package dao;

import model.Ruolo;
import java.io.*;
import java.sql.*;
import java.util.Properties;

public class ConnectionFactory {
    private static Connection connection;

    private ConnectionFactory(){}

    static {
        try (InputStream input = new FileInputStream("resources/db.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            String connection_url = properties.getProperty("CONNECTION_URL");
            String user = properties.getProperty("LOGIN_USER");
            String pass = properties.getProperty("LOGIN_PASS");

            connection = DriverManager.getConnection(connection_url, user, pass);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return connection;
    }

    public static void changeRole(Ruolo ruolo) throws SQLException {
        connection.close();

        try (InputStream input = new FileInputStream("resources/db.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            String connection_url = properties.getProperty("CONNECTION_URL");
            String user = properties.getProperty(ruolo.name() + "_USER");
            String pass = properties.getProperty(ruolo.name() + "_PASS");

            connection = DriverManager.getConnection(connection_url, user, pass);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
