package view;

import database.dao.DirecaoDAO;
import database.model.TB_REPLICACAO_DIRECAO;
import database.model.TB_REPLICACAO_PROCESSO;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class TelaReplicacaoDirecaoView extends JFrame {

    private enum ModoTela {NENHUM, INSERT, UPDATE}
    private TelaReplicacaoDirecaoView.ModoTela modoTela = TelaReplicacaoDirecaoView.ModoTela.NENHUM;

    private final Connection conn;
    private final DirecaoDAO dao;

    private JTextField txfId;

    private JComboBox<TB_REPLICACAO_PROCESSO> cbProcesso;

    private JTextField txfOrigem;
    private JTextField txfDestino;
    private JTextField txfUsuarioOrigem;
    private JTextField txfUsuarioDestino;
    private JTextField txfSenhaOrigem;
    private JTextField txfSenhaDestino;

    private JCheckBox chkHabilitado;


    private JButton btnSavar;
    private JButton btnAdicionar;
    private JButton btnBuscar;
    private JButton btnExcluir;

    public TelaReplicacaoDirecaoView(Connection conn) throws SQLException {
        this.conn = conn;
        this.dao = new DirecaoDAO(conn);

        setTitle("Cadastro de Processos");
        setSize(760, 490);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        btnBuscar = new JButton("BUSCAR");
        btnAdicionar = new JButton("ADICIONAR");
        btnSavar = new JButton("SALVAR");
        btnExcluir = new JButton("EXCLUIR");

        btnBuscar.setBounds(10, 10, 130, 30);
        btnAdicionar.setBounds(150, 10, 130, 30);
        btnSavar.setBounds(290, 10, 130, 30);
        btnExcluir.setBounds(430, 10, 130, 30);

        getContentPane().add(btnBuscar);
        getContentPane().add(btnAdicionar);
        getContentPane().add(btnSavar);
        getContentPane().add(btnExcluir);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(10, 70, 140, 25);
        getContentPane().add(lblId);

        txfId = new JTextField();
        txfId.setBounds(160, 70, 560, 25);
        getContentPane().add(txfId);

        JLabel lblProcesso = new JLabel("PROCESSO:");
        lblProcesso.setBounds(10, 105, 140, 25);
        getContentPane().add(lblProcesso);

        cbProcesso = new JComboBox<>();
        cbProcesso.setBounds(160, 105, 560, 25);
        getContentPane().add(cbProcesso);

        JLabel lblOrigem = new JLabel("ORIGEM:");
        lblOrigem.setBounds(10, 150, 200, 25);
        lblOrigem.setFont(lblOrigem.getFont().deriveFont(Font.BOLD));
        getContentPane().add(lblOrigem);

        JLabel lblDirecaoOrigem = new JLabel("DIRECAO_ORIGEM:");
        lblDirecaoOrigem.setBounds(10, 185, 140, 25);
        getContentPane().add(lblDirecaoOrigem);

        txfOrigem = new JTextField();
        txfOrigem.setBounds(160, 185, 560, 25);
        getContentPane().add(txfOrigem);

        JLabel lblUsuarioOrigem = new JLabel("USUÁRIO ORIGEM:");
        lblUsuarioOrigem.setBounds(10, 220, 140, 25);
        getContentPane().add(lblUsuarioOrigem);

        txfUsuarioOrigem = new JTextField();
        txfUsuarioOrigem.setBounds(160, 220, 560, 25);
        getContentPane().add(txfUsuarioOrigem);

        JLabel lblSenhaOrigem = new JLabel("SENHA_ORIGEM:");
        lblSenhaOrigem.setBounds(10, 255, 140, 25);
        getContentPane().add(lblSenhaOrigem);

        txfSenhaOrigem = new JTextField();
        txfSenhaOrigem.setBounds(160, 255, 560, 25);
        getContentPane().add(txfSenhaOrigem);

        JLabel lblDestino = new JLabel("DESTINO:");
        lblDestino.setBounds(10, 300, 200, 25);
        lblDestino.setFont(lblDestino.getFont().deriveFont(Font.BOLD));
        getContentPane().add(lblDestino);

        JLabel lblDirecaoDestino = new JLabel("DIRECAO_DESTINO:");
        lblDirecaoDestino.setBounds(10, 335, 140, 25);
        getContentPane().add(lblDirecaoDestino);

        txfDestino = new JTextField();
        txfDestino.setBounds(160, 335, 560, 25);
        getContentPane().add(txfDestino);

        JLabel lblUsuarioDestino = new JLabel("USUÁRIO DESTINO:");
        lblUsuarioDestino.setBounds(10, 370, 140, 25);
        getContentPane().add(lblUsuarioDestino);

        txfUsuarioDestino = new JTextField();
        txfUsuarioDestino.setBounds(160, 370, 280, 25);
        getContentPane().add(txfUsuarioDestino);

        JLabel lblSenhaDestino = new JLabel("SENHA_DESTINO:");
        lblSenhaDestino.setBounds(450, 370, 120, 25);
        getContentPane().add(lblSenhaDestino);

        txfSenhaDestino = new JTextField();
        txfSenhaDestino.setBounds(570, 370, 150, 25);
        getContentPane().add(txfSenhaDestino);

        chkHabilitado = new JCheckBox("HABILITADO:");
        chkHabilitado.setBounds(10, 405, 140, 25);
        getContentPane().add(chkHabilitado);

        txfId.setEnabled(false);
        cbProcesso.setEnabled(false);
        chkHabilitado.setEnabled(false);
        txfOrigem.setEnabled(false);
        txfUsuarioOrigem.setEnabled(false);
        txfSenhaOrigem.setEnabled(false);
        txfDestino.setEnabled(false);
        txfUsuarioDestino.setEnabled(false);
        txfSenhaDestino.setEnabled(false);
        btnSavar.setEnabled(false);
        btnExcluir.setEnabled(false);

        btnAdicionar.addActionListener(e -> {
            modoTela = ModoTela.INSERT;

            txfId.setText("");

            if (cbProcesso.getItemCount() > 0) {
                cbProcesso.setSelectedIndex(0);
            }

            chkHabilitado.setSelected(true);

            txfOrigem.setText("");
            txfUsuarioOrigem.setText("");
            txfSenhaOrigem.setText("");
            txfDestino.setText("");
            txfUsuarioDestino.setText("");
            txfSenhaDestino.setText("");

            cbProcesso.setEnabled(true);
            chkHabilitado.setEnabled(true);
            txfOrigem.setEnabled(true);
            txfUsuarioOrigem.setEnabled(true);
            txfSenhaOrigem.setEnabled(true);
            txfDestino.setEnabled(true);
            txfUsuarioDestino.setEnabled(true);
            txfSenhaDestino.setEnabled(true);

            btnSavar.setEnabled(true);
            btnExcluir.setEnabled(true);
        });

        btnSavar.addActionListener(e -> {
            try {
                if (cbProcesso.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "Informe o PROCESSO.");
                    return;
                }

                if (txfOrigem.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Informe a DIRECAO_ORIGEM.");
                    return;
                }

                if (txfDestino.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Informe a DIRECAO_DESTINO.");
                    return;
                }

                if (txfSenhaOrigem.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Informe a SENHA_ORIGEM.");
                    return;
                }

                if (txfSenhaDestino.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Informe a SENHA_DESTINO.");
                    return;
                }

                if (txfUsuarioOrigem.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Informe o USUÁRIO ORIGEM.");
                    return;
                }

                if (txfUsuarioDestino.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Informe o USUÁRIO DESTINO.");
                    return;
                }

                TB_REPLICACAO_PROCESSO pSel = (TB_REPLICACAO_PROCESSO) cbProcesso.getSelectedItem();

                TB_REPLICACAO_DIRECAO d = new TB_REPLICACAO_DIRECAO();
                d.setProcesso_id(pSel.getId());
                d.setHabilitado(chkHabilitado.isSelected());
                d.setDirecao_origem(txfOrigem.getText().trim());
                d.setDirecao_destino(txfDestino.getText().trim());
                d.setUsuario_origem(txfUsuarioOrigem.getText().trim());
                d.setUsuario_destino(txfUsuarioDestino.getText().trim());
                d.setSenha_origem(txfSenhaOrigem.getText().trim());
                d.setSenha_destino(txfSenhaDestino.getText().trim());

                if (modoTela == ModoTela.INSERT) {
                    dao.insert(d);
                    JOptionPane.showMessageDialog(this, "Direção cadastrada!");
                } else if (modoTela == ModoTela.UPDATE) {
                    if (txfId.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Informe o ID da Direção.");
                        return;
                    }

                    d.setId(Integer.parseInt(txfId.getText()));
                    dao.update(d);
                    JOptionPane.showMessageDialog(this, "Direção atualizada!");
                } else {
                    JOptionPane.showMessageDialog(this, "Clique em ADICIONAR ou BUSCAR para cadastrar ou atualizar uma direção.");
                }

                modoTela = ModoTela.NENHUM;

                txfId.setEnabled(false);
                cbProcesso.setEnabled(false);
                chkHabilitado.setEnabled(false);
                txfOrigem.setEnabled(false);
                txfUsuarioOrigem.setEnabled(false);
                txfSenhaOrigem.setEnabled(false);
                txfDestino.setEnabled(false);
                txfUsuarioDestino.setEnabled(false);
                txfSenhaDestino.setEnabled(false);
                btnSavar.setEnabled(false);
                btnExcluir.setEnabled(false);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
            }
        });

        btnExcluir.addActionListener(e -> {
            try {
                if (txfId.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Informe o ID da Direção.");
                    return;
                }

                int op = JOptionPane.showConfirmDialog(this, "Deseja buscar a direção com ID " + txfId.getText().trim() + "?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (op != JOptionPane.YES_OPTION) return;

                long id = Long.parseLong(txfId.getText());
                dao.delete(id);
                JOptionPane.showMessageDialog(this, "Processo excluído com sucesso!");

                modoTela = ModoTela.NENHUM;

                txfId.setText("");

                if (cbProcesso.getItemCount() > 0) {
                    cbProcesso.setSelectedIndex(0);
                }

                chkHabilitado.setSelected(false);

                txfOrigem.setText("");
                txfUsuarioOrigem.setText("");
                txfSenhaOrigem.setText("");
                txfDestino.setText("");
                txfUsuarioDestino.setText("");
                txfSenhaDestino.setText("");

                cbProcesso.setEnabled(false);
                chkHabilitado.setEnabled(false);
                txfOrigem.setEnabled(false);
                txfUsuarioOrigem.setEnabled(false);
                txfSenhaOrigem.setEnabled(false);
                txfDestino.setEnabled(false);
                txfUsuarioDestino.setEnabled(false);
                txfSenhaDestino.setEnabled(false);

                btnSavar.setEnabled(false);
                btnExcluir.setEnabled(false);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao buscar: " + ex.getMessage());
            }
        });

        btnBuscar.addActionListener(e -> {
            try {
                ConsultaDirecaoDialog dlg = new ConsultaDirecaoDialog(this, dao);
                dlg.setVisible(true);

                TB_REPLICACAO_DIRECAO sel =dlg.getSelecionado();
                if (sel == null) return;

                modoTela = ModoTela.UPDATE;

                txfId.setText(String.valueOf(sel.getId()));
                chkHabilitado.setSelected(sel.isHabilitado());
                cbProcesso.setSelectedItem(sel);
                txfOrigem.setText(sel.getDirecao_origem());
                txfUsuarioOrigem.setText(sel.getUsuario_origem());
                txfSenhaOrigem.setText(sel.getSenha_origem());
                txfDestino.setText(sel.getDirecao_destino());
                txfUsuarioDestino.setText(sel.getUsuario_destino());
                txfSenhaDestino.setText(sel.getSenha_destino());

                long id = sel.getProcesso_id();

                for (int i = 0; i < cbProcesso.getItemCount(); i++) {
                    TB_REPLICACAO_PROCESSO item = cbProcesso.getItemAt(i);
                    if (item.getId() == id) {
                        cbProcesso.setSelectedIndex(i);
                        break;
                    }
                }

                cbProcesso.setEnabled(true);
                chkHabilitado.setEnabled(true);
                txfOrigem.setEnabled(true);
                txfUsuarioOrigem.setEnabled(true);
                txfSenhaOrigem.setEnabled(true);
                txfDestino.setEnabled(true);
                txfUsuarioDestino.setEnabled(true);
                txfSenhaDestino.setEnabled(true);
                btnSavar.setEnabled(true);
                btnExcluir.setEnabled(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao buscar: " + ex.getMessage());
            }
        });
    }
}
