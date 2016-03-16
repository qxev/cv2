package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.model.AffiliateCategory;
import cn.clickvalue.cv2.model.PrimaryAffiliateCategory;
import cn.clickvalue.cv2.services.logic.PrimaryAffiliateCategoryService;

public class TestPrimaryAffiliateCategoryService extends
		AbstractTransactionalDataSourceSpringContextTests {
	private PrimaryAffiliateCategoryService primaryAffiliateCategoryService;

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDefaultRollback(false);
		return new String[] { "classpath*:spring-main.xml" };
	}

	public void setPrimaryAffiliateCategoryService(
			PrimaryAffiliateCategoryService primaryAffiliateCategoryService) {
		this.primaryAffiliateCategoryService = primaryAffiliateCategoryService;
	}

	public void test(){
		String[][] str = {{"IT数码","网络","软硬件","电子产品"},
							{"服饰","鞋帽","饰品"},
							{"家居","礼品","鲜花"},
							{"健康","医疗","医药"},
							{"教育","学习","培训","招聘"},
							{"旅游","旅行","酒店"},
							{"女性","时尚","美容"},
							{"商业","财经","营销"},
							{"文艺","图书","文学","摄影"},
							{"娱乐","音乐","影视"},
							{"运动","户外","健身","体育"},
							{"综合","资讯","导航","门户"},
							{"交友","情感","人生","结婚"},
							{"游戏","竞技","模型"},
							{"下载","资源类"},
							{"其他类","个人博客","日志"}};
		for(int i=0; i<str.length; i++){
			
			PrimaryAffiliateCategory primaryAffiliateCategory = new PrimaryAffiliateCategory();
			primaryAffiliateCategory.setCreatedAt(new Date());
			primaryAffiliateCategory.setUpdatedAt(new Date());
			primaryAffiliateCategory.setDescription("主分类");
			primaryAffiliateCategory.setName(str[i][0]);
			List<AffiliateCategory> affiliateCategories = new ArrayList<AffiliateCategory>();
			for(int j=1; j<str[i].length; j++){
				AffiliateCategory affiliateCategory = new AffiliateCategory();
				affiliateCategory.setCreatedAt(new Date());
				affiliateCategory.setUpdatedAt(new Date());
				affiliateCategory.setDescription("次级分类");
				affiliateCategory.setName(str[i][j]);
				affiliateCategory.setPosition(j-1);
				affiliateCategory.setPrimaryAffiliateCategory(primaryAffiliateCategory);
				affiliateCategories.add(affiliateCategory);
			}
			primaryAffiliateCategory.setAffiliateCategories(affiliateCategories);
			primaryAffiliateCategoryService.save(primaryAffiliateCategory);
		}
	}
}
