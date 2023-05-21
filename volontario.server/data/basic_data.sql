INSERT INTO public.roles
VALUES  (1, 'Pracownik Instytucji'),
        (2, 'Administrator Instytucji'),
        (3, 'Wolontariusz'),
        (4, 'Administrator'),
        (5, 'Moderator');

SELECT setval('roles_id_seq', 6, true);

INSERT INTO public.application_states
VALUES  (1, 'Odrzucona'),
        (2, 'Oczekująca'),
        (3, 'Zaakceptowana');

SELECT setval('application_states_id_seq', 4, true);

INSERT INTO public.offer_states
VALUES  (1, 'Nowe'),
        (2, 'W trakcie weryfikacji'),
        (3, 'Odrzucone'),
        (4, 'Opublikowane'),
        (5, 'Zamknięte');

SELECT setval('offer_states_id_seq', 6, true);

INSERT INTO public.experience_level
VALUES  (1, 'Początkujący', 'Początkujący', 10),
        (2, 'Średniozaawansowany', 'Średniozaawansowany', 20),
        (3, 'Weteran', 'Weteran', 30);

SELECT setval('experience_level_id_seq', 4, true);

INSERT INTO public.interest_categories
VALUES  (1, 'Praca z osobami starszymi', 'Praca z osobami starszymi'),
        (2, 'Praca z dziećmi', 'Praca z dziećmi'),
        (3, 'Praca z osobami wykluczonymi', 'Praca z osobami wykluczonymi'),
        (4, 'Praca z osobami z niepełnosprawnościami ruchowymi i fizycznymi', 'Praca z osobami z niepełnosprawnościami ruchowymi i fizycznymi'),
        (5, 'Praca z osobami z niepełnosprawnościami intelektualnymi', 'Praca z osobami z niepełnosprawnościami intelektualnymi'),
        (6, 'Praca ze zwierzętami', 'Praca ze zwierzętami'),
        (7, 'Pomoc organizacyjna przy wydarzeniach', 'Pomoc organizacyjna przy wydarzeniach'),
        (8, 'Dzielenie się wiedzą i umiejętnościami', 'Dzielenie się wiedzą i umiejętnościami')
        (9, 'Prace porządkowe', 'Prace porządkowe');

SELECT setval('interest_categories_id_seq', 9, true);

INSERT INTO public.offer_types
VALUES (1, 'Jednorazowy'),
       (2, 'Cykliczny'),
       (3, 'Ciągły');

SELECT setval('offer_types_id_seq', 4, true);

INSERT INTO public.benefits
VALUES (1, 'Nocleg'),
       (2, 'Transport'),
       (3, 'Pokrycie kosztów dojazdu'),
       (4, 'Posiłki');

SELECT setval('benefits_id_seq', 5, true);