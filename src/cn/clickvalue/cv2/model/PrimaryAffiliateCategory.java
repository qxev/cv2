package cn.clickvalue.cv2.model;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@DiscriminatorValue("primary")
public class PrimaryAffiliateCategory extends Category {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "primaryAffiliateCategory")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	@Cascade({CascadeType.SAVE_UPDATE})
	private List<AffiliateCategory> affiliateCategories;

	public List<AffiliateCategory> getAffiliateCategories() {
		return affiliateCategories;
	}

	public void setAffiliateCategories(
			List<AffiliateCategory> affiliateCategories) {
		this.affiliateCategories = affiliateCategories;
	}
}