--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (1,'24320182203222','6C8AD15971BB81E8265E80F524035C0C','李东儒',0,'63F3C310246D8A0979E7399E3C8BA7F8BB1E44581FDC2D3FDBB69468D9442C58','023DC3B93ACA1E21518808E466FCDDC0','2021-01-17 21:29:16','2021-01-18 00:27:13');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES (1,'123456789','F48BFEC4579985EDD194DCA52F2D79EA','XXX',1,'63F3C310246D8A0979E7399E3C8BA7F8BB1E44581FDC2D3FDBB69468D9442C58','023DC3B93ACA1E21518808E466FCDDC0','2021-01-18 13:39:57',NULL);
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'20210119','48DE5BF1EEF7D151B02A819DA32031EC','李',1,'C9F52917609EBC5734BEA45F8F4B291474D156E8FAC78E7DD6FF6A42D9669A97','51052B581D7E18953160B1A33CD59251',1,'8f05b664e3dcc189bd67bd0c9d51f3257a98c900346c01b222a648af09b52555','2021-01-19 00:40:00',NULL);
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;