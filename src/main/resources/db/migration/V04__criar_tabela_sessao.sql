CREATE TABLE sessao(
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	data DATE NOT NULL,
	hora_inicio TIME NOT NULL,
    hora_fim TIME,
    valor DECIMAL(10,2) NOT NULL,
    tipo_animacao varchar(20) NOT NULL,
    tipo_audio varchar(20) NOT NULL,
    codigo_filme BIGINT(20) NOT NULL,
    codigo_sala BIGINT(20) NOT NULL,
    FOREIGN KEY (codigo_filme) REFERENCES filme(codigo),
    FOREIGN KEY (codigo_sala) REFERENCES sala(codigo)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
