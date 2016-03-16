package cn.clickvalue.cv2.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class UserGroup extends PersistentObject {

	private static final long serialVersionUID = 1L;

	private String description;

	private String name;

	@OneToMany(mappedBy = "userGroup")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private List<User> users;

	@ManyToMany
	@JoinTable(name = "groupright", joinColumns = { @JoinColumn(name = "groupId") }, inverseJoinColumns = { @JoinColumn(name = "operationId") })
	private List<Operation> operation = new ArrayList<Operation>();

	public UserGroup() {
	}

	public String getDescription() {
		return this.description;
	}

	public String getName() {
		return this.name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Operation> getOperation() {
		return operation;
	}

	public void setOperation(List<Operation> operation) {
		this.operation = operation;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}