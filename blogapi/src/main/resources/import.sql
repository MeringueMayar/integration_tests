--this script initiates db for h2 db (used in test profile)
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'john@domain.com', 'John', 'Steward')
insert into user (id, account_status, email, first_name) values (null, 'NEW', 'brian@domain.com', 'Brian')
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'carl@domain.com', 'Carl', 'Johnson')

insert into blog_post values (null, 'Test entry 1', 1)