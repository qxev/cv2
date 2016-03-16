package cn.clickvalue.cv2.services.logic;

import java.util.List;

import cn.clickvalue.cv2.model.AffiliateCampaign;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class AffiliateCampaignService extends BaseService<AffiliateCampaign> {

	private CampaignService campaignService;

	private UserService userService;

	public void setCampaignService(CampaignService campaignService) {
		this.campaignService = campaignService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * create new AffiliateCampaign Object if the object is null by user and campaign.
	 * 
	 * @param user
	 * @param campaign
	 * @return
	 */
	public AffiliateCampaign createOrUpdateAffiliateCampaign(Integer userId, Integer campaignId) {
		AffiliateCampaign affiliateCampaign = getCampaignByCampaignIdAndUserId(campaignId, userId);
		if (affiliateCampaign.getId() == null) {
			Campaign campaign = campaignService.get(campaignId);
			User user = userService.get(userId);
			affiliateCampaign.setCampaign(campaign);
			affiliateCampaign.setUser(user);
		}
		affiliateCampaign.setConcerned(Integer.valueOf(1));
		affiliateCampaign.setShowed(Integer.valueOf(0));
		save(affiliateCampaign);
		return affiliateCampaign;
	}

	public AffiliateCampaign getCampaignByCampaignIdAndUserId(Integer campaignId, Integer userId) {
		String hql = "from AffiliateCampaign a where a.campaign.id = ? and a.user.id = ? and a.concerned = ?";
		List<AffiliateCampaign> affiliateCampaigns = find(hql, campaignId, userId, Integer.valueOf(1));
		if (affiliateCampaigns != null && affiliateCampaigns.size() > 0) {
			return affiliateCampaigns.get(0);
		}
		return new AffiliateCampaign();
	}

	/**
	 * 根据 campaignid userid concerned 判断是否 存在记录
	 * 
	 * @param campaignId
	 * @param userId
	 * @param concerned
	 * @return boolean
	 */
	public boolean isConcerned(Integer campaignId, Integer userId, Integer concerned) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(a.id) from AffiliateCampaign a where a.campaign.id = ? ");
		sb.append(" and a.user.id = ? ");
		sb.append(" and a.concerned = ? ");
		List<?> list = this.getHibernateTemplate().find(sb.toString(), new Object[] { campaignId, userId, concerned });
		return Integer.valueOf(list.get(0).toString()) > 0 ? true : false;
	}
}
