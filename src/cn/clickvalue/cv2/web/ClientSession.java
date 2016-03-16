package cn.clickvalue.cv2.web;

import java.util.ArrayList;
import java.util.List;

public class ClientSession {
    private Integer id;

    private String userName;

    private String userGroupName;

    private Integer language;
    
    
    /**
     * 用来保存一个当前用户对应的其他帐户的ID
     */
    private List<Integer> anotherUserIds;

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }
    
    public List<Integer> getAnotherUserIds() {
		return anotherUserIds;
	}

	public void setAnotherUserIds(List<Integer> anotherUserIds) {
		this.anotherUserIds = anotherUserIds;
	}
	
	public void addAnotherUserId(Integer anotherUserId){
		if(anotherUserIds == null){
			anotherUserIds = new ArrayList<Integer>();
		}
		
		if(!anotherUserIds.contains(anotherUserId)){
			anotherUserIds.add(anotherUserId);
		}
	}
	
	public void deleteAnotherUserId(Integer anotherUserId){
		if(anotherUserIds == null){
			return;
		}
		
		if(!anotherUserIds.contains(anotherUserId)){
			anotherUserIds.remove(anotherUserId);
		}
	}
}
