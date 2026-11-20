package view;

import database.dao.DirecaoDAO;
import database.dao.ReplicacaoProcessoDAO;
import database.model.TB_REPLICACAO_DIRECAO;
import database.model.TB_REPLICACAO_PROCESSO;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class TelaReplicacaoDirecaoView extends JFrame {

    private enum ModoTela { NENHUM, INSERT, UPDATE }

    private ModoTela modoTela = ModoTela.NENHUM;

    private final DirecaoDAO direcaoDao;
    private final ReplicacaoProcessoDAO processoDao;

    private JTextField txfId;
    private JComboBox<TB_REPLICACAO_PROCESSO> cbProcesso;
    private JTextField txfDirecaoOrigem;
    private JTextField txfDirecaoDestino;
    private JTextField txfUsuarioOrigem;
    private JTextField txfUsuarioDestino;
    private JPasswordField txpSenhaOrigem;
    private JPasswordField txpSenhaDestino;
    private JCheckBox chkHabilitado;

    private JButton btnSalvar;
    private JButton btnAdicionar;
    private JButton btnBuscar;
    private JButton btnExcluir;

    public TelaReplicacaoDirecaoView(Connection conexao) throws SQLException {
        this.direcaoDao = new DirecaoDAO(conexao);
        this.processoDao = new ReplicacaoProcessoDAO(conexao);

        setTitle("Cadastro de direções");
        setSize(760, 490);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        btnBuscar = new JButton("BUSCAR");
        btnAdicionar = new JButton("ADICIONAR");
        btnSalvar = new JButton("SALVAR");
        btnExcluir = new JButton("EXCLUIR");

        btnBuscar.setBounds(10, 10, 130, 30);
        btnAdicionar.setBounds(150, 10, 130, 30);
        btnSalvar.setBounds(290, 10, 130, 30);
        btnExcluir.setBounds(430, 10, 130, 30);

        getContentPane().add(btnBuscar);
        getContentPane().add(btnAdicionar);
        getContentPane().add(btnSalvar);
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

        JLabel lblDirecaoOrigem = new JLabel("DIREÇÃO DE ORIGEM:");
        lblDirecaoOrigem.setBounds(10, 185, 140, 25);
        getContentPane().add(lblDirecaoOrigem);

        txfDirecaoOrigem = new JTextField();
        txfDirecaoOrigem.setBounds(160, 185, 560, 25);
        getContentPane().add(txfDirecaoOrigem);

        JLabel lblUsuarioOrigem = new JLabel("USUÁRIO DE ORIGEM:");
        lblUsuarioOrigem.setBounds(10, 220, 140, 25);
        getContentPane().add(lblUsuarioOrigem);

        txfUsuarioOrigem = new JTextField();
        txfUsuarioOrigem.setBounds(160, 220, 560, 25);
        getContentPane().add(txfUsuarioOrigem);

        JLabel lblSenhaOrigem = new JLabel("SENHA DE ORIGEM:");
        lblSenhaOrigem.setBounds(10, 255, 140, 25);
        getContentPane().add(lblSenhaOrigem);

        txpSenhaOrigem = new JPasswordField();
        txpSenhaOrigem.setBounds(160, 255, 560, 25);
        getContentPane().add(txpSenhaOrigem);

        JLabel lblDestino = new JLabel("DESTINO:");
        lblDestino.setBounds(10, 300, 200, 25);
        lblDestino.setFont(lblDestino.getFont().deriveFont(Font.BOLD));
        getContentPane().add(lblDestino);

        JLabel lblDirecaoDestino = new JLabel("DIREÇÃO DE DESTINO:");
        lblDirecaoDestino.setBounds(10, 335, 140, 25);
        getContentPane().add(lblDirecaoDestino);

        txfDirecaoDestino = new JTextField();
        txfDirecaoDestino.setBounds(160, 335, 560, 25);
        getContentPane().add(txfDirecaoDestino);

        JLabel lblUsuarioDestino = new JLabel("USUÁRIO DE DESTINO:");
        lblUsuarioDestino.setBounds(10, 370, 140, 25);
        getContentPane().add(lblUsuarioDestino);

        txfUsuarioDestino = new JTextField();
        txfUsuarioDestino.setBounds(160, 370, 280, 25);
        getContentPane().add(txfUsuarioDestino);

        JLabel lblSenhaDestino = new JLabel("SENHA DE DESTINO:");
        lblSenhaDestino.setBounds(450, 370, 120, 25);
        getContentPane().add(lblSenhaDestino);

        txpSenhaDestino = new JPasswordField();
        txpSenhaDestino.setBounds(570, 370, 150, 25);
        getContentPane().add(txpSenhaDestino);

        chkHabilitado = new JCheckBox("HABILITADO:");
        chkHabilitado.setBounds(10, 405, 140, 25);
        getContentPane().add(chkHabilitado);

        for (TB_REPLICACAO_PROCESSO processo : processoDao.selectAll()) {
            cbProcesso.addItem(processo);
        }

        txfId.setEnabled(false);
        setCamposHabilitados(false);
        btnSalvar.setEnabled(false);
        btnExcluir.setEnabled(false);

        btnAdicionar.addActionListener(event -> {
            modoTela = ModoTela.INSERT;
            limparCampos();
            if (cbProcesso.getItemCount() > 0) {
                cbProcesso.setSelectedIndex(0);
            }
            chkHabilitado.setSelected(true);
            txfId.setEnabled(false);
            setCamposHabilitados(true);
            btnSalvar.setEnabled(true);
            btnExcluir.setEnabled(false);
        });

        btnSalvar.addActionListener(event -> {
            try {
                if (cbProcesso.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Selecione um processo.",
                            "Atenção",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                if (txfDirecaoOrigem.getText().trim().isEmpty()
                        || txfDirecaoDestino.getText().trim().isEmpty()
                        || txfUsuarioOrigem.getText().trim().isEmpty()
                        || txfUsuarioDestino.getText().trim().isEmpty()
                        || txpSenhaOrigem.getPassword().length == 0
                        || txpSenhaDestino.getPassword().length == 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Preencha as direções, os usuários e as senhas de origem e destino.",
                            "Atenção",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                TB_REPLICACAO_PROCESSO processoSelecionado =
                        (TB_REPLICACAO_PROCESSO) cbProcesso.getSelectedItem();
                TB_REPLICACAO_DIRECAO direcao = new TB_REPLICACAO_DIRECAO();
                direcao.setProcesso_id(processoSelecionado.getId());
                direcao.setHabilitado(chkHabilitado.isSelected());
                direcao.setDirecao_origem(txfDirecaoOrigem.getText().trim());
                direcao.setDirecao_destino(txfDirecaoDestino.getText().trim());
                direcao.setUsuario_origem(txfUsuarioOrigem.getText().trim());
                direcao.setUsuario_destino(txfUsuarioDestino.getText().trim());
                direcao.setSenha_origem(new String(txpSenhaOrigem.getPassword()).trim());
                direcao.setSenha_destino(new String(txpSenhaDestino.getPassword()).trim());

                if (modoTela == ModoTela.INSERT) {
                    direcaoDao.insert(direcao);
                    JOptionPane.showMessageDialog(
                            this,
                            "Direção cadastrada com sucesso.",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else if (modoTela == ModoTela.UPDATE) {
                    if (txfId.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Não foi possível identificar a direção para atualização.",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    direcao.setId(Long.parseLong(txfId.getText().trim()));
                    direcaoDao.update(direcao);
                    JOptionPane.showMessageDialog(
                            this,
                            "Direção atualizada com sucesso.",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Use ADICIONAR ou BUSCAR antes de salvar.",
                            "Atenção",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                modoTela = ModoTela.NENHUM;
                txfId.setEnabled(false);
                setCamposHabilitados(false);
                btnSalvar.setEnabled(false);
                btnExcluir.setEnabled(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível salvar a direção: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        btnExcluir.addActionListener(event -> {
            try {
                if (txfId.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Selecione uma direção antes de excluí-la.",
                            "Atenção",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                int opcao = JOptionPane.showConfirmDialog(
                        this,
                        "Deseja realmente excluir a direção selecionada?",
                        "Confirmar exclusão",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (opcao != JOptionPane.YES_OPTION) {
                    return;
                }

                direcaoDao.delete(Long.parseLong(txfId.getText().trim()));
                JOptionPane.showMessageDialog(
                        this,
                        "Direção excluída com sucesso.",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                modoTela = ModoTela.NENHUM;
                limparCampos();
                txfId.setEnabled(false);
                setCamposHabilitados(false);
                btnSalvar.setEnabled(false);
                btnExcluir.setEnabled(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível excluir a direção: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        btnBuscar.addActionListener(event -> {
            try {
                ConsultaDirecaoDialog dialogo = new ConsultaDirecaoDialog(this, direcaoDao);
                dialogo.setVisible(true);

                TB_REPLICACAO_DIRECAO direcao = dialogo.getSelecionado();
                if (direcao == null) {
                    return;
                }

                modoTela = ModoTela.UPDATE;
                txfId.setText(String.valueOf(direcao.getId()));
                chkHabilitado.setSelected(direcao.isHabilitado());
                txfDirecaoOrigem.setText(direcao.getDirecao_origem());
                txfUsuarioOrigem.setText(direcao.getUsuario_origem());
                txpSenhaOrigem.setText(direcao.getSenha_origem());
                txfDirecaoDestino.setText(direcao.getDirecao_destino());
                txfUsuarioDestino.setText(direcao.getUsuario_destino());
                txpSenhaDestino.setText(direcao.getSenha_destino());

                for (int indice = 0; indice < cbProcesso.getItemCount(); indice++) {
                    TB_REPLICACAO_PROCESSO processo = cbProcesso.getItemAt(indice);
                    if (processo.getId() == direcao.getProcesso_id()) {
                        cbProcesso.setSelectedIndex(indice);
                        break;
                    }
                }

                txfId.setEnabled(false);
                setCamposHabilitados(true);
                btnSalvar.setEnabled(true);
                btnExcluir.setEnabled(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível buscar direções: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void limparCampos() {
        txfId.setText("");
        txfDirecaoOrigem.setText("");
        txfDirecaoDestino.setText("");
        txfUsuarioOrigem.setText("");
        txfUsuarioDestino.setText("");
        txpSenhaOrigem.setText("");
        txpSenhaDestino.setText("");
        chkHabilitado.setSelected(false);
    }

    private void setCamposHabilitados(boolean habilitado) {
        cbProcesso.setEnabled(habilitado);
        chkHabilitado.setEnabled(habilitado);
        txfDirecaoOrigem.setEnabled(habilitado);
        txfUsuarioOrigem.setEnabled(habilitado);
        txpSenhaOrigem.setEnabled(habilitado);
        txfDirecaoDestino.setEnabled(habilitado);
        txfUsuarioDestino.setEnabled(habilitado);
        txpSenhaDestino.setEnabled(habilitado);
    }
}
