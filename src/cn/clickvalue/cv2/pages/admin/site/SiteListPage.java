package cn.clickvalue.cv2.pages.admin.site;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Request;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.SiteService;
import cn.clickvalue.cv2.services.logic.UserService;
import cn.clickvalue.cv2.services.logic.admin.AuditingService;
//TODO 条件赛选的时候回有两次完全一样查询，需要好好调下，看看怎么回事，现在初步查到的现象是setuprender被调用了2次
public class SiteListPage extends BasePage {

	@Persist
	private Integer urlUserId;

	@Persist
	@Property
	private String formSiteName;

	@Persist
	@Property
	private String formSiteUrl;

	@Persist
	@Property
	private String formUserName;

	@Persist
	@Property
	private Integer formVerified;

	private String operate;

	private Site site;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Persist
	private GridDataSource dataSource;

	private BeanModel<Site> beanModel;

	@Inject
	private SiteService siteService;

	@Inject
	private UserService userService;

	@Inject
	private AuditingService auditingService;

	@InjectPage
	private MessagePage messagePage;

	@Inject
	private Messages messages;

	@Inject
	private Request request;

	void onActivate(int id) {
		this.urlUserId = id;
	}

	Object onActivate(String event, int id) {
		if ("pass".equals(event)) {
			return passSite(id);
		} else if ("refuse".equals(event)) {
			return refuseSite(id);
		}
		return this;
	}
	
	void onPrepareForSubmit(){
		this.urlUserId = null;
	}

	private Object passSite(int id) {
		try {
			auditingService.passSite(id);
			messagePage.setMessage("批准网站成功。");
		} catch (BusinessException e) {
			messagePage.setMessage("批准网站失败: " + e.getMessage());
		}

		messagePage.setNextPage("admin/site/listpage");
		return messagePage;
	}

	private Object refuseSite(int id) {
		try {
			String refuseReson = request.getParameter("refuseReson");
			auditingService.refuseSite(id, refuseReson);
			messagePage.setMessage("拒绝网站成功。");
		} catch (BusinessException e) {
			messagePage.setMessage("拒绝网站失败: " + e.getMessage());
		}
		messagePage.setNextPage("admin/site/listpage");
		return messagePage;
	}

	void setupRender() {
		initForm();
		initQuery();
		initBeanModel();
	}

	private void initForm() {
		if (urlUserId != null && urlUserId != 0) {
			formUserName = userService.get(urlUserId).getName();
		}
	}

	private void initQuery() {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("user", "user", Criteria.LEFT_JOIN);
		Map<String, Object> map = new HashMap<String, Object>();
		if (urlUserId != null && urlUserId != 0) {
			map.put("user.id", urlUserId);
		}
		if (StringUtils.isNotBlank(formSiteName)) {
			c.addCriterion(Restrictions.like("name", formSiteName,
					MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(formSiteUrl)) {
			c.addCriterion(Restrictions.like("url", formSiteUrl,
					MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(formUserName)) {
			c.addCriterion(Restrictions.like("user.name", formUserName,
					MatchMode.ANYWHERE));
		}
		if (formVerified != null && formVerified != 0) {
			map.put("verified", formVerified);
		} else {
			c.addCriterion(Restrictions.ge("verified", 1));
		}
		c.setCondition(map);
		c.addOrder(Order.desc("createdAt"));
		this.dataSource = new HibernateDataSource(siteService, c);
	}

	private void initBeanModel() {
		beanModel = beanModelSource
				.create(Site.class, true, componentResources);
		beanModel.get("logo").label("网站LOGO").sortable(false);
		beanModel.get("name").label("网站名");
		beanModel.get("url").label("网址").sortable(false);
		beanModel.add("user.name").label("所属用户");
		beanModel.get("verified").label("审核状态").sortable(false);
		beanModel.get("deleted").label("删除状态").sortable(false);
		beanModel.get("createdAt").label("创建日期");
		beanModel.add("operate", null).label("操作").sortable(false);
		beanModel.include("logo", "name", "url", "user.name", "verified",
				"deleted", "createdAt", "operate");
	}

	public String getOperateModel() {
		StringBuffer str = new StringBuffer("");
		str.append("a=查看,");
		str.append("a=编辑,");
		str.append("a=删除");
		return str.toString();
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(GridDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public BeanModel<Site> getBeanModel() {
		return beanModel;
	}

	public void setBeanModel(BeanModel<Site> beanModel) {
		this.beanModel = beanModel;
	}

	public SiteService getSiteService() {
		return siteService;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public AuditingService getAuditingService() {
		return auditingService;
	}

	public void setAuditingService(AuditingService auditingService) {
		this.auditingService = auditingService;
	}

	public MessagePage getMessagePage() {
		return messagePage;
	}

	public void setMessagePage(MessagePage messagePage) {
		this.messagePage = messagePage;
	}

	public Messages getMessages() {
		return messages;
	}

	public void setMessages(Messages messages) {
		this.messages = messages;
	}
}