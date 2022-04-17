-- public.socialgame definition

-- Drop table

-- DROP TABLE public.socialgame;

CREATE TABLE public.socialgame (
                                   uid varchar(50) primary key,
                                   url varchar(100) NOT NULL,
                                   postTitle varchar(500) NULL
);