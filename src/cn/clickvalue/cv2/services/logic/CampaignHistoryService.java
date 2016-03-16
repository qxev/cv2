package cn.clickvalue.cv2.services.logic;

import java.util.ArrayList;
import java.util.List;

import cn.clickvalue.cv2.model.CampaignHistory;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class CampaignHistoryService extends BaseService<CampaignHistory> {

	/**
	 * 根据userid 获取 
	 * @param userId
	 * @param isHasDate
	 *            是否有日期
	 * @return
	 */
	public List<CampaignHistory> findCampaignHistoryByUserId(Integer userId,
			boolean isHasDate) {
		List<CampaignHistory> campaignHistorys = new ArrayList<CampaignHistory>();
		StringBuffer sb = new StringBuffer();
		sb.append(" from CampaignHistory c where c.campaign.user.id = ? and c.confirmDate");
		if (isHasDate) {
			sb.append(" is not null ");
		} else {
			sb.append(" is null ");
		}
		sb.append("order by c.confirmDate DESC, c.createdAt DESC");
		campaignHistorys = find(sb.toString(), userId);
		return campaignHistorys;
	}
	
	
	public CampaignHistory get(Integer id){
		return findUniqueBy("id", id);
	}
}
