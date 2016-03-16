报表系统说明：

共分5个存储过程

1.proc_import_daily_cpmdata
	参数：(开始日期，截止日期)
	说明：根据指定的日期区间，按天汇总affiliatecpmdata表中的数据，自动将CPM数据插入到affiliatetrackdata中；
	执行：此脚本应每天自动执行一次，同一时间区间可反复执行

2.proc_import_daily_trackdata
	参数：<无>
	说明：根据保存在 report_timestamp 表中的时间设定，自动将 affiliatetrackdata 表中的最新数据选入到临时表 report_trackdata 中，
	      在此过程完毕后，自动呼叫另一个过程 proc_parse_daily_commision，见下面的说明
	执行：此脚本可按自定义周期可反复执行


	3.proc_parse_daily_commision
		参数：<无>
		说明：逐行扫描 report_trackdata 表，取系统的佣金规则，按照佣金规则计算佣金，将结果保存到 report_trackmatch 表中。
		执行：由 proc_import_daily_trackdata 自动触发，可反复执行


4.proc_create_daily_commision
	参数：(开始日期，截止日期)
	说明：根据给定的日期区间，按天汇总 report_trackmatch 表中佣金匹配前后的相关收益统计数据（CPM/CPC/CPL/CPS）到 summarycommission 表中，并将垂直的绩效步骤统计转化为水平的统计到表 report_summary 中提供简要的实时统计查询。
	      在此过程执行完毕后，自动呼叫另一个过程 proc_create_daily_effect，见下面说明
	执行：此脚本可按自定义周期反复执行


	5.proc_create_daily_effect
		参数：(开始日期，截止日期)
		说明：根据给定的日期区间，按天汇总各个TrackStep的跟踪效果数据到表 summaryeffect 中
		执行：此脚本可按自定义周期反复执行,由 proc_import_daily_trackdata 自动触发


重建数据操作步骤：

重建CPM
call `cv2`.`proc_import_daily_cpmdata`('2008-09-10 00:00:00','2008-09-23 00:00:00');

修改report_timestamp中日期设定，重建佣金计算结果
call cv2.proc_import_daily_trackdata();


修正match_data中的重复ip+date:
select a.id,a.effect_date,a.effect_ip from match_data a, (select * from (select effect_date, effect_ip,count(1) as total from match_data where task_id = 1225779032349  group by effect_date, effect_ip) t where total > 1) b where a.effect_date = b.effect_date and a.effect_ip = b.effect_ip order by effect_date,effect_ip;

update match_data set effect_date = date_add(effect_date,interval 1 SECOND) where id in (17779,17889,18869,17651,18496,18752,17526,19410,17156,19457,20028,18915);




按佣金具体金额分成规则控制表：

id,
campaignId
start_cv 佣金值一
end_cv 佣金值二
start_date 有效开始日期
end_date 有效截止日期
cv_type 是否为范围值
darwin_share 达闻分成值
site_share 网站分成值


佣金匹配查看执行状态
select a.task_id,a.task_name,a.campaign_id,count(1) as dataTotal ,a.match_total,a.match_success,a.check_success,a.program_cycle,a.exe_status,a.check_message,a.match_message,a.task_startdate,a.task_enddate from match_data b left join match_task a on a.task_id = b.task_id group by task_id;

判断CPL(S)订单号是否重复
select count(1) from (select distinct effect_cid from match_data where task_id = 1225779032349) t;

查看某个campaign的报告刷新情况
SELECT
`report_summary`.`campaignName`,
`report_summary`.`campaignId`,
`report_summary`.`siteName`,
`report_summary`.`siteId`,
`report_summary`.`summaryDate`,
sum(`report_summary`.`cpm_count_old`) as cpmo,
sum(`report_summary`.`cpc_count_old`) as cpco,
sum(`report_summary`.`cpc_count_old`) as cpcn,
sum(`report_summary`.`cpl_count_old`) as cplo,
sum(`report_summary`.`cpl_count_new`) as cpln,
sum(`report_summary`.`cpl_sitecommision_old`) as cplso,
sum(`report_summary`.`cpl_sitecommision_new`) as cplsn,
sum(`report_summary`.`cpl_darwcommision_old`) as cpldo,
sum(`report_summary`.`cpl_darwcommision_new`) as cpldn,
sum(`report_summary`.`sitecommision_total_old`) as sto,
sum(`report_summary`.`sitecommision_total_new`) as stn,
sum(`report_summary`.`darwcommision_total_old`) as dto,
sum(`report_summary`.`darwcommision_total_new`) as dtn
FROM
`report_summary`
where 
`report_summary`.`campaignId` = 6
GROUP BY
`report_summary`.`campaignId`,
`report_summary`.`siteId`,
`report_summary`.`summaryDate`
order by 
`report_summary`.`summaryDate` asc


佣金数据补入：
call `cv2`.`proc_commision_match`(1225779032349,1);
