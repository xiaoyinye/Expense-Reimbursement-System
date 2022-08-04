package dao;

import models.Employee;
import models.Manager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerDAO {
    private ConnectionManager connectionManager;
    private static final Logger logger = LogManager.getLogger(ManagerDAO.class.getName());

    public ManagerDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * find a specific manager by his/her id
     * @param id
     * @return manager who has the exact id
     */
    public Manager getById(Integer id) {
        Connection connection = null;
        Manager manager = null;
        try {
            String sql = "select manager_id, first_name, last_name, username, password from managers where manager_id=?";
            connection = connectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                manager = new Manager(
                        rs.getInt("manager_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                logger.debug("return manager: " + manager);
            }
            return manager;
        } catch (SQLException e) {
            logger.error("get manager by id failed");
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return manager;
    }

    /**
     * verify if a manager with the provided username and password is in database
     * @param username
     * @param password
     * @return the corresponding manager
     */
    public Manager verifyManager(String username, String password) {
        Connection connection = null;
        Manager manager = null;
        try{
            String sql = "select manager_id, first_name, last_name, username, password from managers where username = ? and password = ?";
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            connection.commit();
            if(rs.next()) {
                manager = new Manager(
                        rs.getInt("manager_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
            return manager;
        }catch (SQLException e) {
            logger.error("verify manager failed");
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
