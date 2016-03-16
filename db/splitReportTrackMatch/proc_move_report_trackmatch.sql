set names utf8;
use cv2;
DROP PROCEDURE IF EXISTS `cv2`.`proc_move_report_trackmatch`;
DELIMITER $$
CREATE PROCEDURE `cv2`.`proc_move_report_trackmatch`(IN p_startday varchar(20),IN p_endday varchar(20))
BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE d_id int(11) DEFAULT 0;
	DECLARE d_advertiserId int(11) DEFAULT 0;
	DECLARE d_campaignId int(11) DEFAULT 0;
	DECLARE d_affiliateId int(11) DEFAULT 0;
	DECLARE d_siteId int(11) DEFAULT 0;
	DECLARE d_subSiteId varchar(255) DEFAULT '';
	DECLARE d_advertiseAffiliateId int(11) DEFAULT 0;
	DECLARE d_semId int(11) unsigned DEFAULT 0;
	DECLARE d_trackCode varchar(50) DEFAULT '';
	DECLARE d_dispalyType int(1) DEFAULT 0;
	DECLARE d_dataType int(1) DEFAULT 0;
	DECLARE d_dataTotal bigint(20) DEFAULT 0;
	DECLARE d_ruleType int(4) DEFAULT 0;
	DECLARE d_trackStatus int(1) DEFAULT 0;
	DECLARE d_trackStep int(2) DEFAULT 0;
	DECLARE d_orderid varchar(50) DEFAULT '';
	DECLARE d_orderamount float(14,2) DEFAULT 0;
	DECLARE d_orderinfo varchar(1024) DEFAULT '';
	DECLARE d_trackIp varchar(255) DEFAULT '';
	DECLARE d_trackUser varchar(50) DEFAULT '';
	DECLARE d_referrerUrl varchar(2048) DEFAULT '';
	DECLARE d_trackTime datetime;
	DECLARE d_siteCommisionOld float(12,3) DEFAULT 0;
	DECLARE d_siteCommisionNew float(12,3) DEFAULT 0;
	DECLARE d_darwCommisionOld float(12,3) DEFAULT 0;
	DECLARE d_darwCommisionNew float(12,3) DEFAULT 0;
	DECLARE d_matched smallint(1) DEFAULT 0;
	DECLARE d_matchId varchar(50) DEFAULT '';
	DECLARE d_createdAt datetime;
	DECLARE d_updatedAt datetime;
	DECLARE d_country varchar(100) DEFAULT '';
	DECLARE d_city varchar(100) DEFAULT '';
	
	
	DECLARE cur_in CURSOR FOR SELECT `id`,`advertiserId`,`campaignId`,`affiliateId`,`siteId`,`subSiteId`,`advertiseAffiliateId`,`semId`,`trackCode`,`dispalyType`,`dataType`,`dataTotal`,`ruleType`,`trackStatus`,`trackStep`,`orderid`,`orderamount`,`orderinfo`,`trackIp`,`trackUser`,`referrerUrl`,`trackTime`,`siteCommisionOld`,`siteCommisionNew`,`darwCommisionOld`,`darwCommisionNew`,`matched`,`matchId`,`createdAt`,`updatedAt`,`country`,`city` FROM `report_trackmatch_temp` where `trackTime` >= p_startday and `trackTime` < p_endday ORDER BY `tracktime` DESC limit 1000000;
	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;
	
	set AUTOCOMMIT = 1;
	
	OPEN cur_in;
		REPEAT
			FETCH cur_in INTO d_id, d_advertiserId, d_campaignId, d_affiliateId, d_siteId, d_subSiteId, d_advertiseAffiliateId, d_semId, d_trackCode, d_dispalyType, d_dataType, d_dataTotal, d_ruleType, d_trackStatus, d_trackStep, d_orderid, d_orderamount, d_orderinfo, d_trackIp, d_trackUser, d_referrerUrl, d_trackTime, d_siteCommisionOld, d_siteCommisionNew, d_darwCommisionOld, d_darwCommisionNew, d_matched, d_matchId, d_createdAt, d_updatedAt, d_country, d_city;
				IF NOT done THEN
					INSERT INTO `report_trackmatch_test` (`id`,`advertiserId`,`campaignId`,`affiliateId`,`siteId`,`subSiteId`,`advertiseAffiliateId`,`semId`,`trackCode`,`dispalyType`,`dataType`,`dataTotal`,`ruleType`,`trackStatus`,`trackStep`,`orderid`,`orderamount`,`orderinfo`,`trackIp`,`trackUser`,`referrerUrl`,`trackTime`,`siteCommisionOld`,`siteCommisionNew`,`darwCommisionOld`,`darwCommisionNew`,`matched`,`matchId`,`createdAt`,`updatedAt`,`country`,`city`)
						VALUES(d_id, d_advertiserId, d_campaignId, d_affiliateId, d_siteId, d_subSiteId, d_advertiseAffiliateId, d_semId, d_trackCode, d_dispalyType, d_dataType, d_dataTotal, d_ruleType, d_trackStatus, d_trackStep, d_orderid, d_orderamount, d_orderinfo, d_trackIp, d_trackUser, d_referrerUrl, d_trackTime, d_siteCommisionOld, d_siteCommisionNew, d_darwCommisionOld, d_darwCommisionNew, d_matched, d_matchId, d_createdAt, d_updatedAt, d_country, d_city);
				END IF;
		UNTIL done END REPEAT;
	CLOSE cur_in;
	
	set AUTOCOMMIT = 1;
END $$ 
DELIMITER ;
