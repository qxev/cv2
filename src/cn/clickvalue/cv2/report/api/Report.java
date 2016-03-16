package cn.clickvalue.cv2.report.api;
import java.util.ArrayList;
import java.util.HashMap;
import cn.clickvalue.cv2.report.Dimension;
import com.darwinmarketing.configs.ConfigReader;
import org.apache.commons.lang.StringUtils;
/**
 * 报表程序主入口，用法
 * 1.创建对象:new
 * 2.加入维度定义:addDimension
 * 3.加入数据列定义:setSelectedColumn
 * 4.设置排序字段：setOrderField
 * 4.获取数据：getResult
 * 程序返回的数据是一个ArrayList，每一个节点是一个HashMap，在HashMap中包含了数据列与值的映射。
 * 注意：数据列的值有可能为null
 * @author jackie
 *
 */
public class Report {
    public static final int FIELD_OF_STRING = 0;
    public static final int FIELD_OF_LONG = 1;
    public static final int ORDER_NONE = 0;
    public static final int ORDER_ASC = 1;
    public static final int ORDER_DESC = 2;
    public static boolean inTestMode = true;
    private static final int MAX_SPLIT_TABLE = 100;
    private String dataTable = "";
    private String type = "";
    private int pageId = -1;
    private int pageRecords = 30;
    private int maxColumn = -1;
    private String orderField = "";
    private int orderType = 0;
    private ArrayList<Dimension> dmsList = new ArrayList<Dimension>();
    private HashMap<String,Integer> columns = new HashMap<String,Integer>();
    private ArrayList<String[]> dataFilter = new ArrayList<String[]>();
    private ArrayList<String> selectedFields = new ArrayList<String>();
    private ArrayList<String> headerFields = new ArrayList<String>();
    private String errorMessage = "";
    private String sql = "";
    /**
     * 使用ID构造报表查询
     * @param splitId 用以拆分寻表的ID
     * @param type 报表类型
     * @param maxDataColumn 当前数据报告中数据列的最大数量，是分页的依据之一，不可弄错
     */
    public Report(int splitId,String type,int maxDataColumn) {
        this.dataTable = getTableId(splitId);
        this.type = type;
        this.maxColumn = maxDataColumn;
    }
    /**
     * 获取表名
     * @param splitId 用以拆分的ID值
     * @return
     */
    private String getTableId(int splitId) {
        String ss = "";
        int tid = splitId % MAX_SPLIT_TABLE;
        String stid = (tid >= 10)?"crp_data":"crp_data0";
        if(inTestMode) {stid = "crp_data01";}
        else {
        ss = String.valueOf(tid);
        }
        String ids = stid.concat(ss);
        return ids;
    }
    /**
     * 构造一个报告查询，默认选出符合条件的所有记录，若需要分页，则必须调用setPageId方法设置分页
     * @param dataTable 数据表
     * @param maxDataColumn 当前数据报告中数据列的最大数量，是分页的依据之一，不可弄错
     * @param type 报表类型
     */
    public Report(String dataTable,String type,int maxDataColumn) {
        this.dataTable = dataTable;
        this.type = type;
        this.maxColumn = maxDataColumn;
    }
    /**
     * 构造一个分页的报告查询
     * @param dataTable 数据表
     * @param type 报表类型
     * @param maxDataColumn 当前数据报告中数据列的最大数量，是分页的依据之一，不可弄错
     * @param pageId 起始页
     * @param pageRecords 页行数
     */
    public Report(String dataTable,String type,int maxDataColumn,int pageId,int pageRecords) {
        this.pageId = (pageId<1)?1:pageId;
        this.pageRecords = pageRecords;
        this.dataTable = dataTable;
        this.type = type;
        this.maxColumn = maxDataColumn;
    }
   /**
    * 新定义一个报表维度，作为数据筛选过滤条件
    * @param idField 维度ID字段名，必须指定
    * @param nameField 维度名称字段名，当需要读取其信息时设置，若不需要，请设为null
    * @param memoField 维度第三属性字段名，辅助信息，如网站网址，当需要读取其信息时设置，若不需要，请设为null
    * @param conditions 二维数组，保存本维度的查询过滤条件（根据其idField属性），数组每行含两个元素：操作符，值
    */
    public void addDimension(String idField,String nameField,String memoField,String[][] conditions) {
        Dimension dms = new Dimension();
        String id = idField.toLowerCase();
        dms.setIdField(id);
        selectedFields.add(id);
        if(nameField==null) {}else {
            String name = nameField.toLowerCase();
            dms.setNameField(name);
            selectedFields.add(name);
            headerFields.add(name);
        }
        if(memoField==null) {}else{
            String memo = memoField.toLowerCase();
            dms.setMemoField(memo);
            selectedFields.add(memo);
            headerFields.add(memo);
        }
        dms.setConditions(conditions);
        dmsList.add(dms);
    }
    /**
     * 设置列数据的类型，以正确返回值格式
     * @param fieldName 数据列字段名
     * @param fieldType 数据列字段类型，调用Report.FIELD_OF_?来指定
     * @param conditions 二维数组，保存查询过滤条件，数组每行含两个元素：操作符，值
     */
    public void setSelectedColumn(String fieldName,int fieldType,String[][] conditions) {
        String fd = fieldName.toLowerCase();
        columns.put(fd, fieldType);
        headerFields.add(fd);
        if(conditions == null) {
        }else {
            for(int i=0;i<conditions.length;i++) {
                String[] cs = new String[] {fd,conditions[i][0],conditions[i][1]};
                dataFilter.add(cs);
            }
        }
    }
    /**
     * 设置排序字段
     * @param fieldName 字段名
     * @param orderType 调用Report.ORDER_?来指定：0:不排序，1：升序，2：降序
     */
    public void setOrderField(String fieldName,int orderType) {
        String fd = fieldName.toLowerCase();
        this.orderField = fd;
        this.orderType = (orderType==Report.ORDER_DESC)?Report.ORDER_DESC:Report.ORDER_ASC;
    }
    /**
     * 读取数据字段类型
     * @param fieldName 数据名
     * @return
     */
    public int getDataColumnType(String fieldName) {
        int ty = columns.get(fieldName);
        return ty;
    }
    public ArrayList<String> getSelectedFields() {
        return selectedFields;
    }
    public String getSql() {
        return sql;
    }
    /**
     * 返回报表头列信息对象
     * @return
     */
    public ArrayList<Field> getHeaderFields(){
        ArrayList<Field> flist = new ArrayList<Field>();
        String cf = null;
        Object cv = null;
        String cn = null;
        String en = null;
        String key = null;
        String order = null;
        String sortable = ConfigReader.getString("app.cv2.reports.images.sortable");
        String asc = ConfigReader.getString("app.cv2.reports.images.sortasc");
        String desc = ConfigReader.getString("app.cv2.reports.images.sortdesc");
        String compared = null;
        boolean noCompared = false;
        for(int i=0;i<headerFields.size();i++) {
            cf = headerFields.get(i);
            Field cfd = new Field();
            cfd.setName(cf);
            cv = columns.get(cf);
            if(cv==null) {}else {
                cfd.setCanOrder(true);
            }
            key = "app.cv2.reports.header-fields.".concat(cf).concat(".cn");
            cn = ConfigReader.getString(key);
            key = "app.cv2.reports.header-fields.".concat(cf).concat(".en");
            en = ConfigReader.getString(key);
            cfd.setChinese(cn);
            cfd.setEnglish(en);
            if(cfd.getCanOrder()) order = (cf.equals(orderField))?(orderType==Report.ORDER_DESC?desc:asc):sortable;
            cfd.setOrder(order);
            key = "app.cv2.reports.header-fields.".concat(cf).concat(".compared");
            compared = ConfigReader.getString(key);
            noCompared = StringUtils.isBlank(compared);
            if(noCompared) {}else cfd.setHasCompared(true);
            cfd.setCompared(compared);
            flist.add(cfd);
        }
        return flist;
    }
    /**
     * 不要设置，本方法仅作内部调试使用
     * @param sql
     */
    public void setSql(String ssql) {
        this.sql = ssql;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = this.errorMessage.concat("\n").concat(errorMessage);
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public ArrayList<String[]> getDataFilter() {
        return dataFilter;
    }
    public String getOrderField() {
        return orderField;
    }
    public int getOrderType() {
        return orderType;
    }
    public int getMaxColumn() {
        return maxColumn;
    }
    public String getDataTable() {
        return dataTable;
    }
    public String getType() {
        return type;
    }
    public int getPageId() {
        return pageId;
    }
    public int getPageRecords() {
        return pageRecords;
    }
    public ArrayList<Dimension> getDmsList() {
        return dmsList;
    }
    public HashMap<String, Integer> getColumns() {
        return columns;
    }
    public void setPageId(int pageId) {
        this.pageId = pageId;
    }
    public void setPageRecords(int pageRecords) {
        this.pageRecords = pageRecords;
    }
}
