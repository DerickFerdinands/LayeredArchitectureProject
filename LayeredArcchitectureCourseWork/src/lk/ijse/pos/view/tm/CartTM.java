package lk.ijse.pos.view.tm;

import com.jfoenix.controls.JFXButton;

public class CartTM {
    private String ItemCode;
    private String Description;
    private double unitPrice;
    private int QtyOnHand;
    private int Qty;
    private double cost;
    private double discount;
    private JFXButton btn;

    public CartTM() {
    }

    public CartTM(String itemCode) {
        ItemCode = itemCode;
    }

    public CartTM(String itemCode, String description, double unitPrice, int qtyOnHand, int qty, double cost, double discount, JFXButton btn) {
        ItemCode = itemCode;
        Description = description;
        this.unitPrice = unitPrice;
        QtyOnHand = qtyOnHand;
        Qty = qty;
        this.cost = cost;
        this.discount = discount;
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "CartTM{" +
                "ItemCode='" + ItemCode + '\'' +
                ", Description='" + Description + '\'' +
                ", unitPrice=" + unitPrice +
                ", QtyOnHand=" + QtyOnHand +
                ", Qty=" + Qty +
                ", cost=" + cost +
                ", discount=" + discount +
                ", btn=" + btn +
                '}';
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQtyOnHand() {
        return QtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        QtyOnHand = qtyOnHand;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public JFXButton getBtn() {
        return btn;
    }

    public void setBtn(JFXButton btn) {
        this.btn = btn;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CartTM) {
            return ((CartTM) o).getItemCode().equals(this.ItemCode);
        }
        return false;
    }
}
