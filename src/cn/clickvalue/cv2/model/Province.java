package cn.clickvalue.cv2.model;

import javax.persistence.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class Province extends PersistentObject {

    private static final long serialVersionUID = 1L;

    private Integer provinceId;

    private String province;
    
    private String provinceEnglish;

    public Province() {
    }

    public Integer getProvinceId() {
        return this.provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

	public String getProvinceEnglish() {
		return provinceEnglish;
	}

	public void setProvinceEnglish(String provinceEnglish) {
		this.provinceEnglish = provinceEnglish;
	}
}