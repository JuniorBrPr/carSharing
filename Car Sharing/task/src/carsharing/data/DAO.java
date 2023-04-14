package carsharing.data;

import java.sql.Connection;
import java.util.ArrayList;

public abstract class DAO<T> {
    final Connection connection;

    public DAO(Database database) {
        this.connection = database.getConnection();
    }

    public abstract void createTable();

    public abstract void insert(T t);

    public abstract ArrayList<T> getAll();
}
