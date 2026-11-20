package view;

import database.dao.DirecaoDAO;
import database.model.TB_REPLICACAO_DIRECAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class ConsultaDirecaoDialog extends JDialog {

    private final JTable tabela;
    private final JButton btnSelecionar;
    private final JButton btnCancelar;

    private TB_REPLICACAO_DIRECAO selecionado;

    public ConsultaDirecaoDialog(Frame parent, DirecaoDAO direcaoDao) throws SQLException {
        super(parent, "Consulta de direções", true);
        setSize(900, 420);
        setLocationRelativeTo(parent);
        setLayout(null);

        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("ID");
        modelo.addColumn("ID do processo");
        modelo.addColumn("Origem");
        modelo.addColumn("Destino");
        modelo.addColumn("Habilitado");

        for (TB_REPLICACAO_DIRECAO direcao : direcaoDao.selectAll()) {
            modelo.addRow(new Object[]{
                    direcao.getId(),
                    direcao.getProcesso_id(),
                    direcao.getDirecao_origem(),
                    direcao.getDirecao_destino(),
                    direcao.isHabilitado()
            });
        }

        tabela = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBounds(10, 10, 860, 300);
        add(scrollPane);

        btnSelecionar = new JButton("SELECIONAR");
        btnSelecionar.setBounds(10, 320, 140, 30);
        add(btnSelecionar);

        btnCancelar = new JButton("CANCELAR");
        btnCancelar.setBounds(160, 320, 140, 30);
        add(btnCancelar);

        btnCancelar.addActionListener(event -> {
            selecionado = null;
            dispose();
        });

        btnSelecionar.addActionListener(event -> {
            int linhaSelecionada = tabela.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Selecione um registro.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            long id = Long.parseLong(tabela.getValueAt(linhaSelecionada, 0).toString());
            try {
                selecionado = direcaoDao.selectById(id);
                if (selecionado == null) {
                    JOptionPane.showMessageDialog(
                            this,
                            "O registro selecionado não foi encontrado.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível carregar o registro: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    btnSelecionar.doClick();
                }
            }
        });
    }

    public TB_REPLICACAO_DIRECAO getSelecionado() {
        return selecionado;
    }
}
