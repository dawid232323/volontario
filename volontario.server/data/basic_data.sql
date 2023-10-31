INSERT INTO public.roles
VALUES  (1, 'Pracownik/czka Instytucji'),
        (2, 'Administrator(ka) Instytucji'),
        (3, 'Wolontariusz(ka)'),
        (4, 'Administrator(ka)'),
        (5, 'Moderator(ka)');

SELECT setval('roles_id_seq', 6, true);

INSERT INTO public.application_states
VALUES  (1, 'Odrzucona'),
        (2, 'Oczekująca'),
        (3, 'W trakcie rekrutacji'),
        (4, 'Lista rezerwowa');

SELECT setval('application_states_id_seq', 5, true);

INSERT INTO public.offer_states
VALUES  (1, 'Nowe'),
        (2, 'W trakcie weryfikacji'),
        (3, 'Odrzucone'),
        (4, 'Opublikowane'),
        (5, 'Zamknięte'),
        (6, 'Wygasłe');

SELECT setval('offer_states_id_seq', 7, true);

INSERT INTO public.experience_level
VALUES  (1, 'Początkujący', 'Początkujący', 10, true),
        (2, 'Średniozaawansowany', 'Średniozaawansowany', 20, true),
        (3, 'Weteran', 'Weteran', 30, true);

SELECT setval('experience_level_id_seq', 4, true);

INSERT INTO public.interest_categories
VALUES  (1, 'Praca z osobami starszymi', 'Praca z osobami starszymi', true),
        (2, 'Praca z dziećmi', 'Praca z dziećmi', true),
        (3, 'Praca z osobami wykluczonymi', 'Praca z osobami wykluczonymi', true),
        (4, 'Praca z osobami z niepełnosprawnościami ruchowymi i fizycznymi', 'Praca z osobami z niepełnosprawnościami ruchowymi i fizycznymi', true),
        (5, 'Praca z osobami z niepełnosprawnościami intelektualnymi', 'Praca z osobami z niepełnosprawnościami intelektualnymi', true),
        (6, 'Praca ze zwierzętami', 'Praca ze zwierzętami', true),
        (7, 'Pomoc organizacyjna przy wydarzeniach', 'Pomoc organizacyjna przy wydarzeniach', true),
        (8, 'Dzielenie się wiedzą i umiejętnościami', 'Dzielenie się wiedzą i umiejętnościami', true),
        (9, 'Prace porządkowe', 'Prace porządkowe', true);

SELECT setval('interest_categories_id_seq', 9, true);

INSERT INTO public.offer_types
VALUES (1, 'Jednorazowy'),
       (2, 'Cykliczny'),
       (3, 'Ciągły');

SELECT setval('offer_types_id_seq', 4, true);

INSERT INTO public.benefits
VALUES (1, 'Nocleg', false),
       (2, 'Transport', false),
       (3, 'Pokrycie kosztów dojazdu', false),
       (4, 'Posiłki', false),
       (5, 'Certyfikat ukończenia', true),
       (6, 'Dodatkowe szkolenia', true);

SELECT setval('benefits_id_seq', 5, true);

INSERT INTO public.voluntary_presence_states(id, state)
VALUES (1, 'Nierozstrzygnięta'),
       (2, 'Potwierdzona'),
       (3, 'Zaprzeczona');

SELECT setval('voluntary_presence_states_id_seq', 4, true);