package lk.ijse.pos.dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO<T, ID>  extends SuperDAO{

    boolean save(T model) throws SQLException, ClassNotFoundException;

    boolean update(T model) throws SQLException, ClassNotFoundException;

    boolean delete(ID id) throws SQLException, ClassNotFoundException;

    ArrayList<T> getAll() throws SQLException, ClassNotFoundException;

    ArrayList<T> getMatchingResults(String search) throws SQLException, ClassNotFoundException;

    T get(ID id) throws SQLException, ClassNotFoundException;
}
