INSERT INTO restaurants (restaurant_id, name, phone, description) VALUES ( '123azxczc1qsa', 'KFC', '1231231', 'Kościuszki 43, 47-511 Katowice');
INSERT INTO restaurants (restaurant_id, name, phone, description) VALUES ( '123azxcc1qsa', 'BK', '1231231', 'Paderewskiego 2, 00-035 Warszawa');
INSERT INTO restaurants (restaurant_id, name, phone, description) VALUES ( '123aczc1qsa', 'MCD', '1231231', 'Krakowska 879, 98-204 Kraków');
INSERT INTO restaurants (restaurant_id, name, phone, description) VALUES ( '123azczcqsa', 'Pizzeria Rino', '1231231', 'Wrocławska 87, 92-434 Jelenia Góra');
INSERT INTO restaurants (restaurant_id, name, phone, description) VALUES ( '12azczc1qsa', 'U Piotrusia', '1231231', 'Roździeńska 89, 91-224 Wrocław');

INSERT INTO menu_items (menu_item_id, name, category, description, price, restaurant_id)
VALUES ('123456789s', 'bigos', 'dania główne', 'kapusta, mięso wołowe, pieczarki', 28.90, (SELECT id FROM restaurants WHERE restaurantId = '12azczc1qsa'));