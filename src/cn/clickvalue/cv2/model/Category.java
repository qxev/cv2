package cn.clickvalue.cv2.model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.annotations.DiscriminatorFormula;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
@Table(name="affiliatecategory")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorFormula(
		"case when parent_id = 0 then 'primary' else 'secondary' end"
)
public abstract class Category extends PersistentObject {

	private static final long serialVersionUID = 1L;

	private String name;

	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
