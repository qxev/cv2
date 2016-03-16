DROP PROCEDURE IF EXISTS `cv2`.`proc_move_report_trackmatch_by_day`;
DELIMITER $$
CREATE PROCEDURE `cv2`.`proc_move_report_trackmatch_by_day`(IN p_name varchar(25), IN p_startday varchar(20), IN p_endday varchar(20))
BEGIN
	DECLARE cur_date DATE;
	SET cur_date = p_startday;
	
	WHILE cur_date <= p_endday DO
		CALL proc_move_one_day_report_trackmatch(p_name, cur_date);
		SET cur_date = DATE_ADD(cur_date, INTERVAL 1 DAY);
	END WHILE;
END
$$ DELIMITER ;
