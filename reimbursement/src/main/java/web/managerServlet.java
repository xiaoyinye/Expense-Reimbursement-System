package web;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebServlet(name = "managerServlet", urlPatterns = "/manager/*")
public class managerServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(managerServlet.class.getName());
    private ConnectionManager connectionManager;
    private ManagerService managerService;
    private ObjectMapper om;

    @Override
    public void init() throws ServletException {
        connectionManager = new ConnectionManager("jdbc:postgresql://revature-java-project.cdat3vncn69a.us-east-1.rds.amazonaws.com:5432/reimbursement_test_database","jianglin1997", "Jl3467666", new Driver());
        om = new ObjectMapper();
        managerService = new ManagerService(connectionManager);
        System.out.println("init success");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession userSession = request.getSession(true);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
