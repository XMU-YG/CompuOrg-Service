-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: localhost    Database: compuorg
-- ------------------------------------------------------
-- Server version	8.0.20

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_no` varchar(32) DEFAULT NULL,
  `password` varchar(128) NOT NULL,
  `student_name` varchar(32) DEFAULT NULL,
  `gender` tinyint DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `email_verify` tinyint(3) unsigned zerofill NOT NULL,
  `mobile` varchar(128) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teacher` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `teacher_no` varchar(32) NOT NULL,
  `password` varchar(128) NOT NULL,
  `teacher_name` varchar(32) DEFAULT NULL,
  `gender` tinyint DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `email_verify` tinyint(3) unsigned zerofill NOT NULL,
  `mobile` varchar(128) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `admin_no` varchar(32) NOT NULL,
  `password` varchar(128) NOT NULL,
  `admin_name` varchar(45) DEFAULT NULL,
  `gender` tinyint DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `mobile` varchar(128) DEFAULT NULL,
  `email_verify` tinyint DEFAULT NULL,
  `signature` varchar(500) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `static_memory`
--

DROP TABLE IF EXISTS `static_memory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `static_memory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_no` varchar(32) DEFAULT NULL,
  `address` varchar(32) DEFAULT NULL,
  `data` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `sign_in`
--

DROP TABLE IF EXISTS `sign_in`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sign_in` (
  `id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  `sign_in` datetime DEFAULT NULL,
  `sign_out` datetime DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `test`
--

DROP TABLE IF EXISTS `test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `experiment_id` bigint NOT NULL,
  `test_content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `test_result`
--

DROP TABLE IF EXISTS `test_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_result` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL,
  `test_id` bigint NOT NULL,
  `answer` varchar(1024) DEFAULT NULL,
  `score` int DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `student` (`student_id`),
  KEY `test` (`test_id`),
  CONSTRAINT `student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
  CONSTRAINT `test` FOREIGN KEY (`test_id`) REFERENCES `test` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;