CREATE TABLE sala(
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(125) NOT NULL,
	qtd_assentos INT(50) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO sala (codigo, nome, qtd_assentos) values(1, 'SALA1', 50);
INSERT INTO sala (codigo, nome, qtd_assentos) values(2, 'SALA2', 50);
INSERT INTO sala (codigo, nome, qtd_assentos) values(3, 'SALA3', 75);
INSERT INTO sala (codigo, nome, qtd_assentos) values(4, 'SALA4', 75);
INSERT INTO sala (codigo, nome, qtd_assentos) values(5, 'SALA5', 50);