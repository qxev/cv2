package cn.clickvalue.cv2.pages.affiliate;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.affiliate.BasePage;
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
}