INSERT INTO public.roles
VALUES  (1, 'Pracownik Instytucji'),
        (2, 'Administrator Instytucji'),
        (3, 'Wolontariusz'),
        (4, 'Administrator'),
        (5, 'Moderator');

INSERT INTO public.experience_level
VALUES  (1, 'Początkujący', 'Początkujący', 10),
        (2, 'Średniozaawansowany', 'Średniozaawansowany', 20),
        (3, 'Weteran', 'Weteran', 30);

INSERT INTO public.interest_categories
VALUES  (1, 'Praca z osobami starszymi', 'Praca z osobami starszymi'),
        (2, 'Praca z dziećmi', 'Praca z dziećmi'),
        (3, 'Praca z osobami wykluczonymi', 'Praca z osobami wykluczonymi'),
        (4, 'Praca z osobami z niepełnosprawnościami ruchowymi i fizycznymi', 'Praca z osobami z niepełnosprawnościami ruchowymi i fizycznymi'),
        (5, 'Praca z osobami z niepełnosprawnościami intelektualnymi', 'Praca z osobami z niepełnosprawnościami intelektualnymi'),
        (6, 'Praca ze zwierzętami', 'Praca ze zwierzętami'),
        (7, 'Pomoc organizacyjna przy wydarzeniach', 'Pomoc organizacyjna przy wydarzeniach'),
        (8, 'Dzielenie się wiedzą i umiejętnościami', 'Dzielenie się wiedzą i umiejętnościami');

INSERT INTO public.offer_types
VALUES (1, 'Jednorazowy'),
       (2, 'Cykliczny'),
       (3, 'Ciągły');
