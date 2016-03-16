package cn.clickvalue.cv2.pages.admin.system;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.StreamResponse.XLSAttachment;
import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.util.FileUtils;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.excel.model.WebsiteModel;
import cn.clickvalue.cv2.excel.operation.ExcelWriter;
import cn.clickvalue.cv2.services.logic.SiteService;

public class WebsiteExport extends BasePage {
    
    @Property
    @Persist
    private Integer roleType = 0;
    
    @Inject
    private ExcelWriter excelWriterImpl;
    
    @Inject
    private SiteService siteService;

    
    StreamResponse onSubmit() throws WriteException, IOException {
        
        writerExcel();
        FileInputStream fileInputStream = new FileInputStream(FileUtils.createExcelFile(getUniqueFolderName(), getExcelName()));
        return new XLSAttachment(fileInputStream,"affiliate_website");
    }
    
    private void writerExcel() throws IOException, WriteException {
        WritableWorkbook createWorkbook = Workbook.createWorkbook(FileUtils.createExcelFile(getUniqueFolderName(), getExcelName()));
        WritableSheet ws = createWorkbook.createSheet(getRole(), 0);
        List findActivedSites = siteService.findActivedSites(getRole());
        
        excelWriterImpl.createExcel(ws, WebsiteModel.class, findActivedSites);
        
        createWorkbook.write();
        createWorkbook.close();
    }
    
    private String getRole() {
        if(roleType == 0) {
            return Constants.USER_GROUP_AFFILIATE;
        }else {
            return Constants.USER_GROUP_ADVERTISER;
        }
    }

    private String getExcelName() {
        return "affiliate_website";
    }
    
    private String getUniqueFolderName() {
        return excelWriterImpl.destFolderForUser(getClientSession().getId());
    }
    

}
