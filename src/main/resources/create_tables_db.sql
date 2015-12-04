-- phpMyAdmin SQL Dump
-- version 4.0.6
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 17, 2015 at 11:15 PM
-- Server version: 5.5.33
-- PHP Version: 5.5.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `gpxdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `temp_pass` varchar(255) DEFAULT NULL,
  `temp_pass_active` tinyint(1) NOT NULL DEFAULT '0',
  `active` int(11) NOT NULL DEFAULT '0',
  `level_access` int(11) NOT NULL DEFAULT '2',
  `hash` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `shared_secret` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  PRIMARY KEY (`userid`),
  UNIQUE KEY `email` (`email`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=49 ;

-- --------------------------------------------------------

--
-- Table structure for table `user_group`
--

CREATE TABLE `user_group` (
  `groupid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `description` varchar(70) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  PRIMARY KEY (`groupid`),
  UNIQUE KEY `name` (`name`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=38 ;

-- --------------------------------------------------------

--
-- Table structure for table `user_group_members`
--

CREATE TABLE `user_group_members` (
  `gmid` int(11) NOT NULL AUTO_INCREMENT,
  `groupid` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
  `created` datetime DEFAULT NULL,
  PRIMARY KEY (`gmid`),
  UNIQUE KEY `group_user` (`groupid`,`userid`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

-- --------------------------------------------------------

--
-- Table structure for table `user_login_token`
--

CREATE TABLE `user_login_token` (
  `id` int(9) NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `token` varchar(36) NOT NULL COMMENT 'uuid',
  `userid` int(9) NOT NULL COMMENT 'fk to user.id',
  `set_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `token` (`token`,`userid`),
  KEY `userid` (`userid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;

-- --------------------------------------------------------

--
-- Table structure for table `user_role`
--

CREATE TABLE `user_role` (
  `roleid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `description` varchar(70) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  PRIMARY KEY (`roleid`),
  UNIQUE KEY `name` (`name`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=18 ;

-- --------------------------------------------------------

--
-- Table structure for table `user_role_members`
--

CREATE TABLE `user_role_members` (
  `rmid` int(11) NOT NULL AUTO_INCREMENT,
  `roleid` int(11) NOT NULL,
  `groupid` int(11) NOT NULL,
  `created` datetime DEFAULT NULL,
  PRIMARY KEY (`rmid`),
  UNIQUE KEY `role_group` (`groupid`,`roleid`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=13 ;

-- --------------------------------------------------------

--
-- Table structure for table `user_session`
--

CREATE TABLE `user_session` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `sessionid` varchar(255) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '0',
  `secure` tinyint(1) NOT NULL DEFAULT '0',
  `created_time` datetime DEFAULT NULL,
  `last_active` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sessionid` (`sessionid`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=50 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
