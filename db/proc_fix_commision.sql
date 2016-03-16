drop procedure IF EXISTS `cv2`.`proc_fix_commision`;
DELIMITER $$
CREATE DEFINER=`cv2_production`@`192.168.1.%` PROCEDURE `cv2`.`proc_fix_commision`(IN p_startDate datetime,IN p_endDate datetime,IN p_campaignId bigint(20))
BEGIN
	DECLARE done INT DEFAULT 0;

	DECLARE d_id int(11) DEFAULT 0;
	DECLARE d_advertiseAffiliateId int(11) default 0;
	DECLARE d_semId int(11) unsigned default 0;
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

	DECLARE d_siteCommisionOld float(12,2) default 0;
	DECLARE d_siteCommisionNew float(12,2) default 0;
	DECLARE d_darwCommisionOld float(12,2) default 0;
	DECLARE d_darwCommisionNew float(12,2) default 0;

	DECLARE d_siteCommisionOldFix float(12,2) default 0;
	DECLARE d_darwCommisionOldFix float(12,2) default 0;

	DECLARE d_matched smallint(1) default 0;
	DECLARE d_matchId varchar(30) default '';

	DECLARE d_commissionType int(4) default -1;
	DECLARE d_commissionRuleType int(4) default -1;

	DECLARE d_commissionValue float(12,2) default 0;
	DECLARE d_darwinCommissionValue float(12,2) default 0;
	
	DECLARE cur_in CURSOR FOR SELECT id,advertiseAffiliateId,semId,advertiserId,trackCode,campaignId,affiliateId,siteId,subSiteId,dispalyType,dataType,dataTotal,ruleType,trackStatus,trackStep,orderinfo,trackIp,trackUser,referrerUrl,trackTime,createdAt,siteCommisionOld,siteCommisionNew,darwCommisionOld,darwCommisionNew,matched,matchId,orderid,orderamount,country,city FROM `cv2`.`report_trackmatch` where trackTime >= p_startDate and trackTime < p_endDate;
	DECLARE cur_in_cp CURSOR FOR SELECT id,advertiseAffiliateId,semId,advertiserId,trackCode,campaignId,affiliateId,siteId,subSiteId,dispalyType,dataType,dataTotal,ruleType,trackStatus,trackStep,orderinfo,trackIp,trackUser,referrerUrl,trackTime,createdAt,siteCommisionOld,siteCommisionNew,darwCommisionOld,darwCommisionNew,matched,matchId,orderid,orderamount,country,city FROM `cv2`.`report_trackmatch` where trackTime >= p_startDate and trackTime < p_endDate and campaignId = p_campaignId;

	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;

	/**DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN
		insert into `cv2`.`report_task_result` (task_date,task_memo,task_result)
			values(sysdate(),concat('proc_fix_commision(',p_startDate,',',p_endDate,',',p_campaignId,')'),d_id);
	END;**/

	set AUTOCOMMIT = 1;
	
	if p_campaignId = 0 then
		OPEN cur_in;
	else
		OPEN cur_in_cp;
	end if;

	set d_updatedAt = sysdate();

		REPEAT
			if p_campaignId = 0 then
				FETCH cur_in INTO d_id,d_advertiseAffiliateId,d_semId,d_advertiserId,d_trackCode,d_campaignId,d_affiliateId,d_siteId,d_subSiteId,d_dispalyType,d_dataType,d_dataTotal,d_ruleType,d_trackStatus,d_trackStep,d_orderinfo,d_trackIp,d_trackUser,d_referrerUrl,d_trackTime,d_createdAt,d_siteCommisionOld,d_siteCommisionNew,d_darwCommisionOld,d_darwCommisionNew,d_matched,d_matchId,d_orderid,d_orderamount,d_country,d_city;
			else
				FETCH cur_in_cp INTO d_id,d_advertiseAffiliateId,d_semId,d_advertiserId,d_trackCode,d_campaignId,d_affiliateId,d_siteId,d_subSiteId,d_dispalyType,d_dataType,d_dataTotal,d_ruleType,d_trackStatus,d_trackStep,d_orderinfo,d_trackIp,d_trackUser,d_referrerUrl,d_trackTime,d_createdAt,d_siteCommisionOld,d_siteCommisionNew,d_darwCommisionOld,d_darwCommisionNew,d_matched,d_matchId,d_orderid,d_orderamount,d_country,d_city;
			end if;

			IF NOT done THEN
				set d_siteCommisionOldFix = 0;
				set d_darwCommisionOldFix = 0;
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
									set d_siteCommisionOldFix = d_orderamount*(d_commissionValue/100);
									set d_darwCommisionOldFix = d_orderamount*(d_darwinCommissionValue/100);
								end if;
							elseif d_commissionType = 1 then
								if d_commissionRuleType = 105 then
									/**CPM**/
									set d_siteCommisionOldFix = (d_dataTotal/1000)*d_commissionValue;
									set d_darwCommisionOldFix = (d_dataTotal/1000)*d_darwinCommissionValue;
								else
									set d_siteCommisionOldFix = d_dataTotal*d_commissionValue;
									set d_darwCommisionOldFix = d_dataTotal*d_darwinCommissionValue;
								end if;
							end if;
						end if;
					end if;
				end if;

				if d_siteCommisionOldFix = d_siteCommisionOld and d_darwCommisionOldFix = d_darwCommisionOld then
					set d_siteCommisionOldFix = 0;
					set d_darwCommisionOldFix = 0;
				else
					update `cv2`.`report_trackmatch` 
						set siteCommisionOld = d_siteCommisionOldFix, darwCommisionOld = d_darwCommisionOldFix, updatedAt = d_updatedAt
							where id = d_id;
				end if;
			END IF;
		UNTIL done END REPEAT;

	if p_campaignId = 0 then
		CLOSE cur_in;
	else
		CLOSE cur_in_cp;
	end if;

	set AUTOCOMMIT = 1;
	
	/**rebuild report**/
	call `cv2`.`proc_create_daily_commision_by_campaign`(p_startDate,p_endDate,p_campaignId);

END $$

DELIMITER ;