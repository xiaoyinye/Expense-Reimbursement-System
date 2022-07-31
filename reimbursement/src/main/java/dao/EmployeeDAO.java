package dao;

import models.Employee;
import utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class EmployeeDAO {
    private ConnectionManager connectionManager;
    private static final Logger logger = LogManager.getLogger(EmployeeDAO.class.getName());

    public EmployeeDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public ArrayList<Employee> getAll() {
        Connection connection = null;
        ArrayList<Employee> employees = new ArrayList<>();
        try {
            String sql = "select employee_id, first_name, last_name, username, password, age from employees";
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            connection.commit();
            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("age")
                );
                employees.add(employee);
            }
            return employees;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    logger.error("get all employees failed");
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return employees;
    }


    public Employee getById(Integer id) {
        Connection connection = null;
        Employee employee = null;
        try {
            String sql = "select employee_id, first_name, last_name, username, password, age from employees where employee_id=?";
            connection = connectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                employee = new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("age")
                );
                logger.debug("return employee: " + employee);
            }
            return employee;
        } catch (SQLException e) {
            logger.error("get employee by id failed");
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
        return employee;
    }

    public boolean updateInfo(Integer id, String firstname, String lastname, String password, Integer age) {
        Connection connection = null;
        try {
            String sql = "update employees set first_name = ?, last_name = ?, password = ?, age = ? where employee_id = ?";
            connection = connectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, firstname);
            ps.setString(2, lastname);
            ps.setString(3, password);
            ps.setInt(4,age);
            ps.setInt(5,id);
            int rowsAffected = ps.executeUpdate();
            logger.debug(rowsAffected + "rows updated");
            return rowsAffected > 0;

        }catch (SQLException e) {
            logger.error("employee info not updated correctly");
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
        return false;

    }

    public Employee verifyEmployee(String username, String password) {
        Connection connection = null;
        Employee employee = null;
        try{
            String sql = "select employee_id, first_name, last_name, username, password, age from employees where username = ? and password = ?";
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            connection.commit();
            if(rs.next()) {
                employee = new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("age")
                );
            }
            return employee;
        }catch (SQLException e) {
            logger.error("verify employee failed");
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
