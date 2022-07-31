package service;

import dao.EmployeeDAO;
import dao.ManagerDAO;
import dao.RequestDAO;
import models.Employee;
import models.Manager;
import models.ReimbursementRequest;
import org.junit.Assert;
import org.junit.Test;
import utils.ConnectionManager;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ManagerServiceTest {
    ConnectionManager connectionManagerMock = mock(ConnectionManager.class);
    EmployeeDAO employeeDAOMock = mock(EmployeeDAO.class);
    ManagerDAO managerDAOMock = mock(ManagerDAO.class);
    RequestDAO requestDAOMock = mock(RequestDAO.class);
    ManagerService managerService = new ManagerService(employeeDAOMock,managerDAOMock,requestDAOMock,connectionManagerMock);

    @Test
    public void shouldLoginSuccessfullyAndReturnOne() {
        Manager test = new Manager(1, "A","B","username","password");
        when(managerDAOMock.verifyManager("username","password")).thenReturn(test);
        int actual = managerService.login("username","password");
        Assert.assertEquals(1, managerService.currentUserId);
        Assert.assertEquals(1, actual);
    }

    @Test
    public void shouldFailedAndReturnNegativeOne() {
        when(managerDAOMock.verifyManager("wrongusername","wrongpassword")).thenReturn(null);
        int actual = managerService.login("wrongusername","wrongpassword");
        Assert.assertEquals(-1,actual);
    }

    @Test
    public void returnAllEmployeesTest() {
        ArrayList<Employee> employees= new ArrayList<>();
        Employee employee1 = new Employee(1, "A","B","username","password",30);
        Employee employee2 = new Employee(2,"C","D","user","pass",45);
        employees.add(employee1);
        employees.add(employee2);
        when(employeeDAOMock.getAll()).thenReturn(employees);
        ArrayList<Employee> actual = managerService.viewEmployees();
        Assert.assertEquals(2,actual.size());
        Assert.assertEquals(1,actual.get(0).getEmployeeId());
        Assert.assertEquals(2,actual.get(1).getEmployeeId());
    }

    @Test
    public void returnAllRequestsByEmployeeOne() {
        ArrayList<ReimbursementRequest> requests = new ArrayList<>();
        ReimbursementRequest request1 = new ReimbursementRequest(1, 100,1,0,"pending","ticket");
        ReimbursementRequest request2 = new ReimbursementRequest(2,300,1,1,"approved","software");
        requests.add(request1);
        requests.add(request2);
        when(requestDAOMock.getByEmployeeId(1)).thenReturn(requests);
        ArrayList<ReimbursementRequest> actual = managerService.viewEmployeeRequests(1);
        Assert.assertEquals(2,actual.size());
        Assert.assertEquals(1,actual.get(0).getEmployeeId());
        Assert.assertEquals(1,actual.get(1).getEmployeeId());
    }

    @Test
    public void returnAllPendingRequests() {
        ArrayList<ReimbursementRequest> requests = new ArrayList<>();
        ReimbursementRequest request1 = new ReimbursementRequest(1, 100,1,0,"pending","ticket");
        ReimbursementRequest request2 = new ReimbursementRequest(2,300,1,1,"pending","software");
        requests.add(request1);
        requests.add(request2);
        when(requestDAOMock.getAllPending()).thenReturn(requests);
        ArrayList<ReimbursementRequest> actual = managerService.getAllPending();
        Assert.assertEquals(2,actual.size());
        Assert.assertEquals("pending",actual.get(0).getStatus());
        Assert.assertEquals("pending",actual.get(1).getStatus());
    }

    @Test
    public void returnAllResolvedRequests() {
        ArrayList<ReimbursementRequest> requests = new ArrayList<>();
        ReimbursementRequest request1 = new ReimbursementRequest(1, 100,1,0,"resolved","ticket");
        ReimbursementRequest request2 = new ReimbursementRequest(2,300,1,1,"resolved","software");
        requests.add(request1);
        requests.add(request2);
        when(requestDAOMock.getAllResolved()).thenReturn(requests);
        ArrayList<ReimbursementRequest> actual = managerService.getAllResolved();
        Assert.assertEquals(2,actual.size());
        Assert.assertEquals("resolved",actual.get(0).getStatus());
        Assert.assertEquals("resolved",actual.get(1).getStatus());
    }

    @Test
    public void shouldReturnTrue() {
        managerService.currentUserId = 1;
        when(requestDAOMock.resolve(1,1,"approved")).thenReturn(true);
        Assert.assertEquals(true,managerService.resolve(1,"approved"));
    }
}
