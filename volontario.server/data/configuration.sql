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
VALUES  (1, 'VOL.OFFER.EXPIRATION_BUFFER', '168', 'Defines Offer expiration buffer in hours. When system ' ||
          'checks Offers for expiring, it checks whether difference between actual time and Offer end time is ' ||
          'smaller than buffer. If so, it sends email to offer contact person about Offer expiring soon.' ),
        (2, 'VOL.APPLICATION.ONE_TIME_CONFIRMATION_BUFFER', '336', 'Defines confirmation buffer for Applications of One Time Offers in hours. Only after' ||
        ' time defined in this key passes (since date of Offer end), Volunteer presence can be confirmed or negated.'),
        (3, 'VOL.APPLICATION.MULTI_TIME_CONFIRMATION_BUFFER', '336', 'Defines confirmation buffer for Applications of Regular/Cyclic Offers in hours. Only after' ||
        ' time defined in this key passes (since date of Offer start), Volunteer presence can be confirmed or negated.'),
        (4, 'VOL.VOLUNTARY_PRESENCE.POSTPONE_CONFIRMATION_TIME', '168', 'Defines time by which Volunteer will postpone his Offer confirmation/negation'),
        (5, 'VOL.VOLUNTARY_PRESENCE.MAX_REMINDER_COUNT', '3', 'Defines how many times can Volunteer be reminded about confirmation of presence on given Offer'),
        (6, 'VOL.VOLUNTARY_PRESENCE.CONFIRMATION_TIME_WINDOW_LENGTH', '168', 'Defines how much time since last reminder does Volunteer/Institution have to confirm/deny presence'),
        (7, 'VOL.VOLUNTARY_PRESENCE.DECISION_CHANGE_BUFFER', '168', 'Defines how much time since making decision does Volunteer/Offer Contact Person have to change decision on presence.');

SELECT setval('configuration_entries_id_seq', 8, true);