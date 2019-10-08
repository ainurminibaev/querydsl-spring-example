create table alternative_media_formats (id int8 not null, created_date_time timestamp, last_modified_date_time timestamp, converted_path varchar(255), media_format varchar(255), origin_path varchar(255), primary key (id));
create table file_items (id int8 not null, created_date_time timestamp, last_modified_date_time timestamp, file_item_type varchar(255), file_size int8, name TEXT, item_order int4, owner_id int8, url TEXT, visible_for_all boolean, primary key (id));
create table users (id int8 not null, created_date_time timestamp, last_modified_date_time timestamp, deleted boolean, bonuses int4, email varchar(255), name varchar(255), password_hash varchar(255), phone varchar(255), phone_confirmed boolean, promo_code varchar(255), role varchar(255), primary key (id));
alter table if exists users drop constraint if exists UK_h3pnujywhfpjdgdkvu20ai53e;
alter table if exists users add constraint UK_h3pnujywhfpjdgdkvu20ai53e unique (promo_code);
create sequence alternative_media_formats_seq start 1 increment 10;
create sequence file_items_seq start 1 increment 10;
create sequence users_seq start 1 increment 10;
