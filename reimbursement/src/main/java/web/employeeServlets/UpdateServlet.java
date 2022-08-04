package web.employeeServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Employee;
import models.ReimbursementRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.Driver;
import service.EmployeeService;
import utils.ConnectionManager;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "UpdateServlet", urlPatterns = "/update")
public class UpdateServlet extends HttpServlet {
    private ConnectionManager connectionManager;
    private EmployeeService employeeService;
    private ObjectMapper om;
    private static final Logger logger = LogManager.getLogger(UpdateServlet.class.getName());

    @Override
    public void init() throws ServletException {
        connectionManager = new ConnectionManager("jdbc:postgresql://revature-java-project.cdat3vncn69a.us-east-1.rds.amazonaws.com:5432/reimbursement_database","jianglin1997", "Jl3467666", new Driver());
        om = new ObjectMapper();
        employeeService = new EmployeeService(connectionManager);
        System.out.println("init success");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("inside update servlet");
        HttpSession userSession = request.getSession();
        Integer currentId = 0;
        currentId = (Integer) userSession.getAttribute("currentId");
        if(userSession.getAttribute("currentId") != null && userSession.getAttribute("user") == "employee") {
            logger.debug("inputStream: " + request.getInputStream());
            Employee employee = om.readValue(request.getInputStream(), Employee.class);
            employeeService.setCurrentUserId(currentId);
            logger.debug("employee = " + employee);
            employeeService.updateInfo(employee.getFirstName(),employee.getLastName(),employee.getPassword(),employee.getAge());
            response.setStatus(200);
        } else{
            logger.debug("should enter here when get attribute is null");
            response.setContentType("text/plain");
            response.getWriter().write("You don't have access to this page. Please login as an employee first");
            response.setStatus(403);
        }

    }
}
