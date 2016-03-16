package cn.clickvalue.cv2.excel.config.model;

import java.util.List;

public class ExcelConfigModel {

    private String mapName;

    private List<PropertyModel> propertyModelList;
    

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public List<PropertyModel> getPropertyModelList() {
        return propertyModelList;
    }

    public void setPropertyModelList(List<PropertyModel> propertyModelList) {
        this.propertyModelList = propertyModelList;
    }


}
