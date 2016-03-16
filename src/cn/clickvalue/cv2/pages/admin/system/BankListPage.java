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
import org.hibernate.criterion.Order;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Bank;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.BankService;

public class BankListPage extends BasePage{

    @Property
    private Bank bank;

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

    private BeanModel<Bank> beanModel;
    
    @Component(parameters = { "event=add" })
    private EventLink addBank;
    
    @InjectPage
    private BankEditPage bankEditPage;

    @Inject
    private BankService bankService;

    void onActivate() {
        initQuery();
    }

    Object onAdd() {
        Bank bank = new Bank();
        bankEditPage.setBank(bank);
        return bankEditPage;
    }
    
    private void initQuery() {
        CritQueryObject c = new CritQueryObject();
        c.addOrder(Order.desc("createdAt"));
        this.dataSource = new HibernateDataSource(bankService, c);
    }

    public BeanModel<Bank> getBeanModel() {
        beanModel = beanModelSource.create(Bank.class, true,
                componentResources);
        beanModel.get("name").label("银行名称").sortable(
                true);
        beanModel.get("description").label("说明").sortable(false);
        beanModel.add("operate",null).label("操作").sortable(false);
        beanModel.include("name", "description", "operate");
        return beanModel;
    }
}
