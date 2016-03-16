package cn.clickvalue.cv2.pages.admin.system;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.StreamResponse.XLSAttachment;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.common.util.ExcelUtils;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.AdvertiseAffiliate;
import cn.clickvalue.cv2.model.SemClient;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AdvertiseAffiliateService;
import cn.clickvalue.cv2.services.logic.SemClientService;

public class TrackCodeInfoPage extends BasePage {

	@Property
	@Persist
	private String formTrackCode;

	@Persist
	private List<Integer> advertiseaffiliateIds = new ArrayList<Integer>();

	@Property
	private AdvertiseAffiliate advertiseAffiliate;

	private GridDataSource dataSource;

	private BeanModel<AdvertiseAffiliate> beanModel;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private AdvertiseAffiliateService advertiseAffiliateService;

	@Inject
	private SemClientService semClientService;

	@Component(parameters = { "source=dataSource", "row=advertiseAffiliate", "model=beanModel", "pagerPosition=literal:bottom",
			"rowsPerPage=noOfRowsPerPage" })
	private Grid myGrid;

	@SetupRender
	void setupRender() {
		initQuery();
		initBeanModel();
	}

	private void initQuery() {
		CritQueryObject query = new CritQueryObject();
		query.addJoin("site", "site", Criteria.LEFT_JOIN);
		query.addJoin("site.user", "site.user", Criteria.LEFT_JOIN);

		if (advertiseaffiliateIds != null && advertiseaffiliateIds.size() > 0) {
			query.addCriterion(Restrictions.in("id", advertiseaffiliateIds));
		}

	}

	private CritQueryObject getQuery() {
		CritQueryObject query = new CritQueryObject();
		query.addJoin("site", "site", Criteria.LEFT_JOIN);
		query.addJoin("site.user", "site.user", Criteria.LEFT_JOIN);

		if (advertiseaffiliateIds != null && advertiseaffiliateIds.size() > 0) {
			query.addCriterion(Restrictions.in("id", advertiseaffiliateIds));
		}
		return query;
	}

	private void initBeanModel() {

	}

	void onSubmitFromForm() {
		advertiseaffiliateIds.clear();
		if (StringUtils.isNotBlank(formTrackCode)) {
			for (String id : formTrackCode.split("[\\s,]")) {
				if (NumberUtils.isDigits(id)) {
					advertiseaffiliateIds.add(NumberUtils.toInt(id));
				}
			}
		}
		myGrid.reset();
	}

	public String getTrackCode() {
		return getTrackCode(advertiseAffiliate);
	}

	private String getTrackCode(AdvertiseAffiliate advertiseAffiliate) {

		Integer advertiserId = advertiseAffiliate.getAdvertise().getLandingPage().getCampaign().getUser().getId();
		SemClient semClient = semClientService.findSemClientByUserId(advertiserId);
		if (semClient == null || semClient.getClientId() == null || semClient.getClientId() == 0) {
			return "";
		}
		Integer advertiseAffiliateid = advertiseAffiliate.getId();
		Integer siteId = advertiseAffiliate.getSite().getId();
		Integer advertiseId = advertiseAffiliate.getAdvertise().getId();
		Integer campaignId = advertiseAffiliate.getAdvertise().getCampaignId();
		Integer affiliateId = advertiseAffiliate.getSite().getUser().getId();
		Integer semId = semClient.getClientId();
		StringBuffer sb = new StringBuffer();
		sb.append(String.valueOf(semId));
		sb.append("-");
		sb.append(String.valueOf(campaignId));
		sb.append("-");
		sb.append(String.valueOf(advertiseId));
		sb.append("-");
		sb.append(String.valueOf(affiliateId));
		sb.append("-");
		sb.append(String.valueOf(siteId));
		sb.append("-");
		sb.append(String.valueOf(advertiseAffiliateid));
		return sb.toString();
	}

	public boolean isQuery() {
		return advertiseaffiliateIds.size() > 0;
	}

	Object onExportResult() {
		if (!isQuery()) {
			return this;
		}
		List<AdvertiseAffiliate> result = advertiseAffiliateService.find(getQuery());
		if (result == null || result.size() == 0) {
			addInfo("没有需要导出的数据", false);
			return this;
		}

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (AdvertiseAffiliate advertiseAffiliate : result) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", advertiseAffiliate.getId());
			map.put("trackCode", getTrackCode(advertiseAffiliate));
			map.put("campaign", advertiseAffiliate.getAdvertise().getLandingPage().getCampaign().getName());
			map.put("site", advertiseAffiliate.getSite().getName());
			map.put("siteUrl", advertiseAffiliate.getSite().getUrl());
			map.put("affiliate", advertiseAffiliate.getSite().getUser().getName());
			System.out.println(advertiseAffiliate.getAdvertise().getLandingPage().getCampaign().getName());
			list.add(map);
		}

		String template = "trackCodeInfo";
		String outName = template.concat(String.valueOf(System.currentTimeMillis()));
		try {
			String target = ExcelUtils.mergerXLS(list, template, outName);
			FileInputStream fileInputStream = new FileInputStream(target);
			return new XLSAttachment(fileInputStream, template);
		} catch (Exception e) {
			addError("导出错误，请联系技术", false);
			return this;
		}
	}

	public GridDataSource getDataSource() {
		if (dataSource == null) {
			this.dataSource = new HibernateDataSource(advertiseAffiliateService, getQuery());
		}
		return dataSource;
	}

	public BeanModel<AdvertiseAffiliate> getBeanModel() {
		if (beanModel == null) {
			beanModel = beanModelSource.create(AdvertiseAffiliate.class, true, componentResources);
			beanModel.get("id").label("标识").sortable(false);
			beanModel.add("advertise.landingPage.campaign.name").label("广告活动").sortable(false);
			beanModel.add("site.name").label("网站主").sortable(false);
			beanModel.add("site.user.name").label("网站").sortable(false);
			beanModel.add("site.url").label("网址").sortable(false);
			beanModel.get("trackCode").label("trackCode").sortable(false);
			beanModel.include("id", "trackCode", "advertise.landingPage.campaign.name", "site.user.name", "site.name", "site.url");
		}
		return beanModel;
	}

}
