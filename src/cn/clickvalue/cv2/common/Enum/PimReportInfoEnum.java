package cn.clickvalue.cv2.common.Enum;

public enum PimReportInfoEnum {

	AFFILIATE_NAME("affiliateName", "affiliate", "u.name", " LEFT JOIN user u ON u.id=m.affiliateId", true),
	CAMPAIGN_NAME("campaignName", "campaign", "c.name", " LEFT JOIN campaign c ON c.id=m.campaignId", true), 
	SITE_NAME("siteName", "website", "s.name", " LEFT JOIN site s ON s.id=m.siteId", true), 
	BONUS_DATE("bonusDate", "time", "m.bonusdate", "", true), 
	POINTS("points", "points", "SUM(m.points)", "", true);

	private String field;
	private String label;
	private String dbField;
	private String join;
	private boolean sortable;

	PimReportInfoEnum(String field, String label, String dbField, String join, boolean sortable) {
		this.field = field;
		this.label = label;
		this.dbField = dbField;
		this.sortable = sortable;
		this.join = join;
	}

	public String getField() {
		return field;
	}

	public String getLabel() {
		return label;
	}

	public String getDbField() {
		return dbField;
	}

	public String getJoin() {
		return join;
	}

	public boolean isSortable() {
		return sortable;
	}
}
