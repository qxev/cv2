drop procedure IF EXISTS `cv2_cmatch`.`proc_task_dombs`;
DELIMITER $$
CREATE PROCEDURE `cv2_cmatch`.`proc_task_dombs`(
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
			DECLARE cpc_done INT DEFAULT 0;
			DECLARE d_cpc_id bigint(20);

			DECLARE d_date_scope_start datetime;
			DECLARE d_date_scope_end datetime;
			DECLARE d_date_scope date;
			
			DECLARE cpc_matched CURSOR FOR select data_id from `cv2_cmatch`.`match_mbs_tmp` where match_id = d_matched_id and match_date = d_date_scope;
			DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET cpc_done = 1;
			
			/**DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN
					insert into `cv2_cmatch`.`report_task_result` (task_date,task_memo,task_result)
							values(sysdate(),concat('proc_commision_match(',p_task_id,',',p_type,',',d_data_id,' ',d_affilliate_advertise_id,' -- ',d_effect_aid,')'),0);
			END;**/
			
			set d_date_scope = date(d_effect_date);
			select date_format(d_effect_date,'%Y-%m-%d 00:00:00') into d_date_scope_start;
			select date_format(d_effect_date,'%Y-%m-%d 23:59:59') into d_date_scope_end;			
			
			/**clear old data**/
			delete from `cv2_cmatch`.`match_mbs_tmp` where match_id = d_matched_id and match_date = d_date_scope;
			
			set @cmatched_id = d_matched_id;
			set @cmatched_date = d_date_scope;
			/**create new data**/
			
			set @csql = 'insert into	`cv2_cmatch`.`match_mbs_tmp`(';
			set @csql = concat(@csql,' select @cmatched_id,@cmatched_date,data_id from ');
			set @csql = concat(@csql,'(select a.id as data_id,rand()*a.id as rnd from `cv2_cmatch`.`report_trackmatch` a where campaignId = ',d_campaign_id,' and trackTime >= \'',d_date_scope_start,'\' and trackTime <= \'',d_date_scope_end,'\' order by rnd asc) m');	
			set @csql = concat(@csql,' limit 0, ',d_effect_quantity);
			set @csql = concat(@csql,')');

			prepare fmd from @csql;
			execute fmd;
			deallocate prepare fmd;
			
			OPEN cpc_matched;
					REPEAT
						FETCH cpc_matched INTO d_cpc_id;
						IF NOT cpc_done THEN
						
								/**update matched data**/
								update `cv2_cmatch`.`report_trackmatch` set matched = 1,matchId = d_matched_id,siteCommisionNew = siteCommisionOld,darwCommisionNew = darwCommisionOld where id = d_cpc_id;
								
								/**add counter for matching succeed**/
								set succeedTotal = succeedTotal + 1;								
							
						END IF;
					UNTIL cpc_done END REPEAT;
			CLOSE cpc_matched;

END $$

DELIMITER ;