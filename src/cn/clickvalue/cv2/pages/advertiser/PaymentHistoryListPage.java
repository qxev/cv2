package cn.clickvalue.cv2.pages.advertiser;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.AdvertiserDeposit;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AdvertiserDepositService;

public class PaymentHistoryListPage extends BasePage {

    @SuppressWarnings("unused")
    @Property
    private int noOfRowsPerPage = 15;
    
    @ApplicationState
    private User user;
    
    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private ComponentResources componentResources;

    @Inject
    private AdvertiserDepositService advertiserDepositService;

    @Property
    private GridDataSource dataSource;

    @Property
    private AdvertiserDeposit advertiserDeposit;

    @Property
    private BeanModel<AdvertiserDeposit> beanModel;
    
    void onActivate() {
        initQuery();
        initBeanModel();
    }

    private void initBeanModel() {
        this.beanModel = beanModelSource.create(AdvertiserDeposit.class, true, componentResources);
        beanModel.get("depositType").label("Type");
        beanModel.get("depositValue").label("Amount");
        beanModel.get("depositDate").label("Date");
        beanModel.include("depositType", "depositValue", "depositDate");
    }

    private void initQuery() {
        CritQueryObject c = new CritQueryObject();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", user);
        c.setCondition(map);
        dataSource = new HibernateDataSource(advertiserDepositService, c);
    }

}