package cn.clickvalue.cv2.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Form;

public class TestForm {
	
	@Component
	private Form form;
	
	@Persist
	@Validate("required")
	private String text;
	
	void onValidateForm(){
		System.out.println("onValidate");
	}
	
	void onSuccess(){
		System.out.println("onSuccess");
	}
	
	void onFailure(){
		System.out.println("onFailure");
	}
	
	void onSubmit(){
		System.out.println("onSubmit");
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}


}
