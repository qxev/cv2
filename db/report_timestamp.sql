DROP TABLE IF EXISTS `cv2`.`report_timestamp`;
CREATE TABLE  `cv2`.`report_timestamp` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `timetype` varchar(45) NOT NULL default '',
  `timecval` datetime NOT NULL,
  `addminutes` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `rpt_ttsTimeType` (`timetype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;