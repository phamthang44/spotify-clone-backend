DROP TYPE IF EXISTS public."e_user_type" CASCADE;

DROP TYPE IF EXISTS public."e_user_status" CASCADE;

CREATE TYPE public."e_user_status" AS ENUM (
    'ACTIVE',
    'INACTIVE',
    'NONE',
    'BANNED');

DROP TYPE IF EXISTS public."e_gender" CASCADE;

CREATE TYPE public."e_gender" AS ENUM (
    'MALE',
    'FEMALE',
    'OTHER');

DROP TABLE IF EXISTS public.tbl_role CASCADE;
CREATE TABLE public.tbl_role (
                                 id SERIAL PRIMARY KEY,
                                 role_name varchar(20) UNIQUE NOT NULL
);

DROP TABLE IF EXISTS public.tbl_user CASCADE;
CREATE TABLE public.tbl_user (
                                 id bigserial NOT NULL,
                                 display_name varchar(255) NOT NULL,
                                 date_of_birth date NOT NULL,
                                 gender public."e_gender" NOT NULL,
                                 phone varchar(255) NULL,
                                 email varchar(255) NULL,
                                 "password" varchar(255) NOT NULL,
                                 status public."e_user_status" NOT NULL,
                                 role_id INT NOT NULL REFERENCES public.tbl_role(id),
                                 avatar_url varchar(255) NULL,
                                 created_at timestamp(6) DEFAULT NOW(),
                                 updated_at timestamp(6) DEFAULT NOW(),
                                 CONSTRAINT tbl_user_pkey PRIMARY KEY (id)
);



INSERT INTO public.tbl_role (id, role_name) VALUES
                                                (1, 'ADMIN'),
                                                (2, 'ARTIST'),
                                                (3, 'LISTENER');