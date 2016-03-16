package cn.clickvalue.cv2.services.logic;

import java.util.List;

import cn.clickvalue.cv2.model.AffiliateCategory;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class AffiliateCategoryService extends BaseService<AffiliateCategory> {
    
    public List<AffiliateCategory> queryCategory(){
        return find("from AffiliateCategory ac");
    }
}
