INSERT INTO breeds (name, description, img_url, agility, grooming, hunting)
VALUES ('Doberman', 'The best breed for agility', 'doberman.png', 100, 10, 50),
       ('Labrador retriever', 'The best breed for hunting', 'labrador-retriever.png', 50, 30, 100),
       ('Yorkshire terrier', 'The best breed for grooming', 'yorkshire-terrier.png', 50, 100, 60);

INSERT INTO dogs (name, sex, breed_id, owner_id, agility, grooming, hunting, award_cup, health)
VALUES ('Letun of EspinozaDogs', 'M', 1, 2, 0, 0, 0, 0, 70),
    ('Sisa of EspinozaDogs', 'F', 2, 2, 10, 10, 10, 3, 50),
    ('Letun of AdminDogs', 'M', 3, 1, 10, 10, 10, 3, 10);