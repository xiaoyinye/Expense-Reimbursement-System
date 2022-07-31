package dao;

import models.Employee;
import models.ReimbursementRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;

public class RequestDAO {
    private ConnectionManager connectionManager;
    private static final Logger logger = LogManager.getLogger(RequestDAO.class.getName());

    public RequestDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }


    public ArrayList<ReimbursementRequest> getAll() {
        Connection connection = null;
        ArrayList<ReimbursementRequest> requests = new ArrayList<>();
        try {
            String sql = "select request_id, amount, employee_id , manager_id , status, description from requests";
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            connection.commit();
            while (rs.next()) {
                ReimbursementRequest request = new ReimbursementRequest(
                        rs.getInt("request_id"),
                        rs.getDouble("amount"),
                        rs.getInt("employee_id"),
                        rs.getInt("manager_id"),
                        rs.getString("status"),
                        rs.getString("description")
                );
                requests.add(request);
            }
            return requests;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    logger.error("get all requests failed");
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return requests;
    }

    public ArrayList<ReimbursementRequest> getAllPending() {
        Connection connection = null;
        ArrayList<ReimbursementRequest> requests = new ArrayList<>();
        try {
            String sql = "select request_id, amount, employee_id , manager_id , status, description from requests where status = ?";
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "pending");
            ResultSet rs = ps.executeQuery();
            connection.commit();
            while (rs.next()) {
                ReimbursementRequest request = new ReimbursementRequest(
                        rs.getInt("request_id"),
                        rs.getDouble("amount"),
                        rs.getInt("employee_id"),
                        rs.getInt("manager_id"),
                        rs.getString("status"),
                        rs.getString("description")
                );
                requests.add(request);
            }
            return requests;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    logger.error("get all pending requests failed");
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return requests;
    }

    public ArrayList<ReimbursementRequest> getAllResolved() {
        Connection connection = null;
        ArrayList<ReimbursementRequest> requests = new ArrayList<>();
        try {
            String sql = "select request_id, amount, employee_id , manager_id , status, description from requests where status = ? or status = ?";
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "approved");
            ps.setString(2,"denied");
            ResultSet rs = ps.executeQuery();
            connection.commit();
            while (rs.next()) {
                ReimbursementRequest request = new ReimbursementRequest(
                        rs.getInt("request_id"),
                        rs.getDouble("amount"),
                        rs.getInt("employee_id"),
                        rs.getInt("manager_id"),
                        rs.getString("status"),
                        rs.getString("description")
                );
                requests.add(request);
            }
            return requests;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    logger.error("get all resolved requests failed");
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return requests;
    }

    public ReimbursementRequest getById(Integer id) {
        Connection connection = null;
        ReimbursementRequest request = null;
        try {
            String sql = "select request_id, amount, employee_id , manager_id , status, description from requests where request_id = ?";
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            connection.commit();
            if (rs.next()) {
                request = new ReimbursementRequest(
                        rs.getInt("request_id"),
                        rs.getDouble("amount"),
                        rs.getInt("employee_id"),
                        rs.getInt("manager_id"),
                        rs.getString("status"),
                        rs.getString("description")
                );
                return request;
            }
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    logger.error("get request by id failed");
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public ArrayList<ReimbursementRequest> getByEmployeeId(Integer id) {
        Connection connection = null;
        ArrayList<ReimbursementRequest> requests = new ArrayList<>();
        try {
            String sql = "select request_id, amount, employee_id , manager_id , status, description from requests where employee_id = ?";
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            connection.commit();
            while (rs.next()) {
                ReimbursementRequest request = new ReimbursementRequest(
                        rs.getInt("request_id"),
                        rs.getDouble("amount"),
                        rs.getInt("employee_id"),
                        rs.getInt("manager_id"),
                        rs.getString("status"),
                        rs.getString("description")
                );
                requests.add(request);
            }
            return requests;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    logger.error("get request by employee id failed");
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return requests;
    }

    public Integer insert(ReimbursementRequest request) {
        Connection connection = null;
        int newId = -1;
        try {
            String sql = "insert into requests (amount, employee_id , manager_id , status, description) values (?,?,?,?,?)";
            connection = connectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, request.getAmount());
            ps.setInt(2, request.getEmployeeId());
            ps.setNull(3, Types.NULL);
            ps.setString(4,"pending");
            ps.setString(5,request.getDescription());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if(keys.next()) {
                newId = keys.getInt(1);
                logger.debug("newId is: " + newId);
            }
            return newId;
        } catch (SQLException e) {
            logger.error("insert new request failed");
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return newId;
    }

    public boolean resolve(int requestId, int managerId, String decision) {
        Connection connection = null;
        boolean resolved = false;
        try {
            String sql = "update requests set status = ?, manager_id = ? where request_id = ?";
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, decision);
            ps.setInt(2, managerId);
            ps.setInt(3, requestId);
            ps.executeUpdate();
            connection.commit();
            resolved = true;
        } catch (SQLException e) {
            logger.error("resolved request failed");
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return resolved;
    }
}
