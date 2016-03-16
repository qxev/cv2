package cn.clickvalue.cv2.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
@Table(name = "hp_block")
public class HPBlock extends PersistentObject {

	private static final long serialVersionUID = 6928386098331573624L;

	private String name;

	private String displayName;

	private String folder;

	private String template;

	private Integer permission;
	
	@OneToMany(mappedBy = "hpBlock")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	@OrderBy("sequence,id ASC")
	private List<HPBlockContent> hpBlockContents;
	
	public HPBlock() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public Integer getPermission() {
		return permission;
	}

	public void setPermission(Integer permission) {
		this.permission = permission;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public List<HPBlockContent> getHpBlockContents() {
		return hpBlockContents;
	}

	public void setHpBlockContents(List<HPBlockContent> hpBlockContents) {
		this.hpBlockContents = hpBlockContents;
	}
}
