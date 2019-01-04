INSERT INTO tag (id, title, path_image) VALUES (1,'life','path_1')
INSERT INTO tag (id, title, path_image) VALUES (2,'dogs','path_2')
INSERT INTO tag (id, title, path_image) VALUES (3,'cats','path_3')
INSERT INTO tag (id, title, path_image) VALUES (4,'sport','path_4')

INSERT INTO author (id, mail,  login, password, first_name, last_name) VALUES (1,'testMail1@mail.ru','testLogin1','testPsw1','testFirst1', 'testLast1')
INSERT INTO author (id, mail,  login, password, first_name, last_name) VALUES (2,'testMail2@mail.ru','testLogin2','testPsw2','testFirst2', 'testLast2')
INSERT INTO author (id, mail,  login, password, first_name, last_name) VALUES (3,'testMail3@mail.ru','testLogin3','testPsw3','testFirst3', 'testLast3')

INSERT INTO post (title, description, text, author_id) VALUES ('title1','testDescription1','testText1', 1)
INSERT INTO post (title, description, text, author_id) VALUES ('title2','testDescription2','testText2', 1)
INSERT INTO post (title, description, text, author_id) VALUES ('title3','testDescription3','testText3', 3)


INSERT INTO post_has_tag (post_id, tag_id) VALUES (1,1)
INSERT INTO post_has_tag (post_id, tag_id) VALUES (1,2)
INSERT INTO post_has_tag (post_id, tag_id) VALUES (2,3)
INSERT INTO post_has_tag (post_id, tag_id) VALUES (2,4)
INSERT INTO post_has_tag (post_id, tag_id) VALUES (3,2)
INSERT INTO post_has_tag (post_id, tag_id) VALUES (3,1)
INSERT INTO post_has_tag (post_id, tag_id) VALUES (3,4)