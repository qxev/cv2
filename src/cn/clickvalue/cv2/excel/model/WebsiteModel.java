package cn.clickvalue.cv2.excel.model;

public class WebsiteModel {

    private Integer userId;
    
    private String userName;
    
    private String email;
    
    private String urlName;
    
    private String url;
    
    private String phone;
    
    private String contact;
    
    private String createdAt;
    
    private String categoryName;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhone() {
        return phone==null?"":phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContact() {
        return contact.equals("nullnull")?"":contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

	public String getCategoryName() {
		return categoryName==null?"":categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
    
}
