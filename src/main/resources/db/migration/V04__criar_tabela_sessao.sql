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

INSERT INTO sessao (codigo, data, hora_inicio, hora_fim, valor, tipo_animacao, tipo_audio, codigo_filme, codigo_sala) values(1, '2022-11-04' , '13:00', '15:58', 35.00, 'ANIMACAO2D', 'ORIGINAL', 1, 1);
INSERT INTO sessao (codigo, data, hora_inicio, hora_fim, valor, tipo_animacao, tipo_audio, codigo_filme, codigo_sala) values(2, '2022-11-04' , '15:30', '18:29', 60.00, 'ANIMACAO3D', 'ORIGINAL', 2, 2);
INSERT INTO sessao (codigo, data, hora_inicio, hora_fim, valor, tipo_animacao, tipo_audio, codigo_filme, codigo_sala) values(3, '2022-11-04' , '20:00', '23:21', 35.00, 'ANIMACAO2D', 'DUBLADO', 3, 4);