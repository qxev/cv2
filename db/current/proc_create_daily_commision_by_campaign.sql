drop procedure IF EXISTS `cv2`.`proc_create_daily_commision_by_campaign`;
DELIMITER $$
CREATE DEFINER=`cv2_production`@`192.168.1.%` PROCEDURE `cv2`.`proc_create_daily_commision_by_campaign`(IN p_startday varchar(20),IN p_endday varchar(20),IN p_campaignId bigint(20))
BEGIN
	DECLARE done INT DEFAULT 0;

	DECLARE d_advertiserId int(11) unsigned default 0;
	DECLARE d_campaignId int(11) default 0;
	DECLARE d_affiliateId int(11) default 0;
	DECLARE d_siteId int(11) default 0;
	DECLARE d_subSiteId varchar(255) default '';
	DECLARE d_advertiseAffiliateId int(11) default 0;
	DECLARE d_ruleType int(4) unsigned default 0;
	DECLARE d_trackTime datetime;
	DECLARE d_trackDate date;

	DECLARE d_dataTotalOld bigint(20) default 0;
	DECLARE d_dataTotalNew bigint(20) default 0;
	DECLARE d_orderamountOld float(12,3) default 0; 
	DECLARE d_orderamountNew float(12,3) default 0;
	DECLARE d_siteCommisionOld float(12,3) default 0;
	DECLARE d_siteCommisionNew float(12,3) default 0;
	DECLARE d_darwCommisionOld float(12,3) default 0;
	DECLARE d_darwCommisionNew float(12,3) default 0;

	DECLARE d_advertiserName varchar(255) default '';
	DECLARE d_campaignName varchar(255) default '';
	DECLARE d_affiliateName varchar(255) default '';
	DECLARE d_siteName varchar(255) default '';
	DECLARE d_siteUrl varchar(255) default '';
	DECLARE d_bannerId int(11) default 0;
	DECLARE d_bannerDescription varchar(255) default '';
	DECLARE d_landingPageId int(11) default 0;
	DECLARE d_landingPageUrl varchar(1024) default '';

	DECLARE d_tmpSDate varchar(20) default '';
	DECLARE d_tmpEDate varchar(20) default '';

	DECLARE d_tpm int(1) default 0;
	DECLARE d_matched smallint(5) default 0;

	DECLARE cur_in CURSOR FOR SELECT advertiserId,campaignId,affiliateId,siteId,subSiteId,advertiseAffiliateId,ruleType,date_format(trackTime,'%Y-%m-%d') as trackTime,max(matched) as matched FROM `cv2`.`report_trackmatch` where ruleType >= 100 and trackTime >= p_startday and trackTime < p_endday group by subSiteId,advertiseAffiliateId,ruleType,date_format(trackTime,'%Y-%m-%d');
	DECLARE cur_in_cp CURSOR FOR SELECT advertiserId,campaignId,affiliateId,siteId,subSiteId,advertiseAffiliateId,ruleType,date_format(trackTime,'%Y-%m-%d') as trackTime,max(matched) as matched FROM `cv2`.`report_trackmatch` where campaignId= p_campaignId and ruleType >= 100 and trackTime >= p_startday and trackTime < p_endday group by subSiteId,advertiseAffiliateId,ruleType,date_format(trackTime,'%Y-%m-%d');
	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;

	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN
		insert into `cv2`.`report_task_result` (task_date,task_memo,task_result)
			values(sysdate(),concat('proc_create_daily_commision(',p_startday,',',p_endday,',',p_campaignId,') at ',d_advertiserId,',',d_campaignId,',',d_affiliateId,',',d_siteId,',',d_subSiteId,',',d_advertiseAffiliateId,',',d_ruleType,',',d_trackTime),0);
	END;

	set AUTOCOMMIT = 1;

	truncate `cv2`.`summarydimension`;

	insert into `cv2`.`summarydimension` (advertiserId,advertiserName,campaignId,campaignName,affiliateId,affiliateName,siteId,siteName,siteUrl,advertiseAffiliateId,bannerId,bannerDescription,landingPageId,landingPageUrl,createdAt,updatedAt) (SELECT
		advertiser.id as advertiserId,
		advertiser.name as advertiserName,
		campaign.id as campaignId,
		campaign.name as campaignName,
		affiliate.id as affiliateId,
		affiliate.name as affiliateName,
		site.id as siteId,
		site.name as siteName,
		site.url as siteUrl,
		advertiseaffiliate.id as advertiseAffiliateId,
		banner.id as bannerId,
		banner.content as bannerDescription,
		landingpage.id as landingPageId,
		landingpage.url as landingPageUrl,
		sysdate() as createdAt,
		sysdate() as updatedAt
		FROM
		`cv2`.advertiseaffiliate
		Left Join `cv2`.advertise ON advertise.id = advertiseaffiliate.advertiseid
		Left Join `cv2`.site ON advertiseaffiliate.siteId = site.id
		Left Join `cv2`.landingpage ON landingpage.id = advertise.landingpageid
		Left Join `cv2`.banner ON banner.id = advertise.bannerid
		Left Join `cv2`.campaign ON campaign.id = banner.campaignid
		Left Join `cv2`.`user` AS affiliate ON site.userId = affiliate.id
		Left Join `cv2`.`user` AS advertiser ON campaign.userId = advertiser.id);

	set AUTOCOMMIT = 1;
	if p_campaignId = 0 then
		OPEN cur_in;
	else
		delete from `cv2`.`report_summary` where campaignId = p_campaignId and summaryDate >= date_format(p_startday,'%Y-%m-%d') and summaryDate < date_format(p_endday,'%Y-%m-%d');
		delete from `cv2`.`summarycommission` where campaignId = p_campaignId and summaryDate >= date_format(p_startday,'%Y-%m-%d') and summaryDate < date_format(p_endday,'%Y-%m-%d');
		OPEN cur_in_cp;
	end if;
		REPEAT
			if p_campaignId = 0 then
				FETCH cur_in INTO d_advertiserId,d_campaignId,d_affiliateId,d_siteId,d_subSiteId,d_advertiseAffiliateId,d_ruleType,d_trackTime,d_matched;
			else
				FETCH cur_in_cp INTO d_advertiserId,d_campaignId,d_affiliateId,d_siteId,d_subSiteId,d_advertiseAffiliateId,d_ruleType,d_trackTime,d_matched;
			end if;

			IF NOT done THEN
				set d_tmpSDate = concat(date_format(d_trackTime,'%Y-%m-%d'),' 00:00:00');
				set d_tmpEDate = concat(date_format(d_trackTime,'%Y-%m-%d'),' 23:59:59');
				set d_trackDate = date_format(d_trackTime,'%Y-%m-%d');

				set d_advertiserName = '';
				set d_campaignName = '';
				set d_affiliateName = '';
				set d_siteName = '';
				set d_siteUrl = '';
				set d_bannerId = 0;
				set d_bannerDescription = '';
				set d_landingPageId = 0;
				set d_landingPageUrl = '';
				set d_dataTotalOld = 0;
				set d_orderamountOld = 0;
				set d_siteCommisionOld = 0;
				set d_darwCommisionOld = 0;
				set d_dataTotalNew = 0;
				set d_orderamountNew = 0;
				set d_siteCommisionNew = 0;
				set d_darwCommisionNew = 0;

				if exists(select 1 from `cv2`.`summarydimension` where advertiseAffiliateId = d_advertiseAffiliateId) then
					select advertiserId,campaignId,affiliateId,siteId,advertiserName,campaignName,affiliateName,siteName,siteUrl,bannerId,bannerDescription,landingPageId,landingPageUrl 
						into d_advertiserId,d_campaignId,d_affiliateId,d_siteId,d_advertiserName,d_campaignName,d_affiliateName,d_siteName,d_siteUrl,d_bannerId,d_bannerDescription,d_landingPageId,d_landingPageUrl 
							from `cv2`.`summarydimension` 
								where advertiseAffiliateId = d_advertiseAffiliateId;
				end if;

				if exists(select 1 from `cv2`.`report_trackmatch` where ruleType = d_ruleType and trackTime >= d_tmpSDate and trackTime <= d_tmpEDate and advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId limit 0,1) then
				/**2009-06-04 10:39 group by order id for cpl**/
					/**select if(sum(dataTotal) is null,0,sum(dataTotal)) as dataTotal,if(sum(orderamount) is null,0,sum(orderamount)) as orderamount,if(sum(siteCommisionOld) is null,0,sum(siteCommisionOld)) as siteCommisionOld,if(sum(darwCommisionOld) is null,0,sum(darwCommisionOld)) as darwCommisionOld 
						into d_dataTotalOld,d_orderamountOld,d_siteCommisionOld,d_darwCommisionOld 
							from `cv2`.`report_trackmatch` 
								where ruleType = d_ruleType and trackTime >= d_tmpSDate and trackTime <= d_tmpEDate and advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId  
									group by subSiteId,advertiseAffiliateId,ruleType;**/
					if d_ruleType = 101 then
						if exists(select * from `cv2`.`report_trackmatch` where ruleType = d_ruleType and trackTime >= d_tmpSDate and trackTime <= d_tmpEDate and advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId and length(orderid) > 0 limit 0,1) then
						select if(sum(dataTotal) is null,0,sum(dataTotal)) as dataTotal,if(sum(orderamount) is null,0,sum(orderamount)) as orderamount,if(sum(siteCommisionOld) is null,0,sum(siteCommisionOld)) as siteCommisionOld,if(sum(darwCommisionOld) is null,0,sum(darwCommisionOld)) as darwCommisionOld 
							into d_dataTotalOld,d_orderamountOld,d_siteCommisionOld,d_darwCommisionOld 
								from (select * from `cv2`.`report_trackmatch` where ruleType = d_ruleType and trackTime >= d_tmpSDate and trackTime <= d_tmpEDate and advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId and length(orderid) > 0 group by subSiteId,advertiseAffiliateId,ruleType,orderid) t
										group by subSiteId,advertiseAffiliateId,ruleType;
						end if;
					else
						select if(sum(dataTotal) is null,0,sum(dataTotal)) as dataTotal,if(sum(orderamount) is null,0,sum(orderamount)) as orderamount,if(sum(siteCommisionOld) is null,0,sum(siteCommisionOld)) as siteCommisionOld,if(sum(darwCommisionOld) is null,0,sum(darwCommisionOld)) as darwCommisionOld 
							into d_dataTotalOld,d_orderamountOld,d_siteCommisionOld,d_darwCommisionOld 
								from `cv2`.`report_trackmatch` 
									where ruleType = d_ruleType and trackTime >= d_tmpSDate and trackTime <= d_tmpEDate and advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId 
										group by subSiteId,advertiseAffiliateId,ruleType;
					end if;					
				end if;

				if exists(select 1 from `cv2`.`report_trackmatch` where ruleType = d_ruleType and trackTime >= d_tmpSDate and trackTime <= d_tmpEDate and advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId and matched = 1  limit 0,1) then
					select if(sum(dataTotal) is null,0,sum(dataTotal)) as dataTotal,if(sum(orderamount) is null,0,sum(orderamount)) as orderamount,if(sum(siteCommisionNew) is null,0,sum(siteCommisionNew)) as siteCommisionNew,if(sum(darwCommisionNew) is null,0,sum(darwCommisionNew)) as darwCommisionNew 
						into d_dataTotalNew,d_orderamountNew,d_siteCommisionNew,d_darwCommisionNew 
							from `cv2`.`report_trackmatch` 
								where ruleType = d_ruleType and trackTime >= d_tmpSDate and trackTime <= d_tmpEDate and advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId and matched = 1 
									group by subSiteId,advertiseAffiliateId,ruleType;
				end if;
				
				if exists(select 1 from `cv2`.`report_summary` where advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId and summaryDate = d_trackTime) then
					set d_tpm = 1;
				else
					insert into `cv2`.`report_summary` (advertiserId,advertiserName,campaignId,campaignName,affiliateId,affiliateName,siteId,siteName,siteUrl,subSiteId,advertiseAffiliateId,bannerId,bannerDescription,landingPageId,landingPageUrl,summaryDate,createdAt,matched)
										values(d_advertiserId,d_advertiserName,d_campaignId,d_campaignName,d_affiliateId,d_affiliateName,d_siteId,d_siteName,d_siteUrl,d_subSiteId,d_advertiseAffiliateId,d_bannerId,d_bannerDescription,d_landingPageId,d_landingPageUrl,d_trackTime,sysdate(),d_matched);
				end if;

				if d_ruleType = 105 then
					update `cv2`.`report_summary` set updatedAt = sysdate(),cpm_count_old = d_dataTotalOld,cpm_count_new = d_dataTotalNew,cpm_sitecommision_old = d_siteCommisionOld,cpm_sitecommision_new = d_siteCommisionNew,cpm_darwcommision_old = d_darwCommisionOld,cpm_darwcommision_new = d_darwCommisionNew,sitecommision_total_old = cpm_sitecommision_old + cpc_sitecommision_old + cpl_sitecommision_old + cps_sitecommision_old,sitecommision_total_new = cpm_sitecommision_new + cpc_sitecommision_new + cpl_sitecommision_new + cps_sitecommision_new,darwcommision_total_old = cpm_darwcommision_old + cpc_darwcommision_old + cpl_darwcommision_old + cps_darwcommision_old,darwcommision_total_new = cpm_darwcommision_new + cpc_darwcommision_new + cpl_darwcommision_new + cps_darwcommision_new,matched = d_matched
						 where advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId and summaryDate = d_trackDate;
				elseif d_ruleType = 100 then
					update `cv2`.`report_summary` set updatedAt = sysdate(),cpc_count_old = d_dataTotalOld,cpc_count_new = d_dataTotalNew,cpc_sitecommision_old = d_siteCommisionOld,cpc_sitecommision_new = d_siteCommisionNew,cpc_darwcommision_old = d_darwCommisionOld,cpc_darwcommision_new = d_darwCommisionNew,sitecommision_total_old = cpm_sitecommision_old + cpc_sitecommision_old + cpl_sitecommision_old + cps_sitecommision_old,sitecommision_total_new = cpm_sitecommision_new + cpc_sitecommision_new + cpl_sitecommision_new + cps_sitecommision_new,darwcommision_total_old = cpm_darwcommision_old + cpc_darwcommision_old + cpl_darwcommision_old + cps_darwcommision_old,darwcommision_total_new = cpm_darwcommision_new + cpc_darwcommision_new + cpl_darwcommision_new + cps_darwcommision_new,matched = d_matched
						 where advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId and summaryDate = d_trackDate;
				elseif d_ruleType = 101 then
					update `cv2`.`report_summary` set updatedAt = sysdate(),cpl_count_old = d_dataTotalOld,cpl_count_new = d_dataTotalNew,cpl_sitecommision_old = d_siteCommisionOld,cpl_sitecommision_new = d_siteCommisionNew,cpl_darwcommision_old = d_darwCommisionOld,cpl_darwcommision_new = d_darwCommisionNew,sitecommision_total_old = cpm_sitecommision_old + cpc_sitecommision_old + cpl_sitecommision_old + cps_sitecommision_old,sitecommision_total_new = cpm_sitecommision_new + cpc_sitecommision_new + cpl_sitecommision_new + cps_sitecommision_new,darwcommision_total_old = cpm_darwcommision_old + cpc_darwcommision_old + cpl_darwcommision_old + cps_darwcommision_old,darwcommision_total_new = cpm_darwcommision_new + cpc_darwcommision_new + cpl_darwcommision_new + cps_darwcommision_new,matched = d_matched
						 where advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId and summaryDate = d_trackDate;
				elseif d_ruleType = 102 then
					update `cv2`.`report_summary` set updatedAt = sysdate(),cps_count_old = d_dataTotalOld,cps_count_new = d_dataTotalNew,cps_sitecommision_old = d_siteCommisionOld,cps_sitecommision_new = d_siteCommisionNew,cps_darwcommision_old = d_darwCommisionOld,cps_darwcommision_new = d_darwCommisionNew,sitecommision_total_old = cpm_sitecommision_old + cpc_sitecommision_old + cpl_sitecommision_old + cps_sitecommision_old,sitecommision_total_new = cpm_sitecommision_new + cpc_sitecommision_new + cpl_sitecommision_new + cps_sitecommision_new,darwcommision_total_old = cpm_darwcommision_old + cpc_darwcommision_old + cpl_darwcommision_old + cps_darwcommision_old,darwcommision_total_new = cpm_darwcommision_new + cpc_darwcommision_new + cpl_darwcommision_new + cps_darwcommision_new,matched = d_matched
						 where advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId and summaryDate = d_trackDate;
				end if;

				if exists(select 1 from `cv2`.`summarycommission` where advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId and ruleType = d_ruleType and summaryDate = d_trackDate) then
					update `cv2`.`summarycommission` set dataTotalOld = d_dataTotalOld,dataTotalNew = d_dataTotalNew,orderamountOld = d_orderamountOld,orderamountNew = d_orderamountNew,siteCommisionOld = d_siteCommisionOld,siteCommisionNew = d_siteCommisionNew,darwCommisionOld = d_darwCommisionOld,darwCommisionNew = d_darwCommisionNew, updatedAt = sysdate()
						where advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId and ruleType = d_ruleType and summaryDate = d_trackDate;
				else
					insert into `cv2`.`summarycommission` (advertiserId,advertiserName,campaignId,campaignName,affiliateId,affiliateName,siteId,siteName,siteUrl,subSiteId,advertiseAffiliateId,bannerId,bannerDescription,landingPageId,landingPageUrl,summaryDate,ruleType,dataTotalOld,dataTotalNew,orderamountOld,orderamountNew,siteCommisionOld,siteCommisionNew,darwCommisionOld,darwCommisionNew,createdAt)
										values(d_advertiserId,d_advertiserName,d_campaignId,d_campaignName,d_affiliateId,d_affiliateName,d_siteId,d_siteName,d_siteUrl,d_subSiteId,d_advertiseAffiliateId,d_bannerId,d_bannerDescription,d_landingPageId,d_landingPageUrl,d_trackTime,d_ruleType,d_dataTotalOld,d_dataTotalNew,d_orderamountOld,d_orderamountNew,d_siteCommisionOld,d_siteCommisionNew,d_darwCommisionOld,d_darwCommisionNew,sysdate());
				end if;
			END IF;
		UNTIL done END REPEAT;

	if p_campaignId = 0 then
		CLOSE cur_in;
	else
		CLOSE cur_in_cp;
	end if;

	set AUTOCOMMIT = 1;

	call `cv2`.`proc_create_daily_effect_by_campaign`(p_startday,p_endday,p_campaignId);
END $$

DELIMITER ;
