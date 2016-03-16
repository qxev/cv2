drop procedure IF EXISTS `cv2`.`proc_import_daily_trackdata`;
DELIMITER $$
CREATE DEFINER=`cv2_production`@`192.168.1.%` PROCEDURE `cv2`.`proc_import_daily_trackdata`()
BEGIN
	DECLARE p_startday datetime;
	DECLARE p_endday datetime;
	DECLARE p_addminutes int(11) default 0;

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
	DECLARE d_country varchar(100) default '';
	DECLARE d_city varchar(100) default '';
	
	DECLARE cur_in CURSOR FOR SELECT id,advertiseAffiliateId,semId,advertiseId,trackCode,campaignId,affiliateId,siteId,subSiteId,dispalyType,dataType,dataTotal,ruleType,trackStatus,trackStep,orderinfo,trackIp,trackUser,referrerUrl,trackTime,createdAt,updatedAt,orderid,orderamount,country,city FROM `cv2`.`affiliatetrackdata` where trackTime >= p_startday and trackTime < p_endday;
	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;

	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN
		insert into `cv2`.`report_task_result` (task_date,task_memo,task_result)
			values(sysdate(),concat('proc_import_daily_trackdata(',p_startday,',',p_endday,')'),d_id);
	END;

	set AUTOCOMMIT = 1;

	truncate `cv2`.`report_trackdata`;

	select timecval,addminutes into p_startday,p_addminutes from `cv2`.`report_timestamp` where timetype = 'track_import';
	set p_endday = DATE_ADD(p_startday, INTERVAL p_addminutes SECOND);

	OPEN cur_in;
		REPEAT
			FETCH cur_in INTO d_id,d_advertiseAffiliateId,d_semId,d_advertiseId,d_trackCode,d_campaignId,d_affiliateId,d_siteId,d_subSiteId,d_dispalyType,d_dataType,d_dataTotal,d_ruleType,d_trackStatus,d_trackStep,d_orderinfo,d_trackIp,d_trackUser,d_referrerUrl,d_trackTime,d_createdAt,d_updatedAt,d_orderid,d_orderamount,d_country,d_city;
			IF NOT done THEN
				if exists(select 1 from `cv2`.`report_trackdata` where id = d_id) then
					delete from `cv2`.`report_trackdata` where id = d_id;
				end if;
				if exists(select 1 from `cv2`.`campaign` where id = d_campaignId) and exists(select 1 from `cv2`.`advertise` where campaignId = d_campaignId and id = d_advertiseId) then
					if exists(select 1 from `cv2`.`site` where userId = d_affiliateId and id = d_siteId) then
						if d_advertiseAffiliateId = 0 then
							if not exists(select 1 from `cv2`.`advertiseaffiliate` where advertiseId = d_advertiseId and siteId = d_siteId)  then
								insert into `cv2`.`advertiseaffiliate` (advertiseId,siteId,status,createdAt) values(d_advertiseId,d_siteId,2,sysdate());
							end if;
							select id into d_advertiseAffiliateId from `cv2`.`advertiseaffiliate` where advertiseId = d_advertiseId and siteId = d_siteId order by createdAt asc limit 0,1;
						end if;
						if d_advertiseAffiliateId > 0 then
							if exists(select 1 from `cv2`.`advertiseaffiliate` where advertiseId = d_advertiseId and siteId = d_siteId) then
								select id into d_advertiseAffiliateId from `cv2`.`advertiseaffiliate` where advertiseId = d_advertiseId and siteId = d_siteId order by createdAt asc limit 0,1;
								if not exists(select 1 from `cv2`.`report_trackdata` where advertiseAffiliateId = d_advertiseAffiliateId and ruleType = d_ruleType and trackStep = d_trackStep and trackIp = d_trackIp and trackTime = d_trackTime) then
									insert into `cv2`.`report_trackdata` (id,advertiseAffiliateId,semId,advertiseId,trackCode,campaignId,affiliateId,siteId,subSiteId,dispalyType,dataType,dataTotal,ruleType,trackStatus,trackStep,orderinfo,trackIp,trackUser,referrerUrl,trackTime,createdAt,updatedAt,orderid,orderamount,country,city) 
												values(d_id,d_advertiseAffiliateId,d_semId,d_advertiseId,d_trackCode,d_campaignId,d_affiliateId,d_siteId,d_subSiteId,d_dispalyType,d_dataType,d_dataTotal,d_ruleType,d_trackStatus,d_trackStep,d_orderinfo,d_trackIp,d_trackUser,d_referrerUrl,d_trackTime,d_createdAt,d_updatedAt,d_orderid,d_orderamount,d_country,d_city);
								end if;
							end if;
						end if;
					end if;
				end if;
			END IF;
		UNTIL done END REPEAT;
	CLOSE cur_in;
	
	update `cv2`.`report_timestamp` set timecval = p_endday where timetype = 'track_import';

	set AUTOCOMMIT = 1;

	call `cv2`.proc_parse_daily_commision();
END $$

DELIMITER ;