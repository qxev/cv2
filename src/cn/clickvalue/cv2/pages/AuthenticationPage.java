// Copyright 2007 The Apache Software Foundation  
//  
// Licensed under the Apache License, Version 2.0 (the "License");  
// you may not use this file except in compliance with the License.  
// You may obtain a copy of the License at  
//  
//     http://www.apache.org/licenses/LICENSE-2.0  
//  
// Unless required by applicable law or agreed to in writing, software  
// distributed under the License is distributed on an "AS IS" BASIS,  
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
// See the License for the specific language governing permissions and  
// limitations under the License.  

package cn.clickvalue.cv2.pages;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.logic.UserService;
import cn.clickvalue.cv2.web.CaptchaServiceSingleton;
import cn.clickvalue.cv2.web.ClientSession;

@SuppressWarnings("unused")
public class AuthenticationPage {

	@Persist
	@Property
	private String userName,userPassword,validateCode;
	
	@Persist
	private List<Integer> finiteIds;

	@Inject
	private RequestGlobals globals;


	private Object formEventReturn;

	@Persist
	private Object nextPage;
	
	@Persist
	private Object lastPage;

	@Component
	private Form userLoginForm;

	@Inject
	private UserService userService;

	@ApplicationState
	private ClientSession clientSession;

	/**
	 * @return
	 * 
	 * 验证码判断
	 */
	private boolean isValidate(){
		CaptchaServiceSingleton singleton = CaptchaServiceSingleton.getInstance();
		HttpSession session = globals.getHTTPServletRequest().getSession();
		return singleton.validateCaptchaResponse(validateCode,session);
	}
	
	void onValidateForm(){
		if(!isValidate()){
			userLoginForm.recordError("验证码错误");
			return;
		}
		if(StringUtils.isBlank(userName)){
			userLoginForm.recordError("用户名不能为空!");
			return;
		}
		if(StringUtils.isBlank(userPassword)){
			userLoginForm.recordError("密码不能为空!");
			return;
		}
	}

	Object onSubmitFromUserLoginForm() {
		if(!userLoginForm.isValid()){
			return this;
		}
		User authenticatedUser = userService.authenticate(finiteIds,userName,userPassword);
		if (authenticatedUser != null) {
			clientSession.addAnotherUserId(authenticatedUser.getId());
			return nextPage;
		} else {
			userLoginForm.recordError("您输入的信息不正确");	
			return this;
		}
	}
	
	Object onCancel(){
		return lastPage;
	}
	
	void cleanupRender() {
		userLoginForm.clearErrors();
	}
	
	public List<Integer> getFiniteIds() {
		return finiteIds;
	}

	public void setFiniteIds(List<Integer> finiteIds) {
		this.finiteIds = finiteIds;
	}
	
	public void addFiniteId(Integer finiteId){
		if(finiteIds == null){
			finiteIds = new ArrayList<Integer>();
		}
		finiteIds.add(finiteId);
	}
	
	public Object getNextPage() {
		return nextPage;
	}

	public void setNextPage(Object nextPage) {
		this.nextPage = nextPage;
	}
	
	public Object getLastPage() {
		return lastPage;
	}

	public void setLastPage(Object lastPage) {
		this.lastPage = lastPage;
	}

}
