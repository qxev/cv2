package cn.clickvalue.cv2.pages.admin.cheating;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Cheating;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.CheatingService;
import cn.clickvalue.cv2.services.logic.UserService;

public class CheatingEditPage extends BasePage {
	
	@Property
	@Persist
	private Cheating cheating;
	
	@Property
	private User advertiser;
	
	private Integer cheatingId;
	
	@Inject
	private CheatingService cheatingService;
	
	@Inject
	private UserService userService;
	
	@Component
	private Form editForm;
	
	@InjectPage
	private MessagePage messagePage;
	
	private Object nextPage;
	
	void onActivate(Integer cheatingId){
		if(cheatingId != null && cheatingId != 0){
			this.cheatingId = cheatingId;
		}
	}
	
	Integer onPassivate(){
		return cheatingId;
	}
	
	void setupRender(){
		if(cheatingId == null || cheatingId == 0) throw new BusinessException("cheatingId can't be null");
		cheating = cheatingService.get(cheatingId);
		advertiser = userService.getAdvertiserByCampaignId(cheating.getCampaignId());
	}
	
	void onValidateForm(){
		if(cheating.getCheatBeginAt() != null && cheating.getCheatEndAt() != null){
			if(cheating.getCheatEndAt().before(cheating.getCheatBeginAt())){
				editForm.recordError("作弊结束时间不能比开始时间早");
			}
		}else{
			editForm.recordError("作弊时间不能有空");
		}
	}
	
	void onSuccess(){
		cheatingService.save(cheating);
		messagePage.setMessage("修改成功");
		messagePage.setNextPage("admin/cheating/listpage");
		nextPage = messagePage;
	}
	
	void onFailure(){
		nextPage = this;
	}
	
	Object onSubmit(){
		return nextPage;
	}
	
	void cleanupRender() {
		editForm.clearErrors();
	}
}
