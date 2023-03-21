INSERT INTO breeds (name, description, img_url, agility, grooming, hunting)
VALUES ('Doberman', 'The best breed for agility', 'doberman.png', 100, 10, 50),
       ('Labrador retriever', 'The best breed for hunting', 'labrador-retriever.png', 50, 30, 100),
       ('Yorkshire terrier', 'The best breed for grooming', 'yorkshire-terrier.png', 50, 100, 60);

INSERT INTO dogs (name, sex, breed_id, owner_id, agility, grooming, hunting, award_cup, health)
VALUES ('Letun of EspinozaDogs', 'M', 1, 2, 0, 0, 0, 0, 70),
       ('Sisa of EspinozaDogs', 'F', 2, 2, 10, 10, 10, 10, 50),
       ('Pipilota of EspinozaDogs', 'F', 3, 2, 10, 10, 10, 8, 50),
       ('Pipin of EspinozaDogs', 'M', 3, 2, 10, 10, 10, 3, 50),
       ('Sisa of PirinHill', 'F', 2, 3, 10, 10, 10, 3, 50),
       ('Pipilota of PirinHill', 'F', 3, 3, 10, 10, 10, 3, 50),
       ('Pipin of PirinHill', 'M', 3, 3, 10, 10, 10, 3, 50),
       ('Letun of AdminDogs', 'M', 3, 1, 10, 10, 10, 5, 10);

INSERT INTO sales (id, price)
VALUES (1, 200),
       (2, 100),
       (3, 150),
       (4, 250);

UPDATE dogs SET sale_id = 1 WHERE id = 1;
UPDATE dogs SET sale_id = 2 WHERE id = 2;
UPDATE dogs SET sale_id = 3 WHERE id = 5;
UPDATE dogs SET sale_id = 4 WHERE id = 7;

INSERT INTO partners (id, price, dog_id, active)
VALUES (1, 200, 1, 1),
       (2, 100, 1, 1),
       (3, 150, 4, 1),
       (4, 250, 7, 1);

INSERT INTO dogs_stud_offers (dog_entity_id, stud_offers_id)
VALUES (1, 1),
       (1, 2),
       (4, 3),
       (7, 4);
