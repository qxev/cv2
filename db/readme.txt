����ϵͳ˵����

����5���洢����

1.proc_import_daily_cpmdata
	������(��ʼ���ڣ���ֹ����)
	˵��������ָ�����������䣬�������affiliatecpmdata���е����ݣ��Զ���CPM���ݲ��뵽affiliatetrackdata�У�
	ִ�У��˽ű�Ӧÿ���Զ�ִ��һ�Σ�ͬһʱ������ɷ���ִ��

2.proc_import_daily_trackdata
	������<��>
	˵�������ݱ����� report_timestamp ���е�ʱ���趨���Զ��� affiliatetrackdata ���е���������ѡ�뵽��ʱ�� report_trackdata �У�
	      �ڴ˹�����Ϻ��Զ�������һ������ proc_parse_daily_commision���������˵��
	ִ�У��˽ű��ɰ��Զ������ڿɷ���ִ��


	3.proc_parse_daily_commision
		������<��>
		˵��������ɨ�� report_trackdata ��ȡϵͳ��Ӷ����򣬰���Ӷ��������Ӷ�𣬽�������浽 report_trackmatch ���С�
		ִ�У��� proc_import_daily_trackdata �Զ��������ɷ���ִ��


4.proc_create_daily_commision
	������(��ʼ���ڣ���ֹ����)
	˵�������ݸ������������䣬������� report_trackmatch ����Ӷ��ƥ��ǰ����������ͳ�����ݣ�CPM/CPC/CPL/CPS���� summarycommission ���У�������ֱ�ļ�Ч����ͳ��ת��Ϊˮƽ��ͳ�Ƶ��� report_summary ���ṩ��Ҫ��ʵʱͳ�Ʋ�ѯ��
	      �ڴ˹���ִ����Ϻ��Զ�������һ������ proc_create_daily_effect��������˵��
	ִ�У��˽ű��ɰ��Զ������ڷ���ִ��


	5.proc_create_daily_effect
		������(��ʼ���ڣ���ֹ����)
		˵�������ݸ������������䣬������ܸ���TrackStep�ĸ���Ч�����ݵ��� summaryeffect ��
		ִ�У��˽ű��ɰ��Զ������ڷ���ִ��,�� proc_import_daily_trackdata �Զ�����


�ؽ����ݲ������裺

�ؽ�CPM
call `cv2`.`proc_import_daily_cpmdata`('2008-09-10 00:00:00','2008-09-23 00:00:00');

�޸�report_timestamp�������趨���ؽ�Ӷ�������
call cv2.proc_import_daily_trackdata();


����match_data�е��ظ�ip+date:
select a.id,a.effect_date,a.effect_ip from match_data a, (select * from (select effect_date, effect_ip,count(1) as total from match_data where task_id = 1225779032349  group by effect_date, effect_ip) t where total > 1) b where a.effect_date = b.effect_date and a.effect_ip = b.effect_ip order by effect_date,effect_ip;

update match_data set effect_date = date_add(effect_date,interval 1 SECOND) where id in (17779,17889,18869,17651,18496,18752,17526,19410,17156,19457,20028,18915);




��Ӷ�������ֳɹ�����Ʊ�

id,
campaignId
start_cv Ӷ��ֵһ
end_cv Ӷ��ֵ��
start_date ��Ч��ʼ����
end_date ��Ч��ֹ����
cv_type �Ƿ�Ϊ��Χֵ
darwin_share ���ŷֳ�ֵ
site_share ��վ�ֳ�ֵ


Ӷ��ƥ��鿴ִ��״̬
select a.task_id,a.task_name,a.campaign_id,count(1) as dataTotal ,a.match_total,a.match_success,a.check_success,a.program_cycle,a.exe_status,a.check_message,a.match_message,a.task_startdate,a.task_enddate from match_data b left join match_task a on a.task_id = b.task_id group by task_id;

�ж�CPL(S)�������Ƿ��ظ�
select count(1) from (select distinct effect_cid from match_data where task_id = 1225779032349) t;

�鿴ĳ��campaign�ı���ˢ�����
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


Ӷ�����ݲ��룺
call `cv2`.`proc_commision_match`(1225779032349,1);
