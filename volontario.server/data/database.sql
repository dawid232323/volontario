SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

CREATE TABLE public.experience_level (
    id bigint NOT NULL,
    definition character varying(500),
    name character varying(255),
    value bigint NOT NULL
);

ALTER TABLE public.experience_level OWNER TO postgres;

CREATE SEQUENCE public.experience_level_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.experience_level_id_seq OWNER TO postgres;

ALTER SEQUENCE public.experience_level_id_seq OWNED BY public.experience_level.id;

CREATE TABLE public.institutions (
    id bigint NOT NULL,
    description character varying(4000),
    headquarters character varying(255),
    is_active boolean,
    krs_number character varying(255),
    localization character varying(255),
    name character varying(255),
    path_to_image character varying(255)
);

ALTER TABLE public.institutions OWNER TO postgres;

CREATE SEQUENCE public.institutions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.institutions_id_seq OWNER TO postgres;

ALTER SEQUENCE public.institutions_id_seq OWNED BY public.institutions.id;

CREATE TABLE public.interest_categories (
    id bigint NOT NULL,
    description character varying(750),
    name character varying(255)
);

ALTER TABLE public.interest_categories OWNER TO postgres;

CREATE SEQUENCE public.interest_categories_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.interest_categories_id_seq OWNER TO postgres;

ALTER SEQUENCE public.interest_categories_id_seq OWNED BY public.interest_categories.id;

CREATE TABLE public.roles (
    id bigint NOT NULL,
    name character varying(255)
);

ALTER TABLE public.roles OWNER TO postgres;

CREATE SEQUENCE public.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.roles_id_seq OWNER TO postgres;

ALTER SEQUENCE public.roles_id_seq OWNED BY public.roles.id;

CREATE TABLE public.user_roles (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);

ALTER TABLE public.user_roles OWNER TO postgres;

CREATE TABLE public.users (
    id bigint NOT NULL,
    contact_email_address character varying(255),
    domain_email_address character varying(255),
    first_name character varying(255),
    hashed_password character varying(255),
    is_verified boolean,
    last_name character varying(255),
    phone_number character varying(255),
    institution_id bigint,
    volunteer_data_id bigint
);

ALTER TABLE public.users OWNER TO postgres;

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.users_id_seq OWNER TO postgres;

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;

CREATE TABLE public.volunteer_data (
    id bigint NOT NULL,
    participation_motivation character varying(1500),
    volunteer_experience_id bigint
);

ALTER TABLE public.volunteer_data OWNER TO postgres;

CREATE SEQUENCE public.volunteer_data_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.volunteer_data_id_seq OWNER TO postgres;

ALTER SEQUENCE public.volunteer_data_id_seq OWNED BY public.volunteer_data.id;

CREATE TABLE public.volunteer_interests (
    volunteer_data_id bigint NOT NULL,
    interest_category_id bigint NOT NULL
);

ALTER TABLE public.volunteer_interests OWNER TO postgres;

ALTER TABLE ONLY public.experience_level ALTER COLUMN id SET DEFAULT nextval('public.experience_level_id_seq'::regclass);

ALTER TABLE ONLY public.institutions ALTER COLUMN id SET DEFAULT nextval('public.institutions_id_seq'::regclass);

ALTER TABLE ONLY public.interest_categories ALTER COLUMN id SET DEFAULT nextval('public.interest_categories_id_seq'::regclass);

ALTER TABLE ONLY public.roles ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq'::regclass);

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);

ALTER TABLE ONLY public.volunteer_data ALTER COLUMN id SET DEFAULT nextval('public.volunteer_data_id_seq'::regclass);

COPY public.experience_level (id, definition, name, value) FROM stdin;
\.

COPY public.institutions (id, description, headquarters, is_active, krs_number, localization, name, path_to_image) FROM stdin;
\.

COPY public.interest_categories (id, description, name) FROM stdin;
\.

COPY public.roles (id, name) FROM stdin;
\.

COPY public.user_roles (user_id, role_id) FROM stdin;
\.

COPY public.users (id, contact_email_address, domain_email_address, first_name, hashed_password, is_verified, last_name, phone_number, institution_id, volunteer_data_id) FROM stdin;
\.

COPY public.volunteer_data (id, participation_motivation, volunteer_experience_id) FROM stdin;
\.


COPY public.volunteer_interests (volunteer_data_id, interest_category_id) FROM stdin;
\.

SELECT pg_catalog.setval('public.experience_level_id_seq', 1, false);

SELECT pg_catalog.setval('public.institutions_id_seq', 1, false);

SELECT pg_catalog.setval('public.interest_categories_id_seq', 1, false);

SELECT pg_catalog.setval('public.roles_id_seq', 1, false);

SELECT pg_catalog.setval('public.users_id_seq', 1, false);

SELECT pg_catalog.setval('public.volunteer_data_id_seq', 1, false);

ALTER TABLE ONLY public.experience_level
    ADD CONSTRAINT experience_level_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.institutions
    ADD CONSTRAINT institutions_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.interest_categories
    ADD CONSTRAINT interest_categories_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.volunteer_data
    ADD CONSTRAINT volunteer_data_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fk2qqjpih9isqcs22710v8lef9w FOREIGN KEY (institution_id) REFERENCES public.institutions(id);

ALTER TABLE ONLY public.volunteer_data
    ADD CONSTRAINT fk39m7559p15633t20r0c9t3k3a FOREIGN KEY (volunteer_experience_id) REFERENCES public.experience_level(id);

ALTER TABLE ONLY public.volunteer_interests
    ADD CONSTRAINT fkfeet6spullr04l5b70iwmwi4f FOREIGN KEY (volunteer_data_id) REFERENCES public.volunteer_data(id);

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id) REFERENCES public.roles(id);

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.volunteer_interests
    ADD CONSTRAINT fkiiv7bb7s0rm07pwpo5iq1jm31 FOREIGN KEY (interest_category_id) REFERENCES public.interest_categories(id);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkiwfpd2gu5i6q4c69531xfx5br FOREIGN KEY (volunteer_data_id) REFERENCES public.volunteer_data(id);
