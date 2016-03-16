package cn.clickvalue.cv2.pages.admin.commissionRule;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.tapestry.commons.components.DateTimeField;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.springframework.jdbc.core.JdbcTemplate;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import cn.clickvalue.cv2.StreamResponse.XLSAttachment;
import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.common.util.DefaultBeanFactory;
import cn.clickvalue.cv2.common.util.ExcelUtils;
import cn.clickvalue.cv2.common.util.RealPath;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Commission;
import cn.clickvalue.cv2.webservice.dgm.DgmpublisherwebservicesProxy;

public class commissionExportPage extends BasePage{

	@Component(id = "excelOutput")
	private Form form;
	
	@Component(parameters = { "value=userStartDate",
			"datePattern=yyyy-MM-dd", "disabled=false", "timePicker=false",
			"timePickerAdjacent=true" })
	private DateTimeField startDate;
	
	@Component(parameters = { "value=userEndDate",
			"datePattern=yyyy-MM-dd", "disabled=false", "timePicker=false",
			"timePickerAdjacent=true" })
	private DateTimeField endDate;
	
	@Property
	@Persist
	private Date userStartDate;
	
	@Property
	@Persist
	private Date userEndDate;
	
    private JdbcTemplate jdbcTemplate = (JdbcTemplate) DefaultBeanFactory.getBean("jdbcTemplate");
	
    Object onSubmit() throws FileNotFoundException, SecurityException, IllegalArgumentException, IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, ParserConfigurationException, SAXException{
		DgmpublisherwebservicesProxy dgmpublisherwebservicesProxy = new DgmpublisherwebservicesProxy();
		if (userStartDate==null)
			userStartDate = new Date();
		if (userEndDate==null)
			userEndDate = new Date();
		String data = dgmpublisherwebservicesProxy.getSales("Annie.Gong", "darwin51115592", new Double(0), "all", "Pending", DateUtil.dateToString(userStartDate), DateUtil.dateToString(userEndDate));
//		String data = dgmpublisherwebservicesProxy.getSales("Annie.Gong", "darwin51115592", new Double(0), "all", "Pending", "2009-01-01", "2009-06-08");
		
		InputStream is = new ByteArrayInputStream(data.getBytes());
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		DocumentBuilder builder=factory.newDocumentBuilder();
		Document doc = builder.parse(is);
		String realPath = RealPath.getRoot();
		String fileName = "report"+File.separator + DateUtil.dateToString(userStartDate) + "~" + DateUtil.dateToString(userEndDate) + "Report";
		List<Commission> commisions = getCPSCommission(doc);
		ExcelUtils.mergerJobXLS(commisions,"commission",fileName);
		FileInputStream fileInputStream = new FileInputStream(realPath+fileName+".xls");
		return new XLSAttachment(fileInputStream, DateUtil.dateToString(userStartDate) + "~" + DateUtil.dateToString(userEndDate) + "Report");
	}

	private List<Commission> getCPSCommission(Document doc) {
		List <Commission> commisions = new ArrayList<Commission>();
		for (int i=0; i<doc.getElementsByTagName("sale").getLength();i++){
			Commission commission = new Commission();
			commission.setDate(doc.getElementsByTagName("ActionDate").item(i).getFirstChild().getNodeValue().substring(0, 19));
//			commission.setIp("123.53.44.67");
			commission.setAid(doc.getElementsByTagName("NetworkID").item(i).getFirstChild().getNodeValue());
			commission.setCid(doc.getElementsByTagName("OrderID").item(i).getFirstChild().getNodeValue());
			commission.setQuantity("1");
			commission.setType("CPS");
			commission.setAmount("");
			commission.setCommission(doc.getElementsByTagName("SaleCommission").item(i).getFirstChild().getNodeValue());
			commisions.add(commission);
		}
		
		return commisions;
	}
	
	private List<Commission> getCPCCommission() {
		//TODO
		List <Commission> commisions = new ArrayList<Commission>();
		return commisions;
	}
	
	private List<Commission> getCPLCommission() {
		//TODO
		List <Commission> commisions = new ArrayList<Commission>();
		return commisions;
	}

}
