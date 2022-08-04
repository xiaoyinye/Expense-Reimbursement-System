package dao;

import models.ReimbursementRequest;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.Driver;
import utils.ConnectionManager;

import java.util.ArrayList;

public class RequestDAOTest {
    RequestDAO requestDAO;
    ConnectionManager connectionManager;
    @Before
    public void initBefore() {
        connectionManager = new ConnectionManager("jdbc:postgresql://revature-java-project.cdat3vncn69a.us-east-1.rds.amazonaws.com:5432/reimbursement_test_database","jianglin1997", "Jl3467666", new Driver());
        requestDAO = new RequestDAO(connectionManager);
    }

    @Test
    public void shouldReturnAllRequests(){
        ArrayList<ReimbursementRequest> result = requestDAO.getAll();

        Assert.assertEquals(1, result.get(0).getRequestId());
        Assert.assertEquals(2, result.get(1).getRequestId());
        Assert.assertEquals(3, result.get(2).getRequestId());
        Assert.assertEquals(4, result.get(3).getRequestId());

        Assert.assertEquals(1000, result.get(0).getAmount() ,0);
        Assert.assertEquals(3000, result.get(1).getAmount(), 0);
        Assert.assertEquals(500, result.get(2).getAmount(), 0);
        Assert.assertEquals(95.8, result.get(3).getAmount(), 0);

        Assert.assertEquals(1, result.get(0).getEmployeeId());
        Assert.assertEquals(3, result.get(1).getEmployeeId());
        Assert.assertEquals(2, result.get(2).getEmployeeId());
        Assert.assertEquals(1, result.get(3).getEmployeeId());

        Assert.assertEquals(0, result.get(0).getManagerId());
        Assert.assertEquals(2, result.get(1).getManagerId());
        Assert.assertEquals(1, result.get(2).getManagerId());
        Assert.assertEquals(2, result.get(3).getManagerId());

        Assert.assertEquals("pending", result.get(0).getStatus());
        Assert.assertEquals("denied", result.get(1).getStatus());
        Assert.assertEquals("approved", result.get(2).getStatus());
        Assert.assertEquals("approved", result.get(3).getStatus());

        Assert.assertEquals("plane tickets", result.get(0).getDescription());
        Assert.assertEquals("hotel", result.get(1).getDescription());
        Assert.assertEquals("online courses", result.get(2).getDescription());
        Assert.assertEquals("taxi", result.get(3).getDescription());
    }

    @Test
    public void shouldReturnPendingRequests() {
        ArrayList<ReimbursementRequest> result = requestDAO.getAllPending();
        Assert.assertEquals(1, result.get(0).getRequestId());
        Assert.assertEquals(1000, result.get(0).getAmount() ,0);
        Assert.assertEquals(1, result.get(0).getEmployeeId());
        Assert.assertEquals(0, result.get(0).getManagerId());
        Assert.assertEquals("pending", result.get(0).getStatus());
        Assert.assertEquals("plane tickets", result.get(0).getDescription());
    }

    @Test
    public void shouldReturnResolvedRequests() {
        ArrayList<ReimbursementRequest> result = requestDAO.getAllResolved();
        Assert.assertEquals(2, result.get(0).getRequestId());
        Assert.assertEquals(3, result.get(1).getRequestId());
        Assert.assertEquals(4, result.get(2).getRequestId());

        Assert.assertEquals(3000, result.get(0).getAmount(), 0);
        Assert.assertEquals(500, result.get(1).getAmount(), 0);
        Assert.assertEquals(95.8, result.get(2).getAmount(), 0);

        Assert.assertEquals(3, result.get(0).getEmployeeId());
        Assert.assertEquals(2, result.get(1).getEmployeeId());
        Assert.assertEquals(1, result.get(2).getEmployeeId());

        Assert.assertEquals(2, result.get(0).getManagerId());
        Assert.assertEquals(1, result.get(1).getManagerId());
        Assert.assertEquals(2, result.get(2).getManagerId());

        Assert.assertEquals("denied", result.get(0).getStatus());
        Assert.assertEquals("approved", result.get(1).getStatus());
        Assert.assertEquals("approved", result.get(2).getStatus());

        Assert.assertEquals("hotel", result.get(0).getDescription());
        Assert.assertEquals("online courses", result.get(1).getDescription());
        Assert.assertEquals("taxi", result.get(2).getDescription());
    }

    @Test
    public void shouldReturnSecondRequest() {
        ReimbursementRequest result = requestDAO.getById(2);
        Assert.assertEquals(2, result.getRequestId());
        Assert.assertEquals(3000, result.getAmount(),0);
        Assert.assertEquals(3, result.getEmployeeId());
        Assert.assertEquals(2, result.getManagerId());
        Assert.assertEquals("denied", result.getStatus());
        Assert.assertEquals("hotel", result.getDescription());
    }

    @Test
    public void shouldReturnRequestsFromFirstEmployee() {
        ArrayList<ReimbursementRequest> result = requestDAO.getByEmployeeId(1);
        Assert.assertEquals(1, result.get(0).getRequestId());
        Assert.assertEquals(1000, result.get(0).getAmount(),0);
        Assert.assertEquals(1, result.get(0).getEmployeeId());
        Assert.assertEquals(0, result.get(0).getManagerId());
        Assert.assertEquals("pending", result.get(0).getStatus());
        Assert.assertEquals("plane tickets", result.get(0).getDescription());

        Assert.assertEquals(4, result.get(1).getRequestId());
        Assert.assertEquals(95.8, result.get(1).getAmount(),0);
        Assert.assertEquals(1, result.get(1).getEmployeeId());
        Assert.assertEquals(2, result.get(1).getManagerId());
        Assert.assertEquals("approved", result.get(1).getStatus());
        Assert.assertEquals("taxi", result.get(1).getDescription());
    }

    @Test
    public void shouldInsertRequest() {
        ReimbursementRequest request = new ReimbursementRequest(5, 100, 2, 0, "pending", "gas fee");
        requestDAO.insert(request);
        ReimbursementRequest result = requestDAO.getById(5);
        Assert.assertEquals(5, result.getRequestId());
        Assert.assertEquals(100, result.getAmount(),0);
        Assert.assertEquals(2, result.getEmployeeId());
        Assert.assertEquals(0, result.getManagerId());
        Assert.assertEquals("pending", result.getStatus());
        Assert.assertEquals("gas fee", result.getDescription());

    }

    @Test
    public void shouldReturnFifthRequestApprovedByFirstManager() {
        requestDAO.resolve(5,1,"approved");
        ReimbursementRequest result = requestDAO.getById(5);
        Assert.assertEquals(5, result.getRequestId());
        Assert.assertEquals(100, result.getAmount(),0);
        Assert.assertEquals(2, result.getEmployeeId());
        Assert.assertEquals(1, result.getManagerId());
        Assert.assertEquals("approved", result.getStatus());
        Assert.assertEquals("gas fee", result.getDescription());
    }
}
