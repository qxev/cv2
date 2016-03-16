package cn.clickvalue.cv2.pages.admin.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.commons.components.DateTimeField;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import cn.clickvalue.cv2.StreamResponse.XLSAttachment;
import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.common.util.DefaultBeanFactory;
import cn.clickvalue.cv2.common.util.ExcelUtils;
import cn.clickvalue.cv2.common.util.RealPath;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.ExcelOutput;
import cn.clickvalue.cv2.model.ExportField;
import cn.clickvalue.cv2.model.QueryField;
import cn.clickvalue.cv2.services.logic.ExcelOutputService;

public class ExcelOutputPage extends BasePage{

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
	
    @Property
    @Persist
    private Integer excelOutputId;
    
    @Inject
    private ExcelOutputService excelOutputService;
    
    private JdbcTemplate jdbcTemplate = (JdbcTemplate) DefaultBeanFactory.getBean("jdbcTemplate");
	
	Object onSubmit() throws FileNotFoundException, SecurityException, IllegalArgumentException, IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		if (excelOutputId!=null){
			ExcelOutput excelOutput = excelOutputService.get(excelOutputId);
			String realPath = RealPath.getRoot();
			String fileName = getFilePath(realPath,excelOutput.getCampaignId()) + File.separator + DateUtil.dateToString(userStartDate) + "~" + DateUtil.dateToString(userEndDate) + "Report";
			String sql = getSql(excelOutput);
			List<HashMap<Integer, String>> results = getResult(jdbcTemplate,excelOutput, sql);
			List <QueryField>outputList = getOutputList(excelOutput);
			List<ExportField> datas = initDatas(excelOutput, results);
			ExcelUtils.writeExcel(realPath+"templet/",outputList,"tempFile");
			ExcelUtils.mergerJobXLS(datas,"tempFile",fileName);
			FileInputStream fileInputStream = new FileInputStream(realPath+fileName+".xls");
			return new XLSAttachment(fileInputStream, DateUtil.dateToString(userStartDate) + "~" + DateUtil.dateToString(userEndDate) + "Report");
		} else {
			return null;
		}
	}

	/**
	 * 替换开始时间和结束时间
	 * @param excelOutput
	 * @return
	 */
	private String getSql(ExcelOutput excelOutput) {
		String sql = excelOutput.getSqlContent();
		String startDate = "'"+DateUtil.dateToString(userStartDate)+" 00:00:00'";
		String endDate = "'"+DateUtil.dateToString(userEndDate)+" 23:59:59'";
		if (userStartDate!=null)
			sql = sql.replace("{startDate}", startDate);
		else
			sql = sql.replace("{startDate}", "'1000-01-01 00:00:00'");
		if (userEndDate!=null)
			sql = sql.replace("{endDate}", endDate);
		else
			sql = sql.replace("{endDate}", "'3000-01-01 00:00:00'");
		return sql;
	}
    
	
    @SuppressWarnings("unchecked")
	public SelectModel getCampaignOutputModel() {
    	String sql = "select * from exceloutput left join campaign on campaign.id = exceloutput.campaignId";
		List<HashMap<String,String>> results = jdbcTemplate.query(sql,new RowMapper(){
			public Object mapRow(ResultSet rs, int index) throws SQLException {
				Map u = new HashMap<String,String>();
				u.put("id", rs.getString("exceloutput.id"));
				u.put("campaignName", rs.getString("campaign.Name"));
				return u;
		    }
		});
		List<OptionModel> campaignOutputModel = CollectionFactory.newList();
    	for (HashMap<String,String> map: results) {
			campaignOutputModel.add(new OptionModelImpl(map.get("campaignName"), map.get("id")));
		}
		return new SelectModelImpl(null,campaignOutputModel);
    }
    
	/**
	 * 得到文件路径
	 * @param campaignId
	 * @return
	 */
	private String getFilePath(String realPath, int campaignId) {
		String filePath = realPath+"report"+File.separator+campaignId;
		File path = new File(filePath);
		if (!path.isDirectory())
			path.mkdir();
		return "report"+File.separator+campaignId;
	}
	
	/**
	 * 得到数据结果
	 * @param jdbcTemplate
	 * @param cpcExcelOutput
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<HashMap<Integer, String>> getResult(JdbcTemplate jdbcTemplate,
			final ExcelOutput cpcExcelOutput, String sql) {
		List<HashMap<Integer,String>> results = jdbcTemplate.query(sql,new RowMapper(){
			public Object mapRow(ResultSet rs, int index) throws SQLException {
				String []sqlFields = cpcExcelOutput.getSqlFields().split("\\^");
				Map u = new HashMap<Integer,String>();
				for (int i=0;i<sqlFields.length;i++) {
					u.put(i, rs.getString(sqlFields[i]));
				}
				return u;
		    }
		});
		return results;
	}
	
	/**
	 * 得到Excel表头
	 * @param cpcExcelOutput
	 * @return
	 */
	private List<QueryField> getOutputList(final ExcelOutput cpcExcelOutput) {
		String []title = cpcExcelOutput.getExcelFields().split("\\^");
		List <QueryField>outputList = new ArrayList<QueryField>();
		for (int i=0;i<title.length;i++){
			QueryField queryField = new QueryField();
			queryField.setName(title[i]);
			queryField.setExportName("field"+i);
			outputList.add(queryField);
		}
		return outputList;
	}
	
	/**
	 * 得到导出Model
	 * @param cpcExcelOutput
	 * @param results
	 * @return
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private List<ExportField> initDatas(final ExcelOutput cpcExcelOutput,
			List<HashMap<Integer, String>> results)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		List <ExportField>datas = new ArrayList<ExportField>();
		for (int i=0;i<results.size();i++){
			Map<Integer, String> hashmap = results.get(i);
			ExportField exportField = new ExportField();
			String []sqlFields = cpcExcelOutput.getSqlFields().split("\\^");
			for (int j=0;j<sqlFields.length;j++){
				Method method = exportField.getClass().getDeclaredMethod("setField"+j, String.class);
				method.invoke(exportField, hashmap.get(j));
			}
			datas.add(exportField);
		}
		return datas;
	}
}
