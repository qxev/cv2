package cn.clickvalue.cv2.pages.admin.system;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.common.util.DefaultBeanFactory;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.ExcelOutput;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.pages.demo.DemoListPage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.ExcelOutputService;

public class SqlListPage extends BasePage{

    @Property
    private ExcelOutput excelOutput;

    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private ComponentResources componentResources;

    @SuppressWarnings("unused")
    @Property
    private int noOfRowsPerPage = 15;

    @Persist
    @Property
    private GridDataSource dataSource;

    private BeanModel<ExcelOutput> beanModel;
    
    @Component(parameters = { "event=add" })
    private EventLink addExcelOutput;

    @InjectPage
    private SqlEditPage sqlEditPage;

    @Inject
    private ExcelOutputService excelOutputService;

    void onActivate() {
        initQuery();
    }

    /**
     * @param event 事件标示
     * @param id 广告活动ID
     */
    Object onActivate(String event,int id) {
        if("del".equals(event)) {
        	JdbcTemplate jdbcTemplate = (JdbcTemplate) DefaultBeanFactory.getBean("jdbcTemplate");
        	String sql = "delete from exceloutput where id = ?";
        	jdbcTemplate.update(sql,new Object[]{id});
        }
        return SqlListPage.class;
    }
    
    Object onAdd() {
    	ExcelOutput excelOutput = new ExcelOutput();
        sqlEditPage.setExcelOutput(excelOutput);
        return sqlEditPage;
    }
    
    private void initQuery() {
        CritQueryObject c = new CritQueryObject();
        this.dataSource = new HibernateDataSource(excelOutputService, c);
    }

    public BeanModel<ExcelOutput> getBeanModel() {
        beanModel = beanModelSource.create(ExcelOutput.class, true,
                componentResources);
        beanModel.get("campaignId").label("广告活动ID");
        beanModel.get("excelFields").label("excel显示字段");
        beanModel.get("sqlFields").label("excel输出字段");
        beanModel.get("sqlContent").label("sql语句");
        beanModel.get("jobType").label("执行周期");
        beanModel.get("ruleType").label("佣金规则");
        beanModel.add("operate",null).label("操作").sortable(false);
        beanModel.include("campaignId", "excelFields", "sqlFields", "sqlContent","jobType","ruleType","operate");
        return beanModel;
    }
}
