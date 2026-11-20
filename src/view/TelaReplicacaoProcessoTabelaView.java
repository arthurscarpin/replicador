package view;

import database.dao.ProcessoTabelaDAO;
import database.dao.ReplicacaoProcessoDAO;
import database.model.TB_REPLICACAO_PROCESSO;
import database.model.TB_REPLICACAO_PROCESSO_TABELA;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class TelaReplicacaoProcessoTabelaView extends JFrame {

    private enum ModoTela { NENHUM, INSERT, UPDATE }

    private ModoTela modoTela = ModoTela.NENHUM;

    private final ProcessoTabelaDAO processoTabelaDao;
    private final ReplicacaoProcessoDAO processoDao;

    private JTextField txfId;
    private JComboBox<TB_REPLICACAO_PROCESSO> cbProcesso;
    private JTextField txfTabelaOrigem;
    private JTextField txfTabelaDestino;
    private JTextField txfOrdem;
    private JCheckBox chkHabilitado;
    private JTextArea txaWhere;

    private JButton btnSalvar;
    private JButton btnAdicionar;
    private JButton btnBuscar;
    private JButton btnExcluir;

    public TelaReplicacaoProcessoTabelaView(Connection conexao) throws SQLException {
        this.processoTabelaDao = new ProcessoTabelaDAO(conexao);
        this.processoDao = new ReplicacaoProcessoDAO(conexao);

        setTitle("Cadastro de tabelas do processo");
        setSize(720, 420);
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
        txfId.setBounds(160, 70, 520, 25);
        getContentPane().add(txfId);

        JLabel lblProcesso = new JLabel("PROCESSO:");
        lblProcesso.setBounds(10, 105, 140, 25);
        getContentPane().add(lblProcesso);

        cbProcesso = new JComboBox<>();
        cbProcesso.setBounds(160, 105, 520, 25);
        getContentPane().add(cbProcesso);

        JLabel lblTabelaOrigem = new JLabel("TABELA DE ORIGEM:");
        lblTabelaOrigem.setBounds(10, 140, 140, 25);
        getContentPane().add(lblTabelaOrigem);

        txfTabelaOrigem = new JTextField();
        txfTabelaOrigem.setBounds(160, 140, 520, 25);
        getContentPane().add(txfTabelaOrigem);

        JLabel lblTabelaDestino = new JLabel("TABELA DE DESTINO:");
        lblTabelaDestino.setBounds(10, 175, 140, 25);
        getContentPane().add(lblTabelaDestino);

        txfTabelaDestino = new JTextField();
        txfTabelaDestino.setBounds(160, 175, 520, 25);
        getContentPane().add(txfTabelaDestino);

        JLabel lblOrdem = new JLabel("ORDEM:");
        lblOrdem.setBounds(10, 210, 140, 25);
        getContentPane().add(lblOrdem);

        txfOrdem = new JTextField();
        txfOrdem.setBounds(160, 210, 520, 25);
        getContentPane().add(txfOrdem);

        chkHabilitado = new JCheckBox("HABILITADO:");
        chkHabilitado.setBounds(160, 245, 140, 25);
        getContentPane().add(chkHabilitado);

        JLabel lblWhere = new JLabel("WHERE:");
        lblWhere.setBounds(10, 280, 140, 25);
        getContentPane().add(lblWhere);

        txaWhere = new JTextArea();
        JScrollPane scrollWhere = new JScrollPane(txaWhere);
        scrollWhere.setBounds(160, 280, 520, 80);
        getContentPane().add(scrollWhere);

        for (TB_REPLICACAO_PROCESSO processo : processoDao.selectAll()) {
            cbProcesso.addItem(processo);
        }

        setCamposHabilitados(false);
        txfId.setEnabled(false);
        btnSalvar.setEnabled(false);
        btnExcluir.setEnabled(false);

        btnAdicionar.addActionListener(event -> {
            modoTela = ModoTela.INSERT;
            limparCampos();
            if (cbProcesso.getItemCount() > 0) {
                cbProcesso.setSelectedIndex(0);
            }
            chkHabilitado.setSelected(true);
            setCamposHabilitados(true);
            txfId.setEnabled(false);
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

                if (txfTabelaOrigem.getText().trim().isEmpty()
                        || txfTabelaDestino.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Preencha as tabelas de origem e destino.",
                            "Atenção",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                int ordem;
                try {
                    ordem = Integer.parseInt(txfOrdem.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Informe uma ordem numérica válida.",
                            "Atenção",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                TB_REPLICACAO_PROCESSO processoSelecionado =
                        (TB_REPLICACAO_PROCESSO) cbProcesso.getSelectedItem();
                TB_REPLICACAO_PROCESSO_TABELA processoTabela = new TB_REPLICACAO_PROCESSO_TABELA();
                processoTabela.setProcesso_id(processoSelecionado.getId());
                processoTabela.setTabela_origem(txfTabelaOrigem.getText().trim());
                processoTabela.setTabela_destino(txfTabelaDestino.getText().trim());
                processoTabela.setOrdem(ordem);
                processoTabela.setHabilitado(chkHabilitado.isSelected());
                processoTabela.setDs_where(txaWhere.getText());

                if (modoTela == ModoTela.INSERT) {
                    processoTabelaDao.insert(processoTabela);
                    JOptionPane.showMessageDialog(
                            this,
                            "Registro cadastrado com sucesso.",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else if (modoTela == ModoTela.UPDATE) {
                    if (txfId.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Não foi possível identificar o registro para atualização.",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    processoTabela.setId(Long.parseLong(txfId.getText().trim()));
                    processoTabelaDao.update(processoTabela);
                    JOptionPane.showMessageDialog(
                            this,
                            "Registro atualizado com sucesso.",
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
                setCamposHabilitados(false);
                txfId.setEnabled(false);
                btnSalvar.setEnabled(false);
                btnExcluir.setEnabled(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível salvar o registro: " + ex.getMessage(),
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
                            "Selecione um registro antes de excluí-lo.",
                            "Atenção",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                int opcao = JOptionPane.showConfirmDialog(
                        this,
                        "Deseja realmente excluir o registro?",
                        "Confirmar exclusão",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (opcao != JOptionPane.YES_OPTION) {
                    return;
                }

                processoTabelaDao.delete(Long.parseLong(txfId.getText().trim()));
                JOptionPane.showMessageDialog(
                        this,
                        "Registro excluído com sucesso.",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                modoTela = ModoTela.NENHUM;
                limparCampos();
                setCamposHabilitados(false);
                txfId.setEnabled(false);
                btnSalvar.setEnabled(false);
                btnExcluir.setEnabled(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível excluir o registro: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        btnBuscar.addActionListener(event -> {
            try {
                ConsultaProcessoTabelaDialog dialogo =
                        new ConsultaProcessoTabelaDialog(this, processoTabelaDao);
                dialogo.setVisible(true);

                TB_REPLICACAO_PROCESSO_TABELA processoTabela = dialogo.getSelecionado();
                if (processoTabela == null) {
                    return;
                }

                modoTela = ModoTela.UPDATE;
                txfId.setText(String.valueOf(processoTabela.getId()));
                txfTabelaOrigem.setText(processoTabela.getTabela_origem());
                txfTabelaDestino.setText(processoTabela.getTabela_destino());
                txfOrdem.setText(String.valueOf(processoTabela.getOrdem()));
                chkHabilitado.setSelected(processoTabela.isHabilitado());
                txaWhere.setText(processoTabela.getDs_where());

                for (int indice = 0; indice < cbProcesso.getItemCount(); indice++) {
                    TB_REPLICACAO_PROCESSO processo = cbProcesso.getItemAt(indice);
                    if (processo.getId() == processoTabela.getProcesso_id()) {
                        cbProcesso.setSelectedIndex(indice);
                        break;
                    }
                }

                setCamposHabilitados(true);
                txfId.setEnabled(false);
                btnSalvar.setEnabled(true);
                btnExcluir.setEnabled(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível buscar registros: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void limparCampos() {
        txfId.setText("");
        txfTabelaOrigem.setText("");
        txfTabelaDestino.setText("");
        txfOrdem.setText("");
        chkHabilitado.setSelected(false);
        txaWhere.setText("");
    }

    private void setCamposHabilitados(boolean habilitado) {
        cbProcesso.setEnabled(habilitado);
        txfTabelaOrigem.setEnabled(habilitado);
        txfTabelaDestino.setEnabled(habilitado);
        txfOrdem.setEnabled(habilitado);
        chkHabilitado.setEnabled(habilitado);
        txaWhere.setEnabled(habilitado);
    }
}
