package lk.ijse.pos.entity;

import java.time.LocalDate;

public class Orders {
    private String OrderId;
    private LocalDate OrderDate;
    private String CustId;
    private double Total;

    public Orders() {
    }

    public Orders(String orderId, LocalDate orderDate, String custId, double total) {
        OrderId = orderId;
        OrderDate = orderDate;
        CustId = custId;
        Total = total;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "OrderId='" + OrderId + '\'' +
                ", OrderDate=" + OrderDate +
                ", CustId='" + CustId + '\'' +
                ", Total=" + Total +
                '}';
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public LocalDate getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        OrderDate = orderDate;
    }

    public String getCustId() {
        return CustId;
    }

    public void setCustId(String custId) {
        CustId = custId;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }
}
