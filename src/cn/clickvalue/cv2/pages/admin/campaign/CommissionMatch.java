package cn.clickvalue.cv2.pages.admin.campaign;

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

import cn.clickvalue.cv2.common.grid.HibernateDataSource1;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.dao.hibernate.HqlQueryObject;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.UserService;

public class CommissionMatch extends BasePage {

	@Component(id = "grid", parameters = { "source=dataSource", "row=user",
			"model=beanModel", "pagerPosition=literal:bottom",
			"rowsPerPage=noOfRowsPerPage" })
	private Grid grid;

	@Inject
	private CampaignService campaignService;

	@Persist
	@Property
	private String userName;

	@Property
	private User user;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Property
	private GridDataSource dataSource;

	private BeanModel<User> beanModel;

	@Inject
	private UserService userService;

	@Property
	private Campaign campaign;
	
	/**
	 * 获取hql
	 * @param isCount 是否 总数
	 * @return String
	 */
	private String getHql(boolean isCount) {
		StringBuffer sb = new StringBuffer();
		//TODO:这里if else 两个逻辑分支里面的代码是一样的，抽空查下
		if (isCount) {
			sb.append(" select distinct(u) from User u left join fetch u.campaigns c where c.deleted = 0 and c.verified = 2 ");
			if(this.userName != null && !"".equals(userName)) {
				sb.append(" and u.name = ? ");
			}
			
		} else {
			sb.append(" select distinct(u) from User u left join fetch u.campaigns c where c.deleted = 0 and c.verified = 2 ");
			if(this.userName != null && !"".equals(userName)) {
				sb.append(" and u.name = ? ");
			}
		}
		return sb.toString();
	}


	@SetupRender
	public void setupRender() {
		HqlQueryObject query = new HqlQueryObject(getHql(true));
		
		if(this.userName != null && !"".equals(userName)) {
			query.setParams(new Object[]{userName});
		}
		
		this.dataSource = new HibernateDataSource1(campaignService,query,Campaign.class,getHql(false));
	}

	public BeanModel<User> getBeanModel() {
		beanModel = beanModelSource.create(User.class, true,
				componentResources);
		beanModel.get("name").label("用户名").sortable(false);
		beanModel.add("campaignSize",null).label("广告数").sortable(false);
		beanModel.add("operate", null).label("操作").sortable(false);
		beanModel.include("name","campaignSize", "operate");
		return beanModel;
	}
	
	public int getCampaignSize(){
		return campaignService.getCampaignCount(user.getCampaigns());
	}
}