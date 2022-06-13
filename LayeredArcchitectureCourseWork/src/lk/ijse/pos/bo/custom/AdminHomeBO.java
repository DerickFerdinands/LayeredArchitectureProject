package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBO;

import java.sql.SQLException;

public interface AdminHomeBO extends SuperBO {
    double getTotalOrderRevenue() throws SQLException, ClassNotFoundException;

    int getCustomerCount() throws SQLException, ClassNotFoundException;

    int getItemCount() throws SQLException, ClassNotFoundException;

}
