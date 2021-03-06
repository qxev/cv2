drop procedure IF EXISTS `cv2`.`proc_operation_report`;
DELIMITER $$
CREATE PROCEDURE `cv2`.`proc_operation_report`(IN p_start_day date,IN p_end_day date)
BEGIN
		DECLARE d_pendingApprovalWebSites int(11);
		DECLARE d_approvedWebsites int(11);
		DECLARE d_declinedWebsites int(11);
		DECLARE d_dailyActivedWebsites int(11);
		DECLARE d_activedWebsitesForOneMonth int(11);
		DECLARE d_activedWebsitesForThreeMonth int(11);
		DECLARE d_activedWebsitesForHalfAYear int(11);
		DECLARE d_affiliateClicks int(11);
		DECLARE d_beClickedAds int(11);
		DECLARE d_beClickedCampaigns int(11);
		DECLARE d_beAppliedCampaigns int(11);
		DECLARE d_gotCodes int(11);
		DECLARE d_newCampaigns int(11);
		DECLARE d_newAds int(11);
		DECLARE d_validAds int(11);
		DECLARE d_pageView int(11);
		
		DECLARE s_today date;
		DECLARE c_day date;
		DECLARE c_time datetime;
		DECLARE c_time_start datetime;
		DECLARE c_time_end datetime;
		DECLARE c_back_one_month_day date;
		DECLARE c_back_three_month_day date;
		DECLARE c_back_six_month_day date;
		
		
		set s_today = date(sysdate());
		set c_day = p_start_day;
		set c_time = sysdate();
		
		
		if p_end_day > s_today then
				set p_end_day = s_today;
		end if;
		
		REPEAT
				set c_time_start = date_format(c_day,'%Y-%m-%d 00:00:00');
				set c_time_end = date_format(c_day,'%Y-%m-%d 23:59:59');
				
				set c_back_one_month_day = date_add(c_day,interval -1 month);
				set c_back_three_month_day = date_add(c_day,interval -3 month);
				set c_back_six_month_day = date_add(c_day,interval -6 month);
				
				select count(1) into d_pendingApprovalWebSites from `cv2`.`site` where (verified <= 1) and date(createdAt) = c_day;
				select count(1) into d_approvedWebsites from `cv2`.`site` where verified = 2 and date(updatedAt) = c_day;
				select count(1) into d_declinedWebsites from `cv2`.`site` where verified = 3 and date(updatedAt) = c_day;
				
				select count(1) into d_dailyActivedWebsites from (select siteId from `cv2`.`report_summary` where summaryDate = c_day and cpc_count_old > 0 group by siteId) t;
				select count(1) into d_activedWebsitesForOneMonth from (select siteId from `cv2`.`report_summary` where summaryDate >= c_back_one_month_day and summaryDate <= c_day and cpc_count_old > 0 group by siteId) t;
				select count(1) into d_activedWebsitesForThreeMonth from (select siteId from `cv2`.`report_summary` where summaryDate >= c_back_three_month_day and summaryDate <= c_day and cpc_count_old > 0 group by siteId) t;
				select count(1) into d_activedWebsitesForHalfAYear from (select siteId from `cv2`.`report_summary` where summaryDate >= c_back_six_month_day and summaryDate <= c_day and cpc_count_old > 0 group by siteId) t;
				
				select sum(cpc_count_old) into d_affiliateClicks from `cv2`.`report_summary` where summaryDate = c_day;
				select count(1) into d_beClickedAds from (select bannerId from `cv2`.`report_summary` where summaryDate = c_day and cpc_count_old > 0 group by bannerId) t;
				select count(1) into d_beClickedCampaigns from (select campaignId from `cv2`.`report_summary` where summaryDate = c_day and cpc_count_old > 0 group by campaignId) t;
				
				select count(1) into d_beAppliedCampaigns from (select campaignId from `cv2`.`campaignsite` where verified = 2 and date(createdAt) = c_day group by campaignId) t;
				select count(1) into d_gotCodes from `cv2`.`advertiseaffiliate` where date(createdAt) = c_day;
				select count(1) into d_newCampaigns from `cv2`.`campaign` where startDate = c_day;
				
				select count(1) into d_newAds from `cv2`.`banner` where date(createdAt) = c_day;
				
				select count(1) into d_validAds from `cv2`.`banner` a left join `cv2`.`campaign` b on a.campaignId = b.id where b.verified = 2 and b.startDate <= c_day and b.endDate >= c_day and a.verified = 2;
				select sum(dataTotal) into d_pageView from `cv2`.`affiliatecpmdata` where trackTime >= c_time_start and trackTime <= c_time_end;
				
				/**save**/
				if exists(select 1 from `cv2`.`affiliate_summary` where summaryDate = c_day) then
						delete from `cv2`.`affiliate_summary` where summaryDate = c_day;
			 	end if;	
				
				insert into `cv2`.`affiliate_summary` (pendingApprovalWebSites,approvedWebsites,declinedWebsites,dailyActivedWebsites,activedWebsitesForOneMonth,activedWebsitesForThreeMonth,activedWebsitesForHalfAYear,affiliateClicks,beClickedAds,beClickedCampaigns,beAppliedCampaigns,gotCodes,newCampaigns,newAds,validAds,pageView,summaryDate,createdAt) 
								values(d_pendingApprovalWebSites,d_approvedWebsites,d_declinedWebsites,d_dailyActivedWebsites,d_activedWebsitesForOneMonth,d_activedWebsitesForThreeMonth,d_activedWebsitesForHalfAYear,d_affiliateClicks,d_beClickedAds,d_beClickedCampaigns,d_beAppliedCampaigns,d_gotCodes,d_newCampaigns,d_newAds,d_validAds,d_pageView,c_day,c_time);
				
				
				set c_day = date_add(c_day, interval 1 day);
				
		UNTIL c_day > p_end_day END REPEAT;
		
END $$

DELIMITER ;
