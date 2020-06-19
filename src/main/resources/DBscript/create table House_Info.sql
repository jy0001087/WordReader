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
	CONSTRAINT houseinfo_pkey PRIMARY KEY (house_code)
);
CREATE UNIQUE INDEX houseinfo_house_code_idx ON public.houseinfo USING btree (house_code);