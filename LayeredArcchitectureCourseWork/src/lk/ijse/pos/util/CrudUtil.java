package lk.ijse.pos.util;

import lk.ijse.pos.db.DBConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrudUtil {

    public static <T> T execute(String sql, Object... params) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);

        for (int i = 0; i < params.length; i++) {
            statement.setObject((i + 1), params[i]);
        }
        return sql.startsWith("SELECT") ? (T) statement.executeQuery() : (T) (Boolean) (statement.executeUpdate() >= 1);
    }
}
