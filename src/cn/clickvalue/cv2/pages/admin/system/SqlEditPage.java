package cn.clickvalue.cv2.pages.admin.system;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.HibernateException;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.ExcelOutput;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.ExcelOutputService;

public class SqlEditPage extends BasePage {
    
    @Persist("flash")
    private ExcelOutput excelOutput;
    
    @InjectPage
    private MessagePage messagePage;
    
    @Inject
    private ExcelOutputService excelOutputService;

    
    @SetupRender
    public void setupRender() {
       
    }
    
    void onPrepare() {
        if(excelOutput == null) {
        	excelOutput = new ExcelOutput();
        }
    }
    
    void onActivate(int excelOutputId) {
    	excelOutput = excelOutputService.findUniqueBy("id", excelOutputId);
    }
    
    Object onClicked() {
        return SqlListPage.class;
    }
    Object onSuccess() {
        try {
        	excelOutputService.save(excelOutput);
            messagePage.setMessage("保存sql名称成功！");
        } catch (HibernateException e) {
            messagePage.setMessage("保存sql名称失败！");
        }
        messagePage.setNextPage("admin/system/sqllistpage");
        return messagePage;
    }

    public ExcelOutput getExcelOutput() {
        return excelOutput;
    }

    public void setExcelOutput(ExcelOutput excelOutput) {
        this.excelOutput = excelOutput;
    }
}
