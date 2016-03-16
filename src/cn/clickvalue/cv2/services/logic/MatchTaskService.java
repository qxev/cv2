package cn.clickvalue.cv2.services.logic;

import java.util.Date;
import java.util.List;

import cn.clickvalue.cv2.model.MatchTask;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class MatchTaskService extends BaseService<MatchTask> {

	public Date getLastConfirmDate(Integer campaignId) {
		List<MatchTask> list = this.find("From MatchTask m where m.campaignId=? and exeStatus=3 and matchAction!='fix' order by taskEnddate ASC", campaignId);
		if(list == null || list.size() == 0){
			return null;
		}else{
			return list.get(list.size()-1).getTaskEnddate();
		}
	}
}
