-- RESTAURANTS
INSERT INTO restaurants (restaurant_id, name, phone, email, address, tags)VALUES ('123azxczc1qsa', 'KFC', '1231231', 'kfc@kfc.com', 'Kościuszki 43, 47-511 Katowice', '#fastfood #friedchicken #bites');
INSERT INTO restaurants (restaurant_id, name, phone, email, address, tags) VALUES ( '123azxcc1qsa', 'Burger King', '1231231', 'burgerking@bg.com', 'Paderewskiego 2, 00-035 Warszawa', '#fastfood #burgers');
INSERT INTO restaurants (restaurant_id, name, phone, email, address,tags) VALUES ( '123aczc1qsa', 'McDonald', '1231231', 'mcd@mcd.com', 'Krakowska 879, 98-204 Kraków', '#fastfood #burgers #tortillas #fries');
INSERT INTO restaurants (restaurant_id, name, phone, email, address, tags) VALUES ( '123azczcqsa', 'Pizzeria Rino', '1231231', 'pizzeria@pizzeria-rino.pl', 'Wrocławska 87, 92-434 Jelenia Góra', '#pizza #burgers');
INSERT INTO restaurants (restaurant_id, name, phone, email, address, tags) VALUES ( '12azczc1qsa', 'U Piotrusia', '1231231', 'restauracja@restauracja-piotrus.pl', 'Roździeńska 89, 91-224 Wrocław', '#obiadowe #');

-- KFC
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('kfc001', 'kubełek classic', 'dania główne', 'frytki, udka drobiowe', 20.45, (SELECT id FROM restaurants WHERE restaurant_id = '123azxczc1qsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('kfc002', 'frytki', 'dania główne', 'chrupiące frytki', 6.90, (SELECT id FROM restaurants WHERE restaurant_id = '123azxczc1qsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('kfc003', 'cheeseburger', 'dania główne', 'mięso, ser, sos', 10.50, (SELECT id FROM restaurants WHERE restaurant_id = '123azxczc1qsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('kfc004', 'tortilla', 'dania główne', 'filet drobiowy, pomidor, sałata', 18.99, (SELECT id FROM restaurants WHERE restaurant_id = '123azxczc1qsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('kfc005', 'lody karmelowe', 'desery', 'puszyste lody z polewą karmelową', 8.99, (SELECT id FROM restaurants WHERE restaurant_id = '123azxczc1qsa'));

-- Burger King
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('bk001', 'Whopper', 'dania główne', 'Klasyczny burger z wołowiną, warzywami i sosem', 24.99, (SELECT id FROM restaurants WHERE restaurant_id = '123azxcc1qsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('bk002', 'Frytki klasyczne', 'przekąski', 'Złociste frytki z solą', 7.99, (SELECT id FROM restaurants WHERE restaurant_id = '123azxcc1qsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('bk003', 'Chicken Nuggets', 'przekąski', 'Chrupiące kawałki kurczaka', 15.99, (SELECT id FROM restaurants WHERE restaurant_id = '123azxcc1qsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('bk004', 'Czekoladowy shake', 'napoje', 'Schłodzony napój mleczny o smaku czekolady', 9.50, (SELECT id FROM restaurants WHERE restaurant_id = '123azxcc1qsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('bk005', 'Lody waniliowe', 'desery', 'Delikatne lody o smaku waniliowym', 6.50, (SELECT id FROM restaurants WHERE restaurant_id = '123azxcc1qsa'));

-- McDonalds
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('mc001', 'Big Mac', 'dania główne', 'Burger z dwoma warstwami wołowiny, sałatą, serem i sosem', 22.99, (SELECT id FROM restaurants WHERE restaurant_id = '123aczc1qsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('mc002', 'Frytki', 'przekąski', 'Chrupiące frytki z solą', 6.99, (SELECT id FROM restaurants WHERE restaurant_id = '123aczc1qsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('mc003', 'McChicken', 'dania główne', 'Burger z kurczakiem, sałatą i sosem majonezowym', 19.99, (SELECT id FROM restaurants WHERE restaurant_id = '123aczc1qsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('mc004', 'Szejk truskawkowy', 'napoje', 'Napój mleczny o smaku truskawek', 10.99, (SELECT id FROM restaurants WHERE restaurant_id = '123aczc1qsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('mc005', 'Ciastko jabłkowe', 'desery', 'Słodkie ciastko z nadzieniem jabłkowym', 8.50, (SELECT id FROM restaurants WHERE restaurant_id = '123aczc1qsa'));

-- Pizzeria Rino
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('rino001', 'Pizza Margherita', 'dania główne', 'Klasyczna pizza z sosem pomidorowym, mozzarellą i bazylią', 25.99, (SELECT id FROM restaurants WHERE restaurant_id = '123azczcqsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('rino002', 'Pizza Pepperoni', 'dania główne', 'Pizza z pikantnym salami, sosem pomidorowym i mozzarellą', 29.99, (SELECT id FROM restaurants WHERE restaurant_id = '123azczcqsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('rino003', 'Bruschetta', 'przystawki', 'Grzanki z pomidorami, czosnkiem i bazylią', 12.50, (SELECT id FROM restaurants WHERE restaurant_id = '123azczcqsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('rino004', 'Tiramisu', 'desery', 'Klasyczny włoski deser z mascarpone, kawą i kakao', 15.50, (SELECT id FROM restaurants WHERE restaurant_id = '123azczcqsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('rino005', 'Lemoniada cytrynowa', 'napoje', 'Orzeźwiająca lemoniada z cytryn i mięty', 8.00, (SELECT id FROM restaurants WHERE restaurant_id = '123azczcqsa'));

-- U Piotrusia
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('peter001', 'bigos', 'dania główne', 'kapusta, mięso wołowe, pieczarki', 28.90, (SELECT id FROM restaurants WHERE restaurant_id = '12azczc1qsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('peter002', 'rolada wołowa', 'dania główne', 'mięso wołowe, pieczarki, ser żółty, kapusta czerwona, klusi, sos', 48.90, (SELECT id FROM restaurants WHERE restaurant_id = '12azczc1qsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('peter003', 'zupa pomidorowa', 'zupy', 'ryż', 20.90, (SELECT id FROM restaurants WHERE restaurant_id = '12azczc1qsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('peter004', 'rosół', 'zupy', 'makaron, filet z kurczaka', 24.00, (SELECT id FROM restaurants WHERE restaurant_id = '12azczc1qsa'));
INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('peter005', 'sernik orzechowy', 'desery', 'sernik, orzechy, polewa mleczna', 18.90, (SELECT id FROM restaurants WHERE restaurant_id = '12azczc1qsa'));

-- USERS
INSERT INTO users (user_id, first_name, last_name) VALUES ('user1', 'Jan', 'Nowak');
INSERT INTO users (user_id, first_name, last_name) VALUES ('user2', 'Adam', 'Kowalski');
INSERT INTO users (user_id, first_name, last_name) VALUES ('user3', 'Jim', 'Gordon');