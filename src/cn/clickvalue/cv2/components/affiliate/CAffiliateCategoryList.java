package cn.clickvalue.cv2.components.affiliate;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.PageLoaded;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;

import cn.clickvalue.cv2.model.AffiliateCategory;
import cn.clickvalue.cv2.services.util.selector.GeneralSelectModel;
import cn.clickvalue.cv2.services.util.selector.GeneralValueEncoder;

public class CAffiliateCategoryList {

    private List<AffiliateCategory> affiliateCategorys;
    
    @Persist
    private AffiliateCategory affiliateCategory;
    
    @Inject
    private PropertyAccess propertyAccess;
    
    
    public ValueEncoder<AffiliateCategory> getTestValueEncode() {

//        GeneralValueEncodeCallBack client = new GeneralValueEncodeCallBack() {
//            String field = "id";
//            @Override
//            public String toClient(Object obj) {
//                String value = (String)propertyAccess.get(obj, field);
//                return value;
//            }
//
//            @Override
//            public Object toValue(String str) {
//                for(Object obj : affiliateCategorys) {
//                    if(propertyAccess.get(obj, field).equals(str)) {
//                        return obj;
//                    }
//                }
//                return null;
//            }
//        };
//        return new GeneralValueEncoder<AffiliateCategory>(client);
        
        return new GeneralValueEncoder<AffiliateCategory>(getAffiliateCategorys(),"id",propertyAccess);
    }

    public SelectModel getTestSelectModel() {
//        GeneralGetOptionCallBack option = new GeneralGetOptionCallBack() {
//            public List<OptionModel> getOptions() {
//                List<OptionModel> optionModelList = new ArrayList<OptionModel>();
//                for (Object obj : affiliateCategorys) {
//                    optionModelList.add(new GeneralOptionModel(propertyAccess.get(obj, "name") + "", false, obj, new String[0]));
//                }
//                return null;
//            }
//        };
//        return new GeneralSelectModel<AffiliateCategory>(option);
        return new GeneralSelectModel<AffiliateCategory>(getAffiliateCategorys(),"name",propertyAccess);
    }
    
    @PageLoaded
    public void initail() {
        affiliateCategorys = new ArrayList<AffiliateCategory>();
//        affiliateCategorys.add(new AffiliateCategory(1,"test1",null,null,null,null));
//        affiliateCategorys.add(new AffiliateCategory(2,"test2",null,null,null,null));
//        affiliateCategorys.add(new AffiliateCategory(3,"test3",null,null,null,null));
    }

    public List<AffiliateCategory> getAffiliateCategorys() {
        return affiliateCategorys;
    }

    public void setAffiliateCategorys(List<AffiliateCategory> affiliateCategorys) {
        this.affiliateCategorys = affiliateCategorys;
    }

    public AffiliateCategory getAffiliateCategory() {
        return affiliateCategory;
    }

    public void setAffiliateCategory(AffiliateCategory affiliateCategory) {
        this.affiliateCategory = affiliateCategory;
    }
}
