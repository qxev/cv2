package cn.clickvalue.cv2.model;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@DiscriminatorValue("secondary")
public class AffiliateCategory extends Category {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private PrimaryAffiliateCategory primaryAffiliateCategory;
    
    private Integer position;
    
    private String nameEnglish;

    @OneToMany(mappedBy = "affiliateCategory")
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    private List<AffiliateCategorySite> affiliateCategorySites;

    public AffiliateCategory() {
    }
    
    public List<AffiliateCategorySite> getAffiliateCategorySites() {
        return affiliateCategorySites;
    }

    public void setAffiliateCategorySites(
            List<AffiliateCategorySite> affiliateCategorySites) {
        this.affiliateCategorySites = affiliateCategorySites;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

	public PrimaryAffiliateCategory getPrimaryAffiliateCategory() {
		return primaryAffiliateCategory;
	}

	public void setPrimaryAffiliateCategory(
			PrimaryAffiliateCategory primaryAffiliateCategory) {
		this.primaryAffiliateCategory = primaryAffiliateCategory;
	}

	public String getNameEnglish() {
		return nameEnglish;
	}

	public void setNameEnglish(String nameEnglish) {
		this.nameEnglish = nameEnglish;
	}
    
    
}