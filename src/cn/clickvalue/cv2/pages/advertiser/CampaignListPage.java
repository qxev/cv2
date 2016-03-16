package cn.clickvalue.cv2.pages.advertiser;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.grid.HibernateDataSource1;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.components.advertiser.CApplyingMode;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.dao.hibernate.HqlQueryObject;
import cn.clickvalue.cv2.services.logic.CampaignService;

public class CampaignListPage extends BasePage {
	

	@Component(id = "grid", parameters = {"source=dataSource", "row=campaign", "model=beanModel",
			"pagerPosition=literal:bottom", "rowsPerPage=noOfRowsPerPage"})
	private Grid grid;

	private BeanModel<Campaign> beanModel;

	@Inject
	private BeanModelSource beanModelSource;
	
	@Property
	@InjectComponent(value = "CApplyingMode")
	private CApplyingMode cApplyingMode;
	
	@Inject
	private CampaignService campaignService;

	@Property
	private Campaign campaign;

	@InjectPage
	private CampaignEditPage campaignEditPage;

	@Persist
	@Property
	private String campName;

	@Inject
	private ComponentResources componentResources;

	@Property
	private GridDataSource dataSource;

	@InjectPage
	private MessagePage messagePage;
	
	@SetupRender
	public void setupRender() {
		HqlQueryObject queryObject = new HqlQueryObject(getHql(false));
		this.dataSource = new HibernateDataSource1(campaignService,
				queryObject, Campaign.class, getHql(true));
		
	}

	private void cancelApp() {
		try {
			if (campaign.getVerified() == Constants.PENDING_APPROVAL) {
				updateVerified(Constants.NOT_SUBMITTED, campaign);
				messagePage.setMessage(getMessages().get("action_success"));
			}
		} catch (BusinessException e) {
			messagePage.setMessage(e.getMessage());
		}
	}

	private void deleteCampaign() {
		try {
			updateDeleted(campaign);
			messagePage.setMessage(getMessages().get("action_success"));
		} catch (BusinessException e) {
			messagePage.setMessage(e.getMessage());
		}
	}

	public BeanModel<Campaign> getBeanModel() {
		beanModel = beanModelSource.create(Campaign.class, true,componentResources);
		beanModel.add("logo", null).label("Logo").sortable(false);
		beanModel.get("name").label(getMessages().get("campaign_name")).sortable(false);
		beanModel.get("cpa").label(getMessages().get("commision_rule")).sortable(false);
		beanModel.add("operate", null).label(getMessages().get("operate")).sortable(false);
		beanModel.get("verified").label(getMessages().get("application_status")).sortable(false);
		beanModel.get("region").label(getMessages().get("region")).sortable(false);
		beanModel.add("bannerCount",null).label(getMessages().get("number_of_ads")).sortable(false);
		beanModel.get("startDate").label(getMessages().get("begin_time")).sortable(false);
		beanModel.get("endDate").label(getMessages().get("end_time")).sortable(false);
		beanModel.add("onlineStatus", null).label(getMessages().get("advertising_activity")).sortable(false);
		beanModel.include("logo", "name", "cpa", "region", "verified", "onlineStatus","bannerCount", "startDate", "endDate", "operate");
		return beanModel;
	}
	
	/**
	 * 计算banner数目
	 * @return Integer
	 */
	public Integer getBannerCount(){
		return campaignService.getBannerCount(campaign);
	}

	public String getOnlineStatus() {
		return Constants.formatPublishStatus(getMessages(), campaign);
	}

	private void offlineApp() {
		try {
			if (campaign.getVerified() == Constants.APPROVED) {
				updateVerified(Constants.OFFLINE_PENDING_APPROVAL, campaign);
				messagePage.setMessage(getMessages().get("action_success"));
			}
		} catch (BusinessException e) {
			messagePage.setMessage(e.getMessage());
		}
	}

	Object onActivate(String operation, int campaignId) {
		campaign = campaignService.get(campaignId);
		// 调用事件
		if ("submitApp".equals(operation)) {
			submitApp();
		} else if ("cancelApp".equals(operation)) {
			cancelApp();
		} else if ("delete".equals(operation)) {
			deleteCampaign();
		} else if ("offlineApp".equals(operation)) {
			offlineApp();
		}
		messagePage.setNextPage(Constants.AD_REDIRECT_CAMPAIGN);
		return messagePage;
	}

	private void submitApp() {
		try {
			if (campaign.getVerified() == Constants.NOT_SUBMITTED) {
				updateVerified(Constants.PENDING_APPROVAL, campaign);
				messagePage.setMessage(getMessages().get("action_success"));
			}
		} catch (BusinessException e) {
			messagePage.setMessage(e.getMessage());
		}
	}

	/**
	 * 更新删除状态
	 * @param campaign
	 */
	private void updateDeleted(Campaign campaign) {
		try {
			campaign.setDeleted(Constants.DELETED);
			campaignService.save(campaign);
		} catch (RuntimeException e) {
			throw new BusinessException(getMessages().get("action_fail"));
		}
	}

	/**
	 * 更新审核状态
	 * @param verified
	 * @param campaign
	 */
	private void updateVerified(int verified, Campaign campaign) {
		try {
			campaign.setVerified(verified);
			campaignService.save(campaign);
		} catch (RuntimeException e) {
			throw new BusinessException(getMessages().get("action_fail"));
		}
	}
	
	/**
	 * 获取hql
	 * @param isCount 是否 总数
	 * @return String
	 */
	private String getHql(boolean isCount) {
		StringBuffer sb = new StringBuffer();
		if (isCount) {
			sb.append(" select count(id) from Campaign c where 1 = 1 ");
			sb.append(getWhere());
		} else {
			sb.append(" select distinct(c) from Campaign c left join fetch c.banners b where 1 = 1 ");
			sb.append(getWhere());
			sb.append(" order by c.createdAt desc ");
		}
		return sb.toString();
	}

	/**
	 * 获取条件组合
	 * @return String
	 */
	private String getWhere() {
		StringBuffer sb = new StringBuffer();
		sb.append(" and c.user.id =");
		sb.append(getClientSession().getId());
		sb.append(" and c.deleted = 0 ");
		if (cApplyingMode.getLabelValueModel() != null) {
			sb.append(" and c.verified= ");
			sb.append(Integer.parseInt(cApplyingMode.getLabelValueModel().getValue()));
		}

		if (StringUtils.isNotBlank(campName)) {
			sb.append(" and c.name like '%");
			sb.append(campName);
			sb.append("%' ");
		}
		return sb.toString();
	}

    public String getVerifiedStatus() {
        Integer verified = campaign.getVerified();
        String str = Constants.formatVerified(getMessages(), verified);
        return str;
    }
    
    public String getformatRegion() {
        return Constants.formatRegion(campaign.getRegion(),getMessages().get("day"));
    }
    
}