package web.employeeServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebServlet(name = "SubmitServlet", urlPatterns = "/submit")
public class SubmitServlet extends HttpServlet {
    private ConnectionManager connectionManager;
    private EmployeeService employeeService;
    private ObjectMapper om;
    private static final Logger logger = LogManager.getLogger(SubmitServlet.class.getName());

    @Override
    public void init() throws ServletException {
        connectionManager = new ConnectionManager("jdbc:postgresql://revature-java-project.cdat3vncn69a.us-east-1.rds.amazonaws.com:5432/reimbursement_database","jianglin1997", "Jl3467666", new Driver());
        om = new ObjectMapper();
        employeeService = new EmployeeService(connectionManager);
        System.out.println("init success");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        logger.debug("inside submit servlet");
        HttpSession userSession = request.getSession();
        Integer currentId = 0;
        currentId = (Integer) userSession.getAttribute("currentId");
        if(userSession.getAttribute("currentId") != null && userSession.getAttribute("user") == "employee") {
            logger.debug("inputStream: " + request.getInputStream());
            ReimbursementRequest re = om.readValue(request.getInputStream(), ReimbursementRequest.class);
            re.setEmployeeId(currentId);
            employeeService.submitRequest(re);
            logger.debug("reimbursement = " + re);
            response.setStatus(201);
        } else{
            logger.debug("should enter here when get attribute is null");
            response.setContentType("text/plain");
            response.getWriter().write("You don't have access to this page. Please login as an employee first");
            response.setStatus(403);
        }

    }
}
