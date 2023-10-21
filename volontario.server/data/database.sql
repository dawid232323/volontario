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

CREATE TABLE public.application_states (
                                           id bigint NOT NULL,
                                           name character varying(255)
);

ALTER TABLE public.application_states OWNER TO postgres;

CREATE SEQUENCE public.application_states_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.application_states_id_seq OWNER TO postgres;

ALTER SEQUENCE public.application_states_id_seq OWNED BY public.application_states.id;

CREATE TABLE public.applications (
                                     id bigint NOT NULL,
                                     is_starred boolean,
                                     participation_motivation character varying(255),
                                     offer_id bigint,
                                     state_id bigint,
                                     volunteer_id bigint
);

ALTER TABLE public.applications OWNER TO postgres;

CREATE SEQUENCE public.applications_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.applications_id_seq OWNER TO postgres;

ALTER SEQUENCE public.applications_id_seq OWNED BY public.applications.id;

CREATE TABLE public.benefits (
                                 id bigint NOT NULL,
                                 name character varying(255),
                                 is_used boolean

);

ALTER TABLE public.benefits OWNER TO postgres;

CREATE SEQUENCE public.benefits_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.benefits_id_seq OWNER TO postgres;

ALTER SEQUENCE public.benefits_id_seq OWNED BY public.benefits.id;

CREATE TABLE public.experience_level (
                                         id bigint NOT NULL,
                                         definition character varying(500),
                                         name character varying(255),
                                         value bigint,
                                         is_used boolean
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

CREATE TABLE public.institution_contact_people (
                                                   id bigint NOT NULL,
                                                   contact_email character varying(255),
                                                   first_name character varying(255),
                                                   last_name character varying(255),
                                                   phone_number character varying(255)
);

ALTER TABLE public.institution_contact_people OWNER TO postgres;

CREATE SEQUENCE public.institution_contact_people_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.institution_contact_people_id_seq OWNER TO postgres;

ALTER SEQUENCE public.institution_contact_people_id_seq OWNED BY public.institution_contact_people.id;

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
                                            name character varying(255),
                                            is_used boolean
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

CREATE TABLE public.offer_benefits (
                                       offer_id bigint NOT NULL,
                                       benefit_id bigint NOT NULL
);

ALTER TABLE public.offer_benefits OWNER TO postgres;

CREATE TABLE public.offer_categories (
                                         offer_id bigint NOT NULL,
                                         interest_category_id bigint NOT NULL
);

ALTER TABLE public.offer_categories OWNER TO postgres;

CREATE TABLE public.offer_states (
                                     id bigint NOT NULL,
                                     state character varying(255)
);

ALTER TABLE public.offer_states OWNER TO postgres;

CREATE SEQUENCE public.offer_states_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.offer_states_id_seq OWNER TO postgres;

ALTER SEQUENCE public.offer_states_id_seq OWNED BY public.offer_states.id;

CREATE TABLE public.offer_types (
                                    id bigint NOT NULL,
                                    name character varying(255)
);

ALTER TABLE public.offer_types OWNER TO postgres;

CREATE SEQUENCE public.offer_types_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.offer_types_id_seq OWNER TO postgres;

ALTER SEQUENCE public.offer_types_id_seq OWNED BY public.offer_types.id;

CREATE TABLE public.offers (
                               id bigint NOT NULL,
                               description character varying(3000),
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

CREATE SEQUENCE public.offers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.offers_id_seq OWNER TO postgres;

ALTER SEQUENCE public.offers_id_seq OWNED BY public.offers.id;

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
                              first_name character varying(255),
                              hashed_password character varying(255),
                              is_verified boolean,
                              last_name character varying(255),
                              phone_number character varying(255),
                              institution_id bigint,
                              volunteer_data_id bigint,
                              creation_date timestamp(6) with time zone
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
                                       domain_email_address character varying(255),
                                       participation_motivation character varying(1500),
                                       volunteer_experience_id bigint,
                                       field_of_study character varying(255),
                                       interests character varying(1500),
                                       experience_description character varying(1500)
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

ALTER TABLE ONLY public.application_states ALTER COLUMN id SET DEFAULT nextval('public.application_states_id_seq'::regclass);

ALTER TABLE ONLY public.applications ALTER COLUMN id SET DEFAULT nextval('public.applications_id_seq'::regclass);

ALTER TABLE ONLY public.benefits ALTER COLUMN id SET DEFAULT nextval('public.benefits_id_seq'::regclass);

ALTER TABLE ONLY public.experience_level ALTER COLUMN id SET DEFAULT nextval('public.experience_level_id_seq'::regclass);

ALTER TABLE ONLY public.institution_contact_people ALTER COLUMN id SET DEFAULT nextval('public.institution_contact_people_id_seq'::regclass);

ALTER TABLE ONLY public.institutions ALTER COLUMN id SET DEFAULT nextval('public.institutions_id_seq'::regclass);

ALTER TABLE ONLY public.interest_categories ALTER COLUMN id SET DEFAULT nextval('public.interest_categories_id_seq'::regclass);

ALTER TABLE ONLY public.offer_states ALTER COLUMN id SET DEFAULT nextval('public.offer_states_id_seq'::regclass);

ALTER TABLE ONLY public.offer_types ALTER COLUMN id SET DEFAULT nextval('public.offer_types_id_seq'::regclass);

ALTER TABLE ONLY public.offers ALTER COLUMN id SET DEFAULT nextval('public.offers_id_seq'::regclass);

ALTER TABLE ONLY public.roles ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq'::regclass);

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);

ALTER TABLE ONLY public.volunteer_data ALTER COLUMN id SET DEFAULT nextval('public.volunteer_data_id_seq'::regclass);


ALTER TABLE ONLY public.application_states
    ADD CONSTRAINT application_states_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.applications
    ADD CONSTRAINT applications_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.benefits
    ADD CONSTRAINT benefits_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.experience_level
    ADD CONSTRAINT experience_level_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.institution_contact_people
    ADD CONSTRAINT institution_contact_people_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.institutions
    ADD CONSTRAINT institutions_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.interest_categories
    ADD CONSTRAINT interest_categories_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.offer_states
    ADD CONSTRAINT offer_states_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.offer_types
    ADD CONSTRAINT offer_types_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT offers_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.volunteer_data
    ADD CONSTRAINT volunteer_data_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.applications
    ADD CONSTRAINT applications_offer_id_fkey FOREIGN KEY (offer_id) REFERENCES public.offers(id);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_institution_id_fkey FOREIGN KEY (institution_id) REFERENCES public.institutions(id);

ALTER TABLE ONLY public.volunteer_data
    ADD CONSTRAINT volunteer_data_volunteer_experience_id_fkey FOREIGN KEY (volunteer_experience_id) REFERENCES public.experience_level(id);

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT offers_assigned_moderator_id_fkey FOREIGN KEY (assigned_moderator_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT offers_contact_person_id_fkey FOREIGN KEY (contact_person_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT offers_minimum_experience_id_fkey FOREIGN KEY (minimum_experience_id) REFERENCES public.experience_level(id);

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT offers_offer_state_id_fkey FOREIGN KEY (offer_state_id) REFERENCES public.offer_states(id);

ALTER TABLE ONLY public.institutions
    ADD CONSTRAINT institutions_institution_contact_person_id_fkey FOREIGN KEY (institution_contact_person_id) REFERENCES public.institution_contact_people(id) ON DELETE CASCADE;

ALTER TABLE ONLY public.offer_categories
    ADD CONSTRAINT offer_categories_interest_category_id_fkey FOREIGN KEY (interest_category_id) REFERENCES public.interest_categories(id);

ALTER TABLE ONLY public.offer_benefits
    ADD CONSTRAINT offer_benefits_offer_id_fkey FOREIGN KEY (offer_id) REFERENCES public.offers(id);

ALTER TABLE ONLY public.volunteer_interests
    ADD CONSTRAINT volunteer_interests_volunteer_data_id_fkey FOREIGN KEY (volunteer_data_id) REFERENCES public.volunteer_data(id);

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT offers_institution_id_fkey FOREIGN KEY (institution_id) REFERENCES public.institutions(id);

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.roles(id);

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.volunteer_interests
    ADD CONSTRAINT volunteer_interests_interest_category_id_fkey FOREIGN KEY (interest_category_id) REFERENCES public.interest_categories(id) ON DELETE CASCADE;

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_volunteer_data_id_fkey FOREIGN KEY (volunteer_data_id) REFERENCES public.volunteer_data(id) ON DELETE CASCADE;

ALTER TABLE ONLY public.offer_benefits
    ADD CONSTRAINT offer_benefits_benefit_id_fkey FOREIGN KEY (benefit_id) REFERENCES public.benefits(id);

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT offers_offer_type_id_fkey FOREIGN KEY (offer_type_id) REFERENCES public.offer_types(id);

ALTER TABLE ONLY public.applications
    ADD CONSTRAINT applications_volunteer_id_fkey FOREIGN KEY (volunteer_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.offer_categories
    ADD CONSTRAINT offer_categories_offer_id_fkey FOREIGN KEY (offer_id) REFERENCES public.offers(id);

ALTER TABLE ONLY public.applications
    ADD CONSTRAINT applications_state_id_fkey FOREIGN KEY (state_id) REFERENCES public.application_states(id);
