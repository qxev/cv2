package cn.clickvalue.cv2.services.util.selector;

public interface GeneralValueEncodeCallBack {
    
    public String toClient(Object obj);
    
    public Object toValue(String str);
}
