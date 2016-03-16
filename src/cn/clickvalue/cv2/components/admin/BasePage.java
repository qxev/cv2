package cn.clickvalue.cv2.components.admin;

import java.text.MessageFormat;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import cn.clickvalue.cv2.common.util.MessageUtil;
import cn.clickvalue.cv2.services.util.Auth;
import cn.clickvalue.cv2.services.util.Auth.Role;
import cn.clickvalue.cv2.web.ClientSession;

@Auth( { Role.ADMIN3 })
public class BasePage {
    
    @Inject
    private Logger logger;

    @Inject
    private Messages messages;

    @ApplicationState
    private ClientSession clientSession;

    private int noOfRowsPerPage = 15;
    
    
    @Persist(PersistenceConstants.FLASH)
    private String message;
    
    @Persist(PersistenceConstants.FLASH)
    private String type;
    
 
    /**
     * 添加錯誤信息
     * @param form     主體
     * @param field    主體字段
     * @param message  錯誤信息
     * @param flag     是否國際化
     * @param args     格式化參數
     */
    protected void addError(Form form, Field field,String message, boolean flag, Object...args) { 
      if (field == null || form == null) { 
          return; 
      }
      String errorMessage = buildMessage(message, flag, args);
      setMessage(errorMessage);
      form.recordError(field, errorMessage);
      setType("error");
    }
    
    
    /**
     * 添加錯誤信息
     * 
     * @param form     添加主體
     * @param message  消息
     * @param flag     是否國際化
     * @param args     格式化參數
     */
    protected void addError(Form form,String message, boolean flag, Object...args) { 
        if(form == null){
            return;
        }
        String errorMessage = buildMessage(message, flag, args);
        setMessage(errorMessage);
        form.recordError(errorMessage);
        setType("error");
    }
    
    /**
     * 添加錯誤信息
     * 
     * @param message
     * @param flag    是否使用 國際化
     * @param args    格式化參數
     */
    protected void addError(String message, boolean flag, Object...args) { 
      setMessage(buildMessage(message, flag, args));
      setType("error");
    }
    
    /**
     * 读取国际化文件,然后进行格式化
     * @param key
     * @param args
     * @return String
     */
    protected String getMessageText(String key, Object... args) {
        if (args == null) {
            return getMessages().get(key);
        }

        String msg = MessageUtil.convert(messages.get(key));
        return String.format(msg, args);
    }
    
    /**
     * 添加友好信息,因為只屬於提示成功的所以和onValidateForm方法不衝突,故不提供form.recordError方法等...
     * @param message   消息
     * @param flag      是否國際化
     * @param args      格式化參數
     */
    public void addInfo(String message, boolean flag, Object...args) {
        setMessage(buildMessage(message, flag, args));
        setType("success");     
    }
    
    /**
     * 不带有格式化的普通取message的消息
     * @param key
     * @return String
     */
    protected String getText(String key) {
        return getMessages().get(key);
    }
    
    /**
     * 带格式化的取message的消息
     * @param key
     * @param arg
     * @return String
     */
    protected String getText(String key, Object arg) {
        if (arg == null) {
            return getText(key);
        }
        if (arg instanceof String) {
            return MessageFormat.format(getMessages().get(key), arg);
        } else if (arg instanceof Object[]) {
            return MessageFormat.format(getMessages().get(key), (Object[]) arg);
        } else {
            logger.error("argument '" + arg + "' not String or Object[]");
            return "";
        }
    }
    
    /**
     * 消息创建器,带有国际化和格式化
     * @param key  国际化的key
     * @param flag 是否使用国际化
     * @param args
     * @return String
     */
    private String buildMessage(String key, boolean flag, Object...args) {
        return (flag == true? getMessageText(key, args) : String.format(key, args));
    }

    public ClientSession getClientSession() {
        return clientSession;
    }

    public void setClientSession(ClientSession clientSession) {
        this.clientSession = clientSession;
    }

    public int getNoOfRowsPerPage() {
        return noOfRowsPerPage;
    }

    public void setNoOfRowsPerPage(int noOfRowsPerPage) {
        this.noOfRowsPerPage = noOfRowsPerPage;
    }

    public Messages getMessages() {
        return messages;
    }

    public void setMessages(Messages messages) {
        this.messages = messages;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

}
