create database lhanud29;

create table account(
	account_id int(8) auto_increment primary key not null,
    username varchar(50) not null,
    password varchar(50) not null,
	balance double default 0
);

