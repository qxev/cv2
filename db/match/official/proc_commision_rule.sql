drop procedure IF EXISTS `cv2`.`proc_commision_rule`;
DELIMITER $$
CREATE PROCEDURE `cv2`.`proc_commision_rule`(
IN p_confirm_mode smallint(2),
IN d_campaign_id bigint(20),
IN d_effect_date datetime,
IN d_effect_type smallint(4),
IN d_effect_quantity int(10),
IN d_effect_amount float(14,3),
IN d_effect_commision float(14,3),
OUT d_siteCommisionOld float(12,3),
OUT d_darwCommisionOld float(12,3),
OUT d_siteCommisionNew float(12,3),
OUT d_darwCommisionNew float(12,3))
BEGIN
		DECLARE d_ruleType smallint(4);
		DECLARE d_trackTime datetime;
		DECLARE d_dataTotal  bigint(20);
		DECLARE d_campaignId bigint(20);
		DECLARE d_orderamount float(14,3);
		
		DECLARE d_commissionType int(4) default -1;
		DECLARE d_commissionRuleType int(4) default -1;
		DECLARE d_commissionValue float(12,3) default 0;
		DECLARE d_darwinCommissionValue float(12,3) default 0;
		
		/**DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN
				insert into `cv2`.`report_task_result` (task_date,task_memo,task_result)
						values(sysdate(),concat('proc_commision_match(',p_task_id,',',p_type,',',d_data_id,' ',d_affilliate_advertise_id,' -- ',d_effect_aid,')'),0);
		END;**/

		if d_effect_quantity = 0 then
				set d_effect_quantity = 1;
		end if;
		
		/**compute default commission**/
		set d_ruleType = d_effect_type;
		set d_campaignId = d_campaign_id;
		set d_trackTime = date(d_effect_date);
		set d_dataTotal = d_effect_quantity;
		
		if d_dataTotal = 0 then 
			set d_dataTotal = 1;
		end if;
		set d_orderamount = d_effect_amount;
		
		if d_ruleType >= 100 then
			if exists(select 1 from `cv2`.`commissionrule` where campaignId = d_campaignId and ruleType = d_ruleType and verified = 2 and startDate <= d_trackTime and endDate >= d_trackTime) then
			
					select commissionType,ruleType,commissionValue,darwinCommissionValue into d_commissionType,d_commissionRuleType,d_commissionValue,d_darwinCommissionValue 
							from `cv2`.`commissionrule` 
									where campaignId = d_campaignId and ruleType = d_ruleType and verified = 2 and startDate <= d_trackTime and endDate >= d_trackTime;

					if d_commissionRuleType >= 100 then
						if d_commissionType = 2 then
							if d_commissionRuleType = 102 then
								set d_siteCommisionOld = d_orderamount*(d_commissionValue/100);
								set d_darwCommisionOld = d_orderamount*(d_darwinCommissionValue/100);
							end if;
						elseif d_commissionType = 1 then
							if d_commissionRuleType = 105 then
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
		
		/**compute confirm commission**/
		set @trkDate = date_format(d_effect_date,'%Y-%m-%d 00:00:00');
		if p_confirm_mode = 1 then
				if d_effect_commision > 0 then
						if exists(select 1 from `cv2`.`commissionladder` where campaignId = d_campaign_id and startDate <= @trkDate and endDate >= @trkDate and deleted = 0 and startCommission = d_effect_commision and isRange = 0) then
								select d_effect_quantity*siteCommissionValue,d_effect_quantity*darwinCommissionValue into d_siteCommisionNew,d_darwCommisionNew 
										from `cv2`.`commissionladder` 
												where campaignId = d_campaign_id and startDate <= @trkDate and endDate >= @trkDate and deleted = 0 and startCommission = d_effect_commision and isRange = 0 order by createdAt asc limit 0,1;
						else
								if exists(select 1 from `cv2`.`commissionladder` where campaignId = d_campaign_id and startDate <= @trkDate and endDate >= @trkDate and deleted = 0 and startCommission <= d_effect_commision and endCommission >=  d_effect_commision and isRange = 1) then
											select d_effect_quantity*siteCommissionValue,d_effect_quantity*darwinCommissionValue into d_siteCommisionNew,d_darwCommisionNew 
														from `cv2`.`commissionladder` 
																	where campaignId = d_campaign_id and startDate <= @trkDate and endDate >= @trkDate and deleted = 0 and startCommission <= d_effect_commision and endCommission >=  d_effect_commision and isRange = 1 order by createdAt asc limit 0,1;
								end if;
						end if;
				else
						set d_siteCommisionNew = d_siteCommisionOld;
						set d_darwCommisionNew = d_darwCommisionOld;
				end if;
		end if;
				
END $$

DELIMITER ;
