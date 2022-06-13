package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBO;

import java.sql.SQLException;
import java.util.LinkedHashMap;

public interface ItemMovementBO extends SuperBO {
    LinkedHashMap<String, Integer> getTotalItemOrderQty() throws SQLException, ClassNotFoundException;

    LinkedHashMap<String, Integer> getMostAndLeastMovedItem() throws SQLException, ClassNotFoundException;
}
