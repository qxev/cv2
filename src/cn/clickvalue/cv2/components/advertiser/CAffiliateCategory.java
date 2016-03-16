package cn.clickvalue.cv2.components.advertiser;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.model.AffiliateCategory;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.logic.AffiliateCategoryService;

public class CAffiliateCategory {
    
    public CAffiliateCategory() {
        affCategoryList = affiliateCategoryService.queryCategory();
    }
    
    private AffiliateCategory affiliateCategory;
    
    @Inject
    @Service(value="AffiliateCategoryService")
    private AffiliateCategoryService affiliateCategoryService;
    
    @InjectSelectionModel(labelField = "name", idField = "id")
    private List<AffiliateCategory> affCategoryList = new ArrayList<AffiliateCategory>();


    public AffiliateCategory getAffiliateCategory() {
        return affiliateCategory;
    }

    public void setAffiliateCategory(AffiliateCategory affiliateCategory) {
        this.affiliateCategory = affiliateCategory;
    }


    public List<AffiliateCategory> getAffCategoryList() {
        return affCategoryList;
    }


    public void setAffCategoryList(List<AffiliateCategory> affCategoryList) {
        this.affCategoryList = affCategoryList;
    }

}
