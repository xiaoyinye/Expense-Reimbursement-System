package service;

import dao.EmployeeDAO;
import dao.RequestDAO;
import models.Employee;
import models.ReimbursementRequest;
import org.junit.Assert;
import org.junit.Test;
import utils.ConnectionManager;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmployeeServiceTest {

    ConnectionManager connectionManagerMock = mock(ConnectionManager.class);
    EmployeeDAO employeeDAOMock = mock(EmployeeDAO.class);
    RequestDAO requestDAOMock = mock(RequestDAO.class);
    EmployeeService employeeService = new EmployeeService(connectionManagerMock, employeeDAOMock, requestDAOMock);

    @Test
    public void shouldLoginSuccessfullyAndReturnOne() {
        Employee test = new Employee(1, "A","B","username","password",30);
        when(employeeDAOMock.verifyEmployee("username","password")).thenReturn(test);
        int actual = employeeService.login("username","password");
        Assert.assertEquals(1, employeeService.currentUserId);
        Assert.assertEquals(1, actual);
    }

    @Test
    public void shouldFailedAndReturnNegativeOne() {
        when(employeeDAOMock.verifyEmployee("wrongusername","wrongpassword")).thenReturn(null);
        int actual = employeeService.login("wrongusername","wrongpassword");
        Assert.assertEquals(-1,actual);
    }

    @Test
    public void currentIdShouldBeNagativeOne() {
        employeeService.logout();
        int actual = employeeService.currentUserId;
        Assert.assertEquals(-1,actual);
    }

    @Test
    public void viewInfoTest() {
        employeeService.currentUserId = 1;
        when(employeeDAOMock.getById(1)).thenReturn(new Employee(1, "A","B","username","password",30));
        Employee actual = employeeService.viewInfo();
        Assert.assertEquals(1,actual.getEmployeeId());
        Assert.assertEquals("A",actual.getFirstName());
        Assert.assertEquals("B",actual.getLastName());
        Assert.assertEquals("username",actual.getUsername());
        Assert.assertEquals("password",actual.getPassword());
        Assert.assertEquals(30,actual.getAge());
    }

    @Test
    public void updateInfoTest() {
        employeeService.currentUserId = 1;
        when(employeeDAOMock.updateInfo(1, "A","B","password",19)).thenReturn(true);
        Assert.assertEquals(true, employeeService.updateInfo("A","B","password",19));
    }

    @Test
    public void submitRequestTest() {
        employeeService.currentUserId = 1;
        ReimbursementRequest request = new ReimbursementRequest(3, 95.1,1,0,"pending","food");
        when(requestDAOMock.insert(request)).thenReturn(3);
        Assert.assertEquals(3,employeeService.submitRequest(request));
    }

    @Test
    public void viewPendingRequestsTest() {
        employeeService.currentUserId = 1;
        ArrayList<ReimbursementRequest> requests = new ArrayList<>();
        ReimbursementRequest request1 = new ReimbursementRequest(1, 100,1,0,"pending","ticket");
        ReimbursementRequest request2 = new ReimbursementRequest(2,300,1,1,"approved","software");
        ReimbursementRequest request3 = new ReimbursementRequest(3, 95.1,1,0,"pending","food");
        ReimbursementRequest request4 = new ReimbursementRequest(4, 50,1,1,"denied","cat food");
        requests.add(request1);
        requests.add(request2);
        requests.add(request3);
        requests.add(request4);
        when(requestDAOMock.getByEmployeeId(1)).thenReturn(requests);
        ArrayList<ReimbursementRequest> actual = employeeService.viewPendingRequests();
        Assert.assertEquals(2,actual.size());
        Assert.assertEquals(1,actual.get(0).getRequestId());
        Assert.assertEquals(3,actual.get(1).getRequestId());
    }

    @Test
    public void viewResolvedRequestsTest() {
        employeeService.currentUserId = 1;
        ArrayList<ReimbursementRequest> requests = new ArrayList<>();
        ReimbursementRequest request1 = new ReimbursementRequest(1, 100,1,0,"pending","ticket");
        ReimbursementRequest request2 = new ReimbursementRequest(2,300,1,1,"approved","software");
        ReimbursementRequest request3 = new ReimbursementRequest(3, 95.1,1,0,"pending","food");
        ReimbursementRequest request4 = new ReimbursementRequest(4, 50,1,1,"denied","cat food");
        requests.add(request1);
        requests.add(request2);
        requests.add(request3);
        requests.add(request4);
        when(requestDAOMock.getByEmployeeId(1)).thenReturn(requests);
        ArrayList<ReimbursementRequest> actual = employeeService.viewResolvedRequests();
        Assert.assertEquals(2,actual.size());
        Assert.assertEquals(2,actual.get(0).getRequestId());
        Assert.assertEquals(4,actual.get(1).getRequestId());
    }
}
