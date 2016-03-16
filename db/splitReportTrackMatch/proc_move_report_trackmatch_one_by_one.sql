drop procedure IF EXISTS `cv2`.`proc_move_report_trackmatch_one_by_one`;
DELIMITER $$
CREATE PROCEDURE `cv2`.`proc_move_report_trackmatch_one_by_one`(IN p_name varchar(25), IN p_cur_date varchar(20))
BEGIN
	DECLARE d_step int(4) default 0;
	DECLARE done int(1) default 0;
	
	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET done=1;
	
	SET @sql = concat('SELECT @dataTotal:=count(1) FROM ',p_name,' WHERE trackTime >= "',DATE(p_cur_date),'" AND trackTime < "',DATE(DATE_ADD(p_cur_date, INTERVAL 1 DAY)),'"');
	prepare fmd from @sql;
 	execute fmd;
 	deallocate prepare fmd;
 	select CONCAT('total: ',@dataTotal);
 	WHILE d_step <= @dataTotal DO
		SET @sql = concat('INSERT INTO `report_trackmatch` (SELECT * FROM ',p_name,' WHERE trackTime >= "',DATE(p_cur_date),'" AND trackTime < "',DATE(DATE_ADD(p_cur_date, INTERVAL 1 DAY)),'" limit ',d_step,',1)');
	 	prepare fmd from @sql;
	 	execute fmd;
	 	deallocate prepare fmd;
		set d_step = d_step + 1;
	END WHILE;
END $$

DELIMITER ;