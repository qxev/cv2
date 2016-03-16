package cn.clickvalue.cv2.services.util.selector;

import java.util.List;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.services.PropertyAccess;

public class GeneralValueEncoder<T> implements ValueEncoder<T> {

    private List<T> list;

    private final PropertyAccess access;

    private final String fieldName;
    
    private GeneralValueEncodeCallBack generalValueEncodeCallBack;

    public GeneralValueEncoder(List<T> list, String fieldName,
            PropertyAccess propertyAccess) {
        this.list = list;
        this.fieldName = fieldName;
        this.access = propertyAccess;
    }
    
    public GeneralValueEncoder(
            GeneralValueEncodeCallBack generalValueEncodeCallBack) {
        this.generalValueEncodeCallBack = generalValueEncodeCallBack;
        this.access = null;
        this.fieldName = "";
    }

    public String toClient(T obj) {
        
        if(generalValueEncodeCallBack != null) {
            String str = toClientCallBack(obj);
            return str;
        }
        if (fieldName == null || fieldName.equals("")) {
            return obj + "";
        } else {
            String str = access.get(obj, fieldName) + "";
            return str;
        }
    }

    public T toValue(String string) {

        if(generalValueEncodeCallBack!=null) {
            T value = toValueCallBack(string);
            return value;
        }
        for (T obj : list) {
            if (fieldName == null || fieldName.equals("")) {
                if ((obj + "").equals(string)) {
                    return obj;
                }
            } else {
                String attr = access.get(obj, fieldName) + "";
                if (attr.equals(string)) {
                    return obj;
                }
            }
        }
        return null;
    }
    
    /**
     * @param string
     * @return 按照用户提供的规则根据param返回对象
     * 本方法只有在用户提供了GeneralToValueCallBack接口的实现类的实例时才会被调用
     * 
     */
    @SuppressWarnings("unchecked")
    public T toValueCallBack(String string) {
        T obj = (T)generalValueEncodeCallBack.toValue(string);
        return obj;
    }
    
    /**
     * @param obj
     * @return 返回select中某option的内容
     * 本方法只有在用户提供了GeneralToClientCallBack接口的实现类的实例时才会被调用
     * 
     */
    public String toClientCallBack(T obj) {
        String str = generalValueEncodeCallBack.toClient(obj);
        return str;
    }
}
