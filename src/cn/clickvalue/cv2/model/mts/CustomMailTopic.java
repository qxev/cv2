package cn.clickvalue.cv2.model.mts;

public class CustomMailTopic {
	
	private Integer tid;
	
	private Integer fid;
	
	private String name;
	
	private String url;
	
	private String boardName;
	
	private String boardUrl;
	
	private String simpleTitle;

	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
		this.setUrl("http://bbs.clickvalue.cn/viewthread.php?tid=".concat(tid.toString()).concat("&extra=page%3D2"));
	}

	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
		this.setBoardUrl("http://bbs.clickvalue.cn/forumdisplay.php?fid=".concat(fid.toString()).concat("&page=1"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name.length() > 30){
			this.simpleTitle = name.substring(0, 30-4).concat("...");
		}else{
			this.simpleTitle = name;
		}
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public String getBoardName() {
		return boardName;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}

	public String getBoardUrl() {
		return boardUrl;
	}
	
	public String getSimpleTitle() {
		return simpleTitle;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setBoardUrl(String boardUrl) {
		this.boardUrl = boardUrl;
	}
}
