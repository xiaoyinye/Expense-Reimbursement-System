package web;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession userSession = request.getSession(true);
        userSession.invalidate();
        response.setContentType("text/plain");
        response.getWriter().write("Logout successfully");
        response.setStatus(200);
    }
}
