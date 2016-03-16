package cn.clickvalue.cv2.pages.admin.bulletin;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.criterion.Order;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Bulletin;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.BulletinService;

public class BulletinListPage extends BasePage {
	
	@Property
	private Bulletin bulletin;
	
	@Inject
    private BeanModelSource beanModelSource;
	
    private GridDataSource dataSource;
    
    @Property
    private BeanModel<Bulletin> beanModel;
    
    @Inject
    private ComponentResources componentResources;
    
    @Inject
    private BulletinService bulletinService;
    
    void setupRender(){
    	initForm();
    	initQuery();
    	initBeanModel();
    }
    
    private void initQuery(){
    	CritQueryObject c = new CritQueryObject();
    	
    	
    	c.addOrder(Order.desc("createdAt"));
    	dataSource = new HibernateDataSource(bulletinService,c);
    }
    
    private void initBeanModel(){
    	beanModel = beanModelSource.create(Bulletin.class, true,componentResources);
    	beanModel.get("type").label("接收群体").sortable(false);
    	beanModel.get("subject").label("标题").sortable(false);
    	beanModel.get("createdAt").label("创建时间").sortable(false);
    	beanModel.add("operate",null).label("操作").sortable(false);
    	beanModel.include("type", "subject", "createdAt", "operate");
    }
    
    private void initForm(){
    	
    }
    
    public String getType(){
    	Integer type = bulletin.getType();
    	if(type==null || type==0){
    		return "网站主";
    	}else{
    		return "广告主";
    	}
    	
    }

	public BeanModel<Bulletin> getBeanModel() {
		return beanModel;
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}
    
    
    

}
