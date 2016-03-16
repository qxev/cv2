drop procedure IF EXISTS `cv2`.`proc_task_dofix`;
DELIMITER $$
CREATE PROCEDURE `cv2`.`proc_task_dofix`(
IN d_matched_id varchar(40),
IN d_data_id bigint(20),
IN d_advertiser_id bigint(20),
IN d_campaign_id bigint(20),
IN d_advertis_id bigint(20),
IN d_affilliate_id bigint(20),
IN d_site_id bigint(20),
IN d_affilliate_advertise_id bigint(20),
IN d_subsite_id varchar(200),
IN d_effect_date datetime,
IN d_effect_ip varchar(50),
IN d_effect_cid varchar(100),
IN d_effect_aid varchar(100),
IN d_effect_type smallint(4),
IN d_effect_quantity int(10),
IN d_effect_amount float(14,2),
IN d_effect_commision float(14,2),
IN d_match_action varchar(50),
IN d_matched_check smallint(5),
IN d_matched_done smallint(5),
IN d_matched_message varchar(255),
IN p_auto_confirm smallint(2),
OUT succeedTotal bigint(20))
BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE t_campaign_id bigint(20);
	DECLARE d_continuedo smallint(5) default 0;
	DECLARE d_fixrelation smallint(5) default 0;
	DECLARE d_fixadvertise smallint(5) default 0;	
	DECLARE d_addrelation smallint(5) default 0;	
	DECLARE d_dataType smallint(5) default 0;
	DECLARE d_matched smallint(5) default 0;

	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN
		insert into `cv2`.`report_task_result` (task_date,task_memo,task_result)
			values(sysdate(),concat('proc_commision_match(',p_task_id,',',p_action_type,',',d_data_id,' ',d_affilliate_advertise_id,' -- ',d_effect_aid,')'),0);
	END;

	set d_continuedo = 1;
	set d_matched_message = '';
	set d_fixrelation = 0;
	set d_fixadvertise = 0;
	set d_addrelation = 0;
	set d_dataType = 2;
	set d_matched_done = 3;
	
	/**check site id**/
	if d_site_id > 0 then
			if not exists(select 1 from `cv2`.`site` where id = d_site_id) then
					set d_continuedo = 0;
					set d_matched_message = 'BAD DATA(No such site!)';
					update `cv2`.`match_data` set disabled = 1,matched_message = d_matched_message where id = d_data_id;
			end if;
	else
		/**	if not exists(select 1 from `cv2`.`summarydimension` where advertiseAffiliateId = d_affilliate_advertise_id and campaignId = d_campaign_id) then**/  /** larry:for real-time ipd 2009-12-02 **/
			if not exists(select 1 from `cv2`.`advertiseaffiliate` advertiseaffiliate left join `cv2`.`advertise` advertise on advertise.id = advertiseaffiliate.advertiseId left join `cv2`.`landingpage` landingpage on landingpage.id = advertise.landingpageId where advertiseaffiliate.id = d_affilliate_advertise_id and landingpage.campaignId = d_campaign_id) then

					set d_continuedo = 0;
					set d_matched_message = 'BAD DATA(Need site!)';
					update `cv2`.`match_data` set disabled = 1,matched_message = d_matched_message where id = d_data_id;
			end if;
	end if;
	
	if d_continuedo = 1 then
			/**check affilliate_advertise_id**/
			if d_affilliate_advertise_id > 0 then
				/**	if exists(select 1 from `cv2`.`summarydimension` where advertiseAffiliateId = d_affilliate_advertise_id) then**/  /** larry:for real-time ipd 2009-12-02 **/
					if exists(select 1 from `cv2`.`advertiseaffiliate` where id = d_affilliate_advertise_id) then
						/**	if exists(select 1 from `cv2`.`summarydimension` where advertiseAffiliateId = d_affilliate_advertise_id and campaignId = d_campaign_id) then**/  /** larry:for real-time ipd 2009-12-02 **/
							if exists(select 1 from `cv2`.`advertiseaffiliate` advertiseaffiliate left join `cv2`.`advertise` advertise on advertise.id = advertiseaffiliate.advertiseId left join `cv2`.`landingpage` landingpage on landingpage.id = advertise.landingpageId where advertiseaffiliate.id = d_affilliate_advertise_id and landingpage.campaignId = d_campaign_id) then
								/**	select advertiserId,affiliateId,siteId into d_advertiser_id,d_affilliate_id,d_site_id
											from `cv2`.`summarydimension` where advertiseAffiliateId = d_affilliate_advertise_id;**/  /** larry:for real-time ipd 2009-12-02 **/
									select campaign.userId,site.userId,site.id,advertise.id into d_advertiser_id,d_affilliate_id,d_site_id,d_advertis_id
											from `cv2`.`advertiseaffiliate` advertiseaffiliate 
                                            left join `cv2`.`advertise` advertise on advertise.id = advertiseaffiliate.advertiseId 
                                            left join `cv2`.`landingpage` landingpage on landingpage.id = advertise.landingpageId 
                                            left join `cv2`.`campaign` campaign on campaign.id = landingpage.campaignId
                                            left join `cv2`.`site` site on site.id = advertiseaffiliate.siteId
                                            where advertiseaffiliate.id = d_affilliate_advertise_id;
											
								/**	select advertiseId into d_advertis_id from `cv2`.`advertiseaffiliate` where id = d_affilliate_advertise_id;**/  /** larry:for real-time ipd 2009-12-02 **/
							else
									set d_continuedo = 0;
									set d_matched_message = 'BAD DATA(Aid own a different campaign!)';		
									update `cv2`.`match_data` set disabled = 1,matched_message = d_matched_message where id = d_data_id;					
							end if;
					else 
							set d_fixrelation = 1;
					end if;
			else
				set d_fixrelation = 1;
			end if;
			
			if d_continuedo = 1 then
					/**check relation fix status**/
					if d_fixrelation = 1 then
							/**check advertise id**/
							if d_advertis_id > 0 then
									if exists(select 1 from `cv2`.`advertise` where id = d_advertis_id and campaignId = d_campaign_id and deleted = 0) then
											if exists(select 1 from `cv2`.`advertiseaffiliate` where advertiseId = d_advertis_id and siteId = d_site_id) then
													select id into d_affilliate_advertise_id from `cv2`.`advertiseaffiliate` where advertiseId = d_advertis_id and siteId = d_site_id order by createdAt asc limit 0,1;
											else
													set d_addrelation = 1;
											end if;
									else
											set d_fixadvertise = 1;
											set d_addrelation = 1;
									end if;
							else
									set d_fixadvertise = 1;
									set d_addrelation = 1;
							end if;
							
							/**fix advertise id**/
							if d_fixadvertise = 1 then
									select id into d_advertis_id from `cv2`.`advertise` where campaignId = d_campaign_id and deleted = 0 order by createdAt asc limit 0,1;
									
									/**check relation again**/
									if exists(select 1 from `cv2`.`advertiseaffiliate` where advertiseId = d_advertis_id and siteId = d_site_id) then
											select id into d_affilliate_advertise_id from `cv2`.`advertiseaffiliate` where advertiseId = d_advertis_id and siteId = d_site_id order by createdAt asc limit 0,1;
											set d_addrelation = 0;
									else
											set d_addrelation = 1;
									end if;							
							end if;
							
							/**add relation**/
							if d_addrelation = 1 then
									insert into `cv2`.`advertiseaffiliate` (advertiseId,siteId,status,createdAt) values(d_advertis_id,d_site_id,2,sysdate());
									select last_insert_id() into d_affilliate_advertise_id;
							end if;
					end if;	
					
					/**fix ip**/
					if d_effect_ip = '' then
							set d_effect_ip = concat('fix ',d_data_id);
					end if;
							
					/**generate unique id for fix data, set datatype is 9**/
					insert into `cv2`.`affiliatetrackdata` (campaignId,semId,advertiseId,affiliateId,siteId,advertiseAffiliateId,ruleType,trackStep,trackIp,trackTime,dataType,createdAt) values(0,1,0,0,0,0,0,0,concat(date_format(sysdate(),'%Y%m%d%H%I%S'),' at ',d_data_id,' fix'),sysdate(),9,sysdate());
					set @cmrid = 0;
					select last_insert_id() into @cmrid;
							
					set @siteCommisionOld = 0.000;
					set @darwCommisionOld = 0.000;
					set @siteCommisionNew = 0.000;
					set @darwCommisionNew = 0.000;
							
					/**compute comission**/
					call `cv2`.`proc_commision_rule`(p_auto_confirm,d_campaign_id,d_effect_date,d_effect_type,d_effect_quantity,d_effect_amount,d_effect_commision,@siteCommisionOld,@darwCommisionOld,@siteCommisionNew,@darwCommisionNew);
					
					/**check matching fix mode**/
					if p_auto_confirm = 1 then
							set d_matched = 1;
							set d_matched_id = d_matched_id;
							set d_dataType = 3;
							set d_matched_done = 2;
					end if;
							
					/**fix data into report_trackmatch**/
					insert into `cv2`.`report_trackmatch` (id,advertiseAffiliateId,semId,advertiserId,trackCode,campaignId,affiliateId,siteId,subSiteId,dispalyType,dataType,dataTotal,ruleType,trackStatus,trackStep,orderinfo,trackIp,trackUser,referrerUrl,trackTime,createdAt,siteCommisionOld,siteCommisionNew,darwCommisionOld,darwCommisionNew,matched,matchId,orderid,orderamount) 
									values(@cmrid,d_affilliate_advertise_id,0,d_advertiser_id,d_effect_aid,d_campaign_id,d_affilliate_id,d_site_id,d_subsite_id,0,d_dataType,d_effect_quantity,d_effect_type,1,d_effect_type,'',d_effect_ip,d_effect_ip,'',d_effect_date,sysdate(),@siteCommisionOld,@siteCommisionNew,@darwCommisionOld,@darwCommisionNew,d_matched,d_matched_id,d_effect_cid,d_effect_amount);					
			
					/**update task data status**/
					update `cv2`.`match_data` set matched_done = d_matched_done,matched_message = 'SUCCESS',effect_ip = d_effect_ip, mapping_aid = d_affilliate_advertise_id, mapping_id = @cmrid where id = d_data_id;
			
					/**add counter for matching succeed**/
					set succeedTotal = succeedTotal + 1;
			end if;
	end if;
					
END $$

DELIMITER ;
