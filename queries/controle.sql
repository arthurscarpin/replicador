create table tb_replicacao_processo(
	id bigserial primary key,
	processo varchar(100) not null,
	descricao varchar(300),
	habilitado boolean default true
);

create table tb_replicacao_processo_tabela(
	id bigserial primary key,
	processo_id bigint not null,
	tabela_origem varchar(150) not null,
	tabela_destino varchar(150) not null,
	ordem integer not null,
	habilitado boolean default true,
	ds_where varchar(500) not null
);

create table tb_replicacao_direcao(
	id bigserial primary key,
	direcao_origem varchar(150) not null,
	direcao_destino varchar(150) not null,
	usuario_origem varchar(45) not null,
	usuario_destino varchar(45) not null,
	senha_origem varchar(45) not null,
	senha_destino varchar(45) not null,
	habilitado boolean default true,
	processo_id bigint not null
);

alter table tb_replicacao_direcao
add constraint tb_replicacao_direcao_fk
foreign key (processo_id) references tb_replicacao_processo(id);

alter table tb_replicacao_processo_tabela 
add constraint tb_replicacao_processo_tabela_fk
foreign key (processo_id) references tb_replicacao_processo(id);

