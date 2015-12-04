# ************************************************************
# Sequel Pro SQL dump
# Version 4135
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: localhost (MySQL 5.5.42)
# Database: pauleyc_aps
# Generation Time: 2015-12-03 04:01:21 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table user_group
# ------------------------------------------------------------

LOCK TABLES `user_group` WRITE;
/*!40000 ALTER TABLE `user_group` DISABLE KEYS */;

INSERT INTO `user_group` (`groupid`, `name`, `description`, `created`)
VALUES
	(14,'web_user','Unauthenticated or unauthorized users.','2012-09-05 08:53:55'),
	(19,'registered_user','Previously authenticated users','2012-09-05 09:03:11'),
	(17,'admin','Administrator for application tasks.','2012-09-05 08:54:54'),
	(18,'sysadmin','Responsible for application configuration tasks.','2012-09-05 08:55:08');

/*!40000 ALTER TABLE `user_group` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user_role
# ------------------------------------------------------------

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;

INSERT INTO `user_role` (`roleid`, `name`, `description`, `created`)
VALUES
	(13,'administrator','Controls access','2012-09-05 08:59:30'),
	(14,'submitter','Adds content','2012-09-05 09:00:04'),
	(15,'editor','Updates on behalf of another','2012-09-05 09:00:34'),
	(16,'consumer','Views content','2012-09-05 09:01:07'),
	(17,'anonymous','Anyone','2012-09-05 09:01:42');

/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
