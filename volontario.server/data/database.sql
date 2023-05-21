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
-- Name: application_states; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.application_states (
                                           id bigint NOT NULL,
                                           name character varying(255)
);


ALTER TABLE public.application_states OWNER TO postgres;

--
-- Name: application_states_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.application_states_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.application_states_id_seq OWNER TO postgres;

--
-- Name: application_states_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.application_states_id_seq OWNED BY public.application_states.id;


--
-- Name: applications; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.applications (
                                     id bigint NOT NULL,
                                     is_starred boolean,
                                     participation_motivation character varying(255),
                                     offer_id bigint,
                                     state_id bigint,
                                     volunteer_id bigint
);


ALTER TABLE public.applications OWNER TO postgres;

--
-- Name: applications_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.applications_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.applications_id_seq OWNER TO postgres;

--
-- Name: applications_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.applications_id_seq OWNED BY public.applications.id;


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
-- Name: offer_states; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.offer_states (
                                     id bigint NOT NULL,
                                     state character varying(255)
);


ALTER TABLE public.offer_states OWNER TO postgres;

--
-- Name: offer_states_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.offer_states_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.offer_states_id_seq OWNER TO postgres;

--
-- Name: offer_states_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.offer_states_id_seq OWNED BY public.offer_states.id;


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
                               end_date timestamp(6) with time zone,
                               expiration_date timestamp(6) with time zone,
                               is_experience_required boolean,
                               is_hidden boolean,
                               is_poznan_only boolean,
                               periodic_description character varying(255),
                               place character varying(255),
                               start_date timestamp(6) with time zone,
                               title character varying(100),
                               assigned_moderator_id bigint,
                               contact_person_id bigint,
                               institution_id bigint,
                               minimum_experience_id bigint,
                               offer_state_id bigint,
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
-- Name: application_states id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.application_states ALTER COLUMN id SET DEFAULT nextval('public.application_states_id_seq'::regclass);


--
-- Name: applications id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applications ALTER COLUMN id SET DEFAULT nextval('public.applications_id_seq'::regclass);


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
-- Name: offer_states id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offer_states ALTER COLUMN id SET DEFAULT nextval('public.offer_states_id_seq'::regclass);


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
-- Data for Name: application_states; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.application_states (id, name) FROM stdin;
1	Odrzucona
2	Oczekująca
3	Zaakceptowana
\.


--
-- Data for Name: applications; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.applications (id, is_starred, participation_motivation, offer_id, state_id, volunteer_id) FROM stdin;
\.


--
-- Data for Name: benefits; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.benefits (id, name) FROM stdin;
1	Nocleg
2	Transport
3	Pokrycie kosztów dojazdu
4	Posiłki
\.


--
-- Data for Name: experience_level; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.experience_level (id, definition, name, value) FROM stdin;
1	Początkujący	Początkujący	10
2	Średniozaawansowany	Średniozaawansowany	20
3	Weteran	Weteran	30
\.


--
-- Data for Name: institution_contact_people; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.institution_contact_people (id, contact_email, first_name, last_name, phone_number) FROM stdin;
1	januszex@januszex.pl	Jan	Uszex	123456789
2	Ola@wolontariat.pl	Ola	Wolontariacka	123456788
3	ktoswazny@amu.edu.pl	Ktos	Wazny	123456787
4	typ@niema.pl	Zamknal	Instytucje	123456786
5	pan@warszawka.pl	Pan	Warszawski	123456785
\.


--
-- Data for Name: institutions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.institutions (id, description, headquarters, is_active, krs_number, localization, name, path_to_image, registration_token, institution_contact_person_id) FROM stdin;
1	Januszex	Towarowa	t	1111111111	Poznań	Januszex	\N	\N	1
2	Wolontariaty	Wolontariacka	t	2222222222	Poznań	Wolontariat	\N	\N	2
3	UAM	Wieniawskiego 1	t	3333333333	Poznań	UAM	\N	\N	3
4	Zamknięte	Zamknięta	f	444444444	Poznań	Zamknięte	\N	\N	4
5	Niepoznań	Warszawska	t	5555555555	Warszawa	Niepoznań	\N	\N	5
\.


--
-- Data for Name: interest_categories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.interest_categories (id, description, name) FROM stdin;
1	Praca z osobami starszymi	Praca z osobami starszymi
2	Praca z dziećmi	Praca z dziećmi
3	Praca z osobami wykluczonymi	Praca z osobami wykluczonymi
4	Praca z osobami z niepełnosprawnościami ruchowymi i fizycznymi	Praca z osobami z niepełnosprawnościami ruchowymi i fizycznymi
5	Praca z osobami z niepełnosprawnościami intelektualnymi	Praca z osobami z niepełnosprawnościami intelektualnymi
6	Praca ze zwierzętami	Praca ze zwierzętami
7	Pomoc organizacyjna przy wydarzeniach	Pomoc organizacyjna przy wydarzeniach
8	Dzielenie się wiedzą i umiejętnościami	Dzielenie się wiedzą i umiejętnościami
\.


--
-- Data for Name: offer_benefits; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.offer_benefits (offer_id, benefit_id) FROM stdin;
1	1
1	2
2	1
3	1
\.


--
-- Data for Name: offer_categories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.offer_categories (offer_id, interest_category_id) FROM stdin;
1	1
1	2
2	3
3	4
4	5
6	1
6	2
6	3
\.


--
-- Data for Name: offer_states; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.offer_states (id, state) FROM stdin;
1	Nowe
2	W trakcie weryfikacji
3	Odrzucone
4	Opublikowane
5	Zamknięte
\.


--
-- Data for Name: offer_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.offer_types (id, name) FROM stdin;
1	Jednorazowy
2	Cykliczny
3	Ciągły
\.


--
-- Data for Name: offers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.offers (id, description, end_date, expiration_date, is_experience_required, is_hidden, is_poznan_only, periodic_description, place, start_date, title, assigned_moderator_id, contact_person_id, institution_id, minimum_experience_id, offer_state_id, offer_type_id) FROM stdin;
1	Pomoc w domu dla starców i sierocińcu	2023-05-25 00:00:00+00	2023-05-15 00:00:00+00	t	f	t	\N	Poznań	2023-05-20 00:00:00+00	Pomoc w domu dla starców i sierocińcu	\N	1	1	1	1	1
2	Zbiórka na biednych	2023-07-20 00:00:00+00	2023-05-15 00:00:00+00	t	f	f	Co tydzień o 17 w piątek	Gniezno	2023-05-20 00:00:00+00	Zbiórka na biednych	\N	2	2	1	1	2
3	Pomoc dla niepełnosprawnej pani	2023-06-20 22:00:00+00	2023-06-03 22:00:00+00	f	f	t	\N	Poznań	2023-06-17 22:00:00+00	Pomoc dla niepełnosprawnej pani	\N	2	2	\N	1	1
4	Wolontariat w szkole specjalnej	\N	2023-05-31 22:00:00+00	t	t	t	Każdego tygodnia od 8 do 16 w dni robocze	Poznań	2023-05-28 22:00:00+00	Wolontariat w szkole specjalnej	8	2	2	2	2	3
6	Krótki testowy opis dla pierwszego ogłoszenia	2023-06-20 00:00:00+00	2023-05-15 00:00:00+00	t	f	t	\N	Poznań	2023-05-20 00:00:00+00	Pierwsze ogłoszenie	\N	2	2	1	1	2
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles (id, name) FROM stdin;
1	Pracownik Instytucji
2	Administrator Instytucji
3	Wolontariusz
4	Administrator
5	Moderator
\.


--
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_roles (user_id, role_id) FROM stdin;
1	2
2	2
3	2
4	2
5	2
6	3
7	3
8	5
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, contact_email_address, first_name, hashed_password, is_verified, last_name, phone_number, institution_id, volunteer_data_id) FROM stdin;
1	januszex@januszex.pl	Jan	$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm	t	Uszex	123456789	1	\N
2	Ola@wolontariat.pl	Ola	$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm	t	Wolontariacka	123456788	2	\N
3	ktoswazny@amu.edu.pl	Ktos	$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm	t	Wazny	123456787	3	\N
4	typ@niema.pl	Zamknal	$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm	t	Instytucje	123456786	4	\N
5	pan@warszawka.pl	Pan	$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm	t	Warszawski	123456785	5	\N
6	janwolontariusz@gmail.pl	Jan	$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm	t	Kowalski	123456784	\N	1
7	adamwolontariusz@gmail.pl	Adam	$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm	t	Nowak	123456783	\N	2
8	MODERATOR@gmail.pl	Moderator	$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm	t	Nowak	333456783	\N	\N
\.


--
-- Data for Name: volunteer_data; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.volunteer_data (id, domain_email_address, participation_motivation, volunteer_experience_id) FROM stdin;
1	janwolontariusz@st.amu.edu.pl	Lubię ludzi	1
2	adamwolontariusz@st.amu.edu.pl	Nudzi mi się	2
\.


--
-- Data for Name: volunteer_interests; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.volunteer_interests (volunteer_data_id, interest_category_id) FROM stdin;
1	1
1	2
1	3
2	4
\.


--
-- Name: application_states_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.application_states_id_seq', 4, true);


--
-- Name: applications_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.applications_id_seq', 1, false);


--
-- Name: benefits_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.benefits_id_seq', 5, true);


--
-- Name: experience_level_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.experience_level_id_seq', 4, true);


--
-- Name: institution_contact_people_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.institution_contact_people_id_seq', 6, true);


--
-- Name: institutions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.institutions_id_seq', 6, true);


--
-- Name: interest_categories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.interest_categories_id_seq', 9, true);


--
-- Name: offer_states_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.offer_states_id_seq', 6, true);


--
-- Name: offer_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.offer_types_id_seq', 4, true);


--
-- Name: offers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.offers_id_seq', 6, true);


--
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.roles_id_seq', 6, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 9, true);


--
-- Name: volunteer_data_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.volunteer_data_id_seq', 3, true);


--
-- Name: application_states application_states_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.application_states
    ADD CONSTRAINT application_states_pkey PRIMARY KEY (id);


--
-- Name: applications applications_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applications
    ADD CONSTRAINT applications_pkey PRIMARY KEY (id);


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
-- Name: offer_states offer_states_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offer_states
    ADD CONSTRAINT offer_states_pkey PRIMARY KEY (id);


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
-- Name: applications fk1jq84e96g7lohehw7jb6kesoa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applications
    ADD CONSTRAINT fk1jq84e96g7lohehw7jb6kesoa FOREIGN KEY (offer_id) REFERENCES public.offers(id);


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
-- Name: offers fk3m77ddqpy56ojj3h5acup2s3n; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT fk3m77ddqpy56ojj3h5acup2s3n FOREIGN KEY (assigned_moderator_id) REFERENCES public.users(id);


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
-- Name: offers fk8ebv0rf0cfx12py1n9u8816ga; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT fk8ebv0rf0cfx12py1n9u8816ga FOREIGN KEY (offer_state_id) REFERENCES public.offer_states(id);


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
-- Name: applications fkq9fv25v2qoywmwab9aip27cja; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applications
    ADD CONSTRAINT fkq9fv25v2qoywmwab9aip27cja FOREIGN KEY (volunteer_id) REFERENCES public.users(id);


--
-- Name: offer_categories fkqvn9b3fmdudbojrko4ctw32wt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offer_categories
    ADD CONSTRAINT fkqvn9b3fmdudbojrko4ctw32wt FOREIGN KEY (offer_id) REFERENCES public.offers(id);


--
-- Name: applications fkrleueg857d7gt1bxtox01xa7r; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.applications
    ADD CONSTRAINT fkrleueg857d7gt1bxtox01xa7r FOREIGN KEY (state_id) REFERENCES public.application_states(id);


--
-- PostgreSQL database dump complete
--

