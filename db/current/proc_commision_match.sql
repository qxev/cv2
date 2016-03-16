drop procedure IF EXISTS `cv2`.`proc_commision_match`;
DELIMITER $$
CREATE PROCEDURE `cv2`.`proc_commision_match`(IN p_task_id bigint(20),IN p_type smallint(1))
BEGIN
	DECLARE done INT DEFAULT 0;

	DECLARE d_task_id bigint(20);
	DECLARE d_task_name varchar(100);
	DECLARE d_task_date datetime;
	DECLARE d_task_startdate datetime;
	DECLARE d_task_enddate datetime;
	DECLARE d_task_match_action varchar(100);
	DECLARE d_user_id varchar(50);
	DECLARE d_match_total int(10);
	DECLARE d_check_success int(10);
	DECLARE d_check_failed int(10);
	DECLARE d_match_success int(10);
	DECLARE d_match_failed int(10);
	DECLARE d_check_message varchar(255);
	DECLARE d_match_message varchar(255);
	DECLARE d_exe_status smallint(5);
	DECLARE d_restart_task smallint(5);
	DECLARE d_matched_id varchar(40);
	DECLARE d_task_campaign_id bigint(20);
	DECLARE d_no_confirm_data smallint(1);
	DECLARE d_program_cycle bigint(20);

	DECLARE d_data_id bigint(20);
	DECLARE d_advertiser_id bigint(20);
	DECLARE d_campaign_id bigint(20);
	DECLARE d_advertis_id bigint(20);
	DECLARE d_affilliate_id bigint(20);
	DECLARE d_site_id bigint(20);
	DECLARE d_affilliate_advertise_id bigint(20);
	DECLARE d_subsite_id varchar(200);
	DECLARE d_effect_date datetime;
	DECLARE d_effect_ip varchar(50);
	DECLARE d_effect_cid varchar(100);
	DECLARE d_effect_aid varchar(100);
	DECLARE d_effect_type smallint(4);
	DECLARE d_effect_quantity int(10);
	DECLARE d_effect_amount float(14,2);
	DECLARE d_effect_commision float(14,2);
	DECLARE d_match_action varchar(50);
	DECLARE d_matched_check smallint(5);
	DECLARE d_matched_done smallint(5);
	DECLARE d_matched_message varchar(255);
	
	DECLARE d_date_scope_start datetime;
	DECLARE d_date_scope_end datetime;
	DECLARE d_date_scope_temp datetime;
	DECLARE d_date_scope_next datetime;

	DECLARE share_sql varchar(4000);
	DECLARE check_sql varchar(4000);
	DECLARE read_sql varchar(4000);

	DECLARE d_siteCommissionValue float(14,2);
	DECLARE d_darwCommissionValue float(14,2);
	DECLARE d_siteCommisionOld float(14,2);
	DECLARE d_darwCommisionOld float(14,2);
	DECLARE d_isRange float(14,2);
	DECLARE d_ruleType smallint(4);
	DECLARE d_campaignId bigint(20);
	DECLARE d_trackTime datetime;
	DECLARE d_orderamount float(14,2);
	DECLARE d_dataTotal  bigint(20);

	DECLARE d_commissionType int(4) default -1;
	DECLARE d_commissionRuleType int(4) default -1;

	DECLARE d_commissionValue float(12,2) default 0;
	DECLARE d_darwinCommissionValue float(12,2) default 0;
	DECLARE d_siteCommisionNew float(12,2) default 0;
	DECLARE d_darwCommisionNew float(12,2) default 0;

	DECLARE d_check_counter  bigint(20);
	DECLARE d_match_counter  bigint(20);

	DECLARE d_matched smallint(5) default 0;
	DECLARE d_continuedo smallint(5) default 0;
	DECLARE d_dataType smallint(5) default 0;

	/**Get all pre-matched data under current task**/
	DECLARE cur_in CURSOR FOR SELECT id,advertiser_id,campaign_id,advertis_id,affilliate_id,site_id,affilliate_advertise_id,subsite_id,effect_date,effect_ip,effect_cid,effect_aid,effect_type,effect_quantity,effect_amount,effect_commision,match_action,matched_check,matched_done,matched_message 
		FROM `cv2`.`match_data` where task_id = p_task_id and matched_done = 0;

	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;

	/**DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN
		insert into `cv2`.`report_task_result` (task_date,task_memo,task_result)
			values(sysdate(),concat('proc_commision_match(',p_task_id,',',p_type,',',d_data_id,' ',d_affilliate_advertise_id,' -- ',d_effect_aid,')'),0);
	END;**/

	set AUTOCOMMIT = 1;

	/**check task exists or not**/
	if exists(select 1 from `cv2`.`match_task` where task_id = p_task_id) then 
		/**clear old temp data**/
		delete from `cv2`.`affiliatetrackdata` where dataType = 9;

		/**clear task status**/
		update `cv2`.`match_task` set exe_status = 0, check_message = '', check_success = 0,match_success = 0, match_total = 0,program_cycle = 0 where task_id = p_task_id;

		/**read task information**/
		select match_action,task_startdate,task_enddate,no_confirm_data,campaign_id,task_name,task_date,user_id,match_total,check_success,check_failed,match_success,match_failed,check_message,match_message,exe_status,restart_task,match_id
			into d_task_match_action,d_task_startdate,d_task_enddate,d_no_confirm_data,d_task_campaign_id,d_task_name,d_task_date,d_user_id,d_match_total,d_check_success,d_check_failed,d_match_success,d_match_failed,d_check_message,d_match_message,d_exe_status,d_restart_task,d_matched_id
				from `cv2`.`match_task` where task_id = p_task_id;

		set d_check_success = 0;
		set d_match_success = 0;
		set d_program_cycle = 0;

		/**clear match data status**/
		update `cv2`.`match_data` set matched_done = 0,matched_message = '', mapping_aid = 0, mapping_id = 0, matched_check = 0 where task_id = p_task_id;

		/**mark the task status to start**/
		update `cv2`.`match_task` set exe_status = 1 where task_id = p_task_id;

		select count(1) into d_match_total from `cv2`.`match_data` where task_id = p_task_id and matched_done = 0;
		if d_match_total > 0 and d_matched_id <> '' then

			/**check task type and delete old data**/
			if p_type = 1 then
				/**insert into `cv2`.`report_task_result` (task_date,task_memo,task_result) values(sysdate(),concat('delete old data:',d_matched_id,' ',2),0);
				really match data,clear old data of fixed from matched source, which data type value is 3**/
				if d_task_match_action = 'match' then
					delete from `cv2`.`report_trackmatch` where matchId = d_matched_id and dataType = 3;
					update `cv2`.`report_trackmatch` set matched = 0 where matchId = d_matched_id;
				else
					delete from `cv2`.`report_trackmatch` where matchId = d_matched_id and dataType = 2;
				end if;
				commit;
			end if;
			
			update `cv2`.`match_task` set check_message = 'Data scanning started .....' where task_id = p_task_id;

			/**do match task for data**/
			OPEN cur_in;
				REPEAT
					FETCH cur_in INTO d_data_id,d_advertiser_id,d_campaign_id,d_advertis_id,d_affilliate_id,d_site_id,d_affilliate_advertise_id,d_subsite_id,d_effect_date,d_effect_ip,d_effect_cid,d_effect_aid,d_effect_type,d_effect_quantity,d_effect_amount,d_effect_commision,d_match_action,d_matched_check,d_matched_done,d_matched_message;
					IF NOT done THEN
						set d_dataType = 0;
					  set d_continuedo = 1;
						set d_program_cycle = d_program_cycle + 1;
						select d_check_success mod 100 into d_check_counter;
						select d_match_success mod 100 into d_match_counter;
						if d_check_success > 0 and d_check_counter = 0 then
							update `cv2`.`match_task` set check_success = d_check_success,match_total = d_match_total where task_id = p_task_id;
						end if;
						if d_match_success > 0 and d_match_counter = 0 then
							update `cv2`.`match_task` set match_success = d_match_success,match_total = d_match_total where task_id = p_task_id;
						end if;
						/**comp conditions**/
						set @mr = 0;
						set check_sql = 'select count(1) into @mr ';
						set share_sql = 'from `cv2`.`report_trackmatch` where matched = 0 ';
						if d_affilliate_advertise_id > 0 then 
							set share_sql = concat(share_sql,' and advertiseAffiliateId = ',d_affilliate_advertise_id);
							/**fix id**/
							if exists(select 1 from `cv2`.`summarydimension` where advertiseAffiliateId = d_affilliate_advertise_id) then
								select advertiserId,campaignId,affiliateId,siteId into d_advertiser_id,d_campaign_id,d_affilliate_id,d_site_id
									from `cv2`.`summarydimension` where advertiseAffiliateId = d_affilliate_advertise_id;
								select advertiseId into d_advertis_id from `cv2`.`advertiseaffiliate` where id = d_affilliate_advertise_id;
							else
								if d_campaign_id = 0 or d_site_id = 0 then
									/**stop**/
									set d_continuedo = 0;
								end if;
							end if;
						else 
							if d_campaign_id > 0 then
								set share_sql = concat(share_sql,' and campaignId = ',d_campaign_id);
							end if;
							if d_site_id > 0 then
								set share_sql = concat(share_sql,' and siteId = ',d_site_id);
							end if;
							
								if d_campaign_id = 0 or d_site_id = 0 then
									/**stop**/
									set d_continuedo = 0;
								end if;
						end if;
						
						if d_continuedo = 1 then
							/**continue**/
							if d_effect_cid <> '' then 
								set share_sql = concat(share_sql,' and orderid = \'',d_effect_cid,'\'');
							end if;
							
							set d_date_scope_start = date_format(d_effect_date,'%Y-%m-%d 00:00:00');
							set d_date_scope_end = date_format(d_effect_date,'%Y-%m-%d 23:59:59');
	
							set share_sql = concat(share_sql,' and trackTime >= \'',d_date_scope_start,'\'');
							set share_sql = concat(share_sql,' and trackTime <= \'',d_date_scope_end,'\'');
	
							if d_effect_ip <> '' then
								set share_sql = concat(share_sql,' and trackIp = \'',d_effect_ip,'\'');
							end if;						
	
							/**if the action is fix,then add matchId as query condition**/
							if d_match_action = 'fix' then
								set share_sql = concat(share_sql,' and matchId = \'',d_matched_id,'\'');
							end if;
	
							if d_effect_ip = '' then
								set d_effect_ip = concat(date_format(sysdate(),'%Y%m%d%H%I%S'),' at ',d_data_id);
							end if;
	
							/**execute sql**/
							set @sql = concat(check_sql,share_sql);
	
							/**temp sql for test**/
							/**insert into `cv2`.`report_task_result` (task_date,task_memo,task_result)
											values (sysdate(),@sql,2);**/
							
							/**execute sql**/
							prepare fmd from @sql;
							execute fmd;
							deallocate prepare fmd;
	
							/**check commision rule for current campaign**/
							set @trkDate = sysdate();
							set d_isRange = 0;
							set d_siteCommissionValue = 0;
							set d_darwCommissionValue = 0;
							if d_effect_quantity = 0 then
								set d_effect_quantity = 1;
							end if;
							if d_effect_commision > 0 then
								if exists(select 1 from `cv2`.`commissionladder` where campaignId = d_task_campaign_id and startDate <= @trkDate and endDate >= @trkDate and deleted = 0 and startCommission = d_effect_commision and isRange = 0) then
									select isRange,d_effect_quantity*siteCommissionValue,d_effect_quantity*darwinCommissionValue into d_isRange,d_siteCommissionValue,d_darwCommissionValue 
										from `cv2`.`commissionladder` 
											where campaignId = d_task_campaign_id and startDate <= @trkDate and endDate >= @trkDate and deleted = 0 and startCommission = d_effect_commision order by createdAt asc limit 0,1;
								else
									if exists(select 1 from `cv2`.`commissionladder` where campaignId = d_task_campaign_id and startDate <= @trkDate and endDate >= @trkDate and deleted = 0 and startCommission <= d_effect_commision and endCommission >=  d_effect_commision and isRange = 1) then
										select isRange,d_effect_quantity*siteCommissionValue,d_effect_quantity*darwinCommissionValue into d_isRange,d_siteCommissionValue,d_darwCommissionValue 
											from `cv2`.`commissionladder` 
												where campaignId = d_task_campaign_id and startDate <= @trkDate and endDate >= @trkDate and deleted = 0 and startCommission <= d_effect_commision and endCommission >=  d_effect_commision and isRange = 1 order by createdAt asc limit 0,1;
									end if;
								end if;
							end if;
	
							/**check mathed total**/
							if @mr > 0 then
								/**has found, update the status**/
								set @mrid = 0;
								set @afid = 0;
								set @sql = concat('select id,trackTime,advertiseAffiliateId into @mrid,@trkDate,@afid ',share_sql,' order by trackTime limit 0,1');
								prepare fmd from @sql;
								execute fmd;
								deallocate prepare fmd;
	
								if p_type = 1 then
									if d_match_action = 'match' then
										/**update the record**/
										if d_effect_commision > 0 then
											if d_siteCommissionValue > 0 then
												update `cv2`.`report_trackmatch` set matched = 1,matchId = d_matched_id,dataTotal = d_effect_quantity,siteCommisionNew = d_siteCommissionValue,darwCommisionNew = d_darwCommissionValue where id = @mrid;
											else
												update `cv2`.`report_trackmatch` set matched = 1,matchId = d_matched_id where id = @mrid;
											end if;
										else 
											update `cv2`.`report_trackmatch` set matched = 1,matchId = d_matched_id where id = @mrid;
										end if;
	
										update `cv2`.`match_data` set matched_done = 1, matched_message = 'SUCCESS', mapping_aid = @afid, mapping_id = @mrid  where id = d_data_id;
										set d_match_success = d_match_success + 1;
									else
										update `cv2`.`match_data` set matched_done = 0, matched_message = 'EXCEPTION', mapping_aid = @afid, mapping_id = @mrid  where id = d_data_id;
									end if;
								else 
									/**not really do match, just update it to be checked and has found**/
									update `cv2`.`match_data` set matched_check = 1, matched_message = 'RECORD FOUND', mapping_aid = @afid, mapping_id = @mrid where id = d_data_id;
									set d_check_success = d_check_success + 1;
								end if;
							else
								/**not found,fix the data, set data type is 2**/
								if p_type = 1 then
									if exists(select 1 from `cv2`.`site` where id = d_site_id) then
										/**change datatype default value, set to 2, from fixed source**/
										set d_dataType = 2;
										/**fix advertise id by default**/
										if not exists(select 1 from `cv2`.`advertise` where id = d_advertis_id and campaignId = d_task_campaign_id and deleted = 0) then
											select id into d_advertis_id from `cv2`.`advertise` where campaignId = d_task_campaign_id and deleted = 0 order by createdAt asc limit 0,1;
										end if;
	
										/**fix relationship between advertise and site**/
										if not exists(select 1 from `cv2`.`advertiseaffiliate` where advertiseId = d_advertis_id and siteId = d_site_id)  then
											insert into `cv2`.`advertiseaffiliate` (advertiseId,siteId,status,createdAt) values(d_advertis_id,d_site_id,2,sysdate());
											select last_insert_id() into d_affilliate_advertise_id;
										else
											if d_affilliate_advertise_id < 1 then
												select id into d_affilliate_advertise_id from `cv2`.`advertiseaffiliate` where advertiseId = d_advertis_id and siteId = d_site_id order by createdAt asc limit 0,1;
											end if;
										end if;
	
										/***get new id**/
										insert into `cv2`.`affiliatetrackdata` (campaignId,semId,advertiseId,affiliateId,siteId,advertiseAffiliateId,ruleType,trackStep,trackIp,trackTime,dataType,createdAt) values(0,1,0,0,0,0,0,0,concat(date_format(sysdate(),'%Y%m%d%H%I%S'),' at ',d_data_id),sysdate(),9,sysdate());
										set @cmrid = 0;
										select last_insert_id() into @cmrid;
	
										/**compute default commsion**/
										set d_siteCommisionOld = 0;
										set d_darwCommisionOld = 0;
										set d_siteCommisionNew = 0;
										set d_darwCommisionNew = 0;
	
										set d_ruleType = d_effect_type;
										set d_campaignId = d_task_campaign_id;
										set d_trackTime = d_effect_date;
										set d_dataTotal = d_effect_quantity;
										if d_dataTotal = 0 then 
											set d_dataTotal = 1;
										end if;
										set d_orderamount = d_effect_amount;
										if d_ruleType >= 100 then
											/**insert into `cv2`.`report_task_result` (task_date,task_memo,task_result) values(sysdate(),concat('type normal:',d_campaignId,' ',d_ruleType,' ',date(d_trackTime)),0);**/
											if exists(select 1 from `cv2`.`commissionrule` where campaignId = d_campaignId and ruleType = d_ruleType and verified = 2 and startDate <= date(d_trackTime) and endDate >= date(d_trackTime)) then
												/**insert into `cv2`.`report_task_result` (task_date,task_memo,task_result) values(sysdate(),'found commision',0);
												Find out the current commsion configuration**/
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
										
										if d_match_action <> 'match' then
											set d_matched = 0;
											set d_siteCommissionValue = 0;
											set d_darwCommissionValue = 0;
											set d_siteCommisionNew = 0;
											set d_darwCommisionNew = 0;
										else 
											set d_matched = 1;
											set d_siteCommisionNew = d_siteCommisionOld;
											set d_darwCommisionNew = d_darwCommisionOld;
										end if;
	
										if d_match_action = 'match' then
											/**change datatype to 3, from match source**/
											set d_dataType = 3;
										end if;
	
										/**set commision for new fix-in record**/
										if d_effect_commision > 0 then
											/**use re-computed commsion**/
											insert into `cv2`.`report_trackmatch` (id,advertiseAffiliateId,semId,advertiserId,trackCode,campaignId,affiliateId,siteId,subSiteId,dispalyType,dataType,dataTotal,ruleType,trackStatus,trackStep,orderinfo,trackIp,trackUser,referrerUrl,trackTime,createdAt,siteCommisionOld,siteCommisionNew,darwCommisionOld,darwCommisionNew,matched,matchId,orderid,orderamount) 
													values(@cmrid,d_affilliate_advertise_id,0,d_advertiser_id,d_effect_aid,d_task_campaign_id,d_affilliate_id,d_site_id,d_subsite_id,0,d_dataType,d_effect_quantity,d_effect_type,1,d_effect_type,'',d_effect_ip,d_effect_ip,'',d_effect_date,sysdate(),d_siteCommisionOld,d_siteCommissionValue,d_darwCommisionOld,d_darwCommissionValue,d_matched,d_matched_id,d_effect_cid,d_effect_amount);
										else
											/**use default commsion**/
											insert into `cv2`.`report_trackmatch` (id,advertiseAffiliateId,semId,advertiserId,trackCode,campaignId,affiliateId,siteId,subSiteId,dispalyType,dataType,dataTotal,ruleType,trackStatus,trackStep,orderinfo,trackIp,trackUser,referrerUrl,trackTime,createdAt,siteCommisionOld,siteCommisionNew,darwCommisionOld,darwCommisionNew,matched,matchId,orderid,orderamount) 
													values(@cmrid,d_affilliate_advertise_id,0,d_advertiser_id,d_effect_aid,d_task_campaign_id,d_affilliate_id,d_site_id,d_subsite_id,0,d_dataType,d_effect_quantity,d_effect_type,1,d_effect_type,'',d_effect_ip,d_effect_ip,'',d_effect_date,sysdate(),d_siteCommisionOld,d_siteCommisionNew,d_darwCommisionOld,d_darwCommisionNew,d_matched,d_matched_id,d_effect_cid,d_effect_amount);
										end if;
	
										if d_match_action = 'match' then
											update `cv2`.`match_data` set matched_done = 2,matched_message = 'SUCCESS',effect_ip = d_effect_ip, mapping_aid = d_affilliate_advertise_id, mapping_id = @cmrid where id = d_data_id;
										else
											update `cv2`.`match_data` set matched_done = 3,matched_message = 'SUCCESS',effect_ip = d_effect_ip, mapping_aid = d_affilliate_advertise_id, mapping_id = @cmrid where id = d_data_id;
										end if;
	
										set d_match_success = d_match_success + 1;
									else
										update `cv2`.`match_data` set matched_check = 3,matched_message = 'SITE NOT FOUND' where id = d_data_id;
									end if;
								else
									/**not really match, just update to be checked and not found**/
									update `cv2`.`match_data` set matched_check = 2,matched_message = 'RECORD NOT FOUND' where id = d_data_id;
									set d_check_success = d_check_success + 1;
								end if;
							end if;
						else
							update `cv2`.`match_data` set matched_check = 1, matched_message = 'BAD DATA', mapping_aid = 0, mapping_id = 0 where id = d_data_id;
						end if;
					END IF;
				UNTIL done END REPEAT;
			CLOSE cur_in;
			
			update `cv2`.`match_task` set exe_status = 2,check_success = d_check_success, check_message = 'Data scanning done.' where task_id = p_task_id;
		else
			/**allow to rebuild report**/
			select count(1) into d_match_total from `cv2`.`report_trackmatch` where campaignId = d_task_campaign_id and trackTime >= date_format(d_task_startdate,'%Y-%m-%d 00:00:00') and trackTime <= date_format(d_task_enddate,'%Y-%m-%d 23:59:59');
			if d_no_confirm_data = 1 then
				if p_type = 1 then
					update `cv2`.`report_trackmatch` set matched = 1,matchId = d_matched_id where campaignId = d_task_campaign_id and trackTime >= date_format(d_task_startdate,'%Y-%m-%d 00:00:00') and trackTime <= date_format(d_task_enddate,'%Y-%m-%d 23:59:59');
					update `cv2`.`match_task` set match_total = d_match_total,match_success = d_match_total,check_success = d_match_total where task_id = p_task_id;
				else
					update `cv2`.`match_task` set match_total = d_match_total,check_success = d_match_total where task_id = p_task_id;
				end if;
			else
				update `cv2`.`match_task` set match_total = d_match_total,check_message = 'No confirmation data found, do nothing.' where task_id = p_task_id;
			end if;

			/**no any confirm data**/
			update `cv2`.`match_task` set exe_status = 2,check_message = 'Data scanning done, No confirmation data found.' where task_id = p_task_id;
		end if;

		update `cv2`.`match_task` set check_success = d_check_success,match_success = d_match_success,match_total = d_match_total,program_cycle = d_program_cycle where task_id = p_task_id;

		/**rebuild report for current campaign when really match data**/
		if p_type = 1 and d_match_total > 0 then
			update `cv2`.`match_task` set check_message = 'Rebuilding report .....' where task_id = p_task_id;

			select date_format(d_task_startdate,'%Y-%m-%d 00:00:00') into d_date_scope_start;
			set d_date_scope_next = DATE_ADD(d_task_enddate, INTERVAL 1 DAY);
			select date_format(d_date_scope_next,'%Y-%m-%d 00:00:00') into d_date_scope_end;

			call `cv2`.`proc_create_daily_commision_by_campaign`(d_date_scope_start,d_date_scope_end,d_task_campaign_id);

			/**mark the task status to end**/
			update `cv2`.`match_task` set exe_status = 3,check_message = 'Rebuilding report done.' where task_id = p_task_id;
		end if;

		
	end if;
END $$

DELIMITER ;
