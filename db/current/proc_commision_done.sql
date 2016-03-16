drop procedure IF EXISTS `cv2`.`proc_commision_done`;
DELIMITER $$
CREATE PROCEDURE `cv2`.`proc_commision_done`(
IN d_campaign_id bigint(20),
IN d_task_start date,
IN d_task_end date)
BEGIN
		DECLARE c_day date;
		DECLARE c_continue_do smallint(2);
		DECLARE d_affiliateCommission float(12,2);
		DECLARE d_confirmedAffiliateCommission float(12,2);
		DECLARE d_darwinProfit float(12,2);
		
		set c_continue_do = 1;
		
		if exists(select 1 from `cv2`.`campaignhistory` where campaignId = d_campaign_id and startDate = d_task_start and endDate = d_task_end and confirmDate is null) then
				delete from `cv2`.`campaignhistory` where campaignId = d_campaign_id and startDate = d_task_start and endDate = d_task_end and confirmDate is null;
		else
				if exists(select 1 from `cv2`.`campaignhistory` where campaignId = d_campaign_id and startDate = d_task_start and endDate = d_task_end and confirmDate is not null) then
						set c_continue_do = 0;
				end if;
		end if;
		
		if c_continue_do = 1 then
				if exists(select 1 from `cv2`.`report_summary` where campaignId = d_campaign_id and summaryDate >=  d_task_start and summaryDate <= d_task_end limit 0,1) then
						select sum(sitecommision_total_old),sum(sitecommision_total_new),sum(darwcommision_total_new) into d_affiliateCommission, d_confirmedAffiliateCommission, d_darwinProfit
								from `cv2`.`report_summary` where campaignId = d_campaign_id and summaryDate >=  d_task_start and summaryDate <= d_task_end;
				else
						set d_affiliateCommission = 0;
						set d_confirmedAffiliateCommission = 0;
						set d_darwinProfit = 0;
				end if;
				
				insert into `cv2`.`campaignhistory` (campaignId,startDate,endDate,affiliateCommission,confirmedAffiliateCommission,darwinProfit,createdAt) 
						values(d_campaign_id,d_task_start,d_task_end,d_affiliateCommission,d_confirmedAffiliateCommission,d_darwinProfit,sysdate());
		end if;

END $$

DELIMITER ;