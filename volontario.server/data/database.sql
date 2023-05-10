--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

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

--
-- Name: benefits; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.benefits (
                                 id bigint NOT NULL,
                                 name character varying(255)
);


ALTER TABLE public.benefits OWNER TO postgres;

--
-- Name: benefits_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.benefits_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.benefits_id_seq OWNER TO postgres;

--
-- Name: benefits_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.benefits_id_seq OWNED BY public.benefits.id;


--
-- Name: experience_level; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.experience_level (
                                         id bigint NOT NULL,
                                         definition character varying(500),
                                         name character varying(255),
                                         value bigint
);


ALTER TABLE public.experience_level OWNER TO postgres;

--
-- Name: experience_level_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.experience_level_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.experience_level_id_seq OWNER TO postgres;

--
-- Name: experience_level_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.experience_level_id_seq OWNED BY public.experience_level.id;


--
-- Name: institution_contact_people; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.institution_contact_people (
                                                   id bigint NOT NULL,
                                                   contact_email character varying(255),
                                                   first_name character varying(255),
                                                   last_name character varying(255),
                                                   phone_number character varying(255)
);


ALTER TABLE public.institution_contact_people OWNER TO postgres;

--
-- Name: institution_contact_people_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.institution_contact_people_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.institution_contact_people_id_seq OWNER TO postgres;

--
-- Name: institution_contact_people_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.institution_contact_people_id_seq OWNED BY public.institution_contact_people.id;


--
-- Name: institutions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.institutions (
                                     id bigint NOT NULL,
                                     description character varying(4000),
                                     headquarters character varying(255),
                                     is_active boolean,
                                     krs_number character varying(255),
                                     localization character varying(255),
                                     name character varying(255),
                                     path_to_image character varying(255),
                                     registration_token character varying(255),
                                     institution_contact_person_id bigint
);


ALTER TABLE public.institutions OWNER TO postgres;

--
-- Name: institutions_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.institutions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.institutions_id_seq OWNER TO postgres;

--
-- Name: institutions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.institutions_id_seq OWNED BY public.institutions.id;


--
-- Name: interest_categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.interest_categories (
                                            id bigint NOT NULL,
                                            description character varying(750),
                                            name character varying(255)
);


ALTER TABLE public.interest_categories OWNER TO postgres;

--
-- Name: interest_categories_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.interest_categories_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.interest_categories_id_seq OWNER TO postgres;

--
-- Name: interest_categories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.interest_categories_id_seq OWNED BY public.interest_categories.id;


--
-- Name: offer_benefits; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.offer_benefits (
                                       offer_id bigint NOT NULL,
                                       benefit_id bigint NOT NULL
);


ALTER TABLE public.offer_benefits OWNER TO postgres;

--
-- Name: offer_categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.offer_categories (
                                         offer_id bigint NOT NULL,
                                         interest_category_id bigint NOT NULL
);


ALTER TABLE public.offer_categories OWNER TO postgres;

--
-- Name: offer_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.offer_types (
                                    id bigint NOT NULL,
                                    name character varying(255)
);


ALTER TABLE public.offer_types OWNER TO postgres;

--
-- Name: offer_types_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.offer_types_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.offer_types_id_seq OWNER TO postgres;

--
-- Name: offer_types_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.offer_types_id_seq OWNED BY public.offer_types.id;


--
-- Name: offers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.offers (
                               id bigint NOT NULL,
                               description character varying(500),
                               duration numeric(21,0),
                               end_date timestamp(6) with time zone,
                               expiration_date timestamp(6) with time zone,
                               is_experience_required boolean,
                               is_insurance_needed boolean,
                               is_poznan_only boolean,
                               offer_interval character varying(255),
                               place character varying(255),
                               start_date timestamp(6) with time zone,
                               title character varying(100),
                               week_days character varying(14),
                               contact_person_id bigint,
                               institution_id bigint,
                               minimum_experience_id bigint,
                               offer_type_id bigint
);


ALTER TABLE public.offers OWNER TO postgres;

--
-- Name: offers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.offers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.offers_id_seq OWNER TO postgres;

--
-- Name: offers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.offers_id_seq OWNED BY public.offers.id;


--
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
                              id bigint NOT NULL,
                              name character varying(255)
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.roles_id_seq OWNER TO postgres;

--
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.roles_id_seq OWNED BY public.roles.id;


--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_roles (
                                   user_id bigint NOT NULL,
                                   role_id bigint NOT NULL
);


ALTER TABLE public.user_roles OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
                              id bigint NOT NULL,
                              contact_email_address character varying(255),
                              first_name character varying(255),
                              hashed_password character varying(255),
                              is_verified boolean,
                              last_name character varying(255),
                              phone_number character varying(255),
                              institution_id bigint,
                              volunteer_data_id bigint
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: volunteer_data; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.volunteer_data (
                                       id bigint NOT NULL,
                                       domain_email_address character varying(255),
                                       participation_motivation character varying(1500),
                                       volunteer_experience_id bigint
);


ALTER TABLE public.volunteer_data OWNER TO postgres;

--
-- Name: volunteer_data_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.volunteer_data_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.volunteer_data_id_seq OWNER TO postgres;

--
-- Name: volunteer_data_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.volunteer_data_id_seq OWNED BY public.volunteer_data.id;


--
-- Name: volunteer_interests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.volunteer_interests (
                                            volunteer_data_id bigint NOT NULL,
                                            interest_category_id bigint NOT NULL
);


ALTER TABLE public.volunteer_interests OWNER TO postgres;

--
-- Name: benefits id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefits ALTER COLUMN id SET DEFAULT nextval('public.benefits_id_seq'::regclass);


--
-- Name: experience_level id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.experience_level ALTER COLUMN id SET DEFAULT nextval('public.experience_level_id_seq'::regclass);


--
-- Name: institution_contact_people id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institution_contact_people ALTER COLUMN id SET DEFAULT nextval('public.institution_contact_people_id_seq'::regclass);


--
-- Name: institutions id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institutions ALTER COLUMN id SET DEFAULT nextval('public.institutions_id_seq'::regclass);


--
-- Name: interest_categories id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.interest_categories ALTER COLUMN id SET DEFAULT nextval('public.interest_categories_id_seq'::regclass);


--
-- Name: offer_types id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offer_types ALTER COLUMN id SET DEFAULT nextval('public.offer_types_id_seq'::regclass);


--
-- Name: offers id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers ALTER COLUMN id SET DEFAULT nextval('public.offers_id_seq'::regclass);


--
-- Name: roles id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Name: volunteer_data id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.volunteer_data ALTER COLUMN id SET DEFAULT nextval('public.volunteer_data_id_seq'::regclass);


--
-- Data for Name: benefits; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.benefits (id, name) FROM stdin;
\.


--
-- Data for Name: experience_level; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.experience_level (id, definition, name, value) FROM stdin;
\.


--
-- Data for Name: institution_contact_people; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.institution_contact_people (id, contact_email, first_name, last_name, phone_number) FROM stdin;
\.


--
-- Data for Name: institutions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.institutions (id, description, headquarters, is_active, krs_number, localization, name, path_to_image, registration_token, institution_contact_person_id) FROM stdin;
\.


--
-- Data for Name: interest_categories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.interest_categories (id, description, name) FROM stdin;
\.


--
-- Data for Name: offer_benefits; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.offer_benefits (offer_id, benefit_id) FROM stdin;
\.


--
-- Data for Name: offer_categories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.offer_categories (offer_id, interest_category_id) FROM stdin;
\.


--
-- Data for Name: offer_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.offer_types (id, name) FROM stdin;
\.


--
-- Data for Name: offers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.offers (id, description, duration, end_date, expiration_date, is_experience_required, is_insurance_needed, is_poznan_only, offer_interval, place, start_date, title, week_days, contact_person_id, institution_id, minimum_experience_id, offer_type_id) FROM stdin;
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles (id, name) FROM stdin;
\.


--
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_roles (user_id, role_id) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, contact_email_address, first_name, hashed_password, is_verified, last_name, phone_number, institution_id, volunteer_data_id) FROM stdin;
\.


--
-- Data for Name: volunteer_data; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.volunteer_data (id, domain_email_address, participation_motivation, volunteer_experience_id) FROM stdin;
\.


--
-- Data for Name: volunteer_interests; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.volunteer_interests (volunteer_data_id, interest_category_id) FROM stdin;
\.


--
-- Name: benefits_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.benefits_id_seq', 1, false);


--
-- Name: experience_level_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.experience_level_id_seq', 1, false);


--
-- Name: institution_contact_people_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.institution_contact_people_id_seq', 1, false);


--
-- Name: institutions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.institutions_id_seq', 1, false);


--
-- Name: interest_categories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.interest_categories_id_seq', 1, false);


--
-- Name: offer_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.offer_types_id_seq', 1, false);


--
-- Name: offers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.offers_id_seq', 1, false);


--
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.roles_id_seq', 1, false);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 1, false);


--
-- Name: volunteer_data_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.volunteer_data_id_seq', 1, false);


--
-- Name: benefits benefits_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefits
    ADD CONSTRAINT benefits_pkey PRIMARY KEY (id);


--
-- Name: experience_level experience_level_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.experience_level
    ADD CONSTRAINT experience_level_pkey PRIMARY KEY (id);


--
-- Name: institution_contact_people institution_contact_people_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institution_contact_people
    ADD CONSTRAINT institution_contact_people_pkey PRIMARY KEY (id);


--
-- Name: institutions institutions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institutions
    ADD CONSTRAINT institutions_pkey PRIMARY KEY (id);


--
-- Name: interest_categories interest_categories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.interest_categories
    ADD CONSTRAINT interest_categories_pkey PRIMARY KEY (id);


--
-- Name: offer_types offer_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offer_types
    ADD CONSTRAINT offer_types_pkey PRIMARY KEY (id);


--
-- Name: offers offers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT offers_pkey PRIMARY KEY (id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: volunteer_data volunteer_data_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.volunteer_data
    ADD CONSTRAINT volunteer_data_pkey PRIMARY KEY (id);


--
-- Name: users fk2qqjpih9isqcs22710v8lef9w; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fk2qqjpih9isqcs22710v8lef9w FOREIGN KEY (institution_id) REFERENCES public.institutions(id);


--
-- Name: volunteer_data fk39m7559p15633t20r0c9t3k3a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.volunteer_data
    ADD CONSTRAINT fk39m7559p15633t20r0c9t3k3a FOREIGN KEY (volunteer_experience_id) REFERENCES public.experience_level(id);


--
-- Name: offers fk58i7sd6tw1cwh000gux0o585a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT fk58i7sd6tw1cwh000gux0o585a FOREIGN KEY (contact_person_id) REFERENCES public.users(id);


--
-- Name: offers fk5si9fb7efgpdsmh7bi124tmv1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT fk5si9fb7efgpdsmh7bi124tmv1 FOREIGN KEY (minimum_experience_id) REFERENCES public.experience_level(id);


--
-- Name: institutions fk8x6hngdj3ixk0wkglqtq8dlvu; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institutions
    ADD CONSTRAINT fk8x6hngdj3ixk0wkglqtq8dlvu FOREIGN KEY (institution_contact_person_id) REFERENCES public.institution_contact_people(id);


--
-- Name: offer_categories fkca917sbxgtkmb2q3e45667h2f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offer_categories
    ADD CONSTRAINT fkca917sbxgtkmb2q3e45667h2f FOREIGN KEY (interest_category_id) REFERENCES public.interest_categories(id);


--
-- Name: offer_benefits fkeftql95n91khbriekov9fc0r1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offer_benefits
    ADD CONSTRAINT fkeftql95n91khbriekov9fc0r1 FOREIGN KEY (offer_id) REFERENCES public.offers(id);


--
-- Name: volunteer_interests fkfeet6spullr04l5b70iwmwi4f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.volunteer_interests
    ADD CONSTRAINT fkfeet6spullr04l5b70iwmwi4f FOREIGN KEY (volunteer_data_id) REFERENCES public.volunteer_data(id);


--
-- Name: offers fkgc4vhuooe1nxj7aksdydgo03o; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT fkgc4vhuooe1nxj7aksdydgo03o FOREIGN KEY (institution_id) REFERENCES public.institutions(id);


--
-- Name: user_roles fkh8ciramu9cc9q3qcqiv4ue8a6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- Name: user_roles fkhfh9dx7w3ubf1co1vdev94g3f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: volunteer_interests fkiiv7bb7s0rm07pwpo5iq1jm31; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.volunteer_interests
    ADD CONSTRAINT fkiiv7bb7s0rm07pwpo5iq1jm31 FOREIGN KEY (interest_category_id) REFERENCES public.interest_categories(id);


--
-- Name: users fkiwfpd2gu5i6q4c69531xfx5br; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkiwfpd2gu5i6q4c69531xfx5br FOREIGN KEY (volunteer_data_id) REFERENCES public.volunteer_data(id);


--
-- Name: offer_benefits fkjfpoqpimbuhch3gjbvsnmpsml; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offer_benefits
    ADD CONSTRAINT fkjfpoqpimbuhch3gjbvsnmpsml FOREIGN KEY (benefit_id) REFERENCES public.benefits(id);


--
-- Name: offers fknbx2yjxkl8bjt9o2mn8s2r1jd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT fknbx2yjxkl8bjt9o2mn8s2r1jd FOREIGN KEY (offer_type_id) REFERENCES public.offer_types(id);


--
-- Name: offer_categories fkqvn9b3fmdudbojrko4ctw32wt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offer_categories
    ADD CONSTRAINT fkqvn9b3fmdudbojrko4ctw32wt FOREIGN KEY (offer_id) REFERENCES public.offers(id);


--
-- PostgreSQL database dump complete
--

