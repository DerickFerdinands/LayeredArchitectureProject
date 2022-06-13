package lk.ijse.pos.DTO;

public class CustomerDTO {
    private String Id;
    private String Title;
    private String name;
    private String address;
    private String City;
    private String Province;
    private String PostalCode;

    public CustomerDTO() {
    }

    public CustomerDTO(String id, String title, String name, String address, String city, String province, String postalCode) {
        Id = id;
        Title = title;
        this.name = name;
        this.address = address;
        City = city;
        Province = province;
        PostalCode = postalCode;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "Id='" + Id + '\'' +
                ", Title='" + Title + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", City='" + City + '\'' +
                ", Province='" + Province + '\'' +
                ", PostalCode='" + PostalCode + '\'' +
                '}';
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
