package web;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ContextListener implements ServletContextListener {
    @Resource(name="jdbc/reimbursementDB")
    private DataSource dataSource;
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Context Initialize");
        System.out.println("Check db Connection");

//        try {
//            Connection c = dataSource.getConnection();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        ProduceRepo produceRepo = new ProduceRepo(dataSource);
//
//        servletContextEvent.getServletContext().setAttribute("produceRepo", produceRepo);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Context Destroyed");
    }
}
