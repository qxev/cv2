drop procedure IF EXISTS `cv2`.`proc_create_daily_commision`;
DELIMITER $$
CREATE DEFINER=`cv2_production`@`192.168.1.%` PROCEDURE `cv2`.`proc_create_daily_commision`(IN p_startday varchar(20),IN p_endday varchar(20))
BEGIN
	call `cv2`.`proc_create_daily_commision_by_campaign`(p_startday,p_endday,0);
END $$

DELIMITER ;
