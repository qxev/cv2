package cn.clickvalue.cv2.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;

public class ProvinceCity {
    
    public ProvinceCity() {
        
    }

    private List provinceList;
    
    private List cityList;
    
    @Persist("flash")
    @Property
    private String province;

    @Persist("flash")
    @Property
    private String city;
    
//    @Inject
//    @Service(value="")

//    public List<SelectModel> getChildSelectModels() {
//
//        List<SelectModel> models = new ArrayList<SelectModel>(); 
////        models.add(arg0)
//    }
}
