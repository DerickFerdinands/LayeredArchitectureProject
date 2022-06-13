package lk.ijse.pos.view.tm;

import com.jfoenix.controls.JFXButton;

public class ItemTM {
    private String ItemCode;
    private String Description;
    private String PackSize;
    private Double UnitPrice;
    private int QtyOnHand;
    private String ImageLocation;
    private JFXButton btn;

    public ItemTM() {
    }

    public ItemTM(String itemCode, String description, String packSize, Double unitPrice, int qtyOnHand, String imageLocation, JFXButton btn) {
        ItemCode = itemCode;
        Description = description;
        PackSize = packSize;
        UnitPrice = unitPrice;
        QtyOnHand = qtyOnHand;
        ImageLocation = imageLocation;
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "ItemTM{" +
                "ItemCode='" + ItemCode + '\'' +
                ", Description='" + Description + '\'' +
                ", PackSize='" + PackSize + '\'' +
                ", UnitPrice=" + UnitPrice +
                ", QtyOnHand=" + QtyOnHand +
                ", ImageLocation='" + ImageLocation + '\'' +
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

    public String getPackSize() {
        return PackSize;
    }

    public void setPackSize(String packSize) {
        PackSize = packSize;
    }

    public Double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        UnitPrice = unitPrice;
    }

    public int getQtyOnHand() {
        return QtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        QtyOnHand = qtyOnHand;
    }

    public String getImageLocation() {
        return ImageLocation;
    }

    public void setImageLocation(String imageLocation) {
        ImageLocation = imageLocation;
    }

    public JFXButton getBtn() {
        return btn;
    }

    public void setBtn(JFXButton btn) {
        this.btn = btn;
    }
}
