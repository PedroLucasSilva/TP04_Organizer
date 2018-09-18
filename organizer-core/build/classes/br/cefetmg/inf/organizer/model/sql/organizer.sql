DROP TABLE IF EXISTS tema CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;
DROP TABLE IF EXISTS item CASCADE;
DROP TABLE IF EXISTS tag CASCADE;
DROP TABLE IF EXISTS item_tag CASCADE;

CREATE TABLE tema
(
  seq_tema serial NOT NULL,
  nvl_arredondamento smallint NOT NULL,
  cor_letra character varying(7) NOT NULL,
  cor_fundo character varying(7) NOT NULL,
  cor_secundaria character varying(7) NOT NULL,
  CONSTRAINT tema_pkey PRIMARY KEY (seq_tema)
);

CREATE TABLE usuario
(
  cod_email character varying(64) NOT NULL,
  nom_usuario character varying(40) NOT NULL,
  txt_senha character varying(32) NOT NULL,
  blb_imagem bytea DEFAULT ''::bytea,
  seq_tema integer NOT NULL DEFAULT 1,
  CONSTRAINT usuario_pkey PRIMARY KEY (cod_email)
);

CREATE TABLE item
(
  seq_item serial NOT NULL,
  nom_item character varying(64) NOT NULL,
  des_item character varying(512),
  dat_item date,
  idt_item character varying(3) NOT NULL,
  idt_estado character(1),
  cod_email character varying(64) NOT NULL,
  CONSTRAINT item_pkey PRIMARY KEY (seq_item)
);

CREATE TABLE tag
(
  seq_tag serial NOT NULL,
  nom_tag character varying(20) NOT NULL,
  cod_email character varying(64) NOT NULL,
  CONSTRAINT tag_pkey PRIMARY KEY (seq_tag)
);

CREATE TABLE item_tag
(
  seq_item integer NOT NULL,
  seq_tag integer NOT NULL,
  CONSTRAINT item_tag_pkey PRIMARY KEY (seq_item, seq_tag)
);
