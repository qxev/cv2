package cn.clickvalue.cv2.components;

import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.corelib.components.Grid;

/***
 * 不知道为什么tapestry的grid要将currentPage持久化，真是无奈！
 * 此类组件是为了处理动态beanModel的。
 * @author Tim
 *
 */
public class DynamicGrid extends Grid {
    
    @CleanupRender
    void cleanupRender() {
        super.setCurrentPage(1);
    }
    
}
