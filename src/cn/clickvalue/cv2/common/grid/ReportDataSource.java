package cn.clickvalue.cv2.common.grid;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.Defense;

public class ReportDataSource implements GridDataSource {

    private List list;
    
    private Iterator pageRowList;
    
    private int count = 0;
    
    private Class clazz;

    @SuppressWarnings("unchecked")
    public ReportDataSource(Collection collection,Class clazz,int count)
    {
        Defense.notNull(collection, "collection");
        list = CollectionFactory.newList(collection);
        this.count = count;
        this.clazz = clazz;
        this.pageRowList = list.iterator();
    }

    public int getAvailableRows()
    {
        return count;
    }

    public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints)
    {
        for (SortConstraint constraint : sortConstraints)
        {
//            final ColumnSort sort = constraint.getColumnSort();
//
//            if (sort == ColumnSort.UNSORTED) continue;
//
//            final PropertyConduit conduit = constraint.getPropertyModel().getConduit();
//
//            final Comparator valueComparator = new Comparator<Comparable>()
//            {
//                public int compare(Comparable o1, Comparable o2)
//                {
//                    // Simplify comparison, and handle case where both are nulls.
//
//                    if (o1 == o2) return 0;
//
//                    if (o2 == null) return 1;
//
//                    if (o1 == null) return -1;
//
//                    return o1.compareTo(o2);
//                }
//            };
//
//            final Comparator rowComparator = new Comparator()
//            {
//                public int compare(Object row1, Object row2)
//                {
//                    Comparable value1 = (Comparable) conduit.get(row1);
//                    Comparable value2 = (Comparable) conduit.get(row2);
//
//                    return valueComparator.compare(value1, value2);
//                }
//            };
//
//            final Comparator reverseComparator = new Comparator()
//            {
//                public int compare(Object o1, Object o2)
//                {
//                    int modifier = sort == ColumnSort.ASCENDING ? 1 : -1;
//
//                    return modifier * rowComparator.compare(o1, o2);
//                }
//            };
//
//            // We can freely sort this list because its just a copy.
//
//            Collections.sort(list, reverseComparator);
        }
    }

    /**
     * Returns the type of the first element in the list, or null if the list is empty.
     */
    public Class getRowType()
    {
        return clazz.getClass();
    }

    public Object getRowValue(int index)
    {
        Object entityObject = null;
        if (pageRowList != null && pageRowList.hasNext()) {
            entityObject = pageRowList.next();
        }
        return entityObject;
    }
}
