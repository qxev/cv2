drop procedure IF EXISTS `cv2_cmatch`.`proc_task_domatch`;
DELIMITER $$
CREATE PROCEDURE `cv2_cmatch`.`proc_task_domatch`(
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
OUT succeedTotal bigint(20))
BEGIN
			DECLARE share_sql varchar(4000);
			DECLARE check_sql varchar(4000);
			DECLARE read_sql varchar(4000);
			DECLARE d_continuedo smallint(5);
			DECLARE d_dataType smallint(5) default 0;
			DECLARE d_matched smallint(5) default 0;
			DECLARE d_date_scope_start datetime;
			DECLARE d_date_scope_end datetime;			
			
			/**DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN
					insert into `cv2_cmatch`.`report_task_result` (task_date,task_memo,task_result)
							values(sysdate(),concat('proc_commision_match(',p_task_id,',',p_type,',',d_data_id,' ',d_affilliate_advertise_id,' -- ',d_effect_aid,')'),0);
			END;**/
			
			set d_continuedo = 1;
			set @trkDate = d_effect_date;
			set @mrid = 0;
			set @afid = 0;	
					
			set check_sql = 'select id,trackTime,advertiseAffiliateId into @mrid,@trkDate,@afid ';
			set share_sql = 'from `cv2_cmatch`.`report_trackmatch` where matched = 0 ';
			
			/**condition check: d_affilliate_advertise_id**/
			if d_affilliate_advertise_id > 0 then
					if exists(select 1 from `cv2_cmatch`.`summarydimension` where advertiseAffiliateId = d_affilliate_advertise_id) then
							if exists(select 1 from `cv2_cmatch`.`summarydimension` where advertiseAffiliateId = d_affilliate_advertise_id and campaignId = d_campaign_id) then
									select advertiserId,affiliateId,siteId into d_advertiser_id,d_affilliate_id,d_site_id
											from `cv2_cmatch`.`summarydimension` where advertiseAffiliateId = d_affilliate_advertise_id;
											
									select advertiseId into d_advertis_id from `cv2_cmatch`.`advertiseaffiliate` where id = d_affilliate_advertise_id;
									
									/**add condition**/
									set share_sql = concat(share_sql,' and advertiseAffiliateId = ',d_affilliate_advertise_id);
							else
									set d_continuedo = 0;
									set d_matched_message = 'BAD DATA(Aid own a different campaign!)';		
									update `cv2_cmatch`.`match_data` set disabled = 1,matched_message = d_matched_message where id = d_data_id;					
							end if;
					end if;
			end if;
			
			if d_continuedo = 1 then
					/**condition check: d_effect_cid**/
					if d_effect_cid <> '' then 
							set share_sql = concat(share_sql,' and orderid = \'',d_effect_cid,'\'');
					end if;
				
					/**condition check: d_effect_ip**/
					if d_effect_ip = '' or d_effect_ip = '0' then
					else
						set share_sql = concat(share_sql,' and trackIp = \'',d_effect_ip,'\'');
					end if;	
				
					/**condition check: d_effect_date**/
					set d_date_scope_start = date_format(d_effect_date,'%Y-%m-%d 00:00:00');
					set d_date_scope_end = date_format(d_effect_date,'%Y-%m-%d 23:59:59');
			
					set share_sql = concat(share_sql,' and trackTime >= \'',d_date_scope_start,'\'');
					set share_sql = concat(share_sql,' and trackTime <= \'',d_date_scope_end,'\'');
					
					/**condition check: d_campaign_id**/
					set share_sql = concat(share_sql,' and campaignId = ',d_campaign_id);
					
					/**order by**/
					set share_sql = concat(share_sql,' order by trackTime asc');
					
					/**limit**/
					set share_sql = concat(share_sql,' limit 0,1');
					
					/**execute sql**/
					set @sql = concat(check_sql,share_sql);
					prepare fmd from @sql;
					execute fmd;
					deallocate prepare fmd;
							
					set @siteCommisionOld = 0.000;
					set @darwCommisionOld = 0.000;
					set @siteCommisionNew = 0.000;
					set @darwCommisionNew = 0.000;					
									
					/**check mached**/
					if @mrid > 0 then
							/**compute comission**/
							call `cv2_cmatch`.`proc_commision_rule`(1,d_campaign_id,d_effect_date,d_effect_type,d_effect_quantity,d_effect_amount,d_effect_commision,@siteCommisionOld,@darwCommisionOld,@siteCommisionNew,@darwCommisionNew);
							
							set d_matched = 1;
							set d_matched_id = d_matched_id;
							set d_dataType = 3;
							
							/**update matched data**/
							update `cv2_cmatch`.`report_trackmatch` set matched = 1,matchId = d_matched_id,dataTotal = d_effect_quantity,orderamount = d_effect_amount,siteCommisionOld = @siteCommisionOld, siteCommisionNew = @siteCommisionNew,darwCommisionOld = @darwCommisionOld,darwCommisionNew = @darwCommisionNew where id = @mrid;
							
							/**update task data status**/
							update `cv2_cmatch`.`match_data` set matched_done = 1, matched_message = 'SUCCESS', mapping_aid = @afid, mapping_id = @mrid  where id = d_data_id;
							
							/**add counter for matching succeed**/
							set succeedTotal = succeedTotal + 1;
					else
							/**No site, change it to default**/
							set d_site_id = 1;
							
							/**fix the data for matching**/
							call `cv2_cmatch`.proc_task_dofix(d_matched_id,d_data_id,d_advertiser_id,d_campaign_id,d_advertis_id,d_affilliate_id,d_site_id,d_affilliate_advertise_id,d_subsite_id,d_effect_date,d_effect_ip,d_effect_cid,d_effect_aid,d_effect_type,d_effect_quantity,d_effect_amount,d_effect_commision,d_match_action,d_matched_check,d_matched_done,d_matched_message,1,@cSuccees);							
							
							set succeedTotal = @cSuccees;
							
					end if;	
			end if;
		
END $$

DELIMITER ;