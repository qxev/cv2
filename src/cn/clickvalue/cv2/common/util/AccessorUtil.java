package cn.clickvalue.cv2.common.util;

public class AccessorUtil {

    public static String buildGetter(String fieldName)
    {
        String methodName = "getEntity";
        if (fieldName != null)
        {
            if (!fieldName.startsWith("get"))
            {

                methodName = "get" + fieldName.substring(0, 1).toUpperCase();
                if (fieldName.length() > 1)
                {
                    methodName = methodName + fieldName.substring(1);
                }
                else
                {
                    methodName = fieldName;
                }
            }
            else
            {
                methodName = fieldName;
            }
        }
        return methodName;
    }
}

