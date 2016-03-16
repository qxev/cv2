package cn.clickvalue.cv2.pages;

import org.apache.tapestry5.annotations.Persist;

public class MessagePage {

	@Persist(value = "flash")
	private String message = "操作失败！";

	@Persist(value = "flash")
	private String nextPage = "Start";

	@Persist(value = "flash")
	private String second = "3";

	@Persist(value = "flash")
	private String hidden = "";

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNextPage() {
		return nextPage;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
	}

	public String getHidden() {
		return hidden;
	}

	public void setHidden(String hidden) {
		this.hidden = hidden;
	}

	public String getTarget() {
		if (nextPage.startsWith("/") || nextPage.startsWith("http://") || nextPage.startsWith("https://")) {
			return nextPage;
		}
		return "/".concat(nextPage);
	}
}
