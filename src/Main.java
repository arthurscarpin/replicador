import database.model.TB_REPLICACAO_PROCESSO;

void main() {
    TB_REPLICACAO_PROCESSO tbReplicacaoProcesso = new TB_REPLICACAO_PROCESSO();
    tbReplicacaoProcesso.setId(1);
    tbReplicacaoProcesso.setProcesso("Completo");
    tbReplicacaoProcesso.setDescricao("Processa todas as tabelas");
    tbReplicacaoProcesso.setHabilitado(true);
    IO.println(tbReplicacaoProcesso);
}
