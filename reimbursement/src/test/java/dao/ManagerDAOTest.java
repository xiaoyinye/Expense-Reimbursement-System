package dao;

import models.Employee;
import models.Manager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.Driver;
import utils.ConnectionManager;

public class ManagerDAOTest {
    ManagerDAO managerDAO;
    ConnectionManager connectionManager;
    @Before
    public void initBefore() {
        connectionManager = new ConnectionManager("jdbc:postgresql://revature-java-project.cdat3vncn69a.us-east-1.rds.amazonaws.com:5432/reimbursement_test_database","jianglin1997", "Jl3467666", new Driver());
        managerDAO = new ManagerDAO(connectionManager);
    }

    @Test
    public void shouldReturnSecondManager() {
        Manager result = managerDAO.getById(2);
        Assert.assertEquals(2, result.getManagerId());
        Assert.assertEquals("Emma", result.getFirstName());
        Assert.assertEquals("Lee", result.getLastName());
        Assert.assertEquals("emmalee", result.getUsername());
        Assert.assertEquals("password", result.getPassword());
    }

    @Test
    public void shouldLoginAsThirdManager() {
        Manager result = managerDAO.verifyManager("evelynbrown","password");
        Assert.assertEquals(3, result.getManagerId());
        Assert.assertEquals("Evelyn", result.getFirstName());
        Assert.assertEquals("Brown", result.getLastName());
        Assert.assertEquals("evelynbrown", result.getUsername());
        Assert.assertEquals("password", result.getPassword());
    }

    @Test
    public void shouldReturnNullAsLoginFailure() {
        Manager result = managerDAO.verifyManager("wrongusername", "wrongpassword");
        Assert.assertEquals(null,result);
    }
}
