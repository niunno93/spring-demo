create sequence hibernate_sequence start with 1 increment by 1;
create table user_detail (id bigint not null, creation_date timestamp not null, email varchar(255) not null, modification_date timestamp not null, name varchar(255) not null, primary key (id));
alter table user_detail add constraint UK_al4vy84pj6kshsqrsn9kc63sj unique (email);
