drop procedure IF EXISTS `cv2`.`proc_parse_daily_commision`;
DELIMITER $$
CREATE DEFINER=`cv2_production`@`192.168.1.%` PROCEDURE `cv2`.`proc_parse_daily_commision`()
BEGIN
	DECLARE done INT DEFAULT 0;

	DECLARE d_id int(11) DEFAULT 0;
	DECLARE d_advertiseAffiliateId int(11) default 0;
	DECLARE d_semId int(11) unsigned default 0;
	DECLARE d_advertiseId int(11) unsigned default 0;
	DECLARE d_advertiserId int(11) unsigned default 0;
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
	DECLARE d_orderamount float(14,3) default 0;
	DECLARE d_orderinfo varchar(1024) default '';
	DECLARE d_trackIp varchar(255) default '';
	DECLARE d_trackUser varchar(50) default '';
	DECLARE d_referrerUrl varchar(2048) default '';
	DECLARE d_trackTime datetime;
	DECLARE d_createdAt datetime;
	DECLARE d_updatedAt datetime;
	DECLARE d_country varchar(100) default '';
	DECLARE d_city varchar(100) default '';

	DECLARE d_siteCommisionOld float(12,3) default 0;
	DECLARE d_siteCommisionNew float(12,3) default 0;
	DECLARE d_darwCommisionOld float(12,3) default 0;
	DECLARE d_darwCommisionNew float(12,3) default 0;
	DECLARE d_matched smallint(1) default 0;
	DECLARE d_matchId varchar(30) default '';

	DECLARE d_commissionType int(4) default -1;
	DECLARE d_commissionRuleType int(4) default -1;

	DECLARE d_commissionValue float(12,3) default 0;
	DECLARE d_darwinCommissionValue float(12,3) default 0;
	
	DECLARE cur_in CURSOR FOR SELECT id,advertiseAffiliateId,semId,advertiseId,trackCode,campaignId,affiliateId,siteId,subSiteId,dispalyType,dataType,dataTotal,ruleType,trackStatus,trackStep,orderinfo,trackIp,trackUser,referrerUrl,trackTime,createdAt,updatedAt,orderid,orderamount,country,city FROM `cv2`.`report_trackdata`;
	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;

	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN
		insert into `cv2`.`report_task_result` (task_date,task_memo,task_result)
			values(sysdate(),concat('proc_parse_daily_commision()'),d_id);
	END;

	set AUTOCOMMIT = 1;

	OPEN cur_in;
		REPEAT
			FETCH cur_in INTO d_id,d_advertiseAffiliateId,d_semId,d_advertiseId,d_trackCode,d_campaignId,d_affiliateId,d_siteId,d_subSiteId,d_dispalyType,d_dataType,d_dataTotal,d_ruleType,d_trackStatus,d_trackStep,d_orderinfo,d_trackIp,d_trackUser,d_referrerUrl,d_trackTime,d_createdAt,d_updatedAt,d_orderid,d_orderamount,d_country,d_city;
			IF NOT done THEN
				if exists(select 1 from `cv2`.`semclient` where clientId = d_semId) then
					select advertiserId into d_advertiserId from `cv2`.`semclient` where clientId = d_semId;
				end if;
				set d_siteCommisionOld = 0;
				set d_darwCommisionOld = 0;
				if d_ruleType >= 100 then
					if exists(select 1 from `cv2`.`commissionrule` where campaignId = d_campaignId and ruleType = d_ruleType and verified = 2 and startDate <= date(d_trackTime) and endDate >= date(d_trackTime)) then
						/**Find out the current commsion configuration**/
						select commissionType,ruleType,commissionValue,darwinCommissionValue 
							into d_commissionType,d_commissionRuleType,d_commissionValue,d_darwinCommissionValue 
								from `cv2`.`commissionrule` 
									where campaignId = d_campaignId and ruleType = d_ruleType and verified = 2 and startDate <= date(d_trackTime) and endDate >= date(d_trackTime);
						/*commissionType: 0: percent   1: fixed number*/
						/*ruleType:100:CPC 101:CPL 102:CPS */
						if d_commissionRuleType >= 100 then
							if d_commissionType = 2 then
								/*CPS by percent only*/
								if d_commissionRuleType = 102 then
									set d_siteCommisionOld = d_orderamount*(d_commissionValue/100);
									set d_darwCommisionOld = d_orderamount*(d_darwinCommissionValue/100);
								end if;
							elseif d_commissionType = 1 then
								if d_commissionRuleType = 105 then
									/**CPM**/
									set d_siteCommisionOld = (d_dataTotal/1000)*d_commissionValue;
									set d_darwCommisionOld = (d_dataTotal/1000)*d_darwinCommissionValue;
								else
									set d_siteCommisionOld = d_dataTotal*d_commissionValue;
									set d_darwCommisionOld = d_dataTotal*d_darwinCommissionValue;
								end if;
							end if;
						end if;
					end if;
				end if;
				if exists(select 1 from `cv2`.`report_trackmatch` where id = d_id) then
					update `cv2`.`report_trackmatch` 
						set advertiseAffiliateId = d_advertiseAffiliateId,semId = d_semId,advertiserId = d_advertiseId,trackCode = d_trackCode,campaignId = d_campaignId,affiliateId = d_affiliateId,siteId = d_siteId,subSiteId = d_subSiteId,dispalyType = d_dispalyType,dataType = d_dataType,dataTotal = d_dataTotal,ruleType = d_ruleType,trackStatus = d_trackStatus,trackStep = d_trackStep,orderinfo = d_orderinfo,trackIp = d_trackIp,trackUser = d_trackUser,referrerUrl = d_referrerUrl,trackTime = d_trackTime,createdAt = d_createdAt,updatedAt = d_updatedAt,siteCommisionOld = d_siteCommisionOld,siteCommisionNew = d_siteCommisionNew,darwCommisionOld = d_darwCommisionOld,darwCommisionNew = d_darwCommisionNew,matched = d_matched,matchId = d_matchId,orderid = d_orderid,orderamount = d_orderamount,country = d_country,city = d_city 
							where id = d_id and matched = 0;
				else
					insert into `cv2`.`report_trackmatch` (id,advertiseAffiliateId,semId,advertiserId,trackCode,campaignId,affiliateId,siteId,subSiteId,dispalyType,dataType,dataTotal,ruleType,trackStatus,trackStep,orderinfo,trackIp,trackUser,referrerUrl,trackTime,createdAt,updatedAt,siteCommisionOld,siteCommisionNew,darwCommisionOld,darwCommisionNew,matched,matchId,orderid,orderamount,country,city) 
										values(d_id,d_advertiseAffiliateId,d_semId,d_advertiserId,d_trackCode,d_campaignId,d_affiliateId,d_siteId,d_subSiteId,d_dispalyType,d_dataType,d_dataTotal,d_ruleType,d_trackStatus,d_trackStep,d_orderinfo,d_trackIp,d_trackUser,d_referrerUrl,d_trackTime,d_createdAt,d_updatedAt,d_siteCommisionOld,d_siteCommisionNew,d_darwCommisionOld,d_darwCommisionNew,d_matched,d_matchId,d_orderid,d_orderamount,d_country,d_city);
				end if;
			END IF;
		UNTIL done END REPEAT;
	CLOSE cur_in;

	set AUTOCOMMIT = 1;


END $$

DELIMITER ;