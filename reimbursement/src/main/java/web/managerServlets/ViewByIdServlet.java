package web.managerServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.ReimbursementRequest;
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

@WebServlet(name = "ViewByIdServlet", value = "/viewById/*")
public class ViewByIdServlet extends HttpServlet {
    private ConnectionManager connectionManager;
    private ManagerService managerService;
    private ObjectMapper om;
    private static final Logger logger = LogManager.getLogger(ViewByIdServlet.class.getName());
    @Override
    public void init() throws ServletException {
        connectionManager = new ConnectionManager("jdbc:postgresql://revature-java-project.cdat3vncn69a.us-east-1.rds.amazonaws.com:5432/reimbursement_database","jianglin1997", "Jl3467666", new Driver());
        om = new ObjectMapper();
        managerService = new ManagerService(connectionManager);
        System.out.println("init success");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("inside ViewResolveds doGet method");
        HttpSession userSession = request.getSession();
        if(userSession.getAttribute("currentId") != null && userSession.getAttribute("user") == "manager") {
            int id = 0;
            try {
                id = Integer.parseInt(request.getRequestURI().split("/")[2]);
            } catch (Exception ex) {
                ex.printStackTrace();
                id = 0;
            }

            if (id == 0) {
                response.setContentType("text/plain");
                response.getWriter().write("Please provide the id of the employee you want to view");
                response.setStatus(400);
            } else {
                ArrayList<ReimbursementRequest> requests = managerService.viewEmployeeRequests(id);
                if (requests.size() == 0) {
                    response.setStatus(404);
                } else {
                    response.setStatus(200);
                    response.getWriter().write(om.writeValueAsString(requests));
                }

            }
        } else{
            logger.debug("should enter here when get attribute is null");
            response.setContentType("text/plain");
            response.getWriter().write("You don't have access to this page. Please login as a manager first");
            response.setStatus(403);
        }

    }

}
