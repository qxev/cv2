package cn.clickvalue.cv2.pages.admin.staticpgs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.common.util.RealPath;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.CampaignZhuanti;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CampaignZhuantiService;

public class ZhuantiManager extends BasePage {

	@Property
	@Persist
	private String formCampaignName;

	@Property
	private List<String> campaignNames = new ArrayList<String>();

	@Property
	@Persist
	private String formSubject;

	@Property
	private List<String> subjects = new ArrayList<String>();

	@Inject
	private CampaignZhuantiService campaignZhuantiService;

	@Inject
	private CampaignService campaignService;

	private CampaignZhuanti campaignZhuanti;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private BeanModelSource beanModelSource;

	@InjectPage
	private MessagePage messagePage;

	void setupRender() {
		String sql = "select campaign.name from Campaign campaign where campaign.verified>0 and campaign.deleted=0 order by campaign.name";
		campaignNames = campaignService.findAllCampaignNameByHql(sql);
		subjects = campaignZhuantiService.findAllSubjects();
	}

	public BeanModel<CampaignZhuanti> getBeanModel() {
		BeanModel<CampaignZhuanti> beanModel = beanModelSource.create(CampaignZhuanti.class, true, componentResources);
		beanModel.add("campaign.name").label("广告活动").sortable(true);
		beanModel.get("subject").label("标题").sortable(false);
		beanModel.get("url").label("专题页面").sortable(false);
		beanModel.add("operate", null).label("操作").sortable(false);
		beanModel.include("campaign.name", "subject", "url", "operate");
		return beanModel;
	}

	public Object onDelete(Integer zhuantiId) {
		CampaignZhuanti zhuanti = campaignZhuantiService.get(zhuantiId);
		File file = new File(RealPath.getRealPath(zhuanti.getUrl()));
		if (file.exists()) {
			file.delete();
		}
		campaignZhuantiService.delete(zhuantiId);
		messagePage.setMessage("删除成功！");
		messagePage.setNextPage("/admin/staticpgs/zhuantimanager");
		return messagePage;
	}

	public GridDataSource getDataSource() {
		CritQueryObject query = new CritQueryObject();
		query.addJoin("campaign", "campaign", Criteria.LEFT_JOIN);
		if (StringUtils.isNotBlank(formCampaignName)) {
			query.addCriterion(Restrictions.like("campaign.name", formCampaignName, MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(formSubject)) {
			query.addCriterion(Restrictions.like("subject", formSubject, MatchMode.ANYWHERE));
		}
		query.addCriterion(Restrictions.ne("deleted", 1));
		query.addOrder(Order.desc("createdAt"));
		GridDataSource dataSource = new HibernateDataSource(campaignZhuantiService, query);
		return dataSource;
	}

	public CampaignZhuanti getCampaignZhuanti() {
		return campaignZhuanti;
	}

	public void setCampaignZhuanti(CampaignZhuanti campaignZhuanti) {
		this.campaignZhuanti = campaignZhuanti;
	}
}
