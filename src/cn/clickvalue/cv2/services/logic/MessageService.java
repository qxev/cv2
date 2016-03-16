package cn.clickvalue.cv2.services.logic;

import java.util.Date;

import cn.clickvalue.cv2.model.Message;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class MessageService extends BaseService<Message> {
    
    public Message createMessageForAdmin() {
        Message message = new Message();
        Date currentDate = new Date();
        
        message.setCreatedAt(currentDate);
        message.setUpdatedAt(currentDate);
        return message;
    }

}
