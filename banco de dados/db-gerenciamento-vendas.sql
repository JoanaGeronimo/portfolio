SET SEARCH_PATH TO trabalho2;

CREATE TABLE estado (
  uf VARCHAR(2) PRIMARY KEY,
  estado VARCHAR(50),
  codigo_ibge INT
);

CREATE TABLE cidade (
  idcidade INTEGER PRIMARY KEY ,
  uf VARCHAR(2),
  area VARCHAR(50),
  cidade VARCHAR(50),
  codigo_ibge INT,
  FOREIGN KEY (uf) REFERENCES estado (uf)
);

CREATE TABLE bairro (
  idbairro INTEGER PRIMARY KEY,
  idcidade INT,
  bairro VARCHAR(50),
  FOREIGN KEY (idcidade)
    REFERENCES cidade (idcidade)
);

CREATE TABLE endereco (
  cep VARCHAR(9) PRIMARY KEY,
  idcidade INT,
  idbairro INT,
  logradouro VARCHAR(50),
  tipo_logradouro VARCHAR(50),
  complemento VARCHAR(50),
  FOREIGN KEY (idcidade) REFERENCES cidade (idcidade),
  FOREIGN KEY (idbairro) REFERENCES bairro (idbairro)
);

CREATE TABLE cliente (
  idcliente SERIAL PRIMARY KEY,
  cep VARCHAR(9),
  nome VARCHAR(45),
  telefone VARCHAR(20),
  numero INT,
  nome_cidade VARCHAR(50),
  nome_bairro VARCHAR(50),
  estado VARCHAR(45),
  local VARCHAR(50),
  codigoIBGE INT,
  quantidade_vendas INT DEFAULT 0,
  total_comprado NUMERIC(10,2) DEFAULT 0,
  status VARCHAR(1),
  limite_compra_fiado DOUBLE PRECISION ,
  FOREIGN KEY (cep) REFERENCES endereco (cep)
);

CREATE TABLE venda (
  idvenda SERIAL PRIMARY KEY,
  idcliente INT,
  cep VARCHAR(9),
  numero INT UNIQUE,
  valor_total NUMERIC(10,2) DEFAULT 0,
  data_venda DATE,
  data_pagamento DATE,
  FOREIGN KEY (idcliente) REFERENCES cliente (idcliente)
);

CREATE TABLE produto (
  idproduto SERIAL PRIMARY KEY,
  qtdestoque INT,
  preco_custo NUMERIC(10,2) DEFAULT 0,
  percentual_lucro NUMERIC(10,2) DEFAULT 0,
  preco_venda NUMERIC(10,2) DEFAULT 0
);

CREATE TABLE geo (
  cep VARCHAR(9) PRIMARY KEY,
  altitude VARCHAR(11),
  longitude VARCHAR(11),
  FOREIGN KEY (cep) REFERENCES endereco (cep)
);

CREATE TABLE item_venda (
  idvenda INT,
  idproduto INT,
  quantidade INT,
  valor NUMERIC(10,2),
  PRIMARY KEY (idvenda, idproduto),
  FOREIGN KEY (idvenda) REFERENCES venda (idvenda),
  FOREIGN KEY (idproduto) REFERENCES produto (idproduto)
);

CREATE TABLE resumo_diario (
  ano INTEGER,
  lancamento INT,
  data_pagamento DATE,
  numero_venda INT,
  valor_recebido NUMERIC(10,2) DEFAULT 0,
  saldo_dia NUMERIC(10,2) DEFAULT 0,
  PRIMARY KEY (ano, lancamento)
);

ALTER TABLE bairro
ADD CONSTRAINT id_bairro PRIMARY KEY (id_bairro);

ALTER TABLE estado
ADD CONSTRAINT uf PRIMARY KEY (uf);

ALTER TABLE cidade
ADD CONSTRAINT id_cidade PRIMARY KEY (id_cidade);

ALTER TABLE endereco
ADD CONSTRAINT cep PRIMARY KEY (cep);

ALTER TABLE geo
ADD CONSTRAINT geo_pk PRIMARY KEY (cep);

ALTER TABLE geo
ADD CONSTRAINT cep FOREIGN KEY (cep)
REFERENCES endereco (cep);

ALTER TABLE bairro
ADD CONSTRAINT id_cidade FOREIGN KEY (id_cidade)
REFERENCES cidade (id_cidade);

ALTER TABLE cidade
ADD CONSTRAINT uf FOREIGN KEY (uf)
REFERENCES estado (uf);

ALTER TABLE endereco
ADD CONSTRAINT id_cidade FOREIGN KEY (id_cidade)
REFERENCES cidade (id_cidade);

UPDATE endereco SET id_bairro = NULL WHERE id_bairro = 0;

ALTER TABLE endereco
ADD CONSTRAINT id_bairro FOREIGN KEY (id_bairro)
REFERENCES bairro (id_bairro);

-- Função responsavel por pegar o cep do cliente e preencher os outros campos
-- nomecidade
-- nomebairro
-- estado
-- local
-- codigoIBGE
-- automaticamente
CREATE OR REPLACE FUNCTION preencher_cliente() RETURNS TRIGGER AS $$
BEGIN
  SELECT c.cidade, b.bairro, e.estado,en.local, c.cod_ibge
  INTO NEW.nome_cidade, NEW.nome_bairro, NEW.estado, NEW.local, NEW.codigoIBGE
  FROM cidade c
  JOIN endereco en ON c.id_cidade = en.id_cidade
  JOIN bairro b ON b.id_bairro = en.id_bairro
  JOIN estado e ON c.uf = e.uf
  WHERE en.cep = NEW.cep;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_preencher_cliente
BEFORE INSERT ON cliente
FOR EACH ROW
EXECUTE FUNCTION preencher_cliente();

INSERT INTO cliente (nome, telefone, cep, numero,status,limite_compra_fiado)
VALUES ('Joana', '123456789', '02123005', 40,'A', 1000);
INSERT INTO cliente (nome, telefone, cep, numero,status,limite_compra_fiado)
VALUES ('Elza', '123455678', '02119020', 50,'A', 1000 );
INSERT INTO cliente (nome, telefone, cep, numero,status,limite_compra_fiado)
VALUES ('Pedro', '123455678', '02119020', 200,'A', 200 );
INSERT INTO cliente (nome, telefone, cep, numero,status,limite_compra_fiado)
VALUES ('Selena', '123455678', '03273120', 5243,'A', 200 );

SELECT * FROM cliente;
SELECT * FROM endereco where cep = '02123005';
SELECT * FROM bairro where id_bairro = 25820;
SELECT * FROM cidade where id_cidade = 9668;

SELECT * FROM endereco where cep = '02119020';
SELECT * FROM bairro where id_bairro = 26853;
SELECT * FROM cidade where id_cidade = 9668;

-- Essa função é responsavel por verificar se o cep que se deseja inserir, esta cadastrado, na tabela endereço
-- Define a data de venda como a data atual
-- Se o status do cliente for B, não é possivel realizar a venda para esse cliente
CREATE OR REPLACE FUNCTION verificar_cep_e_verificar_status() RETURNS TRIGGER AS $$
DECLARE
    status_cliente CHAR(1);
BEGIN
    IF NOT EXISTS (SELECT 1 FROM endereco WHERE cep = NEW.cep) THEN
        RAISE EXCEPTION 'O CEP % não está cadastrado na tabela endereco.', NEW.cep;
    END IF;

    NEW.data_venda := CURRENT_DATE;

    SELECT status INTO status_cliente FROM cliente WHERE idcliente = NEW.idcliente;

    IF (status_cliente = 'B') THEN
        RAISE EXCEPTION 'Não é possível realizar vendas para clientes Bloqueados.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_verificar_cep_e_verificar_status
BEFORE INSERT ON venda
FOR EACH ROW
EXECUTE FUNCTION verificar_cep_e_verificar_status();

-- Venda que deve aparecer que o cep não está cadastrado
INSERT INTO venda (idcliente, cep, numero, valor_total, data_venda, data_pagamento) VALUES
(1, '123456', 40, 2000, '2024-05-30', null);

-- Venda Permitida
INSERT INTO venda (idcliente, cep, numero, data_venda, data_pagamento) VALUES
(1, '02123005', 40, '2024-05-19', null);
INSERT INTO venda (idcliente, cep, numero, data_venda, data_pagamento) VALUES
(2, '02990325', 100, '2024-05-19', null);
INSERT INTO venda (idcliente, cep, numero, data_venda, data_pagamento) VALUES
(3, '02932010', 70, '2024-05-19', null);
INSERT INTO venda (idcliente, cep, numero, data_venda, data_pagamento) VALUES
(4, '02932010', 70, '2024-05-19', null);

update cliente set total_comprado = 0 where idcliente = 2;
delete from item_venda where idvenda=10;
delete from venda where idvenda=10;

INSERT INTO venda (idcliente, cep, numero, data_venda, data_pagamento) VALUES
(1, '02123005', 40,'2024-05-30', null);

-- Essa função é responsavel por inserir a quantidade de vendas e o total comprado, depois que uma venda é inserida para o cliente,
--  tambem é identificado o limite de compra fiado do cliente e o total de vendas não pagas do cliente
--  se o total de vendas não paga for maior que o limite e o status do cliente for A, então o status é alterado para B
CREATE OR REPLACE FUNCTION atualizar_vendas_cliente_insert() RETURNS TRIGGER AS $$
DECLARE
    total_fiado DOUBLE PRECISION;
    limite_fiado DOUBLE PRECISION;
BEGIN
    UPDATE cliente
    SET quantidade_vendas = quantidade_vendas + 1,
        total_comprado = total_comprado + NEW.valor_total
    WHERE idcliente = NEW.idcliente;

    SELECT SUM(valor_total)
    INTO total_fiado
    FROM venda
    WHERE idcliente = NEW.idcliente
      AND data_pagamento IS NULL;

    SELECT limite_compra_fiado
    INTO limite_fiado
    FROM cliente
    WHERE idcliente = NEW.idcliente;

    IF total_fiado > limite_fiado THEN
        UPDATE cliente
        SET status = 'B'
        WHERE idcliente = NEW.idcliente
          AND status = 'A';
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trigger_atualizar_vendas_cliente_insert
AFTER INSERT ON venda
FOR EACH ROW
EXECUTE FUNCTION atualizar_vendas_cliente_insert();

-- Venda que deve ultrapassar o limite e bloquear o cliente, a proxima não será possivel
INSERT INTO venda (idcliente, cep, numero, valor_total, data_venda, data_pagamento) VALUES
(1, '02123005', 40, 900, '2024-05-30', null);

-- Venda que não foi possivel realizar, por conta do cliente está bloqueado
INSERT INTO venda (idcliente, cep, numero, valor_total, data_venda, data_pagamento) VALUES
(1, '02123005', 40, 100, '2024-05-30', null);

-- Essa função é responsavel por atualizar o total comprado e a quantidadade de vendas, depois que uma venda é alterada ou deletada,
--  tambem é identificado o limite de compra fiado do cliente e o total de vendas não pagas do cliente
--  se o total de vendas não paga for maior que o limite e o status do cliente for A, então o status é alterado para B
--  se o total de vendas não paga for menor ou igual que o limite e o status do cliente for B, então o status é alterado para A
CREATE OR REPLACE FUNCTION atualizar_vendas_cliente_update_delete() RETURNS TRIGGER AS $$
DECLARE
    total_fiado DOUBLE PRECISION;
    limite_fiado DOUBLE PRECISION;
BEGIN
    IF (TG_OP = 'UPDATE') THEN
        UPDATE cliente
        SET total_comprado = total_comprado - OLD.valor_total + NEW.valor_total
        WHERE idcliente = NEW.idcliente;

        SELECT SUM(valor_total)
        INTO total_fiado
        FROM venda
        WHERE idcliente = NEW.idcliente
          AND data_pagamento IS NULL;

        SELECT limite_compra_fiado
        INTO limite_fiado
        FROM cliente
        WHERE idcliente = NEW.idcliente;

        IF total_fiado > limite_fiado THEN
            UPDATE cliente
            SET status = 'B'
            WHERE idcliente = NEW.idcliente
              AND status = 'A';
        ELSE
            UPDATE cliente
            SET status = 'A'
            WHERE idcliente = NEW.idcliente
              AND status = 'B';
        END IF;

    ELSIF (TG_OP = 'DELETE') THEN
        UPDATE cliente
        SET quantidade_vendas = quantidade_vendas - 1,
            total_comprado = total_comprado - OLD.valor_total
        WHERE idcliente = OLD.idcliente;

        SELECT SUM(valor_total)
        INTO total_fiado
        FROM venda
        WHERE idcliente = OLD.idcliente
          AND data_pagamento IS NULL;

        SELECT limite_compra_fiado
        INTO limite_fiado
        FROM cliente
        WHERE idcliente = OLD.idcliente;

        IF total_fiado <= limite_fiado THEN
            UPDATE cliente
            SET status = 'A'
            WHERE idcliente = OLD.idcliente
              AND status = 'B';
        END IF;
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_atualizar_vendas_cliente_update_delete
AFTER UPDATE OR DELETE ON venda
FOR EACH ROW
EXECUTE FUNCTION atualizar_vendas_cliente_update_delete();

-- Update que verifica se após o valor total da venda ser alterado o valor comprado do cliente tbm é alterado,
-- e verifica se com esse novo valor ele deve ser aprovado ou continuar bloqueado e vice-versa.
UPDATE venda SET valor_total = 500 WHERE idvenda = 4;

UPDATE venda SET valor_total = 1100 WHERE idvenda = 3;

-- delete para verificar se com a exclusão da venda, status do cliente é alterado e o valor e quantidade tbm
DELETE FROM venda WHERE idvenda = 8;

-- Essa trigger é chamada quando há um update na data de pagamento,
-- ela verifica se após o pagamento da venda, o total de vendas não pagas, é menor ou igual que o limite
-- Se o cliente estiver com o status B, mas apÓs o pagamento o total de vendas não pagas, é menor ou igual que o limite
-- então o cliente é liberado para A
CREATE OR REPLACE FUNCTION update_data_pagamento() RETURNS TRIGGER AS $$
DECLARE
    total_fiado DOUBLE PRECISION;
    limite_fiado DOUBLE PRECISION;
BEGIN

    IF (NEW.data_pagamento IS NOT NULL AND OLD.data_pagamento IS NULL) THEN
        SELECT SUM(valor_total)
        INTO total_fiado
        FROM venda
        WHERE idcliente = NEW.idcliente
          AND data_pagamento IS NULL;

        SELECT limite_compra_fiado
        INTO limite_fiado
        FROM cliente
        WHERE idcliente = NEW.idcliente;

        IF total_fiado <= limite_fiado THEN
            UPDATE cliente
            SET status = 'A'
            WHERE idcliente = NEW.idcliente
              AND status = 'B';
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trigger_update_data_pagamento
AFTER UPDATE ON venda
FOR EACH ROW
WHEN (OLD.data_pagamento IS NULL AND NEW.data_pagamento IS NOT NULL)
EXECUTE FUNCTION update_data_pagamento();

INSERT INTO venda (idcliente, cep, numero, valor_total, data_venda, data_pagamento) VALUES
(2, '02123005', 40, 1000, '2024-05-30', null);

UPDATE venda SET data_pagamento = '2024-04-10' WHERE idvenda = 6;

--Essa trigger calcula o preco de venda do produto de acordo com o preco_custo e o percentual_lucro
CREATE OR REPLACE FUNCTION calcular_preco_venda() RETURNS TRIGGER AS $$
BEGIN

    NEW.preco_venda := NEW.preco_custo * (NEW.percentual_lucro / 100.0) + NEW.preco_custo;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_calcular_preco_venda
BEFORE INSERT ON produto
FOR EACH ROW
EXECUTE FUNCTION calcular_preco_venda();

-- Inserção para verificar se o preco_venda está sendo calculado
INSERT INTO produto (qtdestoque, preco_custo, percentual_lucro) VALUES
(100, 500, 50),
(50, 25.75, 30),
(200, 5.00, 15);

-- Essa trigger calcula novamente o preco de venda do produto de acordo com o preco_custo e o percentual_lucro
-- se for alterado o percentual_lucro e o preco_custo
CREATE OR REPLACE TRIGGER trigger_calcular_preco_venda_update
BEFORE UPDATE OF percentual_lucro, preco_custo ON produto
FOR EACH ROW
EXECUTE FUNCTION calcular_preco_venda();

-- updates para verificar se o percentual ou o preco for alterado o preco_venda tambem deve ser
UPDATE produto SET percentual_lucro = 55 WHERE idproduto = 1;
UPDATE produto SET preco_custo = 20 WHERE idproduto = 1;

-- Essa trigger é responsável por inserir no item venda o valor do preco_venda referente a aquele produto que será inserido.
CREATE OR REPLACE FUNCTION inserir_valor_produto_itemvenda()
RETURNS TRIGGER AS $$
BEGIN
    NEW.valor := (SELECT preco_venda FROM produto WHERE idproduto = NEW.idproduto) ;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER tg_inserir_valor_produto_itemvenda
BEFORE INSERT ON item_venda
FOR EACH ROW
EXECUTE FUNCTION inserir_valor_produto_itemvenda();

INSERT INTO item_venda (idvenda, idproduto, quantidade)
VALUES
(8,6,4);

UPDATE item_venda set quantidade = 1 WHERE idproduto = 5 AND idvenda = 10;

DELETE FROM item_venda where idvenda=8 AND idproduto=6;

-- Essa trigger é reponsavel por calcular o valor total da venda e modificar a quantidade do estoque do produto
-- assim que um item_venda é inserido
CREATE OR REPLACE FUNCTION recalcular_valor_total_venda() RETURNS TRIGGER AS $$

BEGIN
UPDATE venda
    SET valor_total = valor_total + (NEW.valor * NEW.quantidade)
    WHERE idvenda = NEW.idvenda;

    UPDATE produto
    SET qtdestoque = qtdestoque - NEW.quantidade
    WHERE idproduto = NEW.idproduto;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_recalcular_valor_total_venda_insert
AFTER INSERT ON item_venda
FOR EACH ROW
EXECUTE FUNCTION recalcular_valor_total_venda();

-- Essa trigger é reponsavel por recalcular o valor total da venda e modificar a quantidade do estoque do produto
-- depois que um item_venda é alterado
CREATE OR REPLACE FUNCTION recalcular_valor_total_venda_update() RETURNS TRIGGER AS $$
DECLARE
    diferenca_quantidade INT;
BEGIN
    diferenca_quantidade := NEW.quantidade - OLD.quantidade;

    UPDATE venda
    SET valor_total = valor_total + (NEW.valor * diferenca_quantidade)
    WHERE idvenda = NEW.idvenda;

    UPDATE produto
    SET qtdestoque = qtdestoque - diferenca_quantidade
    WHERE idproduto = NEW.idproduto;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_recalcular_valor_total_venda_update
AFTER UPDATE ON item_venda
FOR EACH ROW
EXECUTE FUNCTION recalcular_valor_total_venda_update();

INSERT INTO item_venda (idvenda, idproduto, quantidade) VALUES (4, 3, 2);

DELETE FROM item_venda WHERE idvenda = 2 AND idproduto = 2;
set search_path to trabalho2;
UPDATE item_venda SET quantidade = 2 WHERE idvenda = 2 AND idproduto = 2;

-- Essa trigger é reponsavel por recalcular o valor total da venda e modificar a quantidade do estoque do produto
-- depois que um item_venda é deletado
CREATE OR REPLACE FUNCTION recalcular_valor_total_venda_delete() RETURNS TRIGGER AS $$
BEGIN
    UPDATE venda
    SET valor_total = valor_total - (OLD.valor * OLD.quantidade)
    WHERE idvenda = OLD.idvenda;

    UPDATE produto
    SET qtdestoque = qtdestoque + OLD.quantidade
    WHERE idproduto = OLD.idproduto;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_recalcular_valor_total_venda_delete
AFTER DELETE ON item_venda
FOR EACH ROW
EXECUTE FUNCTION recalcular_valor_total_venda_delete();

DELETE FROM item_venda WHERE idvenda = 2 AND idproduto = 1;

-- Essa trigger é reponsavel por atualizar a tabela resumo_diario,
-- assim que uma venda é paga, ou seja, a data_pagamento é !- null
CREATE OR REPLACE FUNCTION atualizar_resumo_diario() RETURNS TRIGGER AS $$
DECLARE
    ano_atual INTEGER;
    proximo_lancamento INTEGER;
    saldo_anterior DOUBLE PRECISION;
    saldo_novo DOUBLE PRECISION;
BEGIN

    ano_atual := EXTRACT(YEAR FROM NEW.data_pagamento);

    SELECT MAX(lancamento) + 1
    INTO proximo_lancamento
    FROM resumo_diario
    WHERE ano = ano_atual;

    IF proximo_lancamento IS NULL THEN
        proximo_lancamento := 1;
    END IF;

    SELECT saldo_dia
    INTO saldo_anterior
    FROM resumo_diario
    WHERE data_pagamento = NEW.data_pagamento
    ORDER BY lancamento DESC
    LIMIT 1;

    IF saldo_anterior IS NULL THEN
        saldo_anterior := 0;
    END IF;

    saldo_novo := saldo_anterior + NEW.valor_total;

    INSERT INTO resumo_diario (ano, lancamento, data_pagamento, numero_venda, saldo_dia, valor_recebido)
    VALUES (
        ano_atual,
        proximo_lancamento,
        NEW.data_pagamento,
        NEW.idvenda,
        saldo_novo,
        NEW.valor_total
    );

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_atualizar_resumo_diario
AFTER UPDATE ON venda
FOR EACH ROW
WHEN (OLD.data_pagamento IS DISTINCT FROM NEW.data_pagamento AND NEW.data_pagamento IS NOT NULL)
EXECUTE FUNCTION atualizar_resumo_diario();

delete from resumo_diario;
update venda set data_pagamento = current_date where idvenda = 7;
update venda set data_pagamento = '2025-05-20' where idvenda = 9;

-- Crie 3 usuarios um para parte de cliente/venda/itemvenda/produto e outro para a outra parte do banco cada usuario
-- pode ter acesso as suas tabelas mas nao pode excluir.
-- Somente o terceiro usuario pode excluir de qualquer tabela.

-- usuario para a tabela cliente/venda/itemvenda/produto
CREATE ROLE vendas with password '1234' login;
GRANT CONNECT ON DATABASE pe3002292 TO vendas;
GRANT ALL PRIVILEGES ON TABLE cliente TO vendas;
GRANT ALL PRIVILEGES ON TABLE venda TO vendas;
GRANT ALL PRIVILEGES ON TABLE item_venda TO vendas;
GRANT ALL PRIVILEGES ON TABLE produto TO vendas;

REVOKE DELETE ON TABLE cliente FROM vendas;
REVOKE DELETE ON TABLE venda FROM vendas;
REVOKE DELETE ON TABLE item_venda FROM vendas;
REVOKE DELETE ON TABLE produto FROM vendas;

-- usuario para a tabela endereco/cidade/bairro/estado/geo
CREATE ROLE localizacao with password '1234' login;
GRANT CONNECT ON DATABASE pe3002292 TO localizacao;
GRANT ALL PRIVILEGES ON TABLE endereco TO localizacao;
GRANT ALL PRIVILEGES ON TABLE cidade TO localizacao;
GRANT ALL PRIVILEGES ON TABLE bairro TO localizacao;
GRANT ALL PRIVILEGES ON TABLE estado TO localizacao;
GRANT ALL PRIVILEGES ON TABLE geo TO localizacao;

REVOKE DELETE ON TABLE endereco FROM localizacao;
REVOKE DELETE ON TABLE cidade FROM localizacao;
REVOKE DELETE ON TABLE bairro FROM localizacao;
REVOKE DELETE ON TABLE estado FROM localizacao;
REVOKE DELETE ON TABLE geo FROM localizacao;

-- usuario para todas as tabelas
CREATE ROLE user_admin with password '1234' login;
GRANT CONNECT ON DATABASE pe3002292 TO user_admin;
GRANT ALL PRIVILEGES ON TABLE cliente TO user_admin;
GRANT ALL PRIVILEGES ON TABLE venda TO user_admin;
GRANT ALL PRIVILEGES ON TABLE item_venda TO user_admin;
GRANT ALL PRIVILEGES ON TABLE produto TO user_admin;
GRANT ALL PRIVILEGES ON TABLE endereco TO user_admin;
GRANT ALL PRIVILEGES ON TABLE cidade TO user_admin;
GRANT ALL PRIVILEGES ON TABLE bairro TO user_admin;
GRANT ALL PRIVILEGES ON TABLE estado TO user_admin;
GRANT ALL PRIVILEGES ON TABLE geo TO user_admin;

--VIEW que mostra para cada VENDA, os dados da venda e o local da entrega ("cidade","uf","bairro",'endereço")
-- e o nome do cliente tamabem
CREATE VIEW dados_venda AS
SELECT
    v.idvenda AS idvenda,
    v.data_venda ,
    v.valor_total,
    c.nome AS nome_cliente,
    e.logradouro AS endereco,
    b.bairro,
    ci.cidade,
    es.uf
FROM
    venda v
JOIN cliente c ON v.idcliente = c.idcliente
JOIN endereco e ON v.cep = e.cep
JOIN bairro b ON e.id_bairro = b.id_bairro
JOIN cidade ci ON b.id_cidade = ci.id_cidade
JOIN estado es ON ci.uf = es.uf;

SELECT * FROM dados_venda;

-- Procedure inserir_cliente
CREATE OR REPLACE PROCEDURE inserir_cliente(
    nome_cliente VARCHAR,
    num_telefone VARCHAR,
    cep_cliente VARCHAR,
    numero_casa INT,
    status_cliente VARCHAR,
    limite DOUBLE PRECISION
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO cliente (nome, telefone, cep, numero, status, limite_compra_fiado)
    VALUES (nome_cliente, num_telefone, cep_cliente, numero_casa, status_cliente, limite);
END;
$$;

CALL inserir_cliente('João da Silva', '123456789', '01029901', 123, 'A', 1000.00);

-- Procedure inserir produto
CREATE OR REPLACE PROCEDURE inserir_produto(
    estoque INT,
    custo_produto NUMERIC,
    lucro NUMERIC
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO produto (qtdestoque, preco_custo, percentual_lucro)
    VALUES (estoque, custo_produto, lucro);
END;
$$;

CALL inserir_produto(60, 10, 5.5);

-- Procedure inserir Venda
CREATE OR REPLACE PROCEDURE inserir_venda(
    id_cliente INT,
    cep_cliente VARCHAR(9),
    numero_nota INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO venda (idcliente, cep, numero)
    VALUES (id_cliente, cep_cliente, numero_nota);
END;
$$;

CALL inserir_venda(5,'09071460', 105);

-- Procedure inserir item venda
CREATE OR REPLACE PROCEDURE inserir_item_venda(
    id_venda INT,
    id_produto INT,
    qtd INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO item_venda (idvenda, idproduto, quantidade)
    VALUES (id_venda, id_produto, qtd);
END;
$$;

CALL inserir_item_venda(13, 7, 5);

-- Procedure inserir_bairro
CREATE OR REPLACE PROCEDURE inserir_bairro(
    nome_bairro VARCHAR(50),
    codigo_cidade INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO bairro (id_bairro, bairro, id_cidade)
    VALUES (nextval('seq_id_bairro'), nome_bairro, codigo_cidade);
END;
$$;

CREATE SEQUENCE seq_id_bairro
START 59537;

CALL inserir_bairro('Sinha',9668);

SELECT * FROM bairro WHERE id_bairro = 59537;

-- Procedure inserir_cidade
CREATE OR REPLACE PROCEDURE inserir_cidade(
    cidade_nome VARCHAR,
    uf_estado VARCHAR,
    codigo INTEGER,
    areaCity DOUBLE PRECISION
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO cidade (id_cidade,cidade, uf, cod_ibge, area)
    VALUES (nextval('seq_id_cidade'),cidade_nome, uf_estado, codigo, areaCity);
END;
$$;

CREATE SEQUENCE seq_id_cidade;

SELECT setval('seq_id_cidade', (SELECT MAX(id_cidade) FROM cidade) + 1);

call inserir_cidade('selenopolis', 'SP', 3541406, 560.637);

SELECT * FROM cidade where cidade ='joanopolis';
SELECT * FROM cidade where cidade ='selenopolis';
--Procedure inserir endereco
CREATE OR REPLACE PROCEDURE inserir_endereco(
    cep_endere VARCHAR,
    nome_rua VARCHAR,
    tipo VARCHAR,
    complemen VARCHAR,
    localid VARCHAR,
    codigo_cidade INT,
    codigo_bairro INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO endereco (cep, logradouro, tipo_logradouro, complemento, local, id_cidade, id_bairro)
    VALUES (cep_endere,nome_rua, tipo, complemen,localid, codigo_cidade,codigo_bairro);
END;
$$;

CALL inserir_endereco('012345678', 'joaninha', 'rua', 'cacau show','avenida 123', 9668,59537);
SELECT * FROM endereco WHERE cep = '012345678';

-- Procedure inserir_geo
CREATE OR REPLACE PROCEDURE inserir_geo(
    geo_cep VARCHAR,
    geo_altitude VARCHAR,
    geo_longitude VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO geo (cep, latitude, longitude)
    VALUES (geo_cep, geo_altitude, geo_longitude);
END;
$$;

CALL inserir_geo('012345678', '-21.1807980', '-46.1920240');
SELECT * FROM geo WHERE cep = '012345678';

-- função que ao ser chamada passando um parametro estado(UF), retorna a quantidade de cidades daquele estado.
CREATE OR REPLACE FUNCTION quantidade_estados(siglauf VARCHAR)
RETURNS INT
LANGUAGE plpgsql
AS $$
DECLARE
    qtdcidades INT;
BEGIN
    SELECT COUNT(*)
    INTO qtdcidades
    FROM cidade
    WHERE cidade.uf = siglauf;

    RETURN qtdcidades;
END;
$$;

SELECT quantidade_estados('SP');

SELECT COUNT(*)
FROM cidade
WHERE uf = 'SP';

-- Função que ao ser informado o CEP retorne a cidade e a quantidade de CEP disponivel. Subtraindo o final do inicial.
CREATE OR REPLACE FUNCTION cidade_e_qtd(numcep VARCHAR)
RETURNS TABLE(cidade VARCHAR, qtdceps INT)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT c.cidade::VARCHAR,
           (SELECT MAX(CAST(e2.cep AS INTEGER)) - MIN(CAST(e2.cep AS INTEGER))
            FROM endereco e2
            WHERE e2.id_cidade = c.id_cidade) AS qtdceps
    FROM endereco e
    JOIN cidade c ON e.id_cidade = c.id_cidade
    WHERE e.cep = numcep;
END;
$$;

SELECT * FROM cidade_e_qtd('01029901');

SELECT c.cidade
FROM endereco e
JOIN cidade c ON e.id_cidade = c.id_cidade
WHERE e.cep = '01029901';

SELECT MAX(CAST(cep AS INTEGER)) - MIN(CAST(cep AS INTEGER)) AS qtdceps
FROM endereco e2
WHERE e2.id_cidade = (
    SELECT e.id_cidade
    FROM endereco e
    WHERE cep = '01029901'
);



