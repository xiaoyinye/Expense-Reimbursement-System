package service;

import dao.EmployeeDAO;
import dao.ManagerDAO;
import dao.RequestDAO;
import models.Employee;
import models.Manager;
import models.ReimbursementRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ConnectionManager;

import java.util.ArrayList;

public class ManagerService {
    private static final Logger logger = LogManager.getLogger(EmployeeService.class.getName());
    int currentUserId = -1;
    EmployeeDAO employeeDAO;
    ManagerDAO managerDAO;
    RequestDAO requestDAO;
    ConnectionManager connectionManager;

    public ManagerService(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.employeeDAO = new EmployeeDAO(connectionManager);
        this.managerDAO = new ManagerDAO(connectionManager);
        this.requestDAO = new RequestDAO(connectionManager);
    }

    public ManagerService(EmployeeDAO employeeDAO, ManagerDAO managerDAO, RequestDAO requestDAO, ConnectionManager connectionManager) {
        this.employeeDAO = employeeDAO;
        this.managerDAO = managerDAO;
        this.requestDAO = requestDAO;
        this.connectionManager = connectionManager;
    }

    public int login(String username, String password) {
        Manager manager = managerDAO.verifyManager(username,password);
        logger.debug("manager is: " + manager);
        if(manager != null) {
            currentUserId = manager.getManagerId();
            logger.debug("currentId is: " + currentUserId);
            return currentUserId;
        } else {
            logger.debug("manager login verification failed");
            return -1;
        }
    }

    public void logout() {
        currentUserId = -1;
    }

    public ArrayList<Employee> viewEmployees() {
        return employeeDAO.getAll();
    }

    public ArrayList<ReimbursementRequest> viewEmployeeRequests(Integer employeeId) {
        return requestDAO.getByEmployeeId(employeeId);
    }

    public ArrayList<ReimbursementRequest> getAllPending() {
        return requestDAO.getAllPending();
    }

    public ArrayList<ReimbursementRequest> getAllResolved() {
        return requestDAO.getAllResolved();
    }

    public boolean resolve(int requestId, String decision) {
        return requestDAO.resolve(requestId,currentUserId,decision);
    }

}
