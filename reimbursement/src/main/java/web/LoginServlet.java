package web;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Employee;
import models.Manager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.Driver;
import service.EmployeeService;
import service.ManagerService;
import utils.ConnectionManager;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(LoginServlet.class.getName());
    private ConnectionManager connectionManager;
    private ManagerService managerService;
    private  EmployeeService employeeService;
    private ObjectMapper om;
    @Override
    public void init() throws ServletException {
        connectionManager = new ConnectionManager("jdbc:postgresql://revature-java-project.cdat3vncn69a.us-east-1.rds.amazonaws.com:5432/reimbursement_test_database","jianglin1997", "Jl3467666", new Driver());
        om = new ObjectMapper();
        managerService = new ManagerService(connectionManager);
        employeeService = new EmployeeService(connectionManager);
        System.out.println("init success");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession userSession = request.getSession(true);
        Integer currentId = 0;
        System.out.println(userSession.getId());
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String type = request.getParameter("type");
        logger.debug("username is " + username + " password is " + password);
        if(username == null || password == null || type == null) {
            response.setContentType("text/plain");
            response.getWriter().write("A complete login information was not provided");
            response.setStatus(400);
        }
        if(type.equals("manager")) {
            currentId = managerService.login(username,password);
            userSession.setAttribute("currentId",currentId);
            logger.debug("currentId is: " + currentId);
            if(currentId != 0) {
                response.setContentType("text/plain");
                response.getWriter().write("Login Successfully. Your manager id is: " + currentId);
                response.setStatus(200);
            } else {
                response.setContentType("text/plain");
                response.getWriter().write("incorrect username/password");
                response.setStatus(401);
            }
        }else if(type.equals("employee")) {
            currentId = employeeService.login(username,password);
            userSession.setAttribute("currentId",currentId);
            logger.debug("currentId is: " + currentId);
            if(currentId != 0) {
                response.setContentType("text/plain");
                response.getWriter().write("Login Successfully. Your employee id is: " + currentId);
                response.setStatus(200);
            } else {
                response.setContentType("text/plain");
                response.getWriter().write("incorrect username/password");
                response.setStatus(401);
            }

        }
    }

}
