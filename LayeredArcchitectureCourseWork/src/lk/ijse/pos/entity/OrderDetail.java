package lk.ijse.pos.entity;

public class OrderDetail {
    private String OrderId;
    private String ItemCode;
    private int OrderQty;
    private double Discount;

    public OrderDetail() {
    }

    public OrderDetail(String orderId, String itemCode, int orderQty, double discount) {
        OrderId = orderId;
        ItemCode = itemCode;
        OrderQty = orderQty;
        Discount = discount;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "OrderId='" + OrderId + '\'' +
                ", ItemCode='" + ItemCode + '\'' +
                ", OrderQty=" + OrderQty +
                ", Discount=" + Discount +
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

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }
}
