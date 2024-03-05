INSERT INTO tb_user(first_name, last_name, birth_date, email, password) VALUES ('Lucas', 'Ibiapino', '2002-07-25', 'lucas.ibiapino07@gmail.com', '$2a$10$iH9YzppeqS4wSK85ynFmNeKfZhiXpvB9diWOouKVqSSVTD.mCmGE6');

INSERT INTO tb_role(authority) VALUES ('ROLE_CLIENT')

INSERT INTO tb_user_role(user_id, role_id) VALUES (1, 1)
