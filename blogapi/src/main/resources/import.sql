--this script initiates db for h2 db (used in test profile)
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'john@domain.com', 'John', 'Steward')
insert into user (id, account_status, email, first_name) values (null, 'NEW', 'brian@domain.com', 'Brian')
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'tom@domain.com', 'Tom', 'Hopkins')
insert into user (id, account_status, email, first_name, last_name) values (null, 'REMOVED', 'dean@domain.com', 'Dean', 'Winchester')
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'Sam@domain.com', 'Sam', 'Winchester')

insert into blog_post (user_id, entry, id) values (1, 'nowy post11', null)
insert into blog_post (user_id, entry, id) values (1, 'nowy post22', null)