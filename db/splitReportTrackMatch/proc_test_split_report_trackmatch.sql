DROP PROCEDURE IF EXISTS `cv2`.`proc_test_split_report_trackmatch`;
DELIMITER $$
CREATE PROCEDURE `cv2`.`proc_test_split_report_trackmatch`()
BEGIN
	DECLARE d_index int(11) DEFAULT 0;
	
	DROP TABLE IF EXISTS `cv2`.`report_trackmatch_test`;
	
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
	  KEY `rpt_thOrderid` (`orderid`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	
	set AUTOCOMMIT = 1;
	
	WHILE d_index<30000000 do
		set d_index = d_index+1;
		select d_index;
		INSERT INTO `report_trackmatch_test` (`id`,`advertiserId`,`campaignId`,`affiliateId`,`siteId`,`subSiteId`,`advertiseAffiliateId`,`semId`,`trackCode`,`dispalyType`,`dataType`,`dataTotal`,`ruleType`,`trackStatus`,`trackStep`,`orderid`,`orderamount`,`orderinfo`,`trackIp`,`trackUser`,`referrerUrl`,`trackTime`,`siteCommisionOld`,`siteCommisionNew`,`darwCommisionOld`,`darwCommisionNew`,`matched`,`matchId`,`createdAt`,`updatedAt`,`country`,`city`)
		VALUES(d_index, d_index+1, d_index+1, d_index+1, d_index+1, d_index+1, d_index+1, d_index+1, CONCAT('d_trackCode',d_index+1), 0, 0, 100, 100, 0, 0, CONCAT('order_id',d_index+1), 0, CONCAT('order_info',d_index+1), '192.168.1.1', CONCAT('user',d_index+1), CONCAT('http://www.clickvalue.cn',d_index+1), now(), 0, 0, 0, 0, 1, 'xxxx', now(), now(), '', '');
	end while;
	
 	set AUTOCOMMIT = 1;
 	
END
$$ DELIMITER ;