-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 172.16.2.10:3306
-- Generation Time: Aug 02, 2020 at 10:39 PM
-- Server version: 10.4.13-MariaDB-1:10.4.13+maria~bionic
-- PHP Version: 7.2.31-1+ubuntu16.04.1+deb.sury.org+1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `s40_sco`
--

-- --------------------------------------------------------

--
-- Table structure for table `inventory`
--

CREATE TABLE IF NOT EXISTS `inventory` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `type` enum('PLAYER','SWORD_SKILL') NOT NULL,
  `size` tinyint(3) UNSIGNED NOT NULL,
  `player_id` int(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `inventory_item`
--

CREATE TABLE IF NOT EXISTS `inventory_item` (
  `inventory_id` int(11) UNSIGNED NOT NULL,
  `slot` tinyint(3) UNSIGNED NOT NULL,
  `quantity` tinyint(3) UNSIGNED NOT NULL,
  `item_id` int(11) UNSIGNED NOT NULL,
  `custom_item_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL CHECK (json_valid(`custom_item_data`)),
  UNIQUE KEY `inventory_slot` (`inventory_id`,`slot`),
  UNIQUE KEY `inventory_id` (`inventory_id`),
  KEY `item_id` (`item_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `item_registry`
--

CREATE TABLE IF NOT EXISTS `item_registry` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
  `rarity` enum('COMMON','UNCOMMON','RARE','LEGENDARY','GODLIKE','ETHEREAL') CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `player`
--

CREATE TABLE IF NOT EXISTS `player` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `uuid` char(32) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
  `name` char(16) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
  `first_login` datetime NOT NULL DEFAULT current_timestamp(),
  `last_login` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `inventory`
--
ALTER TABLE `inventory`
  ADD CONSTRAINT `sync_player_id` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`);

--
-- Constraints for table `inventory_item`
--
ALTER TABLE `inventory_item`
  ADD CONSTRAINT `snyc_inventory_id` FOREIGN KEY (`inventory_id`) REFERENCES `inventory` (`id`),
  ADD CONSTRAINT `sync_item_id` FOREIGN KEY (`item_id`) REFERENCES `item_registry` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
