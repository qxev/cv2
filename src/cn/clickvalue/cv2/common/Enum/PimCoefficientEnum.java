package cn.clickvalue.cv2.common.Enum;

import java.util.Map;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

public enum PimCoefficientEnum {

	CPC(100, 0.1f, "0.1~0.5"), CPL(101, 0.5f, "0.5~1"), CPS(102, 1f, "1~2"), CPM(105, 0.05f, "0.05~0.1");

	private int ruleType;
	private float defaultValue;
	private String recommend;

	private static final Map<Integer, PimCoefficientEnum> mapForRuleType = CollectionFactory.newMap();

	static {
		for (PimCoefficientEnum coefficient : PimCoefficientEnum.values()) {
			mapForRuleType.put(coefficient.getRuleType(), coefficient);
		}
	}

	public static PimCoefficientEnum getByRuleType(int ruleType) {
		return mapForRuleType.get(ruleType);
	}

	PimCoefficientEnum(int ruleType, float defaultValue, String recommend) {
		this.ruleType = ruleType;
		this.defaultValue = defaultValue;
		this.recommend = recommend;
	}

	public int getRuleType() {
		return ruleType;
	}

	public void setRuleType(int ruleType) {
		this.ruleType = ruleType;
	}

	public float getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(float defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
}
