package models;

public class ReimbursementRequest {
    int requestId;
    double amount;
    int employeeId;
    int managerId;
    String status;
    String description;

    public ReimbursementRequest(int requestId, double amount, int employeeId, int managerId, String status, String description) {
        this.requestId = requestId;
        this.amount = amount;
        this.employeeId = employeeId;
        this.managerId = managerId;
        this.status = status;
        this.description = description;
    }

    public ReimbursementRequest() {
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ReimbursementRequest{" +
                "requestId=" + requestId +
                ", amount=" + amount +
                ", employeeId=" + employeeId +
                ", managerId=" + managerId +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
