INSERT INTO tag (title, path_image) VALUES ('life','path_1')
INSERT INTO tag (title, path_image) VALUES ('dogs','path_2')
INSERT INTO tag (title, path_image) VALUES ('cats','path_3')
INSERT INTO tag (title, path_image) VALUES ('sport','path_4')

INSERT INTO author (mail,  login, password, first_name, last_name) VALUES ('testMail1@mail.ru','testLogin1','testPsw1','testFirst1', 'testLast1')
INSERT INTO author (mail,  login, password, first_name, last_name) VALUES ('testMail2@mail.ru','testLogin2','testPsw2','testFirst2', 'testLast2')
INSERT INTO author (mail,  login, password, first_name, last_name) VALUES ('testMail3@mail.ru','testLogin3','testPsw3','testFirst3', 'testLast3')

INSERT INTO post (title,  description, text, author_id) VALUES ('title1','testDescription1','testText1', 1)
INSERT INTO post (title,  description, text, author_id) VALUES ('title2','testDescription2','testText2', 1)
INSERT INTO post (title,  description, text, author_id) VALUES ('title3','testDescription3','testText3', 3)