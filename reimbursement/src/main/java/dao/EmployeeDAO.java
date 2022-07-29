package dao;

import models.Employee;
import utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeDAO {
    private ConnectionManager connectionManager;

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
                        rs.getInt("employeeId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
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
            String sql = "select employee_id, first_name, last_name, username, password, age from employees where id=?";

            connection = connectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                employee = new Employee(
                        rs.getInt("employeeId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("age")
                );
            }
            return employee;
        } catch (SQLException e) {
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

    public void update(Integer Id) {
        Connection connection = null;
        Employee employee = null;

    }


}
