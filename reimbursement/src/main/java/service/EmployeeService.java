package service;

import dao.EmployeeDAO;
import dao.RequestDAO;
import models.Employee;
import models.ReimbursementRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.Driver;
import utils.ConnectionManager;

import java.util.ArrayList;

public class EmployeeService {
    private static final Logger logger = LogManager.getLogger(EmployeeService.class.getName());
    int currentUserId = -1;
//    ConnectionManager connectionManager = new ConnectionManager("jdbc:postgresql://revature-java-project.cdat3vncn69a.us-east-1.rds.amazonaws.com:5432/reimbursement_database","jianglin1997", "Jl3467666", new Driver());
//    EmployeeDAO employeeDAO = new EmployeeDAO(connectionManager);
//    RequestDAO requestDAO = new RequestDAO(connectionManager);
        EmployeeDAO employeeDAO;
        RequestDAO requestDAO;
        ConnectionManager connectionManager;

    public EmployeeService(ConnectionManager connectionManager, EmployeeDAO employeeDAO, RequestDAO requestDAO) {
        this.connectionManager = connectionManager;
        this.employeeDAO = employeeDAO;
        this.requestDAO = requestDAO;
    }

    public EmployeeService(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.employeeDAO = new EmployeeDAO(connectionManager);
        this.requestDAO = new RequestDAO(connectionManager);
    }

    public int login(String username, String password) {
        Employee employee = employeeDAO.verifyEmployee(username,password);
        logger.debug("employee is: " + employee);
        if(employee != null) {
            currentUserId = employee.getEmployeeId();
            logger.debug("currentId is: " + currentUserId);
            return currentUserId;
        } else {
            logger.debug("employee login verification failed");
            return -1;
        }
    }

    public void logout() {
        currentUserId = -1;
    }

    public Employee viewInfo() {
        Employee employee = employeeDAO.getById(currentUserId);
        logger.debug("Employee is: " + employee);
        return employee;
    }

    public boolean updateInfo(String firstname, String lastname, String password, Integer age) {
        return employeeDAO.updateInfo(currentUserId, firstname, lastname, password, age);
    }

    public int submitRequest(ReimbursementRequest request) {
        return requestDAO.insert(request);
    }

    public ArrayList<ReimbursementRequest> viewPendingRequests() {
        ArrayList<ReimbursementRequest> requests = requestDAO.getByEmployeeId(currentUserId);
        ArrayList<ReimbursementRequest> target = new ArrayList<>();
        for(ReimbursementRequest request : requests) {
            if(request.getStatus().equals("pending")) {
                target.add(request);
            }
        }
        return target;
    }

    public ArrayList<ReimbursementRequest> viewResolvedRequests() {
        ArrayList<ReimbursementRequest> requests = requestDAO.getByEmployeeId(currentUserId);
        ArrayList<ReimbursementRequest> target = new ArrayList<>();
        for(ReimbursementRequest request : requests) {
            if(request.getStatus().equals("denied") || request.getStatus().equals("approved")) {
                target.add(request);
            }
        }
        return target;
    }
}
