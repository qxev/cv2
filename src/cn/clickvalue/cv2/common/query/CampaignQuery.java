package cn.clickvalue.cv2.common.query;

public class CampaignQuery implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	private Integer verified;

	private String cpa;

	public CampaignQuery() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getVerified() {
		return verified;
	}

	public void setVerified(Integer verified) {
		this.verified = verified;
	}

	public String getCpa() {
		return cpa;
	}

	public void setCpa(String cpa) {
		this.cpa = cpa;
	}
	
}
