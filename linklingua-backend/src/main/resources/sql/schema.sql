-- ============================================================
-- LinkLingua database initialization script (MySQL 8.0)
-- ============================================================

CREATE DATABASE IF NOT EXISTS `linklingua`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE `linklingua`;

-- 1. City dictionary table
CREATE TABLE IF NOT EXISTS city (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'Primary key ID',
    city_name VARCHAR(50) NOT NULL COMMENT 'City name: Shenzhen / Hong Kong'
) COMMENT='City basic dictionary for cross-border filter';

-- init base data
INSERT INTO city(city_name) VALUES ('Shenzhen'),('Hong Kong');

-- 2. Language tag dictionary table
CREATE TABLE IF NOT EXISTS language_tag (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'Primary key ID',
    lang_name VARCHAR(50) NOT NULL COMMENT 'Language name'
) COMMENT='Language category dictionary';

INSERT INTO language_tag(lang_name)
VALUES('English'),('Mandarin'),('Cantonese'),('Japanese'),('Korean');

-- 3. User table (`user` is a reserved word, so it is wrapped in backticks)
CREATE TABLE IF NOT EXISTS `user` (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'User unique id',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT 'Login account',
    password VARCHAR(100) NOT NULL COMMENT 'Login password',
    create_time DATETIME COMMENT 'Register time'
) COMMENT='Platform system user info';

-- sample user (password is plain text here for demo only)
INSERT INTO `user`(username, password, create_time)
VALUES ('admin', 'admin123', NOW());

-- 4. Event core table (project core)
CREATE TABLE IF NOT EXISTS event_info (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'Unique event id',
    title VARCHAR(200) NOT NULL COMMENT 'Event title',
    description TEXT COMMENT 'Detailed event introduction',
    location VARCHAR(200) NOT NULL COMMENT 'Specific offline address',
    event_start_time DATETIME NOT NULL COMMENT 'Event start datetime',
    max_participant INT DEFAULT 50 COMMENT 'Max join people count',
    city_id INT NOT NULL COMMENT 'Foreign key -> city.id',
    lang_id INT NOT NULL COMMENT 'Foreign key -> language_tag.id',
    publish_user_id INT NOT NULL COMMENT 'Foreign key -> user.id(publisher)',
    event_status TINYINT NOT NULL DEFAULT 1 COMMENT '1:Upcoming,2:Ongoing,3:Finished',
    image VARCHAR(250) COMMENT 'Event cover image URL',
    create_time DATETIME COMMENT 'Event publish time',
    update_time DATETIME COMMENT 'Event last updated time',
    created_by INT COMMENT 'The user id of the user who created this event',
    updated_by INT COMMENT 'The user id of the user who updated this event',
    INDEX idx_city(city_id),
    INDEX idx_lang(lang_id),
    INDEX idx_publisher(publish_user_id),
    INDEX idx_status(event_status)
) COMMENT='Core language exchange event data table';

-- sample event
INSERT INTO event_info
    (title, description, location, event_start_time, max_participant,
     city_id, lang_id, publish_user_id, event_status, image,
     create_time, update_time, created_by, updated_by)
VALUES
    ('Cantonese Coffee Meetup', 'A relaxed meetup to practice Cantonese over coffee.',
     'Coco Park, Futian District', '2026-07-01 19:00:00', 30,
     1, 3, 1, 1, 'https://example.com/cover.png',
     NOW(), NOW(), 1, 1);
