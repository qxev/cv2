package cn.clickvalue.cv2.pages.admin.bulletin;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Bulletin;
import cn.clickvalue.cv2.services.logic.BulletinService;

public class BulletinViewPage extends BasePage {
	
	@Property
	private Bulletin bulletin;
	
	@Inject
	private BulletinService bulletinService;
	
	void onActivate(int id) {
		bulletin = bulletinService.get(id);
    }
	
	public String getType(){
		Integer type = bulletin.getType();
		if(type == null || type == 0){
			return "网站主";
		}else{
			return "广告主";
		}
	}
	
}
