drop procedure IF EXISTS `cv2`.`proc_match_task`;
DELIMITER $$
CREATE PROCEDURE `cv2`.`proc_match_task`(IN p_task_id bigint(20),IN p_action_type smallint(2))
BEGIN
	DECLARE done INT DEFAULT 0;
	
	/**task attributes**/
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
	
	/**match data attributes**/
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
	
	/**time variables**/
	DECLARE d_date_scope_start datetime;
	DECLARE d_date_scope_end datetime;
	DECLARE d_date_scope_temp datetime;
	DECLARE d_date_scope_next datetime;

	DECLARE d_match_counter  bigint(20);

	/**Get all pre-matched data under current task**/
	DECLARE cur_in CURSOR FOR SELECT id,advertiser_id,campaign_id,advertis_id,affilliate_id,site_id,affilliate_advertise_id,subsite_id,effect_date,effect_ip,effect_cid,effect_aid,effect_type,effect_quantity,effect_amount,effect_commision,match_action,matched_check,matched_done,matched_message 
		FROM `cv2`.`match_data` where task_id = p_task_id and matched_done = 0 and disabled = 0;

	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;

	/**DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN
		insert into `cv2`.`report_task_result` (task_date,task_memo,task_result)
			values(sysdate(),concat('proc_commision_match(',p_task_id,',',p_type,',',d_data_id,' ',d_affilliate_advertise_id,' -- ',d_effect_aid,')'),0);
	END;**/

	set AUTOCOMMIT = 1;

	/**check task exists or not**/
	if exists(select 1 from `cv2`.`match_task` where task_id = p_task_id) then 

		/**reset task status**/
		update `cv2`.`match_task` set exe_status = 1, check_message = '', check_success = 0,match_success = 0, match_total = 0,program_cycle = 0,exe_starttime = sysdate() where task_id = p_task_id;
	
		/**read task information**/
		select match_action,task_startdate,task_enddate,no_confirm_data,campaign_id,task_name,task_date,user_id,match_total,check_success,check_failed,match_success,match_failed,check_message,match_message,exe_status,restart_task,match_id
				into d_task_match_action,d_task_startdate,d_task_enddate,d_no_confirm_data,d_task_campaign_id,d_task_name,d_task_date,d_user_id,d_match_total,d_check_success,d_check_failed,d_match_success,d_match_failed,d_check_message,d_match_message,d_exe_status,d_restart_task,d_matched_id
					from `cv2`.`match_task` where task_id = p_task_id;
		
		/**init statistics counters**/
		set d_match_success = 0;
		set d_program_cycle = 0;
		set d_match_counter = 0;
		set @matchSucceed = 0;		
		
		/**check action type: 0: matching and rebuild report, 1:matching, 2:rebuild report**/
		/**matching**/
		if p_action_type = 0 or p_action_type = 1 then
			
			/**clear old temp data**/
			delete from `cv2`.`affiliatetrackdata` where dataType = 9;

			/**clear match data status**/
			update `cv2`.`match_data` set matched_done = 0,matched_message = '', mapping_aid = 0, mapping_id = 0, matched_check = 0, disabled = 0 where task_id = p_task_id;
			update `cv2`.`match_data` set effect_ip = '' where task_id = p_task_id and effect_ip like '%at%';
			
			/**count data for matching**/
			select count(1) into d_match_total from `cv2`.`match_data` where task_id = p_task_id and matched_done = 0;
			
			update `cv2`.`match_task` set match_total = d_match_total where task_id = p_task_id;
			
			/**check data total for matching**/
			if d_match_total > 0 then
				/**check task type and delete old data**/
				if d_task_match_action = 'match' then
						/**delete old matching fixed data**/
						delete from `cv2`.`report_trackmatch` where matchId = d_matched_id and dataType = 3;
						
						/**update matching status**/
						update `cv2`.`report_trackmatch` set matched = 0,siteCommisionNew = 0,darwCommisionNew = 0  where matchId = d_matched_id;
				end if;
				if d_task_match_action = 'fix' then
						/**delete all fixed data**/
						delete from `cv2`.`report_trackmatch` where matchId = d_matched_id and dataType = 2;
				end if;
				
				if d_task_match_action = 'mbs' then
						/**update matching status**/
						update `cv2`.`report_trackmatch` set matched = 0,siteCommisionNew = 0,darwCommisionNew = 0 where matchId = d_matched_id;
				end if;
				
				/**update task status**/
				update `cv2`.`match_task` set check_message = 'Data scanning started .....' where task_id = p_task_id;
	
				/**begin matching**/
				OPEN cur_in;
					REPEAT
						FETCH cur_in INTO d_data_id,d_advertiser_id,d_campaign_id,d_advertis_id,d_affilliate_id,d_site_id,d_affilliate_advertise_id,d_subsite_id,d_effect_date,d_effect_ip,d_effect_cid,d_effect_aid,d_effect_type,d_effect_quantity,d_effect_amount,d_effect_commision,d_match_action,d_matched_check,d_matched_done,d_matched_message;
						IF NOT done THEN
							/**progressing information for matching**/
							set d_program_cycle = d_program_cycle + 1;
							select d_program_cycle mod 300 into d_match_counter;
							if d_program_cycle > 0 and d_match_counter = 0 then
								update `cv2`.`match_task` set check_success = d_program_cycle  where task_id = p_task_id;
							end if;
							
							/**matching data by detail**/
							if d_match_action = 'match' then
								call `cv2`.proc_task_domatch(d_matched_id,d_data_id,d_advertiser_id,d_campaign_id,d_advertis_id,d_affilliate_id,d_site_id,d_affilliate_advertise_id,d_subsite_id,d_effect_date,d_effect_ip,d_effect_cid,d_effect_aid,d_effect_type,d_effect_quantity,d_effect_amount,d_effect_commision,d_match_action,d_matched_check,d_matched_done,d_matched_message,@matchSucceed);
							end if;
							
							/**fix data by detail**/
							if d_match_action = 'fix' then
								call `cv2`.proc_task_dofix(d_matched_id,d_data_id,d_advertiser_id,d_campaign_id,d_advertis_id,d_affilliate_id,d_site_id,d_affilliate_advertise_id,d_subsite_id,d_effect_date,d_effect_ip,d_effect_cid,d_effect_aid,d_effect_type,d_effect_quantity,d_effect_amount,d_effect_commision,d_match_action,d_matched_check,d_matched_done,d_matched_message,0,@matchSucceed);
							end if;
							
							/**matching data by total summary, for CPC**/
							if d_match_action = 'mbs' then
								call `cv2`.proc_task_dombs(d_matched_id,d_data_id,d_advertiser_id,d_campaign_id,d_advertis_id,d_affilliate_id,d_site_id,d_affilliate_advertise_id,d_subsite_id,d_effect_date,d_effect_ip,d_effect_cid,d_effect_aid,d_effect_type,d_effect_quantity,d_effect_amount,d_effect_commision,d_match_action,d_matched_check,d_matched_done,d_matched_message,1,@matchSucceed);
							end if;

						END IF;
					UNTIL done END REPEAT;
				CLOSE cur_in;
				
				select count(1) into d_match_success from `cv2`.`match_data` where task_id = p_task_id and matched_done > 0 and disabled = 0;
				
				update `cv2`.`match_task` set exe_status = 2,check_success = d_program_cycle,match_success = d_match_success,program_cycle = d_program_cycle, check_message = 'Data scanning done.' where task_id = p_task_id;
			else
				set d_check_message = 'done.';
				/**check no_confirm_data attribute**/
				if d_no_confirm_data = 1 then
					/**check match action**/
					if d_task_match_action = 'match' then
						/**count data for matching from report_trackmatch**/
						select count(1) into d_match_total from `cv2`.`report_trackmatch` where trackTime >= date_format(d_task_startdate,'%Y-%m-%d 00:00:00') and trackTime <= date_format(d_task_enddate,'%Y-%m-%d 23:59:59') and campaignId = d_task_campaign_id;
						
						/**update matching status**/
						update `cv2`.`report_trackmatch` set matched = 1,matchId = d_matched_id,siteCommisionNew=siteCommisionOld, darwCommisionNew=darwCommisionOld where trackTime >= date_format(d_task_startdate,'%Y-%m-%d 00:00:00') and trackTime <= date_format(d_task_enddate,'%Y-%m-%d 23:59:59') and campaignId = d_task_campaign_id;
					end if;
				else
					set d_check_message = 'No confirmation data found, do nothing.';
				end if;
	
				/**update task status**/
				/**update `cv2`.`match_task` set exe_status = 3,check_success = d_match_total,match_success = d_match_total,check_message = d_check_message where task_id = p_task_id;**/
			end if;
		end if;
		
		/**rebuild report**/
		if p_action_type = 0 or p_action_type = 2 then
				/**update task status**/
				update `cv2`.`match_task` set check_message = 'Rebuilding report .....' where task_id = p_task_id;
	
				/**change date**/
				select date_format(d_task_startdate,'%Y-%m-%d 00:00:00') into d_date_scope_start;
				set d_date_scope_next = DATE_ADD(d_task_enddate, INTERVAL 1 DAY);
				select date_format(d_date_scope_next,'%Y-%m-%d 00:00:00') into d_date_scope_end;

				/**execute rebuild**/
				call `cv2`.`proc_create_daily_commision_by_campaign`(d_date_scope_start,d_date_scope_end,d_task_campaign_id);
	
				if d_task_match_action = 'match' then
						/**update commision for advertiser**/
						call `cv2`.`proc_commision_done`(d_task_campaign_id,date(d_task_startdate),date(d_task_enddate));
				end if;
	
				/**update task status**/
				update `cv2`.`match_task` set exe_status = 3,check_message = 'Rebuilding report done.' where task_id = p_task_id;
		end if;
		
		update `cv2`.`match_task` set exe_endtime = sysdate() where task_id = p_task_id;
	end if;
END $$

DELIMITER ;