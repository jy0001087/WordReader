-- public.houseinfo definition

-- Drop table

-- DROP TABLE public.houseinfo;

CREATE TABLE public.houseinfo (
                                  address varchar(50) NULL,
                                  area int4 NULL,
                                  gdlocation varchar(50) NULL,
                                  floor varchar(15) NULL,
                                  price int4 NULL,
                                  housetype varchar(20) NULL,
                                  oriented varchar(5) NULL,
                                  house_code varchar(40) NOT NULL,
                                  update_timestamp timestamp NULL,
                                  houseurl varchar(100) NULL,
                                  house_status int4 NULL,
                                  -- 0：初始  1：存续  9：失效
                                  CONSTRAINT houseinfo_pkey PRIMARY KEY (house_code)
);
CREATE UNIQUE INDEX houseinfo_house_code_idx ON public.houseinfo USING btree (house_code);

-- Table: public.lianjiacdhouse

-- DROP TABLE IF EXISTS public.lianjiacdhouse;

CREATE TABLE IF NOT EXISTS public.lianjiacdhouse
(
    houseid character varying(50) COLLATE pg_catalog."default" NOT NULL,
    proportion numeric(20,3),
    price numeric(10,3),
    housetype character varying(50) COLLATE pg_catalog."default",
    followinfo character varying(50) COLLATE pg_catalog."default",
    orientation character varying(50) COLLATE pg_catalog."default",
    status character varying(50) COLLATE pg_catalog."default",
    url character varying(500) COLLATE pg_catalog."default",
    decoration character varying(50) COLLATE pg_catalog."default",
    fetchdate timestamp without time zone,
    updatedate timestamp without time zone,
    title character varying(50) COLLATE pg_catalog."default",
    taxfree character varying(50) COLLATE pg_catalog."default",
    floor character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT lianjiacdhouse_pkey PRIMARY KEY (houseid)
    )
                        WITH (
                            OIDS = FALSE
                            )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.lianjiacdhouse
    OWNER to tools;