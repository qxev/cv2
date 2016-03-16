package cn.clickvalue.cv2.model;

import java.util.Date;

public class BBSMember implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private int uid;

	private String secques;

	private int gender;

	private int adminid;

	private int groupid;

	private int groupexpiry;

	private String extgroupids;

	private String regip;

	private int regdate;

	private String lastip;

	private int lastvisit;

	private int lastactivity;

	private int lastpost;

	private int posts;

	private int digestposts;

	private int oltime;

	private int pageviews;

	private int credits;

	private int extcredits1;
	
	private int extcredits2;
	
	private int extcredits3;
	
	private int extcredits4;
	
	private int extcredits5;
	
	private int extcredits6;
	
	private int extcredits7;
	
	private int extcredits8;

	private int avatarshowid;

	private Date bday;

	private int sigstatus;

	private int tpp;

	private int ppp;

	private int styleid;

	private String dateformat;

	private int timeformat;

	private int pmsound;

	private int showemail;

	private int newsletter;

	private int invisible;

	private String timeoffset;

	private int newpm;

	private int accessmasks;

	private String username;

	private String password;

	private String email;
	
	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSecques() {
		return secques;
	}

	public void setSecques(String secques) {
		this.secques = secques;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getAdminid() {
		return adminid;
	}

	public void setAdminid(int adminid) {
		this.adminid = adminid;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public int getGroupexpiry() {
		return groupexpiry;
	}

	public void setGroupexpiry(int groupexpiry) {
		this.groupexpiry = groupexpiry;
	}

	public String getExtgroupids() {
		return extgroupids;
	}

	public void setExtgroupids(String extgroupids) {
		this.extgroupids = extgroupids;
	}

	public String getRegip() {
		return regip;
	}

	public void setRegip(String regip) {
		this.regip = regip;
	}

	public int getRegdate() {
		return regdate;
	}

	public void setRegdate(int regdate) {
		this.regdate = regdate;
	}

	public String getLastip() {
		return lastip;
	}

	public void setLastip(String lastip) {
		this.lastip = lastip;
	}

	public int getLastvisit() {
		return lastvisit;
	}

	public void setLastvisit(int lastvisit) {
		this.lastvisit = lastvisit;
	}

	public int getLastactivity() {
		return lastactivity;
	}

	public void setLastactivity(int lastactivity) {
		this.lastactivity = lastactivity;
	}

	public int getLastpost() {
		return lastpost;
	}

	public void setLastpost(int lastpost) {
		this.lastpost = lastpost;
	}

	public int getPosts() {
		return posts;
	}

	public void setPosts(int posts) {
		this.posts = posts;
	}

	public int getDigestposts() {
		return digestposts;
	}

	public void setDigestposts(int digestposts) {
		this.digestposts = digestposts;
	}

	public int getOltime() {
		return oltime;
	}

	public void setOltime(int oltime) {
		this.oltime = oltime;
	}

	public int getPageviews() {
		return pageviews;
	}

	public void setPageviews(int pageviews) {
		this.pageviews = pageviews;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public int getExtcredits1() {
		return extcredits1;
	}

	public void setExtcredits1(int extcredits1) {
		this.extcredits1 = extcredits1;
	}

	public int getExtcredits2() {
		return extcredits2;
	}

	public void setExtcredits2(int extcredits2) {
		this.extcredits2 = extcredits2;
	}

	public int getExtcredits3() {
		return extcredits3;
	}

	public void setExtcredits3(int extcredits3) {
		this.extcredits3 = extcredits3;
	}

	public int getExtcredits4() {
		return extcredits4;
	}

	public void setExtcredits4(int extcredits4) {
		this.extcredits4 = extcredits4;
	}

	public int getExtcredits5() {
		return extcredits5;
	}

	public void setExtcredits5(int extcredits5) {
		this.extcredits5 = extcredits5;
	}

	public int getExtcredits6() {
		return extcredits6;
	}

	public void setExtcredits6(int extcredits6) {
		this.extcredits6 = extcredits6;
	}

	public int getExtcredits7() {
		return extcredits7;
	}

	public void setExtcredits7(int extcredits7) {
		this.extcredits7 = extcredits7;
	}

	public int getExtcredits8() {
		return extcredits8;
	}

	public void setExtcredits8(int extcredits8) {
		this.extcredits8 = extcredits8;
	}

	public int getAvatarshowid() {
		return avatarshowid;
	}

	public void setAvatarshowid(int avatarshowid) {
		this.avatarshowid = avatarshowid;
	}

	public Date getBday() {
		return bday;
	}

	public void setBday(Date bday) {
		this.bday = bday;
	}

	public int getSigstatus() {
		return sigstatus;
	}

	public void setSigstatus(int sigstatus) {
		this.sigstatus = sigstatus;
	}

	public int getTpp() {
		return tpp;
	}

	public void setTpp(int tpp) {
		this.tpp = tpp;
	}

	public int getPpp() {
		return ppp;
	}

	public void setPpp(int ppp) {
		this.ppp = ppp;
	}

	public int getStyleid() {
		return styleid;
	}

	public void setStyleid(int styleid) {
		this.styleid = styleid;
	}

	public String getDateformat() {
		return dateformat;
	}

	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}

	public int getTimeformat() {
		return timeformat;
	}

	public void setTimeformat(int timeformat) {
		this.timeformat = timeformat;
	}

	public int getPmsound() {
		return pmsound;
	}

	public void setPmsound(int pmsound) {
		this.pmsound = pmsound;
	}

	public int getShowemail() {
		return showemail;
	}

	public void setShowemail(int showemail) {
		this.showemail = showemail;
	}

	public int getNewsletter() {
		return newsletter;
	}

	public void setNewsletter(int newsletter) {
		this.newsletter = newsletter;
	}

	public int getInvisible() {
		return invisible;
	}

	public void setInvisible(int invisible) {
		this.invisible = invisible;
	}

	public String getTimeoffset() {
		return timeoffset;
	}

	public void setTimeoffset(String timeoffset) {
		this.timeoffset = timeoffset;
	}

	public int getNewpm() {
		return newpm;
	}

	public void setNewpm(int newpm) {
		this.newpm = newpm;
	}

	public int getAccessmasks() {
		return accessmasks;
	}

	public void setAccessmasks(int accessmasks) {
		this.accessmasks = accessmasks;
	}
}
