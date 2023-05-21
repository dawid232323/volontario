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
VALUES (1, 'janwolontariusz@st.amu.edu.pl', 'Lubię ludzi', 1),
       (2, 'adamwolontariusz@st.amu.edu.pl', 'Nudzi mi się', 2);

SELECT setval('volunteer_data_id_seq', 3, true);

--
-- Password for all is SecurePassword123_
--
INSERT INTO public.users
VALUES (1, 'januszex@januszex.pl', 'Jan', '$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm', true, 'Uszex', '123456789', 1, null),
       (2, 'Ola@wolontariat.pl', 'Ola', '$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm', true, 'Wolontariacka', '123456788', 2, null),
       (3, 'ktoswazny@amu.edu.pl', 'Ktos', '$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm', true, 'Wazny', '123456787', 3, null),
       (4, 'typ@niema.pl', 'Zamknal', '$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm', true, 'Instytucje', '123456786', 4, null),
       (5, 'pan@warszawka.pl', 'Pan', '$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm', true, 'Warszawski', '123456785', 5, null),
       (6, 'janwolontariusz@gmail.pl', 'Jan', '$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm', true, 'Kowalski', '123456784', null, 1),
       (7, 'adamwolontariusz@gmail.pl', 'Adam', '$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm', true, 'Nowak', '123456783', null, 2),
       (8, 'MODERATOR@gmail.pl', 'Moderator', '$2a$10$69b70HN7dLVmE1EZwtXlV.4PD/Np3Nj9osdJxvlewM99fpYfrVUTm', true, 'Nowak', '333456783', null, null);

SELECT setval('users_id_seq', 9, true);

INSERT INTO public.offers(
    id, description, end_date, expiration_date, is_experience_required, is_poznan_only,
    place, start_date, title, contact_person_id, institution_id, minimum_experience_id,
    offer_type_id, offer_state_id, assigned_moderator_id, is_hidden, periodic_description)
VALUES (1, 'Pomoc w domu dla starców i sierocińcu', '2023-05-25 00:00:00+00', '2023-05-15 00:00:00+00', true,
        true, 'Poznań', '2023-05-20 00:00:00+00', 'Pomoc w domu dla starców i sierocińcu', 1, 1, 1, 1, 1, NULL, false,
        NULL),
       (2, 'Zbiórka na biednych', '2023-07-20 00:00:00+00', '2023-05-15 00:00:00+00',
        true, false, 'Gniezno', '2023-05-20 00:00:00+00', 'Zbiórka na biednych', 2, 2, 1, 2, 1, NULL, false,
        'Co tydzień o 17 w piątek'),
       (3, 'Pomoc dla niepełnosprawnej pani', '2023-06-20 22:00:00+00', '2023-06-03 22:00:00+00', false,
        true, 'Poznań', '2023-06-17 22:00:00+00', 'Pomoc dla niepełnosprawnej pani', 2, 2, NULL, 1, 1, NULL, false,
        NULL),
       (4, 'Wolontariat w szkole specjalnej', '2023-06-31 22:00:00+00', '2023-05-31 22:00:00+00', true,
        true, 'Poznań', '2023-05-28 22:00:00+00', 'Wolontariat w szkole specjalnej', 2, 2, 2, 3, 2, 8, true,
        'Każdego tygodnia od 8 do 16 w dni robocze');

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
       (8, 5);

INSERT INTO public.volunteer_interests
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 4);