package cn.clickvalue.cv2.common.grid;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.SortConstraint;

public abstract class GridHelper {

    /**
     * 获取排序对象，t5有bug,不能直接传SortModel
     * @param sortContraints
     * @return
     */
    public static Sort getSort(List<SortConstraint> sortContraints,Sort... defaultSorts) {
        Sort objSort = (defaultSorts == null || defaultSorts.length == 0) ? new Sort() : defaultSorts[0];
        
        for (SortConstraint constraint : sortContraints) {
            final ColumnSort sort = constraint.getColumnSort();

            if (sort == ColumnSort.UNSORTED)
                continue;

            final PropertyModel propertyModel = constraint.getPropertyModel();

            int i = StringUtils.countMatches(propertyModel.getPropertyName(),
                    ".");

            String sortName = null;

            if (i > 1) {
                sortName = StringUtils.substring(propertyModel
                        .getPropertyName(), propertyModel.getPropertyName()
                        .indexOf(".") + 1);
            } else {
                sortName = propertyModel.getPropertyName();
            }
             
            objSort.setSortName(sortName);
            
            if (sort.name().equals("ASCENDING")) {
                objSort.setSortType("asc");
            } else {
                objSort.setSortType("desc");
            }
           
        }
        return objSort;
    }
    
    /**
     * 计算分页数 limit ?,perPage
     * @param currentPage
     * @param perPage
     * @return ?
     */
    public static int countPager(int currentPage,int perPage) {
        int countPage = 0;
        if(currentPage != 1) {
            countPage = (currentPage - 1) * perPage;
        }
        return countPage;
    }
}
