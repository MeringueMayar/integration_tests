--this script initiates db for h2 db (used in test profile)
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'john@domain.com', 'John', 'Steward')
insert into user (id, account_status, email, first_name) values (null, 'NEW', 'brian@domain.com', 'Brian')
insert into user (id, account_status, email, first_name) values (null, 'CONFIRMED', 'cyprian@domain.com', 'Cyprian')
insert into user (id, account_status, email, first_name) values (null, 'REMOVED', 'denis@domain.com', 'Denis')
insert into blog_post (id, entry, user_id) values (null, 'Post to like.', 1);