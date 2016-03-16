/*
MySQL Backup
Source Host:           192.168.1.199
Source Server Version: 5.0.45-log
Source Database:       dw_ms
Date:                  2008/12/08 11:38:37
*/

SET FOREIGN_KEY_CHECKS=0;
use dw_ms;
#----------------------------
# Table structure for ms_config
#----------------------------
CREATE TABLE `ms_config` (
  `id` bigint(20) NOT NULL auto_increment,
  `c_name` varchar(255) NOT NULL,
  `c_value` varchar(255) NOT NULL default '',
  `created_at` datetime default NULL,
  `updated_at` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=128 DEFAULT CHARSET=utf8;
#----------------------------
# No records for table ms_config
#----------------------------

#----------------------------
# Table structure for ms_database
#----------------------------
CREATE TABLE `ms_database` (
  `id` bigint(20) NOT NULL auto_increment,
  `db_name` varchar(60) NOT NULL,
  `db_connection` varchar(500) NOT NULL,
  `db_username` varchar(100) NOT NULL,
  `db_password` varchar(50) NOT NULL,
  `created_at` datetime default NULL,
  `updated_at` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;
#----------------------------
# No records for table ms_database
#----------------------------

#----------------------------
# Table structure for ms_datasource
#----------------------------
CREATE TABLE `ms_datasource` (
  `id` bigint(20) NOT NULL auto_increment,
  `task_id` bigint(20) NOT NULL default '0',
  `db_id` bigint(20) NOT NULL default '0',
  `ds_from_type` smallint(2) NOT NULL default '0',
  `ds_usage_type` smallint(2) NOT NULL default '0',
  `ds_value` text NOT NULL,
  `ds_parameter_name` varchar(80) default NULL,
  `created_at` datetime default NULL,
  `updated_at` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;
#----------------------------
# No records for table ms_datasource
#----------------------------

#----------------------------
# Table structure for ms_log
#----------------------------
CREATE TABLE `ms_log` (
  `id` bigint(20) NOT NULL auto_increment,
  `report_id` bigint(20) NOT NULL,
  `l_date` datetime NOT NULL,
  `l_toemail` varchar(255) NOT NULL,
  `l_tonick` varchar(255) NOT NULL,
  `l_succeed` smallint(1) NOT NULL default '0',
  `l_retry` smallint(4) NOT NULL default '0',
  `l_reason` text NOT NULL,
  `created_at` datetime default NULL,
  `updated_at` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
#----------------------------
# No records for table ms_log
#----------------------------

#----------------------------
# Table structure for ms_report
#----------------------------
CREATE TABLE `ms_report` (
  `id` bigint(20) NOT NULL,
  `task_id` bigint(20) NOT NULL,
  `r_date` date NOT NULL,
  `r_succeed` bigint(11) NOT NULL default '0',
  `r_failed` bigint(11) NOT NULL default '0',
  `created_at` datetime default NULL,
  `updated_at` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
#----------------------------
# No records for table ms_report
#----------------------------

#----------------------------
# Table structure for ms_task
#----------------------------
CREATE TABLE `ms_task` (
  `id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) NOT NULL default '0',
  `tk_name` varchar(255) NOT NULL default '',
  `tk_subject` text,
  `tk_start_date` date default NULL,
  `tk_end_date` date default NULL,
  `tk_repeat_mode` smallint(2) NOT NULL default '0',
  `tk_repeat_at` smallint(2) NOT NULL default '0',
  `tk_execed_times` int(10) NOT NULL default '0',
  `tk_enabled` smallint(1) NOT NULL default '0',
  `created_at` datetime default NULL,
  `updated_at` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
#----------------------------
# No records for table ms_task
#----------------------------

#----------------------------
# Table structure for ms_template
#----------------------------
CREATE TABLE `ms_template` (
  `id` bigint(20) NOT NULL auto_increment,
  `task_id` bigint(20) NOT NULL default '0',
  `tp_path` varchar(255) NOT NULL default '',
  `tp_is_attachment` smallint(1) NOT NULL default '0',
  `tp_content_type` smallint(1) NOT NULL default '0',
  `tp_enabled` smallint(1) NOT NULL default '0',
  `created_at` datetime default NULL,
  `updated_at` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;
#----------------------------
# No records for table ms_template
#----------------------------

#----------------------------
# Table structure for ms_user
#----------------------------
CREATE TABLE `ms_user` (
  `id` bigint(20) NOT NULL auto_increment,
  `u_username` varchar(80) NOT NULL,
  `u_password` varchar(50) NOT NULL,
  `u_email` varchar(100) NOT NULL,
  `description` varchar(255) default NULL,
  `created_at` datetime default NULL,
  `updated_at` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=202 DEFAULT CHARSET=utf8;
#----------------------------
# Records for table ms_user
#----------------------------


insert  into ms_user values 
(186, 'admin', '698d51a19d8a121ce581499d7b701668', 'yinhua625@126.com', '111', '2008-12-01 10:24:10', '2008-12-01 10:24:10');

