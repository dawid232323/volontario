INSERT INTO public.institution_contact_people
VALUES (1, 'januszex@januszex.pl', 'Jan', 'Uszex', '123456789'),
       (2, 'Ola@wolontariat.pl', 'Ola', 'Wolontariacka', '123456788'),
       (3, 'ktoswazny@amu.edu.pl', 'Ktos', 'Wazny', '123456787'),
       (4, 'typ@niema.pl', 'Zamknal', 'Instytucje', '123456786'),
       (5, 'pan@warszawka.pl', 'Pan', 'Warszawski', '123456785');

SELECT setval('institution_contact_people_id_seq', 6, true);

INSERT INTO public.institutions
VALUES  (1, 'Januszex', 'Towarowa', true, '1111111111', 'Poznań', 'Januszex', null, null, 1),
        (2, 'Wolontariaty', 'Wolontariacka', true, '2222222222', 'Poznań', 'Wolontariat', null, null, 2),
        (3, 'UAM', 'Wieniawskiego 1', true, '3333333333', 'Poznań', 'UAM', null, null, 3),
        (4, 'Zamknięte', 'Zamknięta', false, '444444444', 'Poznań', 'Zamknięte', null, null, 4),
        (5, 'Niepoznań', 'Warszawska', true, '5555555555', 'Warszawa', 'Niepoznań', null, null, 5);

SELECT setval('institutions_id_seq', 6, true);

INSERT INTO public.volunteer_data
VALUES (1, 'janwolontariusz@st.amu.edu.pl', 'Lubię ludzi', 1, 'Kulturoznawstwo', 'Muzyka, podróże, seriale', 'Pomoc w organizacji wielkiego grilowania UAM'),
       (2, 'adamwolontariusz@st.amu.edu.pl', 'Nudzi mi się', 2, 'Socjologia', 'Podróże w różne ciekawe miejsca', null);

SELECT setval('volunteer_data_id_seq', 3, true);

--
-- Password for all is SecurePassword123_
--
INSERT INTO public.users
VALUES (1, 'januszex@januszex.pl', 'Jan', '$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm', true, 'Uszex', '123456789', 1, null, '1970-01-01 00:00:00+00'),
       (2, 'Ola@wolontariat.pl', 'Ola', '$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm', true, 'Wolontariacka', '123456788', 2, null, '1970-01-01 00:00:00+00'),
       (3, 'ktoswazny@amu.edu.pl', 'Ktos', '$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm', true, 'Wazny', '123456787', 3, null, '1970-01-01 00:00:00+00'),
       (4, 'typ@niema.pl', 'Zamknal', '$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm', true, 'Instytucje', '123456786', 4, null, '1970-01-01 00:00:00+00'),
       (5, 'pan@warszawka.pl', 'Pan', '$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm', true, 'Warszawski', '123456785', 5, null, '1970-01-01 00:00:00+00'),
       (6, 'janwolontariusz@gmail.pl', 'Jan', '$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm', true, 'Kowalski', '123456784', null, 1, '1970-01-01 00:00:00+00'),
       (7, 'adamwolontariusz@gmail.pl', 'Adam', '$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm', true, 'Nowak', '123456783', null, 2, '1970-01-01 00:00:00+00'),
       (8, 'MODERATOR@gmail.pl', 'Moderator', '$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm', true, 'Nowak', '333456783', 1, null, '1970-01-01 00:00:00+00'),
       (9, 'admin@admin.gmail.com', 'Administrator', '$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm', true, 'Admin', '333456783', null, null, '1970-01-01 00:00:00+00');

SELECT setval('users_id_seq', 10, true);

INSERT INTO public.offers(
    id, description, end_date, expiration_date, is_experience_required, is_poznan_only,
    place, start_date, title, contact_person_id, institution_id, minimum_experience_id,
    offer_type_id, offer_state_id, assigned_moderator_id, is_hidden, periodic_description)
VALUES (1, 'Pomagamy w jednym z domów spokojnej starości w Poznaniu. ' ||
           'Nasza pomoc opiera się głównie na wsparciu personelu sprzątającego. Często pełnimy też rolę pomocy kuchennej.' ||
           'Szukamy ciepłych, cierpliwych i wyrozumiałych osób. W zamian oferujemy szczery i piękny uśmiech naszych podopiecznych',
        '2023-05-25 00:00:00+00', '2023-05-25 00:00:00+00', true,
        true, 'Poznań', '2023-05-20 00:00:00+00', 'Pomoc w domu spokojnej starości', 1, 1, 1, 1, 1, NULL, false,
        NULL),
       (2, 'Potrzebujemy osoby, która pomoże naszej organizacji w przeprowadzaniu zbiórek datków dla osób potrzebujących.' ||
           'Pomagamy głównie osobom poszkodowanym podczas wojny w Ukrainie. Wolontariat polega na zbieraniu datków w ' ||
           'różnych miejscach Gniezna.',
            '2023-07-20 00:00:00+00', '2023-05-15 00:00:00+00',
        true, false, 'Gniezno', '2023-05-20 00:00:00+00', 'Pomoc w zbiórce dla potrzebujących', 2, 2, 1, 2, 1, NULL, false,
        'Zbieramy się co tydzień w poniedziałki i soboty o godzinie 11 w sedzibie naszej organizacji na ulicy Poznańskiej'),
       (3, 'Pomagamy jednej z podpiecznych naszej fundacji w wykonywaniu codziennych obowiązków. ' ||
           'Ze względu na trudności w poruszaniu się, pani Grażyna potrzebuje pomocy w czynnościach takich jak ' ||
           'gotowanie, sprzątanie, robienie zakupów spożywczych. Wolontariuszom jesteśmy w stanie zapewnić zwrot ' ||
           'kosztów zakupu biletów ZTM.',
        '2023-06-20 22:00:00+00', '2023-06-03 22:00:00+00', false,
        true, 'Poznań', '2023-06-17 22:00:00+00', 'Pomoc starszej Pani z niepełnosprawnością', 2, 2, NULL, 1, 1, NULL, false,
        'Pani Grażyna oczekuje naszej pomocy dwa razy w tygodniu, w 3 dniowych odstępach. Mieszka na ulicy Grunwaldzkiej.'),
       (4, 'Poszukujemy osoby lubiącej zabawę z dziećmi. Pomagamy w lokalnej szkole podstawowej, a konkretniej w świetlicy. ' ||
           'Wolontariat polega na animowaniu czasu dzieciom przebywającym w świetlicy po lekcjach. Na miejscu dostępne są róznego ' ||
           'rodzaju klocki, lalki, gry planszowe.',
        '2023-06-30 22:00:00+00', '2023-05-31 22:00:00+00', true,
        true, 'Poznań', '2023-05-28 22:00:00+00', 'Wolontariat w świetlicy szkoły podstawowej specjalnej', 2, 2, 2, 3, 2, 8, true,
        'Do wyboru 2 dni szkolne w ciągu tygodnia, w godzinach od 13 do 17');

SELECT setval('offers_id_seq', 5, true);

INSERT INTO public.offer_benefits
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (3, 1);

INSERT INTO public.offer_categories
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (3, 4),
       (4, 5);

INSERT INTO public.user_roles
VALUES (1, 2),
       (2, 2),
       (3, 2),
       (4, 2),
       (5, 2),
       (6, 3),
       (7, 3),
       (8, 5),
       (9, 4);

INSERT INTO public.volunteer_interests
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 4);