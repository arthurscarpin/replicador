create table bombas(
	id bigserial primary key,
	identificador varchar(50) not null,
	tipo_combustivel varchar(50) not null,
	ativo boolean not null default true,
	created_at timestamp not null default now(),
	updated_at timestamp not null default now()
);

create table clientes(
	id bigserial primary key,
	nome varchar(150) not null,
	documento varchar(20) not null,
	created_at timestamp not null default now(),
	updated_at timestamp not null default now()
);

create table funcionarios(
	id bigserial primary key,
	nome varchar(150)  not null,
	cpf varchar(14) not null unique,
	ativo boolean not null default true,
	created_at timestamp not null default now(),
	updated_at timestamp not null default now()
);

create table abastecimentos(
	id bigserial primary key,
	funcionario_id bigint not null,
	cliente_id bigint,
	bomba_id bigint not null,
	litro numeric(10, 3) not null,
	valor_total numeric(10, 2) not null,
	data_hora timestamp not null,
	created_at timestamp not null default now(),
	updated_at timestamp not null default now(),
	
	constraint fk_abastecimentos_funcionario
	foreign key (funcionario_id)
	references funcionarios(id),
	
	constraint fk_abastecimentos_cliente
	foreign key (cliente_id)
	references clientes(id),
	
	constraint fk_abastecimentos_bomba
	foreign key (bomba_id)
	references bombas(id)
);