drop table if exists ent_person;
create table ent_person(id serial primary key, first_name varchar(50), family_name varchar(100) not null, birth_date date);
