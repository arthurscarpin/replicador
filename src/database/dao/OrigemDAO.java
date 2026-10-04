package database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrigemDAO {

    private Connection conn;

    private PreparedStatement psSelect;

    public OrigemDAO(Connection conn) {
        this.conn = conn;
    }

    public ResultSet selectComandoOrigem(final String tabela, String where) throws SQLException {
        psSelect = conn.prepareStatement("SELECT * FROM " + tabela + " WHERE " + where);
        return psSelect.executeQuery();
    }
}
