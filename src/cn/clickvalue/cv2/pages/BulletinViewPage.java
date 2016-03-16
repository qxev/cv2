package cn.clickvalue.cv2.pages;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.model.Bulletin;
import cn.clickvalue.cv2.services.logic.BulletinService;

public class BulletinViewPage {

	private Bulletin bulletin;

	@Inject
	private BulletinService bulletinService;

	void onActivate(String id) {
		if (NumberUtils.isDigits(id)) {
			bulletin = bulletinService.get(NumberUtils.toInt(id));
		}
	}

	void onActivate() {
		if (bulletin == null) {
			bulletin = bulletinService.createBulletin();
		}
	}

	public Bulletin getBulletin() {
		return bulletin;
	}

	public void setBulletin(Bulletin bulletin) {
		this.bulletin = bulletin;
	}
}
