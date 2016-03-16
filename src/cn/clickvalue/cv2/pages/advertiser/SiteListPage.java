package cn.clickvalue.cv2.pages.advertiser;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.SiteService;

public class SiteListPage extends BasePage {

	@InjectPage
	private SiteEditPage siteEditPage;

	@InjectPage
	private MessagePage messagePage;

	@ApplicationState
	@Property
	private User user;

	@Property
	@Persist
	private Site site;

	@Inject
	@Service("siteService")
	private SiteService siteService;

	@Property
	@Persist
	private String siteState;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@SuppressWarnings("unused")
	@Property
	private int noOfRowsPerPage = 15;

	@Persist
	@Property
	private GridDataSource dataSource;

	private BeanModel<Site> beanModel;

	@Inject
	private Messages messages;

	/**
	 * 預處理方法 太恶心了 如果对象一多 手都断了
	 */
	void onPrepare() {
		CritQueryObject query = new CritQueryObject();
		Map<String, Object> map = CollectionFactory.newMap();
		filter(map, query);
		query.setCondition(map);
		dataSource = new HibernateDataSource(siteService, query);
	}

	/**
	 * Add a custom column to hold the row no to the table.
	 */
	public BeanModel<Site> getBeanModel() {
		this.beanModel = beanModelSource.create(Site.class, true,
				componentResources);
		beanModel.get("name").label(getMessages().get("website"));
		beanModel.get("url").label("URL");
		beanModel.get("description").label(getMessages().get("description"));
		beanModel.get("verified").label(getMessages().get("verify_the_status"));
		beanModel.add("operate", null).label(getMessages().get("operate")).sortable(false);
		beanModel.include("name", "url", "description", "verified", "operate");
		return beanModel;
	}

	Object onActivate(String arg, int siteId) {
		site = siteService.get(siteId);
		if ("submitApp".equals(arg)) {
			submitApp();
		} else if ("delete".equals(arg)) {
			deleteSite();
		}
		messagePage.setNextPage(Constants.AD_REDIRECT_SITE);
		return messagePage;
	}

	private void submitApp() {
		try {
			if (site.getVerified() == Constants.NOT_SUBMITTED) {
				updateVerified(1, site);
				messagePage.setMessage(getMessages().get("action_success"));
			}
		} catch (BusinessException e) {
			messagePage.setMessage(e.getMessage());
		}
	}

	private void deleteSite() {
		try {
			updateDeleted(site);
			messagePage.setMessage(getMessages().get("action_success"));
		} catch (BusinessException e) {
			messagePage.setMessage(e.getMessage());
		}
	}

	private void updateDeleted(Site site) {
		try {
			site.setDeleted(Constants.DELETED);
			siteService.save(site);
		} catch (RuntimeException e) {
			throw new BusinessException(getMessages().get("action_fail"));
		}
	}

	private void updateVerified(int verified, Site site) {
		try {
			site.setVerified(verified);
			siteService.save(site);
		} catch (RuntimeException e) {
			throw new BusinessException(getMessages().get("action_fail"));
		}

	}

	/**
	 * @return 字符串的optionModel
	 *         直接在页面上写的时候的格式为：literal:value=label,value=label,......
	 */
	// public String getOperateModel() {
	// StringBuffer str = new StringBuffer("");
	// str.append("/advertiser/SiteViewPage/");
	// str.append(site.getId());
	// str.append("=查看网站信息,");
	//
	// str.append("/advertiser/EditSitePage/");
	// str.append(site.getId());
	// str.append("=编辑网站信息,");
	//        
	// if(site.getVerified() != 1) {
	// str.append("/advertiser/SiteViewPage/");
	// str.append(site.getId());
	// str.append("=提交审核,");
	// }
	//        
	// str.append("/advertiser/SiteViewPage/");
	// str.append(site.getId());
	// str.append("=删除网站信息,");
	// return str.toString();
	// }
	/**
	 * 条件过滤器
	 * 
	 * @param query
	 */
	private void filter(Map<String, Object> map, CritQueryObject query) {
		map.put("user.id", user.getId());
		query.addCriterion(Restrictions.ne("deleted", Integer.valueOf(1)));
		query.addOrder(Order.desc("createdAt"));
		if (StringUtils.isNotEmpty(siteState)) {
			map.put("verified", new Integer(siteState));
		}
	}

	public boolean isNotVerified() {
		return site.getVerified() != 2;
	}

	public String getSiteState(Integer index) {
		return Constants.formatVerified(messages, index);
	}

	Object onSuccess() {
		return null;
	}
}