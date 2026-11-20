package view;

import database.dao.ReplicacaoProcessoDAO;
import database.model.TB_REPLICACAO_PROCESSO;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class TelaReplicacaoProcessoView extends JFrame {

    private enum ModoTela { NENHUM, INSERT, UPDATE }

    private ModoTela modoTela = ModoTela.NENHUM;

    private final ReplicacaoProcessoDAO processoDao;

    private JTextField txfId;
    private JTextField txfProcesso;
    private JTextField txfDescricao;
    private JCheckBox chkHabilitado;

    private JButton btnSalvar;
    private JButton btnAdicionar;
    private JButton btnBuscar;
    private JButton btnExcluir;

    public TelaReplicacaoProcessoView(Connection conexao) throws SQLException {
        this.processoDao = new ReplicacaoProcessoDAO(conexao);

        setTitle("Cadastro de processos");
        setSize(620, 320);
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
        lblId.setBounds(10, 70, 120, 25);
        getContentPane().add(lblId);

        txfId = new JTextField();
        txfId.setBounds(140, 70, 420, 25);
        getContentPane().add(txfId);

        JLabel lblProcesso = new JLabel("PROCESSO:");
        lblProcesso.setBounds(10, 105, 120, 25);
        getContentPane().add(lblProcesso);

        txfProcesso = new JTextField();
        txfProcesso.setBounds(140, 105, 420, 25);
        getContentPane().add(txfProcesso);

        JLabel lblDescricao = new JLabel("DESCRIÇÃO:");
        lblDescricao.setBounds(10, 140, 120, 25);
        getContentPane().add(lblDescricao);

        txfDescricao = new JTextField();
        txfDescricao.setBounds(140, 140, 420, 25);
        getContentPane().add(txfDescricao);

        chkHabilitado = new JCheckBox("HABILITADO:");
        chkHabilitado.setBounds(10, 175, 120, 25);
        getContentPane().add(chkHabilitado);

        txfId.setEnabled(false);
        txfProcesso.setEnabled(false);
        txfDescricao.setEnabled(false);
        chkHabilitado.setSelected(false);
        chkHabilitado.setEnabled(false);
        btnSalvar.setEnabled(false);
        btnExcluir.setEnabled(false);

        btnAdicionar.addActionListener(event -> {
            modoTela = ModoTela.INSERT;

            txfId.setText("");
            txfProcesso.setText("");
            txfDescricao.setText("");
            chkHabilitado.setSelected(true);

            txfId.setEnabled(false);
            txfProcesso.setEnabled(true);
            txfDescricao.setEnabled(true);
            chkHabilitado.setEnabled(true);
            btnSalvar.setEnabled(true);
            btnExcluir.setEnabled(false);
        });

        btnSalvar.addActionListener(event -> {
            try {
                if (txfProcesso.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Informe o nome do processo.",
                            "Atenção",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                TB_REPLICACAO_PROCESSO processo = new TB_REPLICACAO_PROCESSO();
                processo.setProcesso(txfProcesso.getText().trim());
                processo.setDescricao(txfDescricao.getText().trim());
                processo.setHabilitado(chkHabilitado.isSelected());

                if (modoTela == ModoTela.INSERT) {
                    processoDao.insert(processo);
                    JOptionPane.showMessageDialog(
                            this,
                            "Processo cadastrado com sucesso.",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else if (modoTela == ModoTela.UPDATE) {
                    if (txfId.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Não foi possível identificar o processo para atualização.",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    processo.setId(Long.parseLong(txfId.getText().trim()));
                    processoDao.update(processo);
                    JOptionPane.showMessageDialog(
                            this,
                            "Processo atualizado com sucesso.",
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
                txfProcesso.setEnabled(false);
                txfDescricao.setEnabled(false);
                chkHabilitado.setEnabled(false);
                btnSalvar.setEnabled(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível salvar o processo: " + ex.getMessage(),
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
                            "Selecione um processo antes de excluí-lo.",
                            "Atenção",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                int opcao = JOptionPane.showConfirmDialog(
                        this,
                        "Deseja realmente excluir o processo?",
                        "Confirmar exclusão",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (opcao != JOptionPane.YES_OPTION) {
                    return;
                }

                long id = Long.parseLong(txfId.getText().trim());
                processoDao.delete(id);
                JOptionPane.showMessageDialog(
                        this,
                        "Processo excluído com sucesso.",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                modoTela = ModoTela.NENHUM;
                txfId.setText("");
                txfProcesso.setText("");
                txfDescricao.setText("");
                chkHabilitado.setSelected(false);

                txfProcesso.setEnabled(false);
                txfDescricao.setEnabled(false);
                chkHabilitado.setEnabled(false);
                btnSalvar.setEnabled(false);
                btnExcluir.setEnabled(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível excluir o processo: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        btnBuscar.addActionListener(event -> {
            try {
                ConsultaProcessoDialog dialogo = new ConsultaProcessoDialog(this, processoDao);
                dialogo.setVisible(true);
                TB_REPLICACAO_PROCESSO processoSelecionado = dialogo.getSelecionado();

                if (processoSelecionado == null) {
                    return;
                }

                modoTela = ModoTela.UPDATE;
                txfId.setText(String.valueOf(processoSelecionado.getId()));
                txfProcesso.setText(processoSelecionado.getProcesso());
                txfDescricao.setText(processoSelecionado.getDescricao());
                chkHabilitado.setSelected(processoSelecionado.isHabilitado());

                txfProcesso.setEnabled(true);
                txfDescricao.setEnabled(true);
                chkHabilitado.setEnabled(true);
                btnSalvar.setEnabled(true);
                btnExcluir.setEnabled(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível buscar processos: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}
