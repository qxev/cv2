package cn.clickvalue.cv2.model;

import javax.persistence.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class City extends PersistentObject {

    private static final long serialVersionUID = 1L;

    private Integer cityId;

    private String city;

    private String cityEnglish;

    private Integer father;

    public City() {
    }

    public Integer getCityId() {
        return this.cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getFather() {
        return this.father;
    }

    public void setFather(Integer father) {
        this.father = father;
    }

	public String getCityEnglish() {
		return cityEnglish;
	}

	public void setCityEnglish(String cityEnglish) {
		this.cityEnglish = cityEnglish;
	}

}