package cn.clickvalue.cv2.components.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.PrimaryKeyEncoder;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.model.AffiliateCategory;
import cn.clickvalue.cv2.model.PrimaryAffiliateCategory;
import cn.clickvalue.cv2.services.logic.AffiliateCategoryService;
import cn.clickvalue.cv2.services.logic.PrimaryAffiliateCategoryService;

@IncludeJavaScriptLibrary("classpath:cn/clickvalue/cv2/components/common/affiliateCategoryRegion.js")
public class AffiliateCategoryRegion {

	@Persist
	@Property
	private AffiliateCategory affiliateCategory;

	@Property
	private PrimaryAffiliateCategory primaryAffiliateCategory;

	@SuppressWarnings("unused")
	@Property
	private List<PrimaryAffiliateCategory> primaryAffiliateCategories;

	@Inject
	private AffiliateCategoryService affiliateCategoryService;

	@Inject
	private PrimaryAffiliateCategoryService primaryAffiliateCategoryService;
	
	@Environmental
	private RenderSupport renderSupport;

	private PrimaryKeyEncoder<Integer, AffiliateCategory> primaryKeyEncoder = new PrimaryKeyEncoder<Integer, AffiliateCategory>() {

		public void prepareForKeys(List<Integer> keys) {

		}

		public Integer toKey(AffiliateCategory value) {
			return value.getId();
		}

		public AffiliateCategory toValue(Integer key) {
			return affiliateCategoryService.get(key);
		}

	};
	
	@Parameter(principal = true)
	private List<AffiliateCategory> checkedObjs;
	
	@Parameter(value = "5", defaultPrefix = BindingConstants.LITERAL)
	private Integer maxCategoryNumber;

	public void setupRender() {
		primaryAffiliateCategories = primaryAffiliateCategoryService.findAll();
		if (checkedObjs == null) {
			checkedObjs = new ArrayList<AffiliateCategory>();
		}
	}
	
	public void afterRender(){
		renderSupport.addScript("checkedNumber(".concat(maxCategoryNumber.toString()).concat(");"));
	}

	public boolean isChecked() {
		return checkedObjs.contains(affiliateCategory);
	}

	public PrimaryKeyEncoder<Integer, AffiliateCategory> getPrimaryKeyEncoder() {
		return primaryKeyEncoder;
	}

	public List<AffiliateCategory> getCheckedObjs() {
		return checkedObjs;
	}

	public void setCheckedObjs(List<AffiliateCategory> checkedObjs) {
		this.checkedObjs = checkedObjs;
	}
	
	public Integer getMaxCategoryNumber() {
		return maxCategoryNumber;
	}

	public void setMaxCategoryNumber(Integer maxCategoryNumber) {
		this.maxCategoryNumber = maxCategoryNumber;
	}
	
	public boolean isCheckAllAble(){
		return maxCategoryNumber == 0;
	}

	public void setChecked(boolean checked) {
		if (checked) {
			if (!checkedObjs.contains(affiliateCategory)) {
				checkedObjs.add(affiliateCategory);
			}
		} else {
			if (checkedObjs.contains(affiliateCategory)) {
				checkedObjs.remove(affiliateCategory);
			}
		}
	}
}
