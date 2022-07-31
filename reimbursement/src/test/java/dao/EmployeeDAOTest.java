package dao;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import models.Employee;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.Driver;
import utils.ConnectionManager;
import java.util.ArrayList;


public class EmployeeDAOTest
{

    EmployeeDAO employeeDAO;
    ConnectionManager connectionManager;
    @Before
    public void initBefore() {
        connectionManager = new ConnectionManager("jdbc:postgresql://revature-java-project.cdat3vncn69a.us-east-1.rds.amazonaws.com:5432/reimbursement_test_database","jianglin1997", "Jl3467666", new Driver());
        employeeDAO = new EmployeeDAO(connectionManager);
    }
    @Test
    public void shouldReturnAllEmployee() {
        ArrayList<Employee> result = employeeDAO.getAll();
        Assert.assertEquals(1, result.get(0).getEmployeeId());
        Assert.assertEquals(2, result.get(1).getEmployeeId());
        Assert.assertEquals(3, result.get(2).getEmployeeId());

        Assert.assertEquals("James", result.get(0).getFirstName());
        Assert.assertEquals("Michael", result.get(1).getFirstName());
        Assert.assertEquals("Mary", result.get(2).getFirstName());

        Assert.assertEquals("Smith", result.get(0).getLastName());
        Assert.assertEquals("Smith", result.get(1).getLastName());
        Assert.assertEquals("Williams", result.get(2).getLastName());

        Assert.assertEquals("jamessmith", result.get(0).getUsername());
        Assert.assertEquals("michaelsmith", result.get(1).getUsername());
        Assert.assertEquals("marywilliams", result.get(2).getUsername());

        Assert.assertEquals("password", result.get(0).getPassword());
        Assert.assertEquals("password", result.get(1).getPassword());
        Assert.assertEquals("password", result.get(2).getPassword());

        Assert.assertEquals(24, result.get(0).getAge());
        Assert.assertEquals(35, result.get(1).getAge());
        Assert.assertEquals(27, result.get(2).getAge());
    }

    @Test
    public void shouldReturnSecondEmployee() {
        Employee result = employeeDAO.getById(2);
        Assert.assertEquals(2, result.getEmployeeId());
        Assert.assertEquals("Michael", result.getFirstName());
        Assert.assertEquals("Smith", result.getLastName());
        Assert.assertEquals("michaelsmith", result.getUsername());
        Assert.assertEquals("password", result.getPassword());
        Assert.assertEquals(35,result.getAge());
    }

    @Test
    public void shouldUpdateInfo() {
        employeeDAO.updateInfo(3, "Will", "Chen", "123", 18);
        Employee result = employeeDAO.getById(3);
        Assert.assertEquals(3, result.getEmployeeId());
        Assert.assertEquals("Will", result.getFirstName());
        Assert.assertEquals("Chen", result.getLastName());
        Assert.assertEquals("marywilliams", result.getUsername());
        Assert.assertEquals("123", result.getPassword());
        Assert.assertEquals(18,result.getAge());
        employeeDAO.updateInfo(3,"Mary","Williams","password",27);
    }

    @Test
    public void shouldLoginAsThirdEmployee() {
        Employee result = employeeDAO.verifyEmployee("marywilliams","password");
        Assert.assertEquals(3, result.getEmployeeId());
        Assert.assertEquals("Mary", result.getFirstName());
        Assert.assertEquals("Williams", result.getLastName());
        Assert.assertEquals("marywilliams", result.getUsername());
        Assert.assertEquals("password", result.getPassword());
        Assert.assertEquals(27,result.getAge());
    }

    @Test
    public void shouldReturnNullAsLoginFailure() {
        Employee result = employeeDAO.verifyEmployee("wrongusername", "wrongpassword");
        Assert.assertEquals(null,result);
    }
}