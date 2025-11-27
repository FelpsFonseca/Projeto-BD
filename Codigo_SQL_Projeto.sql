CREATE DATABASE IF NOT EXISTS mydb;
USE mydb;

-- TABELA: casa
DROP TABLE IF EXISTS professor_has_aluno, animal, professor, varinha, aluno, casa;
CREATE TABLE casa (
  casa_id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(45) NOT NULL,
  paleta_de_cores VARCHAR(45),
  principios VARCHAR(45)
);

CREATE TABLE aluno (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome_completo VARCHAR(45) NOT NULL,
  idade INT NOT NULL,
  trouxa_ou_puro_sangue TINYINT(1) NOT NULL,
  casa_casa_id INT,
  CONSTRAINT fk_aluno_casa
    FOREIGN KEY (casa_casa_id)
    REFERENCES casa(casa_id)
);

-- TABELA: varinha (FK -> aluno)
CREATE TABLE varinha (
  varinha_id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  tipo_de_madeira VARCHAR(45) NOT NULL,
  aluno_id INT NOT NULL,
  CONSTRAINT fk_varinha_aluno
    FOREIGN KEY (aluno_id)
    REFERENCES aluno(id)
);

-- TABELA: professor
CREATE TABLE professor (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome_completo VARCHAR(100) NOT NULL,
  materia VARCHAR(45) NOT NULL,
  idade INT NOT NULL
);

-- TABELA: animal (FK -> aluno)
CREATE TABLE animal (
  animal_id INT AUTO_INCREMENT PRIMARY KEY,
  nome_completo VARCHAR(100) NOT NULL,
  especie VARCHAR(100) NOT NULL,
  aluno_id INT NOT NULL,
  CONSTRAINT fk_animal_aluno
    FOREIGN KEY (aluno_id)
    REFERENCES aluno(id)
);

-- TABELA DE JUNÇÃO: professor_has_aluno (N:N)
CREATE TABLE professor_has_aluno (
  professor_id INT NOT NULL,
  aluno_id INT NOT NULL,
  PRIMARY KEY (professor_id, aluno_id),
  CONSTRAINT fk_pha_professor
    FOREIGN KEY (professor_id)
    REFERENCES professor(id),
  CONSTRAINT fk_pha_aluno
    FOREIGN KEY (aluno_id)
    REFERENCES aluno(id)
);

-- CASAS
INSERT INTO casa (nome, paleta_de_cores, principios)
VALUES
  ('Grifinoria', 'Vermelho/Dourado', 'Coragem'),
  ('Sonserina',  'Verde/Prata',     'Ambição'),
  ('Corvinal',   'Azul/Bronze',     'Inteligência');

-- ALUNOS
INSERT INTO aluno (nome_completo, idade, trouxa_ou_puro_sangue, casa_casa_id)
VALUES
  ('Harry Potter',    17, 1, (SELECT casa_id FROM casa WHERE nome='Grifinoria')),
  ('Hermione Granger',17, 1, (SELECT casa_id FROM casa WHERE nome='Grifinoria')),
  ('Draco Malfoy',    17, 1, (SELECT casa_id FROM casa WHERE nome='Sonserina'));

-- PROFESSORES
INSERT INTO professor (nome_completo, materia, idade)
VALUES
  ('Minerva McGonagall', 'Transfiguração', 70),
  ('Severus Snape',      'Poções',         38),
  ('Albus Dumbledore',   'Diretor',        115);

-- VARINHAS 
INSERT INTO varinha (nome, tipo_de_madeira, aluno_id)
VALUES
  ('Azevinho e Pena de Fênix', 'Azevinho', (SELECT id FROM aluno WHERE nome_completo='Harry Potter')),
  ('Videira e Fibra de Dragão', 'Videira', (SELECT id FROM aluno WHERE nome_completo='Hermione Granger')),
  ('Teixo e Fibra de Dragão',   'Teixo',   (SELECT id FROM aluno WHERE nome_completo='Draco Malfoy'));

-- ANIMAIS
INSERT INTO animal (nome_completo, especie, aluno_id)
VALUES
  ('Edwiges',      'Coruja', (SELECT id FROM aluno WHERE nome_completo='Harry Potter')),
  ('Bichento',     'Gato',   (SELECT id FROM aluno WHERE nome_completo='Hermione Granger')),
  ('Serpente de Draco', 'Cobra', (SELECT id FROM aluno WHERE nome_completo='Draco Malfoy'));

-- PROFESSOR_HAS_ALUNO (N:N)
INSERT INTO professor_has_aluno (professor_id, aluno_id)
SELECT (SELECT id FROM professor WHERE nome_completo='Minerva McGonagall'),
       id
FROM aluno
WHERE casa_casa_id = (SELECT casa_id FROM casa WHERE nome='Grifinoria');

INSERT INTO professor_has_aluno (professor_id, aluno_id)
VALUES
  ((SELECT id FROM professor WHERE nome_completo='Severus Snape'),
   (SELECT id FROM aluno WHERE nome_completo='Draco Malfoy')),
  ((SELECT id FROM professor WHERE nome_completo='Albus Dumbledore'),
   (SELECT id FROM aluno WHERE nome_completo='Harry Potter'));
   
   -- UPDATE: todos os alunos da Grifinoria ganham 1 ano
UPDATE aluno
SET idade = idade + 1
WHERE casa_casa_id = (SELECT casa_id FROM casa WHERE nome='Grifinoria');

-- DELETE:
DELETE FROM animal
WHERE especie = 'Cobra';

-- ALTER: adiciona coluna de e-mail e cria UNIQUE
ALTER TABLE aluno
  ADD COLUMN email VARCHAR(120),
  ADD UNIQUE KEY uq_aluno_email (email);

UPDATE aluno
SET email = CONCAT(REPLACE(LOWER(nome_completo), ' ', '.'), '@hogwarts.edu.br')
WHERE email IS NULL;

-- DROP:
ALTER TABLE casa
  DROP COLUMN principios;
  
CREATE USER IF NOT EXISTS 'appuser'@'localhost' IDENTIFIED BY 'SenhaForte!123';
GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE ON mydb.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;	

CREATE OR REPLACE VIEW v_alunos_com_casa AS
SELECT
  a.id,
  a.nome_completo,
  a.idade,
  c.nome AS casa,
  (SELECT COUNT(*) FROM animal an WHERE an.aluno_id = a.id) AS qtd_animais
FROM aluno a
LEFT JOIN casa c  ON c.casa_id = a.casa_casa_id;

SELECT * FROM v_alunos_com_casa;

DELIMITER $$
CREATE FUNCTION fn_qtd_animais_por_aluno(p_aluno_id INT)
RETURNS INT
READS SQL DATA
BEGIN
  DECLARE v_qtd INT;
  SELECT COUNT(*) INTO v_qtd FROM animal WHERE aluno_id = p_aluno_id;
  RETURN v_qtd;
END $$
DELIMITER ;

SELECT a.nome_completo, fn_qtd_animais_por_aluno(a.id) AS animais
FROM aluno a;

DELIMITER $$
CREATE PROCEDURE sp_atribuir_professor_por_casa(
  IN p_professor_id INT,
  IN p_casa_id INT
)
BEGIN
  INSERT IGNORE INTO professor_has_aluno (professor_id, aluno_id)
  SELECT p_professor_id, a.id
  FROM aluno a
  WHERE a.casa_casa_id = p_casa_id;
END $$
DELIMITER ;

CALL sp_atribuir_professor_por_casa(
  (SELECT id FROM professor WHERE nome_completo='Severus Snape'),
  (SELECT casa_id FROM casa WHERE nome='Sonserina')
);