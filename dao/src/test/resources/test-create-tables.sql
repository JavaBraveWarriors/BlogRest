-- create-schema
-- CREATE SCHEMA `blog` DEFAULT CHARACTER SET utf8;

-- create-tables.sql
CREATE TABLE `author`
(
  `id`                bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `mail`              varchar(45)         NOT NULL,
  `login`             varchar(45)         NOT NULL,
  `password`          varchar(255)        NOT NULL,
  `first_name`        varchar(45) DEFAULT NULL,
  `last_name`         varchar(45) DEFAULT NULL,
  `registration_date` date        DEFAULT NULL,
  `phone`             varchar(20) DEFAULT NULL,
  `description`       text,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

CREATE TABLE `tag`
(
  `id`         bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `title`      varchar(255)        NOT NULL,
  `path_image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

CREATE TABLE `post`
(
  `id`           bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `title`        varchar(100)        NOT NULL,
  `description`  varchar(600)        NOT NULL,
  `text`         text                NOT NULL,
  `created_date` date                NOT NULL,
  `path_image`   varchar(45) DEFAULT NULL,
  `author_id`    bigint(11) unsigned NOT NULL,
  PRIMARY KEY (`id`, `author_id`),
  -- KEY `fk_post_1_idx` (`author_id`),
  CONSTRAINT `fk_post_1` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

CREATE TABLE `post_has_tag`
(
  `post_id` bigint(11) unsigned NOT NULL,
  `tag_id`  bigint(11) unsigned NOT NULL,
  PRIMARY KEY (`post_id`, `tag_id`),
  KEY `fk_post_has_tag_2_idx` (`tag_id`),
  CONSTRAINT `fk_post_has_tag_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_post_has_tag_2` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

