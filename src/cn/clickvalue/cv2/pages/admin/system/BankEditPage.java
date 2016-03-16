package cn.clickvalue.cv2.pages.admin.system;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.HibernateException;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Bank;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.BankService;

public class BankEditPage extends BasePage {
    
    @Persist("flash")
    private Bank bank;
    
    @InjectPage
    private MessagePage messagePage;
    
    @Inject
    private BankService bankService;

    
    @SetupRender
    public void setupRender() {
       
    }
    
    void onPrepare() {
        if(bank == null) {
            bank = new Bank();
        }
    }
    
    void onActivate(int bankId) {
        bank = bankService.findUniqueBy("id", bankId);
    }
    
    Object onClicked() {
        return BankListPage.class;
    }
    Object onSuccess() {
        try {
            bankService.save(bank);
            messagePage.setMessage("保存银行名称成功！");
        } catch (HibernateException e) {
            messagePage.setMessage("保存银行名称失败！");
        }
        messagePage.setNextPage("admin/system/banklistpage");
        return messagePage;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
}
