package utils;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private String url;
    private String username;
    private String password;

    private Driver driver;
    private boolean driverReady;

    public ConnectionManager(String url, String username, String password, Driver driver) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.driver = driver;

        this.driverReady = false;

    }

    private void registerDriver() throws SQLException {
        if(!driverReady) {
            DriverManager.registerDriver(this.driver);
        }
    }

    public Connection getConnection() throws SQLException{
        if(!driverReady) {
            registerDriver();
        }

        return DriverManager.getConnection(url, username, password);
    }
}
