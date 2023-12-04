create table address (fk_address_id integer unique, id serial not null, zip integer, city_name varchar(255), house_number varchar(255), street varchar(255), primary key (id));
create table customer (created_at date not null, id serial not null, department varchar(255) not null, first_name varchar(255) not null unique, last_name varchar(255), primary key (id));
create table customer_item (fk_customer_id integer, id serial not null, item varchar(255), primary key (id));
alter table if exists address add constraint FKplywm3srjau47r83n1ft99wpu foreign key (fk_address_id) references customer;
alter table if exists customer_item add constraint FKrn5wxufrbu2k3r67uhvo4dbd0 foreign key (fk_customer_id) references customer;
