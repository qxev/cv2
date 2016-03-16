package cn.clickvalue.cv2.common.Enum;

import java.util.Map;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

public enum ReportInfoEnum {

	CAMPAIGN_NAME("campaignName", "campaign", "campaignname", true), 
	SITE_NAME("siteName", "website", "sitename", true), 
	SUB_SITE_ID("subsiteId", "sub_site", "subsiteid", true), 
	TRACK_TIME("tracktime", "time", "tracktime", true), 
	TRACK_IP("trackip", "ip", "campaignname", true), 
	SITE_COMMISSION_OLD("siteCommisionOld", "confirming_commission", "sitecommissionold", true), 
	SITE_COMMISSION_NEW("siteCommisionNew", "confirmed_commission", "sitecommissionnew", true), 
	ORDER_AMOUNT("orderamount", "order_amount", "orderamount", true), 
	MATCHED("matched", "Confirms_condition", "matched", true), 
	RULE_TYPE("ruleType", "rule_type", "ruletype", true), 
	ORDER_ID("orderId", "order_id", "orderid", true);

	private String field;
	private String label;
	private String dbField;
	private boolean sortable;

	private static final Map<String, ReportInfoEnum> fieldMap = CollectionFactory.newMap();
	static {
		for (ReportInfoEnum r : ReportInfoEnum.values()) {
			fieldMap.put(r.getField(), r);
		}
	}

	ReportInfoEnum(String field, String label, String dbField, boolean sortable) {
		this.field = field;
		this.label = label;
		this.dbField = dbField;
		this.sortable = sortable;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDbField() {
		return dbField;
	}

	public void setDbField(String dbField) {
		this.dbField = dbField;
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

}
