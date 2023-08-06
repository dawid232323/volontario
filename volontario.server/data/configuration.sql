CREATE TABLE public.configuration_entries (
                                     id bigint NOT NULL,
                                     key character varying(150),
                                     value character varying(10000),
                                     description character varying(300)
);

ALTER TABLE public.configuration_entries OWNER TO postgres;

CREATE SEQUENCE public.configuration_entries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.configuration_entries_id_seq OWNER TO postgres;
ALTER SEQUENCE public.configuration_entries_id_seq OWNED BY public.configuration_entries.id;


INSERT INTO public.configuration_entries
VALUES  (1, 'VOL.OFFER.EXPIRATION_BUFFER', '168', 'Defines offer expiration buffer in hours. When system ' ||
          'checks offers for expiring, it checks whether difference between actual time and offer end time is ' ||
          'smaller than buffer. If so, it sends email to offer contact person about offer expiring soon.' );

SELECT setval('configuration_entries_id_seq', 2, true);