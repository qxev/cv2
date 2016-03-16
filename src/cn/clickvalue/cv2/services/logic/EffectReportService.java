package cn.clickvalue.cv2.services.logic;

import java.util.Calendar;
import java.util.List;

import cn.clickvalue.cv2.model.EffectReport;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class EffectReportService extends BaseService<EffectReport> {

	// public String summaryAll(EffectReport effectReport) {
	// StringBuffer sb = new StringBuffer();
	// sb.append(" select from EffectReport er where er.advertiserId = ? and
	// er.summaryDate between ? and ? ");
	//        
	// }

	public void summaryAll(Integer advertiserId) {
		Calendar instance = Calendar.getInstance();
		Calendar instance1 = Calendar.getInstance();
		instance.set(2008, 1, 2);
		instance1.set(2008, 10, 10);
		
		
		Object[]obj ={ advertiserId, instance.getTime(), instance1.getTime()};
		List find = this.getHibernateTemplate().find(" select sum(cpmCountOld) from ReportSummary e where e.advertiserId = ? and e.summaryDate between ? and ? group by e.summaryDate ",obj);
		
//		StringBuffer sb = new StringBuffer();
//		sb.append(" from ReportSummary e where e.advertiserId = ? and e.summaryDate between ? and ? ");
//		List objects = find(sb.toString(),obj);
	}
}
