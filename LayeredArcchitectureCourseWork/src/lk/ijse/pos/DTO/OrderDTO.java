package lk.ijse.pos.DTO;

import java.time.LocalDate;
import java.util.List;

public class OrderDTO {
    private String OrderId;
    private LocalDate OrderDate;
    private String CustId;
    private double Total;
    private List<OrderDetailDTO> List;

    public List<OrderDetailDTO> getList() {
        return List;
    }

    public void setList(List<OrderDetailDTO> obList) {
        this.List = obList;
    }

    public OrderDTO(String orderId, LocalDate orderDate, String custId, double total, List<OrderDetailDTO> obList) {
        OrderId = orderId;
        OrderDate = orderDate;
        CustId = custId;
        Total = total;
        this.List = obList;
    }

    public OrderDTO() {
    }

    public OrderDTO(String orderId, LocalDate orderDate, String custId, double total) {
        OrderId = orderId;
        OrderDate = orderDate;
        CustId = custId;
        Total = total;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
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
