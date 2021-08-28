insert into ent_person(first_name, family_name, birth_date) values('Иван', 'Иванов', to_date('2000-05-15', 'YYYY-MM-DD'));
insert into ent_person(first_name, family_name, birth_date) values('Пётр', 'Петров', to_date('2001-06-16', 'YYYY-MM-DD'));
insert into ent_person(first_name, family_name) values('Сидор', 'Сидоров');
insert into ent_person(family_name, birth_date) values('Кузнецов', to_date('2002-07-17', 'YYYY-MM-DD'));
insert into ent_person(family_name) values('Мамин-Сибиряк');
commit;
