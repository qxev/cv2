package cn.clickvalue.cv2.pages.advertiser;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.CampaignSite;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.BusinessMailSender;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CampaignSiteService;

public class AffiliateListPage extends BasePage {

    @ApplicationState
    private User user;

    @Inject
    private Messages message;

    @InjectPage
    private MessagePage messagePage;

    @Persist
    @Property
    private int campaignId;

    @Persist
    @Property
    private CampaignSite campaignSite;

    @Persist
    @Property
    private String siteName;

    @Persist
    @Property
    private Integer verified;

    @Persist
    @Property
    private Site site;

    @Persist
    @Property
    private String url;

    @Property
    private boolean check;

    private Form search;

    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private ComponentResources componentResources;

    @SuppressWarnings("unused")
    @Property
    private int noOfRowsPerPage = 15;

    @Persist
    private GridDataSource dataSource;

    private BeanModel<CampaignSite> beanModel;

    @Inject
    @Service(value = "campaignSiteService")
    private CampaignSiteService campaignSiteService;

    @Inject
    @Service(value = "campaignService")
    private CampaignService campaignService;
    
    @Inject
    private BusinessMailSender businessMailSender;

    void onPrepare() {
        CritQueryObject c = new CritQueryObject();
        Map<String, Object> map = new HashMap<String, Object>();
        c.addJoin("campaign", "campaign", Criteria.INNER_JOIN);
        c.addJoin("campaign.user", "campaignUser", Criteria.INNER_JOIN);
        c.addJoin("site.user", "user", Criteria.INNER_JOIN);
        c.addJoin("site", "site", Criteria.INNER_JOIN);

        map.put("campaignUser.id", user.getId());
        map.put("campaign.id", campaignId);
        if (verified != null) {
            map.put("verified", verified);
        } else {
            map.put("verified", Constants.PENDING_APPROVAL);
        }
        if (siteName != null) {
            c.addCriterion(Restrictions.like("site.name", siteName,
                    MatchMode.ANYWHERE));
        }
        if (url != null) {
            c.addCriterion(Restrictions.like("site.url", url,
                    MatchMode.ANYWHERE));
        }

        c.setCondition(map);
        this.dataSource = new HibernateDataSource(campaignSiteService, c);
    }

    public GridDataSource getDataSource() throws Exception {
        return dataSource;
    }

    public BeanModel<CampaignSite> getBeanModel() {
        beanModel = beanModelSource.create(CampaignSite.class, true,
                componentResources);
        beanModel.add("site.user.name").label(getMessages().get("publisher")).sortable(true);
        beanModel.add("site.name").label(getMessages().get("website_name")).sortable(true);
        beanModel.add("site.url").label("URL").sortable(true);
        beanModel.get("verified").label(getMessages().get("joins_condition")).sortable(true);
        beanModel.get("updatedAt").label(getMessages().get("the_date_of_application")).sortable(true);
        beanModel.add("operate", null).label(getMessages().get("operate"));
        // beanModel.add("checkbox",null).label(getMessages().get("operate"));
        beanModel.include("site.user.name", "site.name", "site.url",
                "verified", "updatedAt", "operate");
        return beanModel;
    }

    void onActivate(int id) {
        campaignId = id;
    }

    Object onActivate(String arg, int id) {
        campaignSite = campaignSiteService.get(id);
        if ("apply".equals(arg)) {
            apply();
        } else if ("refuse".equals(arg)) {
            refuse();
        }
        messagePage.setNextPage(Constants.AD_REDIRECT_AFFILIATE);
        return messagePage;
    }

    private void apply() {
        try {
            updateVerified(Constants.APPROVED, campaignSite);
            messagePage.setMessage(getMessages().get("action_success"));
        } catch (BusinessException e) {
            messagePage.setMessage(e.getMessage());
        }
    }

    private void refuse() {
        try {
            updateVerified(Constants.REFUSED, campaignSite);
            messagePage.setMessage(getMessages().get("action_success"));
        } catch (BusinessException e) {
            messagePage.setMessage(e.getMessage());
        }
    }

    private void updateVerified(int verified, CampaignSite campaignSite) {
		try {
			if (campaignSite.getVerified() == Constants.PENDING_APPROVAL
					|| campaignSite.getVerified() == Constants.APPROVED
					|| campaignSite.getVerified() == Constants.REFUSED) {
				campaignSite.setVerified(verified);
				campaignSiteService.save(campaignSite);
				// 发送审核邮件
				businessMailSender.auditingCampaignSiteMail(campaignSite);
			}
		} catch (RuntimeException e) {
			throw new BusinessException(getMessages().get("action_fail"));
		}
	}

    public String getVerifiedStatus() {
        Integer verified = campaignSite.getVerified();
        String str = Constants.formatVerified(message, verified);
        return str;
    }

    public String getCampaignName() {
        return campaignService.get(campaignId).getName();
    }

}