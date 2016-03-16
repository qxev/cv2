package cn.clickvalue.cv2.common.grid;

public class Sort implements java.io.Serializable {

	// 排序的字段名
	private String sortName = "summaryDate";

	// 排序方式 asc || desc
	private String sortType = "asc";

	public Sort() {
	}

	public Sort(String sortName, String sortType) {
		this.sortName = sortName;
		this.sortType = sortType;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

}
