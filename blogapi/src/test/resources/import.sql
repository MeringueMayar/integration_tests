--this script initiates db for integration tests 
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'john@domain.com', 'John', 'Steward')
insert into user (id, account_status, email, first_name) values (null, 'NEW', 'brian@domain.com', 'Brian')
insert into blog_post values (null, 'blog-test', 1)
insert into blog_post values (null, 'blog-test', 2)