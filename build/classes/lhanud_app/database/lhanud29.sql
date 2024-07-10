create database lhanud29;

create table account(
	account_id int(8) auto_increment primary key not null,
    username varchar(50) not null,
    password varchar(50) not null,
	balance double default 0
);

create table transaction(
	transaction_id int auto_increment primary key not null,
    account_id int(8) not null,
    transaction_type varchar(100) not null,
    amount double not null,
    content varchar(2000)
);

alter table transaction add constraint foreign key (account_id) references account(account_id);

alter table transaction add column transaction_date timestamp not null;

create table admin(
	admin_id int auto_increment primary key not null,
    admin varchar(50) not null,
    password varchar(50) not null
);

insert into admin (admin_id, admin, password) values (1, 'admin', 1);

alter table account add column question_id int(8) not null;
alter table account add column security_answer varchar(1000) not null;

create table security_question(
	question_id int(8) auto_increment primary key not null,
    question varchar(1000) not null
);

insert into security_question (question_id, question) values (1, 'Biet danh cua ban la gi?');
insert into security_question (question_id, question) values (2, 'Nguoi yeu cua ban ten gi?');
insert into security_question (question_id, question) values (3, 'Mau sac yeu thich cua ban?');
insert into security_question (question_id, question) values (4, 'Thanh pho nao vua di da moi?');
insert into security_question (question_id, question) values (5, 'Nudle dang yeu nhat the gioi dung khong?');

alter table account add constraint account_question_fk1 foreign key (question_id) references security_question(question_id);