package cn.clickvalue.cv2.tracking.configs.loader;

import java.util.ArrayList;
import java.util.List;

import cn.clickvalue.cv2.tracking.configs.db.Accessor;
import cn.clickvalue.cv2.tracking.configs.entities.Advertise;

public class AdvertiseLoader {
	public static List<Advertise> loadAllAdvertise() throws Exception {
		String sql = "select * from Advertise a where a.deleted = 0 or a.deleted is null";
		List<Advertise> advertises = new ArrayList<Advertise>();
		advertises = Accessor.executeQuery(Advertise.class, sql, "cv2");
		return advertises;
	}
}
