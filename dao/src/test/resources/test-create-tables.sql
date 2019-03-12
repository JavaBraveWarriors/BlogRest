-- create-schema
-- CREATE SCHEMA `blog` DEFAULT CHARACTER SET utf8;

DROP TABLE IF EXISTS `author`;
CREATE TABLE `author`
(
  `id`                bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `mail`              varchar(45)         NOT NULL,
  `login`             varchar(45)         NOT NULL,
  `password`          varchar(255)        NOT NULL,
  `first_name`        varchar(45) DEFAULT NULL,
  `last_name`         varchar(45) DEFAULT NULL,
  `registration_date` datetime    DEFAULT NULL,
  `phone`             varchar(20) DEFAULT NULL,
  `description`       text,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`
(
  `id`         bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `title`      varchar(255)        NOT NULL,
  `path_image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`
(
  `id`           bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `title`        varchar(200)        NOT NULL,
  `description`  varchar(600)        NOT NULL,
  `text`         text                NOT NULL,
  `created_date` datetime    DEFAULT NULL,
  `path_image`   varchar(45) DEFAULT NULL,
  `author_id`    bigint(11) unsigned NOT NULL,
  `comments_count` bigint(11) unsigned DEFAULT '0',
  `views_count` bigint(11) unsigned DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `INDEX` (`author_id`),
  CONSTRAINT `fk_post_1` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `post_has_tag`;
CREATE TABLE `post_has_tag`
(
  `post_id` bigint(11) unsigned NOT NULL,
  `tag_id`  bigint(11) unsigned NOT NULL,
  PRIMARY KEY (`post_id`, `tag_id`),
  KEY `fk_post_has_tag_2_idx` (`tag_id`),
  CONSTRAINT `fk_post_has_tag_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_post_has_tag_tag` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `view`;
CREATE TABLE `view`
(
  `view_id`   bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `post_id`   bigint(11) unsigned NOT NULL,
  `author_id` bigint(11) unsigned NOT NULL,
  PRIMARY KEY (`view_id`),
  KEY `fk_view_1_idx` (`post_id`),
  KEY `fk_view_2_idx` (`author_id`),
  CONSTRAINT `fk_view_author` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_view_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `like`;
CREATE TABLE `like`
(
  `like_id`   bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `post_id`   bigint(11) unsigned NOT NULL,
  `author_id` bigint(11) unsigned NOT NULL,
  PRIMARY KEY (`like_id`),
  KEY `fk_like_1_idx` (`author_id`),
  KEY `fk_like_2_idx` (`post_id`),
  CONSTRAINT `fk_like_author` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_like_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`
(
  `comment_id`           bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `comment_author_id`    bigint(11) unsigned NOT NULL,
  `comment_post_id`      bigint(11) unsigned NOT NULL,
  `comment_text`         text                NOT NULL,
  `comment_created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `fk_comment_1_idx` (`comment_author_id`),
  KEY `fk_comment_2_idx` (`comment_post_id`),
  CONSTRAINT `fk_comment_author` FOREIGN KEY (`comment_author_id`) REFERENCES `author` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_post` FOREIGN KEY (`comment_post_id`) REFERENCES `post` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

