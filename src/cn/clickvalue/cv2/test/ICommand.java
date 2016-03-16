package cn.clickvalue.cv2.test;

import java.util.Map;

public interface ICommand {

    /**
     *
     * @return True if operation is successful. Otherwise returns false.
     */
    public boolean execute(Map<String,Object>context);

}

