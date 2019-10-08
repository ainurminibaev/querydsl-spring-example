-- insert users
insert into users (id, created_date_time, last_modified_date_time, deleted, bonuses, email, name, password_hash, phone,
                   phone_confirmed, promo_code, role)
values (nextval('users_seq'), current_timestamp, current_timestamp, false, 0, 'test1@mail.ru', 'Вася',
        '$2a$10$.OXwmn8Ooj/xh1Cui9/evOGXTaAtFFJBU16DFJhS2OznpqzUJHacW', '+79875220302', true, '09wdad', 'CLIENT');

insert into users (id, created_date_time, last_modified_date_time, deleted, bonuses, email, name, password_hash, phone,
                   phone_confirmed, promo_code, role)
values (nextval('users_seq'), current_timestamp, current_timestamp, false, 0, 'test2@mail.ru', 'Петя',
        '$2a$10$.OXwmn8Ooj/xh1Cui9/evOGXTaAtFFJBU16DFJhS2OznpqzUJHacW', '+79775220232', true, '45wrsa', 'CLIENT');

insert into users (id, created_date_time, last_modified_date_time, deleted, bonuses, email, name, password_hash, phone,
                   phone_confirmed, promo_code, role)
values (nextval('users_seq'), current_timestamp, current_timestamp, false, 0, 'test3@mail.ru', 'Валера',
        '$2a$10$.OXwmn8Ooj/xh1Cui9/evOGXTaAtFFJBU16DFJhS2OznpqzUJHacW', '+79775720302', true, 'fsdjda', 'ADMIN');

insert into users (id, created_date_time, last_modified_date_time, deleted, bonuses, email, name, password_hash, phone,
                   phone_confirmed, promo_code, role)
values (nextval('users_seq'), current_timestamp, current_timestamp, false, 0, 'test4@mail.ru', 'Петр',
        '$2a$10$.OXwmn8Ooj/xh1Cui9/evOGXTaAtFFJBU16DFJhS2OznpqzUJHacW', '+79675220302', true, '4redsa', 'CLIENT');

insert into users (id, created_date_time, last_modified_date_time, deleted, bonuses, email, name, password_hash, phone,
                   phone_confirmed, promo_code, role)
values (nextval('users_seq'), current_timestamp, current_timestamp, false, 0, 'test5@mail.ru', 'Генадий',
        '$2a$10$.OXwmn8Ooj/xh1Cui9/evOGXTaAtFFJBU16DFJhS2OznpqzUJHacW', '+79875226302', true, 'fdsifj', 'SUPER_ADMIN');