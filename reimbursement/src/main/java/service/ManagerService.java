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
    private static final Logger logger = LogManager.getLogger(ManagerService.class.getName());
    int currentUserId = 0;
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

    /**
     * manager login
     * @param username
     * @param password
     * @return 0 if login failed and manager's id if login successfully
     */
    public int login(String username, String password) {
        Manager manager = managerDAO.verifyManager(username,password);
        logger.debug("manager is: " + manager);
        if(manager != null) {
            currentUserId = manager.getManagerId();
            logger.debug("currentId is: " + currentUserId);
        } else {
            currentUserId = 0;
            logger.debug("manager login verification failed");
            logger.debug("failed currentId is " + currentUserId);
        }
        return currentUserId;
    }

    /**
     * get all employees
     * @return all employees
     */
    public ArrayList<Employee> viewEmployees() {
        return employeeDAO.getAll();
    }

    /**
     * get requests of a specified employee
     * @param employeeId
     * @return all requests of a specified employee
     */
    public ArrayList<ReimbursementRequest> viewEmployeeRequests(Integer employeeId) {
        return requestDAO.getByEmployeeId(employeeId);
    }

    /**
     * get all pending requests
     * @return all pending requests
     */
    public ArrayList<ReimbursementRequest> getAllPending() {
        return requestDAO.getAllPending();
    }

    /**
     * get all resolved requests
     * @return all resolved requests
     */
    public ArrayList<ReimbursementRequest> getAllResolved() {
        return requestDAO.getAllResolved();
    }

    /**
     * approve or deny a specific request
     * @param requestId
     * @param decision
     * @return true if resolve successfully and false if resolve unsuccessfully
     */
    public boolean resolve(int requestId, String decision) {
        return requestDAO.resolve(requestId,currentUserId,decision);
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }
}
