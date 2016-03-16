package cn.clickvalue.cv2.pages.affiliate;

import java.util.List;

import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.util.ResultObject;
import cn.clickvalue.cv2.common.util.SelectModelUtil;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.Bulletin;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.logic.BbsSynService;
import cn.clickvalue.cv2.services.logic.BulletinService;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.SummaryCommissionService;

public class HomePage extends BasePage {

	@Component(parameters = { "source=bulletins", "value=bulletin" })
	private Loop bulletinLoop;

	@Component(parameters = { "source=campaigns", "value=campaign" })
	private Loop campaignLoop;

	@Component(parameters = { "source=summaryCommissions", "value=resultObject" })
	private Loop summaryCommissionLoop;

	@Inject
	private CampaignService campaignService;

	@Inject
	private SelectModelUtil selectModelUtil;

	@Inject
	private SummaryCommissionService summaryCommissionService;

	@Inject
	private BulletinService bulletinService;

	@Property
	private List<Bulletin> bulletins;

	@Property
	private List<Campaign> campaigns;

	@Property
	private Campaign campaign;

	@Property
	private Bulletin bulletin;

	@Property
	private String operate;

	@Property
	private List<ResultObject> summaryCommissions;

	@Property
	private ResultObject resultObject;

	@Environmental
	private RenderSupport renderSupport;

	@ApplicationState
	@Property
	private User user;

	@SetupRender
	public void setupRender() {
		summaryCommissions = summaryCommissionService.findSummaryCommissionByAffiliateId(getClientSession().getId());
		this.bulletins = bulletinService.getBulletins(0);
		this.campaigns = campaignService.getCampaigns(2);
	}

	public SelectModel getOperateModel() {
		return selectModelUtil.getOperateModel(campaign, getMessages());
	}
}