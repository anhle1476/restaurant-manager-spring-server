CREATE DATABASE  IF NOT EXISTS `restaurant` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `restaurant`;
-- MySQL dump 10.13  Distrib 8.0.23, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: restaurant
-- ------------------------------------------------------
-- Server version	8.0.23-0ubuntu0.20.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `app_table`
--

DROP TABLE IF EXISTS `app_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_table` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `app_table_name` varchar(255) DEFAULT NULL,
  `parent_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_a6js82xlj3e4wmymgg9e7g28t` (`app_table_name`),
  KEY `FK14tphmbfmacd0kqjipugnshq2` (`parent_id`),
  CONSTRAINT `FK14tphmbfmacd0kqjipugnshq2` FOREIGN KEY (`parent_id`) REFERENCES `app_table` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_table`
--

LOCK TABLES `app_table` WRITE;
/*!40000 ALTER TABLE `app_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `app_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bill` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `direct_discount` bigint NOT NULL,
  `direct_discount_description` varchar(255) DEFAULT NULL,
  `last_price` bigint NOT NULL,
  `pay_time` datetime(6) DEFAULT NULL,
  `start_time` datetime(6) DEFAULT NULL,
  `app_table_id` int DEFAULT NULL,
  `discount_id` int DEFAULT NULL,
  `staff_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32xp41o9v675mfxojxthyusg6` (`app_table_id`),
  KEY `FKdvi8dqm1x4rp0reelkmr97vus` (`discount_id`),
  KEY `FKsnmgdkqs3padcdqqxuh1p5vpf` (`staff_id`),
  CONSTRAINT `FK32xp41o9v675mfxojxthyusg6` FOREIGN KEY (`app_table_id`) REFERENCES `app_table` (`id`),
  CONSTRAINT `FKdvi8dqm1x4rp0reelkmr97vus` FOREIGN KEY (`discount_id`) REFERENCES `discount` (`id`),
  CONSTRAINT `FKsnmgdkqs3padcdqqxuh1p5vpf` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill`
--

LOCK TABLES `bill` WRITE;
/*!40000 ALTER TABLE `bill` DISABLE KEYS */;
/*!40000 ALTER TABLE `bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill_detail`
--

DROP TABLE IF EXISTS `bill_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bill_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `done_quantity` int NOT NULL,
  `last_order_time` datetime(6) DEFAULT NULL,
  `price_per_unit` bigint DEFAULT NULL,
  `quantity` int NOT NULL,
  `bill_id` int DEFAULT NULL,
  `food_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKeolgwyayei3o80bb7rj7t207q` (`bill_id`),
  KEY `FK7yx3emrxtrbkg526nhle3xrt6` (`food_id`),
  CONSTRAINT `FK7yx3emrxtrbkg526nhle3xrt6` FOREIGN KEY (`food_id`) REFERENCES `food` (`id`),
  CONSTRAINT `FKeolgwyayei3o80bb7rj7t207q` FOREIGN KEY (`bill_id`) REFERENCES `bill` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill_detail`
--

LOCK TABLES `bill_detail` WRITE;
/*!40000 ALTER TABLE `bill_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `bill_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discount`
--

DROP TABLE IF EXISTS `discount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discount` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `discount_name` varchar(255) DEFAULT NULL,
  `percent` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_8dxvvhpnxj565wsvijnu8lusp` (`discount_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discount`
--

LOCK TABLES `discount` WRITE;
/*!40000 ALTER TABLE `discount` DISABLE KEYS */;
/*!40000 ALTER TABLE `discount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `food`
--

DROP TABLE IF EXISTS `food`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `food` (
  `id` int NOT NULL AUTO_INCREMENT,
  `available` bit(1) NOT NULL,
  `deleted` bit(1) NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `food_name` varchar(255) DEFAULT NULL,
  `price` bigint NOT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `food_type_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_owcaxj73hkgavmpqaivj5o4yb` (`food_name`),
  KEY `FKf3bmvf487ya6cqglsh3lotiiq` (`food_type_id`),
  CONSTRAINT `FKf3bmvf487ya6cqglsh3lotiiq` FOREIGN KEY (`food_type_id`) REFERENCES `food_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `food`
--

LOCK TABLES `food` WRITE;
/*!40000 ALTER TABLE `food` DISABLE KEYS */;
/*!40000 ALTER TABLE `food` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `food_type`
--

DROP TABLE IF EXISTS `food_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `food_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `food_type_name` varchar(255) DEFAULT NULL,
  `refundable` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_qy3i9q0xxqjspd5l69lv9821v` (`food_type_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `food_type`
--

LOCK TABLES `food_type` WRITE;
/*!40000 ALTER TABLE `food_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `food_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reserve_table`
--

DROP TABLE IF EXISTS `reserve_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reserve_table` (
  `reserving_orders_id` int NOT NULL,
  `app_tables_id` int NOT NULL,
  KEY `FKq973ww1o6dpkj46ce05o5dfba` (`app_tables_id`),
  KEY `FKs06b4lryk06hjc3rv3nknlsph` (`reserving_orders_id`),
  CONSTRAINT `FKq973ww1o6dpkj46ce05o5dfba` FOREIGN KEY (`app_tables_id`) REFERENCES `app_table` (`id`),
  CONSTRAINT `FKs06b4lryk06hjc3rv3nknlsph` FOREIGN KEY (`reserving_orders_id`) REFERENCES `reserving_order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reserve_table`
--

LOCK TABLES `reserve_table` WRITE;
/*!40000 ALTER TABLE `reserve_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `reserve_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reserving_order`
--

DROP TABLE IF EXISTS `reserving_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reserving_order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(255) DEFAULT NULL,
  `customer_phone_number` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `reserving_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reserving_order`
--

LOCK TABLES `reserving_order` WRITE;
/*!40000 ALTER TABLE `reserving_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `reserving_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `deleted` bit(1) NOT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ADMIN',_binary '\0','qtv'),(2,'CHEF',_binary '\0','db'),(3,'CASHIER',_binary '\0','tn'),(4,'MISC',_binary '\0','Giữ xe'),(5,'MISC',_binary '\0','Chạy bàn'),(6,'MISC',_binary '\0','Vệ sinh');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `salary_detail`
--

DROP TABLE IF EXISTS `salary_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `salary_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `first_date_of_month` date DEFAULT NULL,
  `number_of_shift` int NOT NULL,
  `salary` bigint NOT NULL,
  `total_overtime_hours` float NOT NULL,
  `staff_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3buhi7x5tjot2vyvcuhhppef8` (`staff_id`),
  CONSTRAINT `FK3buhi7x5tjot2vyvcuhhppef8` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salary_detail`
--

LOCK TABLES `salary_detail` WRITE;
/*!40000 ALTER TABLE `salary_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `salary_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `deleted` bit(1) NOT NULL,
  `note` varchar(300) DEFAULT NULL,
  `shift_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKajqm53soraqn6bpdsfvxtbv52` (`shift_id`),
  CONSTRAINT `FKajqm53soraqn6bpdsfvxtbv52` FOREIGN KEY (`shift_id`) REFERENCES `shift` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_detail`
--

DROP TABLE IF EXISTS `schedule_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `overtime_hours` float NOT NULL,
  `schedule_id` int DEFAULT NULL,
  `staff_id` int DEFAULT NULL,
  `violation_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK67bx1tlwbgmrlb1s0nf0jlf0y` (`schedule_id`),
  KEY `FKj99vm4vsy1yx9xysb4da8677e` (`staff_id`),
  KEY `FKcdhfj454ihfen53qgtc7et3p4` (`violation_id`),
  CONSTRAINT `FK67bx1tlwbgmrlb1s0nf0jlf0y` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`),
  CONSTRAINT `FKcdhfj454ihfen53qgtc7et3p4` FOREIGN KEY (`violation_id`) REFERENCES `violation` (`id`),
  CONSTRAINT `FKj99vm4vsy1yx9xysb4da8677e` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_detail`
--

LOCK TABLES `schedule_detail` WRITE;
/*!40000 ALTER TABLE `schedule_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shift`
--

DROP TABLE IF EXISTS `shift`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shift` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `shift_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ct4h1pflyvxcmdbrnn5umlu5u` (`shift_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shift`
--

LOCK TABLES `shift` WRITE;
/*!40000 ALTER TABLE `shift` DISABLE KEYS */;
/*!40000 ALTER TABLE `shift` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `salary_per_shift` bigint NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5bbdfuitxii0b63v2v3f0r22x` (`role_id`),
  CONSTRAINT `FK5bbdfuitxii0b63v2v3f0r22x` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES (1,_binary '\0','thang map dit','$2a$10$5Nz2b05pzlOHsNiueoqpQuXKy8BxVIiYCMoBbzrr4a0fHdex.Kkg2','0123456789',20000,'admin',1),(2,_binary '\0','le quang hiep','$2a$10$MaooJRf4W/aeVpCiFqxhXO/7MnKa34Nl4/brP.I5LCh3Zv6IxV7Tu','0123456780',20000,'admin2',1),(3,_binary '\0','Clayton Skinner','$2a$10$uysVXjEJ9J4c51h6CGKtb.fjjXGNEKciBZmB56o8onQ/dIX4UeYMq','0234567895',819685,'nucudu',2),(4,_binary '\0','Basia Morton','$2a$10$dg0oRV1zeqeXxvF0vWzGmeAlQrdfTlbBmuHeWpRoC/Mtuq8dFvYEi','09757637386',19717200,'beparu',2),(5,_binary '\0','Larissa Gomez','$2a$10$ztlauAazWJEN50oCfaVRs.k1fm2Nh5mvQmsFpR57qyUCLAg3eTEte','0235692366',7940921,'luxecyku',2),(6,_binary '\0','Kaden Campos','$2a$10$654mdbLBKBbD9IaKrT/uAeo9je.c/VgvqkhK6UQLlkowrbkI119RC','0926239752',8011631,'nibezoj',2),(7,_binary '\0','Dominique Curry','$2a$10$SmqdHrUL4y289nWvGhGzReduz/GOxe0iGugwHXk3eNmd8eJAfwlT.','0923869235',4616424,'mebuko',1),(8,_binary '\0','Maya Howe','$2a$10$DOwz5AH8n04J5NoBSUb7eeXYYm/Cdaj4zlYzrRkzZ64L7vHwdouoy','0926523956',18619477,'dekitimu',5),(9,_binary '\0','Malcolm Glover','$2a$10$U0tnmvz.6OhyHtTVp6Z0/.ONiBhTCZLRsmcnC4R2oJr3TfGbjzqdm','0925239563',13954220,'pununyz',5);
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `violation`
--

DROP TABLE IF EXISTS `violation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `violation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `fines_percent` int NOT NULL,
  `violation_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jph9ae6udfgm4ob451vrj5wb` (`violation_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `violation`
--

LOCK TABLES `violation` WRITE;
/*!40000 ALTER TABLE `violation` DISABLE KEYS */;
/*!40000 ALTER TABLE `violation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `violation_detail`
--

DROP TABLE IF EXISTS `violation_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `violation_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `fines_percent` int NOT NULL,
  `number_of_violations` int NOT NULL,
  `salary_detail_id` int DEFAULT NULL,
  `violation_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh6jb8ykemmh45aq0ptu4341og` (`salary_detail_id`),
  KEY `FKol2yod7yx8scbdff24f214c5l` (`violation_id`),
  CONSTRAINT `FKh6jb8ykemmh45aq0ptu4341og` FOREIGN KEY (`salary_detail_id`) REFERENCES `salary_detail` (`id`),
  CONSTRAINT `FKol2yod7yx8scbdff24f214c5l` FOREIGN KEY (`violation_id`) REFERENCES `violation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `violation_detail`
--

LOCK TABLES `violation_detail` WRITE;
/*!40000 ALTER TABLE `violation_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `violation_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'restaurant'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-04-16  9:05:45
