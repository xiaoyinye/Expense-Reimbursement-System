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
    int currentUserId = 0;
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

    /**
     * employee log in
     * @param username
     * @param password
     * @return 0 if login failed and employee's id if login successfully
     */
    public int login(String username, String password) {
        Employee employee = employeeDAO.verifyEmployee(username,password);
        logger.debug("employee is: " + employee);
        if(employee != null) {
            currentUserId = employee.getEmployeeId();
            logger.debug("currentId is: " + currentUserId);
        } else {
            currentUserId = 0;
            logger.debug("employee login verification failed");
        }
        return currentUserId;
    }


    /**
     * view employee's information
     * @return employee
     */
    public Employee viewInfo() {
        Employee employee = employeeDAO.getById(currentUserId);
        logger.debug("Employee is: " + employee);
        return employee;
    }

    /**
     * update employee's information
     * @param firstname
     * @param lastname
     * @param password
     * @param age
     * @return true if updated successfully and false if updated unsuccessfully
     */
    public boolean updateInfo(String firstname, String lastname, String password, Integer age) {
        return employeeDAO.updateInfo(currentUserId, firstname, lastname, password, age);
    }

    /**
     * submit a new request
     * @param request
     * @return the id of the newly inserted request
     */
    public int submitRequest(ReimbursementRequest request) {
        return requestDAO.insert(request);
    }

    /**
     * view all pending requests
     * @return all pending requests
     */
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

    /**
     * view all resolved requests
     * @return all resolved requests
     */
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

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }
}
