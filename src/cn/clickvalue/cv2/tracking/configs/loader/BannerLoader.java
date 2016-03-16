package cn.clickvalue.cv2.tracking.configs.loader;

import java.util.HashMap;
import java.util.Map;

import cn.clickvalue.cv2.tracking.configs.db.Accessor;
import cn.clickvalue.cv2.tracking.configs.entities.Banner;

public class BannerLoader{

    public static Map<Integer,Banner> loadAllBanners() throws Exception{
        String sql = "select * from Banner b where b.deleted = 0 or b.deleted is null";
        Map<Integer,Banner> map = new HashMap<Integer,Banner>();
        map = Accessor.executeQuery(Integer.class, Banner.class, 1, sql, "cv2");
        return map;
    }
}
