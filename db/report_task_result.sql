DROP TABLE IF EXISTS `cv2`.`report_task_result`;
CREATE TABLE  `cv2`.`report_task_result` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `task_date` datetime NOT NULL,
  `task_memo` varchar(2048) default NULL,
  `task_result` smallint(5) unsigned NOT NULL default '1',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;