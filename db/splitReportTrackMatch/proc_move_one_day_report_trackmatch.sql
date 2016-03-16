DROP PROCEDURE IF EXISTS `cv2`.`proc_move_one_day_report_trackmatch`;
DELIMITER $$
CREATE PROCEDURE `cv2`.`proc_move_one_day_report_trackmatch`(IN p_name varchar(25), IN p_cur_date varchar(20))
BEGIN
	DECLARE d_count int(4) default 0;
	DECLARE d_step int(4) default 0;
	
	SET @sql = concat('SELECT @sourceDataTotal:=count(1) FROM ',p_name,' WHERE trackTime >= "',DATE(p_cur_date),'" AND trackTime < "',DATE(DATE_ADD(p_cur_date, INTERVAL 1 DAY)),'"');
	prepare fmd from @sql;
 	execute fmd;
 	deallocate prepare fmd;
 	
 	SET @sql = concat('SELECT @targetDataTotal:=count(1) FROM `report_trackmatch` WHERE trackTime >= "',DATE(p_cur_date),'" AND trackTime < "',DATE(DATE_ADD(p_cur_date, INTERVAL 1 DAY)),'"');
	prepare fmd from @sql;
 	execute fmd;
 	deallocate prepare fmd;
 	
 	IF @sourceDataTotal <> @targetDataTotal THEN
 	
 		SET @sql = concat('DELETE FROM `report_trackmatch` WHERE trackTime >= "',DATE(p_cur_date),'" AND trackTime < "',DATE(DATE_ADD(p_cur_date, INTERVAL 1 DAY)),'"');
		prepare fmd from @sql;
	 	execute fmd;
	 	deallocate prepare fmd;
 		
 		SET d_count = @sourceDataTotal DIV 10000;
	 	WHILE d_step <= d_count DO
			SET @sql = concat('INSERT INTO `report_trackmatch` (SELECT * FROM ',p_name,' where trackTime >= "',DATE(p_cur_date),'" AND trackTime < "',DATE(DATE_ADD(p_cur_date, INTERVAL 1 DAY)),'" limit ',10000*(d_step),',10000)');
		 	prepare fmd from @sql;
		 	execute fmd;
		 	deallocate prepare fmd;
			set d_step = d_step + 1;
		END WHILE;
 	END IF;
END
$$ DELIMITER ;
