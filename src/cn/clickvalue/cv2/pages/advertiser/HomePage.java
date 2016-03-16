package cn.clickvalue.cv2.pages.advertiser;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.AdvertiserAccount;
import cn.clickvalue.cv2.model.AdvertiserSumInfo;
import cn.clickvalue.cv2.model.Bulletin;
import cn.clickvalue.cv2.model.CampaignSite;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AdvertiserAccountService;
import cn.clickvalue.cv2.services.logic.AdvertiserSumInfoService;
import cn.clickvalue.cv2.services.logic.BbsSynService;
import cn.clickvalue.cv2.services.logic.BulletinService;
import cn.clickvalue.cv2.services.logic.BusinessMailSender;
import cn.clickvalue.cv2.services.logic.CampaignSiteService;

@IncludeStylesheet("context:/assets/tapestry/5.0.13/default.css")
public class HomePage extends BasePage {

	@InjectPage
	private MessagePage messagePage;

	@Persist
	@Property
	private CampaignSite campaignSite;

	@Persist
	@Property
	private AdvertiserAccount advertiseraccount;

	@Persist
	@Property
	private Site site;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@SuppressWarnings("unused")
	@Property
	private int noOfRowsPerPage = 5;

	@Persist
	private GridDataSource dataSource;

	private BeanModel<CampaignSite> beanModel;

	@Inject
	@Service(value = "campaignSiteService")
	private CampaignSiteService campaignSiteService;

	@Inject
	@Service(value = "advertiseraccountService")
	private AdvertiserAccountService advertiseraccountService;

	private Bulletin bulletin;

	@Inject
	private BulletinService bulletinService;

	@Inject
	private AdvertiserSumInfoService advertiserSumInfoService;

	private List<Bulletin> bulletins;

	@Component(parameters = {"source=bulletins", "value=bulletin"})
	private Loop bulletinLoop;

	@Environmental
	private RenderSupport renderSupport;

	@Inject
	private BusinessMailSender businessMailSender;

	@Component(parameters = {"source=advertiserSumInfos", "value=advertiserSumInfo"})
	private Loop advertiserSumInfoLoop;

	private List<AdvertiserSumInfo> advertiserSumInfos;

	@Property
	private AdvertiserSumInfo advertiserSumInfo;
	
	@ApplicationState
	private User user;
	
	@Inject
	private BbsSynService bbsSynService;
	
	public GridDataSource getDataSource() throws Exception {
		return dataSource;
	}

	public BeanModel<CampaignSite> getBeanModel() {
		beanModel = beanModelSource.create(CampaignSite.class, true, componentResources);
		beanModel.add("site.user.name").label(getMessages().get("publishers"));
		beanModel.add("site.name").label(getMessages().get("website_name"));
		beanModel.add("site.url").label(getMessages().get("website"));
		beanModel.add("campaign.name").label(getMessages().get("campaign_name"));
		beanModel.get("updatedAt").label(getMessages().get("the_date_of_application"));
		beanModel.add("operate", null).label(getMessages().get("operate"));
		beanModel.include("campaign.name", "site.user.name", "site.name", "site.url", "updatedAt", "operate");
		return beanModel;
	}

	void afterRender() {
		renderSupport.addScript("Table.Rows.stripe('bulletin');");
	}

	@SetupRender
	public void setupRender() {
		this.bulletins = bulletinService.getBulletins(1);
		advertiserSumInfos = advertiserSumInfoService.findAdvertiserSumInfoByUserId(getClientSession().getId());
	}

	public List<CampaignSite> getAffiliatedWebMasters() {
		CritQueryObject c = new CritQueryObject();
		Map<String, Object> map = new HashMap<String, Object>();
		c.addJoin("campaign", "campaign", Criteria.INNER_JOIN);
		c.addJoin("campaign.user", "user", Criteria.INNER_JOIN);
		c.addJoin("site", "site", Criteria.LEFT_JOIN);
		map.put("verified", Integer.valueOf(1));
		map.put("campaign.user.id", this.getClientSession().getId());
		c.setFirstResult(0);
		c.setMaxResults(4);
		c.addOrder(Order.desc("updatedAt"));
		c.setCondition(map);
		List<CampaignSite> campaignSites = campaignSiteService.find(c);
		return campaignSites;
	}

	Object onActivate(String arg, int id) {
		campaignSite = campaignSiteService.get(id);
		if ("apply".equals(arg)) {
			apply();
		} else if ("refuse".equals(arg)) {
			refuse();
		}
		messagePage.setNextPage(Constants.AD_REDIRECT_HOMEPAGE);
		return messagePage;
	}

	public String getBalances() {
		advertiseraccount = advertiseraccountService.findUniqueBy("advertiserId", getClientSession().getId());
		if (advertiseraccount == null) {
			return "0";
		}
		BigDecimal balances = advertiseraccount.getTotalIncome().subtract(advertiseraccount.getTotalexpense());
		return String.valueOf(balances);
	}
	
//	public String getBBSLink() throws UnsupportedEncodingException{
//		BBSMember member = bbsMemberService.findMemberInfoByUserName(this.getClientSession().getUserName());
//		return bbsMemberService.getBBSLink(member);
//	}

	public List<Bulletin> getBulletins() {
		return bulletins;
	}

	public void setBulletins(List<Bulletin> bulletins) {
		this.bulletins = bulletins;
	}

	public Bulletin getBulletin() {
		return bulletin;
	}

	public void setBulletin(Bulletin bulletin) {
		this.bulletin = bulletin;
	}

	public List<AdvertiserSumInfo> getAdvertiserSumInfos() {
		return advertiserSumInfos;
	}

	public void setAdvertiserSumInfos(List<AdvertiserSumInfo> advertiserSumInfos) {
		this.advertiserSumInfos = advertiserSumInfos;
	}

	private void updateVerified(int verified, CampaignSite campaignSite) {
		try {
			if (campaignSite.getVerified() == Constants.NOT_SUBMITTED
					|| campaignSite.getVerified() == Constants.PENDING_APPROVAL) {
				campaignSite.setVerified(verified);
				campaignSiteService.save(campaignSite);
				// 发送审核邮件
				businessMailSender.auditingCampaignSiteMail(campaignSite);
			}
		} catch (RuntimeException e) {
			throw new BusinessException(getMessages().get("approve_refuse_failed"));
		}
	}

	private void apply() {
		try {
			updateVerified(Constants.APPROVED, campaignSite);
			messagePage.setMessage(getMessages().get("approve_success"));
		} catch (BusinessException e) {
			messagePage.setMessage(e.getMessage());
		}
	}
	
	private void refuse() {
		try {
			updateVerified(Constants.REFUSED, campaignSite);
			messagePage.setMessage(getMessages().get("refuse_success"));
		} catch (BusinessException e) {
			messagePage.setMessage(e.getMessage());
		}
	}
}