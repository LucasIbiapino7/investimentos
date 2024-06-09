INSERT INTO tb_user(first_name, last_name, birth_date, email, password) VALUES ('Lucas', 'Ibiapino', '2002-07-25', 'lucas.ibiapino07@gmail.com', '$2a$10$iH9YzppeqS4wSK85ynFmNeKfZhiXpvB9diWOouKVqSSVTD.mCmGE6');

INSERT INTO tb_role(authority) VALUES ('ROLE_CLIENT')

INSERT INTO tb_user_role(user_id, role_id) VALUES (1, 1)

INSERT INTO tb_account(user_id, name, description, balance, portfolio_number) VALUES (1, 'minha conta', 'Conta do Lucas', 1000.0, 0);

INSERT INTO tb_portfolio(description, total_value, account_id) VALUES ('Ativos', 0.0, 1);

INSERT INTO tb_stock(name, long_name) VALUES ('AMZO34', 'Amazon.inc')
INSERT INTO tb_stock(name, long_name) VALUES ('PETR4', 'Petrobrás')

INSERT INTO tb_portfolio_stock(stock_id, portfolio_id, price, quantity, value_purchased) VALUES ('AMZO34', 1, 40.0, 5, 200.0);
INSERT INTO tb_portfolio_stock(stock_id, portfolio_id, price, quantity, value_purchased) VALUES ('PETR4', 1, 10.0, 10, 100.0);
