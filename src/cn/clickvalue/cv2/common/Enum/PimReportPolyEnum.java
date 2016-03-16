package cn.clickvalue.cv2.common.Enum;

public enum PimReportPolyEnum {

	AFFILIATE("m.affiliateId", "affiliate"), 
	CAMPAIGN("m.campaignId", "campaign"), 
	SITE("m.siteId", "website"), 
	BONUS_DATE("m.bonusDate", "bonusDate");

	private String dbField;
	private String label;

	PimReportPolyEnum(String field, String label) {
		this.dbField = field;
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public String getDbField() {
		return dbField;
	}
}
