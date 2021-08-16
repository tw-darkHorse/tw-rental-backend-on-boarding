CREATE TABLE IF NOT EXISTS `rental_database`.`house` (
    `id` bigint(0) NOT NULL AUTO_INCREMENT,
    `name` nvarchar(100) NULL,
    `location` nvarchar(500) NULL,
    `price` decimal(19,6) NULL,
    `established_time` TIMESTAMP NULL,
    `status` nvarchar(20) NULL,
    `created_time` TIMESTAMP NULL,
    `updated_time` TIMESTAMP NULL,
    PRIMARY KEY (`id`)
);