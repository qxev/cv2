drop procedure IF EXISTS `cv2`.`proc_task_domatch`;
DELIMITER $$
CREATE PROCEDURE `cv2`.`proc_task_domatch`(
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
			DECLARE order_sql varchar(4000);
			DECLARE default_sql varchar(4000);
			DECLARE final_sql varchar(4000);
			DECLARE ip_sql varchar(4000);
			DECLARE read_sql varchar(4000);
			DECLARE d_continuedo smallint(5);

			DECLARE d_matched smallint(5) default 0;
			DECLARE d_matchId varchar(40) default '';
			DECLARE d_date_scope_start datetime;
			DECLARE d_date_scope_end datetime;			

			/**for fix data when rule type is not same**/
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

			/**DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN
					insert into `cv2`.`report_task_result` (task_date,task_memo,task_result)
							values(sysdate(),concat('proc_commision_match(',p_task_id,',',p_type,',',d_data_id,' ',d_affilliate_advertise_id,' -- ',d_effect_aid,')'),0);
			END;**/
			
			set d_continuedo = 1;
			set @trkDate = d_effect_date;
			set @mrid = 0;
			set @afid = 0;	
			set @rtype = 0;
					
			set check_sql = 'select id,trackTime,advertiseAffiliateId,ruleType into @mrid,@trkDate,@afid,@rtype ';
			set share_sql = 'from `cv2`.`report_trackmatch` where matched = 0 ';
			
			/**condition check: d_affilliate_advertise_id**/
			if d_affilliate_advertise_id > 0 then
					if exists(select 1 from `cv2`.`summarydimension` where advertiseAffiliateId = d_affilliate_advertise_id) then
							if exists(select 1 from `cv2`.`summarydimension` where advertiseAffiliateId = d_affilliate_advertise_id and campaignId = d_campaign_id) then
									select advertiserId,affiliateId,siteId into d_advertiser_id,d_affilliate_id,d_site_id
											from `cv2`.`summarydimension` where advertiseAffiliateId = d_affilliate_advertise_id;
											
									select advertiseId into d_advertis_id from `cv2`.`advertiseaffiliate` where id = d_affilliate_advertise_id;
									
									/**add condition**/
									set share_sql = concat(share_sql,' and advertiseAffiliateId = ',d_affilliate_advertise_id);
							else
									set d_continuedo = 0;
									set d_matched_message = 'BAD DATA(Aid own a different campaign!)';		
									update `cv2`.`match_data` set disabled = 1,matched_message = d_matched_message where id = d_data_id;					
							end if;
					end if;
			end if;
			
			if d_continuedo = 1 then
			
					/**condition check: d_campaign_id**/
					set share_sql = concat(share_sql,' and campaignId = ',d_campaign_id);		
					
					/**condition check: d_effect_date**/
					set d_date_scope_start = date_format(d_effect_date,'%Y-%m-%d 00:00:00');
					set d_date_scope_end = date_format(d_effect_date,'%Y-%m-%d 23:59:59');
			
					if d_effect_type = 100 then
							set share_sql = concat(share_sql,' and trackTime = \'',d_effect_date,'\'');
					else
							set share_sql = concat(share_sql,' and trackTime >= \'',d_date_scope_start,'\'');
							set share_sql = concat(share_sql,' and trackTime <= \'',d_date_scope_end,'\'');
					end if;
			
					set order_sql = share_sql;
					set default_sql = share_sql;
					set ip_sql = share_sql;
					set final_sql = share_sql;
			
					set @ord_check = 0;
					set @ips_check = 0;
			
					/**condition check: d_effect_cid**/
					if d_effect_cid <> '' then 
							set @ord_check = 1;
							set order_sql = concat(order_sql,' and orderid = \'',d_effect_cid,'\'');
							set default_sql = concat(default_sql,' and orderid = \'',d_effect_cid,'\'');
					end if;
				
					/**condition check: d_effect_ip**/
					if d_effect_ip = '' then
							set @ips_check = 0;
					else
							set @ips_check = 1;
							set ip_sql = concat(ip_sql,' and trackIp = \'',d_effect_ip,'\'');
							set default_sql = concat(default_sql,' and trackIp = \'',d_effect_ip,'\'');
					end if;	

					/**order by**/
					set order_sql = concat(order_sql,' order by trackTime asc');
					set ip_sql = concat(ip_sql,' order by trackTime asc');
					set default_sql = concat(default_sql,' order by trackTime asc');
					set final_sql = concat(final_sql,' order by trackTime asc');
					
					/**limit**/
					set order_sql = concat(order_sql,' limit 0,1');
					set ip_sql = concat(ip_sql,' limit 0,1');
					set default_sql = concat(default_sql,' limit 0,1');
					set final_sql = concat(final_sql,' limit 0,1');
					
					/**execute default sql**/
					set @sql = concat(check_sql,default_sql);
					prepare fmd from @sql;
					execute fmd;
					deallocate prepare fmd;
					
					if @mrid > 0 then
							set @tmp = 0;
					else
							if @ord_check = 1 then
									/***check order***/
									set @sql = concat(check_sql,order_sql);
									prepare fmd from @sql;
									execute fmd;
									deallocate prepare fmd;
							end if;
							
							if @mrid > 0 then
									set @tmp = 0;
							else
									if @ips_check = 1 then
											/***check ip***/
											set @sql = concat(check_sql,ip_sql);
											prepare fmd from @sql;
											execute fmd;
											deallocate prepare fmd;
									end if;
									
									if @mrid > 0 then
											set @tmp = 0;
									else
											set @sql = concat(check_sql,final_sql);
											prepare fmd from @sql;
											execute fmd;
											deallocate prepare fmd;
									end if;
							end if;
					end if;
							
					set @siteCommisionOld = 0.000;
					set @darwCommisionOld = 0.000;
					set @siteCommisionNew = 0.000;
					set @darwCommisionNew = 0.000;					
									
					/**check mached**/
					if @mrid > 0 then
					
							/**compute comission**/
							call `cv2`.`proc_commision_rule`(1,d_campaign_id,d_effect_date,d_effect_type,d_effect_quantity,d_effect_amount,d_effect_commision,@siteCommisionOld,@darwCommisionOld,@siteCommisionNew,@darwCommisionNew);
														
							/**check ruletype**/
							if @rtype = d_effect_type then

									set d_matched = 1;
									
									/**update matched data**/
									update `cv2`.`report_trackmatch` set matched = 1,matchId = d_matched_id,dataTotal = d_effect_quantity,orderamount = d_effect_amount,siteCommisionOld = @siteCommisionOld, siteCommisionNew = @siteCommisionNew,darwCommisionOld = @darwCommisionOld,darwCommisionNew = @darwCommisionNew where id = @mrid;
									
									/**update task data status**/
									update `cv2`.`match_data` set matched_done = 1, matched_message = 'SUCCESS', mapping_aid = @afid, mapping_id = @mrid  where id = d_data_id;
									
									/**add counter for matching succeed**/
									set succeedTotal = succeedTotal + 1;
							else
									/**tracking lost, fix data into track_match**/
									SELECT advertiseAffiliateId,semId,advertiserId,trackCode,campaignId,affiliateId,siteId,subSiteId,dispalyType,dataType,dataTotal,ruleType,trackStatus,trackStep,orderinfo,trackIp,trackUser,referrerUrl,trackTime,createdAt,updatedAt,orderid,orderamount,country,city 
											into d_advertiseAffiliateId,d_semId,d_advertiserId,d_trackCode,d_campaignId,d_affiliateId,d_siteId,d_subSiteId,d_dispalyType,d_dataType,d_dataTotal,d_ruleType,d_trackStatus,d_trackStep,d_orderinfo,d_trackIp,d_trackUser,d_referrerUrl,d_trackTime,d_createdAt,d_updatedAt,d_orderid,d_orderamount,d_country,d_city
												 from `cv2`.`report_trackmatch` where id = @mrid;
									
									/**change field value for current matching**/
									set d_dataType = 3;
									set d_ruleType = d_effect_type;
									set d_dataTotal = d_effect_quantity;
									
									if d_dataTotal = 0 then
											set d_dataTotal = 1;
									end if;
									
									set d_orderamount = d_effect_amount;
									set d_orderid = d_effect_cid;
									set d_matched = 1;
									set d_matchId = d_matched_id;

									/**generate unique id for fix data, set datatype is 9**/
									insert into `cv2`.`affiliatetrackdata` (campaignId,semId,advertiseId,affiliateId,siteId,advertiseAffiliateId,ruleType,trackStep,trackIp,trackTime,dataType,createdAt) values(0,1,0,0,0,0,0,0,concat(date_format(sysdate(),'%Y%m%d%H%I%S'),' at ',d_data_id,' fix'),sysdate(),9,sysdate());
									set @cmridn = 0;
									select last_insert_id() into @cmridn;
									
									set d_trackStep = d_effect_type;
									set d_trackIp = concat(d_trackIp,' at ',@cmridn);
									
									/**save**/
									insert into `cv2`.`report_trackmatch` (id,advertiseAffiliateId,semId,advertiserId,trackCode,campaignId,affiliateId,siteId,subSiteId,dispalyType,dataType,dataTotal,ruleType,trackStatus,trackStep,orderinfo,trackIp,trackUser,referrerUrl,trackTime,createdAt,updatedAt,siteCommisionOld,siteCommisionNew,darwCommisionOld,darwCommisionNew,matched,matchId,orderid,orderamount,country,city) 
											values(@cmridn,d_advertiseAffiliateId,d_semId,d_advertiserId,d_trackCode,d_campaignId,d_affiliateId,d_siteId,d_subSiteId,d_dispalyType,d_dataType,d_dataTotal,d_ruleType,d_trackStatus,d_trackStep,d_orderinfo,d_trackIp,d_trackUser,d_referrerUrl,d_trackTime,d_createdAt,d_updatedAt,@siteCommisionOld,@siteCommisionNew,@darwCommisionOld,@darwCommisionNew,d_matched,d_matchId,d_orderid,d_orderamount,d_country,d_city);
									
									/**update task data status**/
									update `cv2`.`match_data` set matched_done = 2, matched_message = 'SUCCESS', mapping_aid = @afid, mapping_id = @cmridn  where id = d_data_id;
									
									/**add counter for matching succeed**/
									set succeedTotal = succeedTotal + 1;
							end if;
					else
							/**No site, change it to default**/
							set d_site_id = 1;
							
							/**fix the data for matching**/
							call `cv2`.proc_task_dofix(d_matched_id,d_data_id,d_advertiser_id,d_campaign_id,d_advertis_id,d_affilliate_id,d_site_id,d_affilliate_advertise_id,d_subsite_id,d_effect_date,d_effect_ip,d_effect_cid,d_effect_aid,d_effect_type,d_effect_quantity,d_effect_amount,d_effect_commision,d_match_action,d_matched_check,d_matched_done,d_matched_message,1,@cSuccees);							
							
							set succeedTotal = @cSuccees;
							
					end if;	
			end if;
		
END $$

DELIMITER ;
