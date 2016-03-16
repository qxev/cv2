package cn.clickvalue.cv2.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
@Table(name = "hp_block_content")
public class HPBlockContent extends PersistentObject {

	private static final long serialVersionUID = -783250236663353832L;

	private String url;

	private String cdata;

	private String image;

	private Integer entityId;
	
	private Integer sequence;
	
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hpBlockId")
	private HPBlock hpBlock;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCdata() {
		return cdata;
	}

	public void setCdata(String cdata) {
		this.cdata = cdata;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public HPBlock getHpBlock() {
		return hpBlock;
	}

	public void setHpBlock(HPBlock hpBlock) {
		this.hpBlock = hpBlock;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}
