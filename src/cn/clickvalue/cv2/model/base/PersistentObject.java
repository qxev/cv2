package cn.clickvalue.cv2.model.base;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.clickvalue.cv2.common.util.DateUtil;

@MappedSuperclass
public class PersistentObject implements Serializable {

	private static final long serialVersionUID = 1L;

	protected static final Log logger = LogFactory.getLog(PersistentObject.class);

	private Date createdAt;

	private Date updatedAt;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	public Date getCreatedAt() {
		return createdAt;
	}

	public String getCreatedAtDisplay() {
		if (createdAt != null)
			return DateUtil.dateToString(createdAt);
		else
			return "";
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int compareTo(Object obj) {
		PersistentObject o = (PersistentObject) obj;
		return new CompareToBuilder().append(this.id, o.id).toComparison();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj instanceof PersistentObject == false) {
			return false;
		}
		PersistentObject o = (PersistentObject) obj;
		return new EqualsBuilder().append(this.getId(), o.getId()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(id).toHashCode();
	}

	public boolean isNull() {
		return (id == null);
	}
}
