package cn.clickvalue.cv2.excel.parseXml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocument;
import org.dom4j.io.SAXReader;

import cn.clickvalue.cv2.excel.config.model.ExcelConfigModel;
import cn.clickvalue.cv2.excel.config.model.PropertyModel;
import cn.clickvalue.cv2.excel.constants.ExcelConstants;
import cn.clickvalue.cv2.excel.model.AffiliateSummaryDailyModel;
import cn.clickvalue.cv2.excel.operation.ExcelWriterImpl;

public class ParseXmlServiceImpl implements ParseXmlService {
    
    public ParseXmlServiceImpl() {
        
    }

    public ExcelConfigModel loadExcelConfigModel(Class clazz) {
        Element element = this.getModelElement(clazz);
        ExcelConfigModel excelConfigModel = new ExcelConfigModel();
        if(element != null){
            excelConfigModel.setMapName(element.attributeValue(ExcelConstants.MODEL_CLASS));
            excelConfigModel.setPropertyModelList(this.getPropertyModelList(element));          
        }
        return excelConfigModel;
    }

    private Element getModelElement(Class clazz) {
        Document document = getDocument(clazz);
        
        Element root = document.getRootElement();
        List elements = root.elements();
        Element element = null;
        Element returnElement = null;
        for(Iterator it = elements.iterator();it.hasNext();){
            element = (Element) it.next();
            if(element.attributeValue("id").equals(clazz.getSimpleName())){
                returnElement = element;
                break;
            }
        }
        return returnElement;
    }
    
    private Document getDocument(Class clazz) {
        SAXReader saxReader = new SAXReader();
        Document document = new DOMDocument();
        
        StringBuffer sb = new StringBuffer();
        sb.append("/cn/clickvalue/cv2/excel/model/");
        sb.append(clazz.getSimpleName());
        sb.append(".xml");
        
        InputStream xmlInputStream = this.getClass().getResourceAsStream(sb.toString());
      
        try {
            document = saxReader.read(xmlInputStream);
        } catch (DocumentException e) {
            
        }
        return document;
    }
    
    /*
     * 将PropertyModel放进一个List<PropertyModel>
     */
    private List<PropertyModel> getPropertyModelList(Element model) {
        List<PropertyModel>  propertyModelList = new ArrayList<PropertyModel>();
        List list = model.elements();
        Element element = null;
        
        for(Iterator it = list.iterator();it.hasNext();) {
            element = (Element) it.next();
            PropertyModel propertyModel = new PropertyModel();
            propertyModel.setName(element.attributeValue(ExcelConstants.PROPERTY_NAME));
            propertyModel.setColumn(element.attributeValue(ExcelConstants.PROPERTY_CLOUMN));
            propertyModel.setExcelTitleName(element.attributeValue(ExcelConstants.PROPERTY_EXCEL_TITLE_NAME));
            propertyModel.setDataType(element.attributeValue(ExcelConstants.PROPERTY_DATA_TYPE));
            propertyModel.setMaxLength(element.attributeValue(ExcelConstants.PROPERTY_MAX_LENGTH));
            
            propertyModel.setFixity(element.attributeValue(ExcelConstants.PROPERTY_FIXITY));
            propertyModel.setCodeTableName(element.attributeValue(ExcelConstants.PROPERTY_CODE_TABLE_NAME));
            propertyModel.setDefaultValue(element.attributeValue(ExcelConstants.PROPERTY_DEFAULT));
            
            propertyModelList.add(propertyModel);
            
        }
        return propertyModelList;
    }
    
    public static void main(String[] args) throws IOException, RowsExceededException, WriteException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
       
       WritableWorkbook createWorkbook = Workbook.createWorkbook(new File("d:/a.xls"));
       WritableSheet ws = createWorkbook.createSheet("report", 0);
       
       ExcelWriterImpl ew = new ExcelWriterImpl();
       
       List modelList = new ArrayList();
       for(int i=0;i<10;i++) {
           AffiliateSummaryDailyModel as = new AffiliateSummaryDailyModel();
           as.setSummaryDate(new Date());
           as.setActivedWebsitesForHalfAYear(null);
           as.setActivedWebsitesForOneMonth(22);
           as.setActivedWebsitesForThreeMonth(23);
           as.setAffiliateClicks(24);
           as.setApprovedWebsites(25);
           as.setBeAppliedCampaigns(26);
           as.setBeClickedAds(27);
           as.setBeClickedCampaigns(20);
           as.setDailyActivedWebsites(6);
           as.setDeclinedWebsites(7);
           as.setGotCodes(8);
           as.setNewAds(9);
           as.setNewCampaigns(10);
           as.setPendingApprovalWebSites(11);
           as.setValidAds(12);
           as.setPageView(13);
           modelList.add(as);
       }
       
         ew.createExcel(ws, AffiliateSummaryDailyModel.class, modelList);
       createWorkbook.write();
       createWorkbook.close();
    }

}
