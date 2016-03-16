CREATE DEFINER=`cv2_production`@`192.168.1.%` PROCEDURE `cv2`.`proc_test`(IN p_startday integer)
BEGIN
	insert into `cv2`.`report_task_result` (task_date,task_memo,task_result)
			values(sysdate(),concat('proc_test()'),p_startday);
	
END