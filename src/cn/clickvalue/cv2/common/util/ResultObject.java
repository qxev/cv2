package cn.clickvalue.cv2.common.util;

import java.math.BigDecimal;

/**
 * 用来存放 结果集
 */
public class ResultObject implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer value1;
	private String value2;
	private String value3;
	private BigDecimal value4;
	private BigDecimal value5;
	
	public ResultObject(){}

	public ResultObject(Integer value1,String value2,BigDecimal value4,BigDecimal value5) {
		this.value1 = value1;
		this.value2 = value2;
		this.value4 = value4;
		this.value5 = value5;
	}

	public Integer getValue1() {
		return value1;
	}

	public void setValue1(Integer value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

	public BigDecimal getValue4() {
		return value4;
	}

	public void setValue4(BigDecimal value4) {
		this.value4 = value4;
	}

	public BigDecimal getValue5() {
		return value5;
	}

	public void setValue5(BigDecimal value5) {
		this.value5 = value5;
	}
}
