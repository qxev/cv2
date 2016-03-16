drop procedure IF EXISTS `cv2`.`proc_create_daily_effect_by_campaign`;
DELIMITER $$
CREATE DEFINER=`cv2_production`@`192.168.1.%` PROCEDURE `cv2`.`proc_create_daily_effect_by_campaign`(IN p_startday varchar(20),IN p_endday varchar(20),IN p_campaignId bigint(20))
BEGIN
	DECLARE done INT DEFAULT 0;

	DECLARE d_advertiserId int(11) unsigned default 0;
	DECLARE d_campaignId int(11) default 0;
	DECLARE d_affiliateId int(11) default 0;
	DECLARE d_siteId int(11) default 0;
	DECLARE d_subSiteId varchar(255) default '';
	DECLARE d_advertiseAffiliateId int(11) default 0;
	DECLARE d_trackStep int(4) unsigned default 0;
	DECLARE d_trackTime datetime;
	DECLARE d_trackDate date;

	DECLARE d_dataTotalOld bigint(20) default 0;
	DECLARE d_dataTotalNew bigint(20) default 0;

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

	/**fetch daily dimension**/
	DECLARE cur_in CURSOR FOR SELECT advertiserId,campaignId,affiliateId,siteId,subSiteId,advertiseAffiliateId,trackStep,date_format(trackTime,'%Y-%m-%d') as trackTime FROM `cv2`.`report_trackmatch` where trackTime >= p_startday and trackTime < p_endday group by subSiteId,advertiseAffiliateId,trackStep,date_format(trackTime,'%Y-%m-%d');
	DECLARE cur_in_cp CURSOR FOR SELECT advertiserId,campaignId,affiliateId,siteId,subSiteId,advertiseAffiliateId,trackStep,date_format(trackTime,'%Y-%m-%d') as trackTime FROM `cv2`.`report_trackmatch` where campaignId = p_campaignId and trackTime >= p_startday and trackTime < p_endday group by subSiteId,advertiseAffiliateId,trackStep,date_format(trackTime,'%Y-%m-%d');
	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;

	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN
		insert into `cv2`.`report_task_result` (task_date,task_memo,task_result)
			values(sysdate(),concat('proc_create_daily_effect(',p_startday,',',p_endday,',',p_campaignId,') at ',d_advertiserId,',',d_campaignId,',',d_affiliateId,',',d_siteId,',',d_subSiteId,',',d_advertiseAffiliateId,',',d_ruleType,',',d_trackTime),0);
	END;

	set AUTOCOMMIT = 1;

	if p_campaignId = 0 then
		OPEN cur_in;
	else
		delete from `cv2`.`summaryeffect` where campaignId = p_campaignId and summaryDate >= date_format(p_startday,'%Y-%m-%d') and summaryDate < date_format(p_endday,'%Y-%m-%d');
		OPEN cur_in_cp;
	end if;

		REPEAT
			if p_campaignId = 0 then
				FETCH cur_in INTO d_advertiserId,d_campaignId,d_affiliateId,d_siteId,d_subSiteId,d_advertiseAffiliateId,d_trackStep,d_trackTime;
			else
				FETCH cur_in_cp INTO d_advertiserId,d_campaignId,d_affiliateId,d_siteId,d_subSiteId,d_advertiseAffiliateId,d_trackStep,d_trackTime;
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
				set d_dataTotalNew = 0;

				/**fetch dimension detail information**/
				if exists(select 1 from `cv2`.`summarydimension` where advertiseAffiliateId = d_advertiseAffiliateId) then
					select advertiserName,campaignName,affiliateName,siteName,siteUrl,bannerId,bannerDescription,landingPageId,landingPageUrl 
						into d_advertiserName,d_campaignName,d_affiliateName,d_siteName,d_siteUrl,d_bannerId,d_bannerDescription,d_landingPageId,d_landingPageUrl 
							from `cv2`.`summarydimension`
								where advertiseAffiliateId = d_advertiseAffiliateId;
				end if;

				/**compute unmatched total**/
				if exists(select 1 from `cv2`.`report_trackmatch` where trackStep = d_trackStep and advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId and trackTime >= d_tmpSDate and trackTime <= d_tmpEDate limit 0,1) then
					select if(sum(dataTotal) is null,0,sum(dataTotal)) as dataTotal
						into d_dataTotalOld
							from `cv2`.`report_trackmatch` 
								where trackStep = d_trackStep and advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId and trackTime >= d_tmpSDate and trackTime <= d_tmpEDate 
									group by subSiteId,advertiseAffiliateId,trackStep;
				end if;

				/**compute matched total**/
				if exists(select 1 from `cv2`.`report_trackmatch` where trackStep = d_trackStep and advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId and trackTime >= d_tmpSDate and trackTime <= d_tmpEDate and matched = 1 limit 0,1) then
					select if(sum(dataTotal) is null,0,sum(dataTotal)) as dataTotal
						into d_dataTotalNew
							from `cv2`.`report_trackmatch` 
								where trackStep = d_trackStep and advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId and trackTime >= d_tmpSDate and trackTime <= d_tmpEDate and matched = 1 
									group by subSiteId,advertiseAffiliateId,trackStep;
				end if;
				
				/**save to summary**/
				if exists(select 1 from `cv2`.`summaryeffect` where advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId and trackStep = d_trackStep and summaryDate = d_trackDate) then
					update `cv2`.`summaryeffect` set dataTotalOld = d_dataTotalOld,dataTotalNew = d_dataTotalNew,updatedAt = sysdate()
						where advertiseAffiliateId = d_advertiseAffiliateId and subSiteId = d_subSiteId and trackStep = d_trackStep and summaryDate = d_trackDate;
				else
					insert into `cv2`.`summaryeffect` (advertiserId,advertiserName,campaignId,campaignName,affiliateId,affiliateName,siteId,siteName,siteUrl,subSiteId,advertiseAffiliateId,bannerId,bannerDescription,landingPageId,landingPageUrl,summaryDate,trackStep,dataTotalOld,dataTotalNew,createdAt)
										values(d_advertiserId,d_advertiserName,d_campaignId,d_campaignName,d_affiliateId,d_affiliateName,d_siteId,d_siteName,d_siteUrl,d_subSiteId,d_advertiseAffiliateId,d_bannerId,d_bannerDescription,d_landingPageId,d_landingPageUrl,d_trackTime,d_trackStep,d_dataTotalOld,d_dataTotalNew,sysdate());
				end if;
			END IF;
		UNTIL done END REPEAT;

	if p_campaignId = 0 then
		CLOSE cur_in;
	else
		CLOSE cur_in_cp;
	end if;

	set AUTOCOMMIT = 1;
END $$

DELIMITER ;
