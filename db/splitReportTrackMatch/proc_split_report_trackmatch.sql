set names utf8;
use cv2;
DROP PROCEDURE IF EXISTS `cv2`.`proc_split_report_trackmatch`;
DELIMITER $$
CREATE PROCEDURE `cv2`.`proc_split_report_trackmatch`()
BEGIN
	DECLARE d_name varchar(255) default 'report_trackmatch_';
	DECLARE d_split_date varchar(20) default DATE_FORMAT(DATE_SUB(now(), INTERVAL 2 MONTH),'%Y-%m-01');
	DECLARE d_start_date datetime;
	DECLARE d_end_date datetime;
	SET d_name = CONCAT(d_name,DATE_FORMAT(DATE_SUB(now(), INTERVAL 3 MONTH),'%Y%m'));
	
	ALTER TABLE `report_trackmatch_test` RENAME TO `report_trackmatch_temp`;
	
 	CREATE TABLE `report_trackmatch_test` (
	  `id` int(11) NOT NULL,
	  `advertiserId` int(11) unsigned default NULL,
	  `campaignId` int(11) default NULL,
	  `affiliateId` int(11) default NULL,
	  `siteId` int(11) default NULL,
	  `subSiteId` varchar(255) default NULL,
	  `advertiseAffiliateId` int(11) default NULL,
	  `semId` int(11) unsigned default NULL,
	  `trackCode` varchar(50) default NULL,
	  `dispalyType` int(1) default '0',
	  `dataType` int(1) default '0',
	  `dataTotal` bigint(20) default '0',
	  `ruleType` int(4) unsigned default NULL,
	  `trackStatus` int(1) default '0',
	  `trackStep` int(2) default '0',
	  `orderid` varchar(50) default '',
	  `orderamount` float(14,2) default '0.00',
	  `orderinfo` varchar(1024) default NULL,
	  `trackIp` varchar(255) default NULL,
	  `trackUser` varchar(50) default NULL,
	  `referrerUrl` varchar(2048) default NULL,
	  `trackTime` datetime default NULL,
	  `siteCommisionOld` float(12,3) default '0.000',
	  `siteCommisionNew` float(12,3) default '0.000',
	  `darwCommisionOld` float(12,3) default '0.000',
	  `darwCommisionNew` float(12,3) default '0.000',
	  `matched` smallint(1) default '0',
	  `matchId` varchar(50) default NULL,
	  `createdAt` datetime default NULL,
	  `updatedAt` datetime default NULL,
	  `country` varchar(100) default '',
	  `city` varchar(100) default '',
	  PRIMARY KEY  (`id`),
	  UNIQUE KEY `rpt_thuid` (`advertiseAffiliateId`,`ruleType`,`trackStep`,`trackIp`,`trackTime`),
	  KEY `rpt_thTrackStep` (`trackStep`),
	  KEY `rpt_thTrackTime` (`trackTime`),
	  KEY `rpt_thTrackIp` (`trackIp`),
	  KEY `rpt_thTrackStatus` (`trackStatus`),
	  KEY `rpt_thCampaignId` (`campaignId`),
	  KEY `rpt_thAffiliateId` (`affiliateId`),
	  KEY `rpt_thTrackUser` (`trackUser`),
	  KEY `rpt_thTrackCode` (`trackCode`),
	  KEY `rpt_thSemId` (`semId`),
	  KEY `rpt_thAdvertiseId` (`advertiserId`),
	  KEY `rpt_thTrackType` (`ruleType`),
	  KEY `rpt_thMatchId` (`matchId`),
	  KEY `rpt_thMatched` (`matched`),
	  KEY `rpt_thAdvertiseAffiliateId` (`advertiseAffiliateId`),
	  KEY `rpt_thSubSiteId` (`subSiteId`),
	  KEY `rpt_thSiteId` (`siteId`),
	  KEY `rpt_thOrderid` (`orderid`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	
	SET d_end_date = DATE_ADD(CURDATE(), INTERVAL 1 DAY);
	SET d_start_date = d_end_date;
	WHILE d_start_date > d_split_date do
		SET d_start_date = DATE_SUB(d_end_date, INTERVAL 1 DAY);
		select d_start_date,d_end_date;
		call `cv2`.`proc_move_report_trackmatch`(DATE_FORMAT(d_start_date,'%Y-%m-%d'),DATE_FORMAT(d_end_date,'%Y-%m-%d'));
		SET d_end_date = d_start_date;
	end WHILE;
	
 	SET @sql = concat('ALTER TABLE `report_trackmatch_temp` RENAME TO `',d_name,'`');
	prepare fmd from @sql;
 	execute fmd;
 	deallocate prepare fmd;
 	
END $$ 
DELIMITER ;