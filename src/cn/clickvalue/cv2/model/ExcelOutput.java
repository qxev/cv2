package cn.clickvalue.cv2.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Entity;


@javax.persistence.Entity
@Entity(dynamicUpdate = true, dynamicInsert = true)
@Table(name = "excelOutput")
public class ExcelOutput implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
    
    private String jobType;
    
    private int campaignId;

    private String excelFields;
    
    private String sqlFields;

    private String sqlContent;
    
    private String ruleType;

	public ExcelOutput() {
	}

	public int getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}

	public String getExcelFields() {
		return excelFields;
	}

	public void setExcelFields(String excelFields) {
		this.excelFields = excelFields;
	}

	public String getSqlContent() {
		return sqlContent;
	}

	public void setSqlContent(String sqlContent) {
		this.sqlContent = sqlContent;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getSqlFields() {
		return sqlFields;
	}

	public void setSqlFields(String sqlFields) {
		this.sqlFields = sqlFields;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	

}
