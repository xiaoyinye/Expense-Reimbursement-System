package web.managerServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.Driver;
import service.ManagerService;
import utils.ConnectionManager;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ResolveServlet", urlPatterns = "/resolve")
public class ResolveServlet extends HttpServlet {
    private ConnectionManager connectionManager;
    private ManagerService managerService;
    private ObjectMapper om;
    private static final Logger logger = LogManager.getLogger(ResolveServlet.class.getName());
    @Override
    public void init() throws ServletException {
        connectionManager = new ConnectionManager("jdbc:postgresql://revature-java-project.cdat3vncn69a.us-east-1.rds.amazonaws.com:5432/reimbursement_database","jianglin1997", "Jl3467666", new Driver());
        om = new ObjectMapper();
        managerService = new ManagerService(connectionManager);
       logger.debug("init success");
    }


    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession userSession = request.getSession(true);
        Integer currentId = 0;
        String user = null;
        String decision = request.getParameter("decision");
        String id = request.getParameter("id");
        currentId = (Integer) userSession.getAttribute("currentId");
        if(userSession.getAttribute("currentId") != null && userSession.getAttribute("user") == "manager") {
            if (decision == null || id == null) {
                response.setContentType("text/plain");
                response.getWriter().write("A complete resolve information was not provided");
                response.setStatus(400);
            } else {
                try {
                    managerService.setCurrentUserId(currentId);
                    managerService.resolve(Integer.parseInt(id), decision);
                    response.setStatus(200);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    response.setContentType("text/plain");
                    response.getWriter().write("A complete resolve information was not provided");
                    response.setStatus(400);
                }
            }
        }
    }
}
