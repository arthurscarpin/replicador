package view;

import database.dao.ReplicacaoProcessoDAO;
import database.model.TB_REPLICACAO_PROCESSO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class ConsultaProcessoDialog extends JDialog {

    private final JTable tabela;
    private final JButton btnSelecionar;
    private final JButton btnCancelar;

    private TB_REPLICACAO_PROCESSO selecionado;

    public ConsultaProcessoDialog(Frame parent, ReplicacaoProcessoDAO processoDao) throws SQLException {
        super(parent, "Consulta de processos", true);
        setSize(700, 400);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(null);

        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("ID");
        modelo.addColumn("Processo");
        modelo.addColumn("Descrição");
        modelo.addColumn("Habilitado");

        for (TB_REPLICACAO_PROCESSO processo : processoDao.selectAll()) {
            modelo.addRow(new Object[]{
                    processo.getId(),
                    processo.getProcesso(),
                    processo.getDescricao(),
                    processo.isHabilitado()
            });
        }

        tabela = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBounds(10, 10, 680, 300);
        add(scrollPane);

        btnSelecionar = new JButton("SELECIONAR");
        btnSelecionar.setBounds(10, 320, 140, 30);
        add(btnSelecionar);

        btnCancelar = new JButton("CANCELAR");
        btnCancelar.setBounds(160, 320, 140, 30);
        add(btnCancelar);

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    btnSelecionar.doClick();
                }
            }
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

            TB_REPLICACAO_PROCESSO processo = new TB_REPLICACAO_PROCESSO();
            processo.setId(Long.parseLong(tabela.getValueAt(linhaSelecionada, 0).toString()));
            processo.setProcesso(tabela.getValueAt(linhaSelecionada, 1).toString());
            processo.setDescricao(tabela.getValueAt(linhaSelecionada, 2).toString());
            processo.setHabilitado(Boolean.parseBoolean(tabela.getValueAt(linhaSelecionada, 3).toString()));
            selecionado = processo;
            dispose();
        });

        btnCancelar.addActionListener(event -> {
            selecionado = null;
            dispose();
        });
    }

    public TB_REPLICACAO_PROCESSO getSelecionado() {
        return selecionado;
    }
}
