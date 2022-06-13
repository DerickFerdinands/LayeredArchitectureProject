package lk.ijse.pos.entity;

import java.time.LocalDate;

public class Custom {
    private String CustId;
    private String CustTitle;
    private String Custname;
    private String address;
    private String City;
    private String Province;
    private String PostalCode;
    private String ItemCode;
    private String Description;
    private String PackSize;
    private Double UnitPrice;
    private int QtyOnHand;
    private String ImageLocation;
    private String OrderId;
    private int OrderQty;
    private double Discount;
    private LocalDate OrderDate;
    private double Total;

    public Custom(String custId, String custTitle, String custname, String address, String city, String province, String postalCode, String itemCode, String description, String packSize, Double unitPrice, int qtyOnHand, String imageLocation, String orderId, int orderQty, double discount, LocalDate orderDate, double total) {
        CustId = custId;
        CustTitle = custTitle;
        Custname = custname;
        this.address = address;
        City = city;
        Province = province;
        PostalCode = postalCode;
        ItemCode = itemCode;
        Description = description;
        PackSize = packSize;
        UnitPrice = unitPrice;
        QtyOnHand = qtyOnHand;
        ImageLocation = imageLocation;
        OrderId = orderId;
        OrderQty = orderQty;
        Discount = discount;
        OrderDate = orderDate;
        Total = total;
    }
    public Custom(String itemCode, String description, Double unitPrice, int qtyOnHand, String orderId, int orderQty, double discount, double total) {
        ItemCode = itemCode;
        Description = description;
        UnitPrice = unitPrice;
        OrderId = orderId;
        OrderQty = orderQty;
        Discount = discount;
        Total = total;
        QtyOnHand=qtyOnHand;
    }
    @Override
    public String toString() {
        return "Custom{" +
                "CustId='" + CustId + '\'' +
                ", CustTitle='" + CustTitle + '\'' +
                ", Custname='" + Custname + '\'' +
                ", address='" + address + '\'' +
                ", City='" + City + '\'' +
                ", Province='" + Province + '\'' +
                ", PostalCode='" + PostalCode + '\'' +
                ", ItemCode='" + ItemCode + '\'' +
                ", Description='" + Description + '\'' +
                ", PackSize='" + PackSize + '\'' +
                ", UnitPrice=" + UnitPrice +
                ", QtyOnHand=" + QtyOnHand +
                ", ImageLocation='" + ImageLocation + '\'' +
                ", OrderId='" + OrderId + '\'' +
                ", OrderQty=" + OrderQty +
                ", Discount=" + Discount +
                ", OrderDate=" + OrderDate +
                ", Total=" + Total +
                '}';
    }

    public String getCustId() {
        return CustId;
    }

    public void setCustId(String custId) {
        CustId = custId;
    }

    public String getCustTitle() {
        return CustTitle;
    }

    public void setCustTitle(String custTitle) {
        CustTitle = custTitle;
    }

    public String getCustname() {
        return Custname;
    }

    public void setCustname(String custname) {
        Custname = custname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
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

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
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

    public LocalDate getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        OrderDate = orderDate;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }
}
