package cn.clickvalue.cv2.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class AdvertiserCategory extends PersistentObject {

    private static final long serialVersionUID = 1L;

    @Column(name="parent_id")
    private Integer parentId;
    
    private Integer position;

    private String name;

    private String description;

    @OneToMany(mappedBy = "advertiserCategory")
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    private List<AdvertiserCategorySite> advertiserCategorySites;

    /** default constructor */
    public AdvertiserCategory() {
    }

    // Property accessors
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

    public List<AdvertiserCategorySite> getAdvertiserCategorySites() {
        return advertiserCategorySites;
    }

    public void setAdvertiserCategorySites(
            List<AdvertiserCategorySite> advertiserCategorySites) {
        this.advertiserCategorySites = advertiserCategorySites;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}