package cn.clickvalue.cv2.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.clickvalue.cv2.model.base.PersistentObject;

/**
 * 第三方ID对应关系
 * @author harry.zhu
 *
 */
@Entity
@Table(name = "partnerid")
public class PartnerId extends PersistentObject {

    private static final long serialVersionUID = 1L;
    
     /**
     * 合作伙伴类型 
     * 1：拍拍
     */
    private int type;

    /**
     * 系统中的ID
     */
    private String ourId;

    /**
     * 合作伙伴的Id
     */
    private String partnerId;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getOurId() {
		return ourId;
	}

	public void setOurId(String ourId) {
		this.ourId = ourId;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

}