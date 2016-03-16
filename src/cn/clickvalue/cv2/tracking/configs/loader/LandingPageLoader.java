package cn.clickvalue.cv2.tracking.configs.loader;

import java.util.HashMap;
import java.util.Map;

import cn.clickvalue.cv2.tracking.configs.db.Accessor;
import cn.clickvalue.cv2.tracking.configs.entities.LandingPage;

public class LandingPageLoader {

	public static Map<Integer, LandingPage> loadAllLandingPage() throws Exception {
		Map<Integer, LandingPage> map = new HashMap<Integer, LandingPage>();
		String sql = "select * from LandingPage l where l.deleted = 0 or l.deleted is null";
		map = Accessor.executeQuery(Integer.class, LandingPage.class, 1, sql, "cv2");
		return map;
	}
}
