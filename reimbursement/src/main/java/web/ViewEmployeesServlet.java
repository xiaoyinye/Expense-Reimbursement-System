package web;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.Driver;
import service.ManagerService;
import utils.ConnectionManager;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "viewEmployeesServlet", urlPatterns = "/viewEmployees")
public class ViewEmployeesServlet extends HttpServlet {
    private ConnectionManager connectionManager;
    private ManagerService managerService;
    private ObjectMapper om;
    private static final Logger logger = LogManager.getLogger(LoginServlet.class.getName());
    @Override
    public void init() throws ServletException {
        connectionManager = new ConnectionManager("jdbc:postgresql://revature-java-project.cdat3vncn69a.us-east-1.rds.amazonaws.com:5432/reimbursement_test_database","jianglin1997", "Jl3467666", new Driver());
        om = new ObjectMapper();
        managerService = new ManagerService(connectionManager);
        System.out.println("init success");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("inside viewEmployeesServlet do get method");
        HttpSession userSession = request.getSession();
        Integer currentId = 0;
        logger.debug("currentId is " + currentId);
        logger.debug("getAttribute: " + userSession.getAttribute("currentId"));
        if(userSession.getAttribute("currentId") != null) {
            currentId = (Integer) userSession.getAttribute("currentId");
            ArrayList<Employee> employees = new ArrayList<>();
            employees = managerService.viewEmployees();
            System.out.println("size of employee " + employees.size());
            response.setContentType("application/json");
            response.getWriter().write(om.writeValueAsString(employees));
            System.out.println("get into do get method");
        } else{
            logger.debug("should enter here when get attribute is null");
            response.getWriter().write("You don't have access to this page. Please login first");
            response.setStatus(403);
        }
    }

}
