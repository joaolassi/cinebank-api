CREATE TABLE usuario (
	codigo BIGINT(20) PRIMARY KEY,
	nome VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	senha VARCHAR(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE permissao (
	codigo BIGINT(20) PRIMARY KEY,
	descricao VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE usuario_permissao (
	codigo_usuario BIGINT(20) NOT NULL,
	codigo_permissao BIGINT(20) NOT NULL,
	PRIMARY KEY (codigo_usuario, codigo_permissao),
	FOREIGN KEY (codigo_usuario) REFERENCES usuario(codigo),
	FOREIGN KEY (codigo_permissao) REFERENCES permissao(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO usuario (codigo, nome, email, senha) values (1, 'Administrador', 'admin@printwayy.com', '$2a$10$gNwO3Q0cJcWNPi.aAR011OPHCVPxIf0POsOOkEax3nO.Gvr9zxS2C');
INSERT INTO usuario (codigo, nome, email, senha) values (2, 'John Doe', 'nobody@printwayy.com', '$2a$10$NayDFG7si0uWd0qHdtBbWu7o28DCZemFrwMzBVoVO2KKfxSLc3aJu');

INSERT INTO permissao (codigo, descricao) values (1, 'ROLE_PESQUISAR_SALA');

INSERT INTO permissao (codigo, descricao) values (2, 'ROLE_CADASTRAR_FILME');
INSERT INTO permissao (codigo, descricao) values (3, 'ROLE_REMOVER_FILME');
INSERT INTO permissao (codigo, descricao) values (4, 'ROLE_PESQUISAR_FILME');

INSERT INTO permissao (codigo, descricao) values (5, 'ROLE_CADASTRAR_SESSAO');
INSERT INTO permissao (codigo, descricao) values (6, 'ROLE_REMOVER_SESSAO');
INSERT INTO permissao (codigo, descricao) values (7, 'ROLE_PESQUISAR_SESSAO');

-- admin
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 1);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 2);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 3);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 4);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 5);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 6);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 7);

-- jhon
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (2, 1);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (2, 4);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (2, 7);