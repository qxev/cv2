DROP PROCEDURE IF EXISTS `cv2`.`proc_advertiser_sum`;
DELIMITER $$
CREATE DEFINER=`cv2_production`@`192.168.1.%` PROCEDURE `cv2`.`proc_advertiser_sum`()
BEGIN
DECLARE done,p_advertiserId,p_campaignId,p_value,p_type,p_advid,p_campId,p_sites INT(11) DEFAULT 0;
DECLARE p_campName,p_campaignName varchar(255);
DECLARE cur1 CURSOR FOR SELECT advertiserId,campaignId,campaignName,ruleType,sum(dataTotalOld) totalData FROM `cv2`.summarycommission WHERE affiliateId>0 AND balanced=0 GROUP BY advertiserId,campaignId,ruleType;
DECLARE cur2 CURSOR FOR SELECT advertiserId,campaignId,campaignName,count(*) sites FROM `cv2`.summaryDimension GROUP BY campaignId;
DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done=1;

OPEN cur1;
REPEAT FETCH cur1 INTO p_advertiserId,p_campaignId,p_campaignName,p_type,p_value;
IF NOT done THEN
	IF NOT EXISTS(select 1 from `cv2`.advertiserSumInfo where campaignId=p_campaignId and userId=p_advertiserId) THEN INSERT INTO `cv2`.advertiserSumInfo(userId,campaignId,campaignName,createdAt,updatedAt) VALUES (p_advertiserId,p_campaignId,p_campaignName,now(),now());
	END IF;
	IF p_type=100 THEN UPDATE `cv2`.advertiserSumInfo SET cpc=p_value WHERE campaignId=p_campaignId and userId=p_advertiserId;
	ELSEIF p_type=101 THEN UPDATE `cv2`.advertiserSumInfo SET cpl=p_value WHERE campaignId=p_campaignId and userId=p_advertiserId;
	ELSEIF p_type=102 THEN UPDATE `cv2`.advertiserSumInfo SET cps=p_value WHERE campaignId=p_campaignId and userId=p_advertiserId;
	ELSEIF p_type=105 THEN UPDATE `cv2`.advertiserSumInfo SET cpm=p_value WHERE campaignId=p_campaignId and userId=p_advertiserId;
	END IF;
END IF;
UNTIL done END REPEAT;
CLOSE cur1;

SET done=0;

OPEN cur2;
REPEAT FETCH cur2 INTO p_advid,p_campId,p_campName,p_sites;
IF NOT done THEN
	IF p_advid IS NOT NULL THEN
		IF NOT EXISTS(SELECT 1 FROM `cv2`.advertiserSumInfo WHERE campaignId=p_campId and userId=p_advid) THEN
			INSERT INTO `cv2`.`advertiserSumInfo` (userId,campaignId,campaignName,createdAt,updatedAt) VALUES (p_advid,p_campId,p_campName,sysdate(),sysdate());
		END IF;
		UPDATE `cv2`.advertiserSumInfo SET sites=p_sites WHERE campaignId=p_campId and userId=p_advid;
	END IF;
END IF;
UNTIL done END REPEAT;
CLOSE cur2;

END;
$$
DELIMITER ;