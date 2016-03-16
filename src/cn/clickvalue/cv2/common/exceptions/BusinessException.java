package cn.clickvalue.cv2.common.exceptions;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private List<Exception> exceptions = new ArrayList<Exception>();

	public void add(Exception exception) {
		exceptions.add(exception);
	}

	public void add(String message) {
		exceptions.add(new Exception(message));
	}

	public List<Exception> getExceptions() {
		return exceptions;
	}

	public void setExceptions(List<Exception> exceptions) {
		this.exceptions = exceptions;
	}

	public BusinessException() {
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}
}
