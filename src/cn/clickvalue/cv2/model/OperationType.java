package cn.clickvalue.cv2.model;

import javax.persistence.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class OperationType extends PersistentObject {

    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    public OperationType() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}