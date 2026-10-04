package database.dao;

import database.model.TB_REPLICACAO_DIRECAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DirecaoDAO {

    private Connection conn;

    private static final String SQL_SELECT_ALL = "SELECT * FROM tb_replicacao_direcao";

    private static final String SQL_SELECT_BY_ID = "SELECT * FROM tb_replicacao_direcao WHERE id = ?";

    private static final String SQL_INSERT = """
        INSERT INTO tb_replicacao_direcao (
            direcao_origem,
            direcao_destino,
            usuario_origem,
            usuario_destino,
            senha_origem,
            senha_destino,
            habilitado,
            processo_id
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

    private static final String SQL_UPDATE = """
        UPDATE tb_replicacao_direcao 
        SET direcao_origem = ?, 
            direcao_destino = ?, 
            usuario_origem = ?, 
            usuario_destino = ?, 
            senha_origem = ?, 
            senha_destino = ?, 
            habilitado = ?, 
            processo_id = ? 
        WHERE id = ?
        """;

    private static final String SQL_DELETE = "DELETE FROM tb_replicacao_direcao WHERE id = ?";

    private PreparedStatement pstSelectAll;
    private PreparedStatement pstSelectById;
    private PreparedStatement pstInsert;
    private PreparedStatement pstUpdate;
    private PreparedStatement pstDelete;

    public DirecaoDAO(Connection conn) throws Exception {
        this.conn = conn;
        this.pstSelectAll = conn.prepareStatement(SQL_SELECT_ALL);
        this.pstSelectById = conn.prepareStatement(SQL_SELECT_BY_ID);
        this.pstInsert = conn.prepareStatement(SQL_INSERT);
        this.pstUpdate = conn.prepareStatement(SQL_UPDATE);
        this.pstDelete = conn.prepareStatement(SQL_DELETE);
    }


    public ArrayList<TB_REPLICACAO_DIRECAO> selectAll() throws SQLException {
        ArrayList<TB_REPLICACAO_DIRECAO> lista = new ArrayList<>();
        try (ResultSet rs = pstSelectAll.executeQuery()) {
            while (rs.next()) {
                lista.add(map(rs));
            }
        }
        return lista;
    }

    public TB_REPLICACAO_DIRECAO selectById(int id) throws SQLException {
        pstSelectById.setInt(1, id);
        try (ResultSet rs = pstSelectById.executeQuery()) {
            return rs.next() ? map(rs) : null;
        }
    }

    public void insert(TB_REPLICACAO_DIRECAO tb) throws SQLException {
        pstInsert.setString(1, tb.getDirecao_origem());
        pstInsert.setString(2, tb.getDirecao_destino());
        pstInsert.setString(3, tb.getUsuario_origem());
        pstInsert.setString(4, tb.getUsuario_destino());
        pstInsert.setString(5, tb.getSenha_origem());
        pstInsert.setString(6, tb.getSenha_destino());
        pstInsert.setBoolean(7, tb.isHabilitado());
        pstInsert.setLong(8, tb.getProcesso_id());
        pstInsert.executeUpdate();
    }

    public void update(TB_REPLICACAO_DIRECAO tb) throws SQLException {
        pstUpdate.setString(1, tb.getDirecao_destino());
        pstUpdate.setString(2, tb.getDirecao_origem());
        pstUpdate.setString(3, tb.getUsuario_origem());
        pstUpdate.setString(4, tb.getUsuario_destino());
        pstUpdate.setString(5, tb.getSenha_origem());
        pstUpdate.setString(6, tb.getSenha_destino());
        pstUpdate.setBoolean(7, tb.isHabilitado());
        pstUpdate.setLong(8, tb.getProcesso_id());
        pstUpdate.setLong(9, tb.getId());
        pstUpdate.executeUpdate();
    }

    public void delete(int id) throws SQLException {
        pstDelete.setLong(1, id);
        pstDelete.executeUpdate();
    }

    private TB_REPLICACAO_DIRECAO map(ResultSet rs) throws SQLException {
        TB_REPLICACAO_DIRECAO tb = new TB_REPLICACAO_DIRECAO();
        tb.setId(rs.getInt("id"));
        tb.setDirecao_destino(rs.getString("direcao_destino"));
        tb.setDirecao_origem(rs.getString("direcao_origem"));
        tb.setUsuario_destino(rs.getString("usuario_destino"));
        tb.setUsuario_origem(rs.getString("usuario_origem"));
        tb.setSenha_destino(rs.getString("senha_destino"));
        tb.setSenha_origem(rs.getString("senha_origem"));
        tb.setHabilitado(rs.getBoolean("habilitado"));
        tb.setProcesso_id(rs.getLong("processo_id"));
        return tb;
    }
}
