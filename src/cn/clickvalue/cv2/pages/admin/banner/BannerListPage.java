package cn.clickvalue.cv2.pages.admin.banner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Banner;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.LandingPage;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.pages.admin.landingPage.LandingPageTestPage;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.BannerService;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.admin.AuditingService;

public class BannerListPage extends BasePage {

    /**
     * 用户选择的操作
     */
    private String operate;

    private Banner banner;

    @Persist
    private String formCampaignName;

    @Persist
    private OptionModelImpl formbannerType;

    @Persist
    private LandingPage formLandingPage;

    @Persist
    @InjectSelectionModel(idField = "value", labelField = "label")
    private List<OptionModel> bannerTypeList = new ArrayList<OptionModel>();

    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private ComponentResources componentResources;

    @Persist
    private GridDataSource dataSource;

    private BeanModel<Banner> beanModel;

    @Inject
    private BannerService bannerService;

    @Inject
    private CampaignService campaignService;

    @Inject
    private AuditingService auditingService;

    @InjectPage
    private MessagePage messagePage;

    @InjectPage
    private LandingPageTestPage landingPageTestPage;

    void onActivate(int id) {
        if (id != 0) {
            formCampaignName = campaignService.findUniqueBy("id", id).getName();
        }
    }

    Object onActivate(String event, int id) {
        if ("passOnline".equals(event)) {
            passBannerOnline(id);
            messagePage.setNextPage("admin/banner/listpage");
            return messagePage;
        } else if ("refuseOnline".equals(event)) {
            refuseBannerOnline(id);
            messagePage.setNextPage("admin/banner/listpage");
            return messagePage;
        } else if ("test".equals(event)) {
            testBanner(id);
            return landingPageTestPage;
        }
        return this;
    }

    private void testBanner(int bannerId) {
        Banner b = bannerService.get(bannerId);
        landingPageTestPage.setBanner(b);
    }

    private void passBannerOnline(int id) {
        try {
            auditingService.passBanner(id);
            messagePage.setMessage("批准广告上线成功。");
        } catch (Exception e) {
            messagePage.setMessage("批准广告上线失败，请重试！");
        }
    }

    private void refuseBannerOnline(int id) {
        try {
            auditingService.refuseBanner(id);
            messagePage.setMessage("拒绝广告上线成功。");
        } catch (Exception e) {
            messagePage.setMessage("拒绝广告上线失败，请重试！");
        }
    }

    void onPrepare() {
        initForm();
        initQuery();
    }

    private void initForm() {
        bannerTypeList = Constants.getBannerTypesOptions();

    }

    private void initQuery() {
        CritQueryObject c = new CritQueryObject();
        c.addJoin("campaign", "campaign", Criteria.LEFT_JOIN);
        c.addJoin("advertises", "advertises", Criteria.INNER_JOIN);
        c.addJoin("advertises.landingPage", "advertises.landingPage",
                Criteria.LEFT_JOIN);
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(formCampaignName)) {
            c.addCriterion(Restrictions.like("campaign.name", formCampaignName,
                    MatchMode.ANYWHERE));
        }
        if (formbannerType != null && formbannerType.getValue() != null) {
            map.put("bannerType", formbannerType.getValue().toString());
        }
        if (formLandingPage != null && formLandingPage.getId() != null
                && formLandingPage.getId() != 0) {
            map.put("advertises.landingPage", formLandingPage);
        }
        map.put("deleted", 0);
        c.setCondition(map);
        c.addOrder(Order.desc("createdAt"));
        this.dataSource = new HibernateDataSource(bannerService, c);
    }

    public List<String> getViewParameters() {
        List<String> list = new ArrayList<String>();
        list.add(banner.getCampaign().getId().toString());
        list.add("admin/banner/listPage");
        return list;
    }

    public GridDataSource getDataSource() throws Exception {
        return dataSource;
    }

    public BeanModel<Banner> getBeanModel() {
        beanModel = beanModelSource.create(Banner.class, true,
                componentResources);
        beanModel.get("bannerType").label("广告类型").sortable(false);
        beanModel.add("campaign.name").label("所属广告活动");
        beanModel.get("verified").label("审核状态");
        beanModel.add("size", null).label("广告尺寸(宽*高)").sortable(false);
        beanModel.get("content").label("广告内容").sortable(false);
        beanModel.add("operate", null).label("操作").sortable(false);
        beanModel.include("bannerType", "campaign.name", "verified",
                "size", "content", "operate");
        return beanModel;
    }

    public boolean isNotTextBanner() {
        if (banner.getBannerType() != null
                && Integer.parseInt(banner.getBannerType()) == Constants.BANNER_TYPE_TEXT) {
            return false;
        }
        return true;
    }
    
    public boolean isPendingApproval() {
        Campaign campaign = banner.getCampaign();
        if(campaign!=null && campaign.getActived()==0 && campaign.getVerified()==1 && (banner.getVerified()==0||banner.getVerified()==1)) {
            return true;
        }
        return false;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
    }

    public OptionModelImpl getFormbannerType() {
        return formbannerType;
    }

    public void setFormbannerType(OptionModelImpl formbannerType) {
        this.formbannerType = formbannerType;
    }

    public List<OptionModel> getBannerTypeList() {
        return bannerTypeList;
    }

    public void setBannerTypeList(List<OptionModel> bannerTypeList) {
        this.bannerTypeList = bannerTypeList;
    }

    public BannerService getBannerService() {
        return bannerService;
    }

    public void setBannerService(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    public String getFormCampaignName() {
        return formCampaignName;
    }

    public void setFormCampaignName(String formCampaignName) {
        this.formCampaignName = formCampaignName;
    }

    public CampaignService getCampaignService() {
        return campaignService;
    }

    public void setCampaignService(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    public LandingPage getFormLandingPage() {
        return formLandingPage;
    }

    public void setFormLandingPage(LandingPage formLandingPage) {
        this.formLandingPage = formLandingPage;
    }
}