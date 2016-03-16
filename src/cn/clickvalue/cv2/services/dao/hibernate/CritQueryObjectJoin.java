package cn.clickvalue.cv2.services.dao.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * crit query object 的关联关系
 */
public class CritQueryObjectJoin implements Serializable {
	/**
	 * CritQueryObjectJoin
	 */
	private static final long serialVersionUID = 1L;

	private String associationPath;

	private String alias;

	private int joinType;

	public CritQueryObjectJoin(String associationPath, String alias,
			int joinType) {
		super();
		this.associationPath = associationPath;
		this.alias = alias;
		this.joinType = joinType;
	}

	public CritQueryObjectJoin() {
		super();
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAssociationPath() {
		return associationPath;
	}

	public void setAssociationPath(String associationPath) {
		this.associationPath = associationPath;
	}

	public int getJoinType() {
		return joinType;
	}

	public void setJoinType(int joinType) {
		this.joinType = joinType;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CritQueryObjectJoin == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		CritQueryObjectJoin rhs = (CritQueryObjectJoin) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj)).append(
				associationPath, rhs.associationPath).append(alias, rhs.alias)
				.append(joinType, rhs.joinType).isEquals();

	}

}
