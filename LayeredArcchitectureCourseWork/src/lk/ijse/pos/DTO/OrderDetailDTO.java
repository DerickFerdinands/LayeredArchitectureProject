package lk.ijse.pos.DTO;

public class OrderDetailDTO {
    private String OrderId;
    private String ItemCode;
    private int OrderQty;
    private double Discunt;

    @Override
    public String toString() {
        return "OrderDetailDTO{" +
                "OrderId='" + OrderId + '\'' +
                ", ItemCode='" + ItemCode + '\'' +
                ", OrderQty=" + OrderQty +
                ", Discunt=" + Discunt +
                '}';
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public int getOrderQty() {
        return OrderQty;
    }

    public void setOrderQty(int orderQty) {
        OrderQty = orderQty;
    }

    public double getDiscunt() {
        return Discunt;
    }

    public void setDiscunt(double discunt) {
        Discunt = discunt;
    }

    public OrderDetailDTO() {
    }

    public OrderDetailDTO(String orderId, String itemCode, int orderQty, double discunt) {
        OrderId = orderId;
        ItemCode = itemCode;
        OrderQty = orderQty;
        Discunt = discunt;
    }
}
