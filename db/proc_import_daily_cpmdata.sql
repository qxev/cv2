drop procedure IF EXISTS `cv2`.`proc_import_daily_cpmdata`;
DELIMITER $$
CREATE DEFINER=`cv2_production`@`192.168.1.%` PROCEDURE `cv2`.`proc_import_daily_cpmdata`(IN p_startday varchar(20),IN p_endday varchar(20))
BEGIN
	DECLARE done INT DEFAULT 0;

	DECLARE d_id int(11) DEFAULT 0;
	DECLARE d_advertiseAffiliateId int(11) default 0;
	DECLARE d_semId int(11) unsigned default 0;
	DECLARE d_advertiseId int(11) unsigned default 0;
	DECLARE d_trackCode varchar(50) default '';
	DECLARE d_campaignId int(11) default 0;
	DECLARE d_affiliateId int(11) default 0;
	DECLARE d_siteId int(11) default 0;
	DECLARE d_subSiteId varchar(255) default '';
	DECLARE d_dispalyType int(1) default 0;
	DECLARE d_dataType int(1) default 0;
	DECLARE d_dataTotal bigint(20) default 0;
	DECLARE d_ruleType int(4) unsigned default 0;
	DECLARE d_trackStatus int(1) default 0;
	DECLARE d_trackStep int(2) default 0;
	DECLARE d_orderid varchar(50) default '';
	DECLARE d_orderamount float(14,2) default 0;
	DECLARE d_orderinfo varchar(1024) default '';
	DECLARE d_trackIp varchar(255) default '';
	DECLARE d_trackUser varchar(50) default '';
	DECLARE d_referrerUrl varchar(2048) default '';
	DECLARE d_trackTime datetime;
	DECLARE d_createdAt datetime;
	DECLARE d_updatedAt datetime;
	
	DECLARE cur_in CURSOR FOR SELECT advertiseAffiliateId,semId,advertiseId,trackCode,campaignId,affiliateId,siteId,subSiteId,dispalyType,dataType,sum(dataTotal) as dataTotal,ruleType,trackStatus,trackStep,orderinfo,trackIp,trackUser,referrerUrl,DATE_FORMAT(trackTime,'%Y-%m-%d 00:00:00') as trackTime,createdAt,updatedAt,orderid,orderamount FROM `cv2`.`affiliatecpmdata` where trackTime >= p_startday and trackTime < p_endday and trackStep = 105 and trackStatus = 1 group by trackCode,DATE_FORMAT( trackTime, '%Y-%m-%d 00:00:00' );
	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;

	DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
		ROLLBACK;
		set AUTOCOMMIT = 1;
		insert into `cv2`.`report_task_result` (task_date,task_memo,task_result)
			values(sysdate(),concat('proc_import_daily_cpmdata(',p_startday,',',p_endday,')'),0);
	END;
	set AUTOCOMMIT = 0;

	OPEN cur_in;
		REPEAT
			FETCH cur_in INTO d_advertiseAffiliateId,d_semId,d_advertiseId,d_trackCode,d_campaignId,d_affiliateId,d_siteId,d_subSiteId,d_dispalyType,d_dataType,d_dataTotal,d_ruleType,d_trackStatus,d_trackStep,d_orderinfo,d_trackIp,d_trackUser,d_referrerUrl,d_trackTime,d_createdAt,d_updatedAt,d_orderid,d_orderamount;
			IF NOT done THEN
				if exists(select 1 from `cv2`.`affiliatetrackdata` where advertiseAffiliateId = d_advertiseAffiliateId and ruleType = 105 and trackStep = 105 and trackIp = d_trackIp and trackTime = d_trackTime) then
					update `cv2`.`affiliatetrackdata` set dataTotal = d_dataTotal,updatedAt = sysdate() where advertiseAffiliateId = d_advertiseAffiliateId and ruleType = 105 and trackStep = 105 and trackIp = d_trackIp and trackTime = d_trackTime;
				else
					insert into `cv2`.`affiliatetrackdata` (advertiseAffiliateId,semId,advertiseId,trackCode,campaignId,affiliateId,siteId,subSiteId,dispalyType,dataType,dataTotal,ruleType,trackStatus,trackStep,orderinfo,trackIp,trackUser,referrerUrl,trackTime,createdAt,orderid,orderamount) 
										values(d_advertiseAffiliateId,d_semId,d_advertiseId,d_trackCode,d_campaignId,d_affiliateId,d_siteId,d_subSiteId,d_dispalyType,d_dataType,d_dataTotal,d_ruleType,d_trackStatus,d_trackStep,d_orderinfo,d_trackIp,d_trackUser,d_referrerUrl,d_trackTime,d_createdAt,d_orderid,d_orderamount);
				end if;
			END IF;
		UNTIL done END REPEAT; 
	CLOSE cur_in;

	COMMIT;

	set AUTOCOMMIT = 1;
END $$

DELIMITER ;
