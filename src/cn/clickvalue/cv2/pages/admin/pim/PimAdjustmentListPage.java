package cn.clickvalue.cv2.pages.admin.pim;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.StreamResponse.XLSAttachment;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.common.util.ExcelUtils;
import cn.clickvalue.cv2.common.util.FileUploadUtils;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.PimAdjustment;
import cn.clickvalue.cv2.model.PimReportSummary;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.PimAdjustmentService;
import cn.clickvalue.cv2.services.logic.PimReportSummaryService;
import cn.clickvalue.cv2.services.logic.UserService;

public class PimAdjustmentListPage extends BasePage {

	private static final Map<String, Integer> SIGN_MAP;
	static {
		SIGN_MAP = new HashMap<String, Integer>();
		SIGN_MAP.put("+", 1);
		SIGN_MAP.put("-", -1);
		SIGN_MAP.put("＋", 1);
		SIGN_MAP.put("－", -1);
	}

	@Property
	private PimAdjustment pimAdjustment;

	@Property
	@Persist
	private String formAffiliateName;

	@Property
	@Persist
	private Date formStartDate;

	@Property
	@Persist
	private Date formEndDate;

	@Property
	private UploadedFile file;

	@Property
	@Inject
	private BeanModelSource beanModelSource;

	private GridDataSource dataSource;

	private BeanModel<PimAdjustment> beanModel;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private PimAdjustmentService pimAdjustmentService;

	@Inject
	private PimReportSummaryService pimReportSummaryService;

	@Inject
	private UserService userService;

	@Component
	private Form uploadForm;

	@Component
	private Form uploadEditForm;

	@Component(parameters = { "source=dataSource", "row=pimAdjustment", "model=beanModel", "pagerPosition=literal:bottom",
			"rowsPerPage=noOfRowsPerPage" })
	private Grid grid;

	void setupRender() {
		dataSource = new HibernateDataSource(pimAdjustmentService, getQuery());
		beanModel = beanModelSource.create(PimAdjustment.class, true, componentResources);
		beanModel.add("affiliate.name").label("网站主").sortable(false);
		beanModel.get("bonusValue").label("奖惩积分").sortable(false);
		beanModel.get("description").label("描述").sortable(false);
		beanModel.get("createdAt").label("创建时间").sortable(true);
		beanModel.add("operate", null).label("操作").sortable(false);
		beanModel.include("affiliate.name", "bonusValue", "description", "createdAt", "operate");
	}

	private CritQueryObject getQuery() {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("affiliate", "affiliate", Criteria.LEFT_JOIN);
		c.addCriterion(Restrictions.eq("bonusWay", 0));
		if (StringUtils.isNotEmpty(formAffiliateName)) {
			c.addCriterion(Restrictions.like("affiliate.name", formAffiliateName, MatchMode.ANYWHERE));
		}
		if (formStartDate != null) {
			c.addCriterion(Restrictions.ge("createdAt", formStartDate));
		}
		if (formEndDate != null) {
			c.addCriterion(Restrictions.le("createdAt", formEndDate));
		}
		c.addOrder(Order.desc("updatedAt"));
		return c;
	}

	public String getFormatBonusValue() {
		float value = pimAdjustment.getBonusValue();
		return (value > 0 ? "奖" : value == 0 ? "" : "罚").concat(String.valueOf(Math.abs(pimAdjustment.getBonusValue())));
	}

	void onSubmitFromSearchForm() {
		if (formEndDate != null) {
			formEndDate = DateUtils.addSeconds(formEndDate, 86399);// 24*60*60-1
		}
		grid.reset();
	}

	Object onSubmitFromUploadForm() {
		if (file == null) {
			uploadForm.recordError("文件不能为空");
			return this;
		}
		try {
			String[] types = { ".xls" };
			String fileName = FileUploadUtils.upload(file, "proxy", types);
			List<Map<String, String>> rows = ExcelUtils.readExl("excel/pimAdjustmentNew.properties", fileName);
			List<PimAdjustment> adjustments = new ArrayList<PimAdjustment>();
			for (int i = 0; i < rows.size(); i++) {
				Map<String, String> row = rows.get(i);
				String rAffiliateName = row.get("affiliateName");
				String rSign = row.get("sign");
				String rBonusValue = row.get("bonusValue");
				String rDescription = row.get("description");

				User affiliate = null;
				int sign = 0;
				long bonusValue = 0L;

				if (StringUtils.isEmpty(rAffiliateName)) {
					uploadForm.recordError(String.format("第%d行:网站名不能为空", i + 1));
				} else {
					affiliate = userService.getUserByName(rAffiliateName);
					if (affiliate == null || affiliate.getDeleted() == 1 || affiliate.getUserGroup().getId() != 2) {
						uploadForm.recordError(String.format("第%d行:该网站主不存在", i + 1));
					}
				}
				if (StringUtils.isEmpty(rSign)) {
					uploadForm.recordError(String.format("第%d行:奖惩不能为空", i + 1));
				} else {
					sign = SIGN_MAP.get(rSign) == null ? 0 : SIGN_MAP.get(rSign);
					if (sign == 0) {
						uploadForm.recordError(String.format("第%d行:无法识别奖惩标志", i + 1));
					}
				}
				if (StringUtils.isEmpty(rBonusValue)) {
					uploadForm.recordError(String.format("第%d行:积分值不能为空", i + 1));
				} else {
					bonusValue = NumberUtils.toLong(rBonusValue, 0L);
					if (bonusValue <= 0) {
						uploadForm.recordError(String.format("第%d行:积分值必须为一个数字,且大于0", i + 1));
					}
				}

				if (affiliate == null || Math.abs(sign) != 1 || bonusValue < 0) {
					continue;
				}

				// 验证惩罚后会不会出现负的总积分
				if (sign == -1) {
					long totalPoints = 0L;
					long newPoints = bonusValue;
					List<PimReportSummary> summaries = pimReportSummaryService.findBy("affiliate.id", affiliate.getId());
					if (summaries.size() > 0) {
						totalPoints = summaries.get(0).getPoints();
					}
					long finalPoints = totalPoints - newPoints;
					if (finalPoints < 0) {
						String s = "第%d行:操作后，网站主%s,总积分(%d)- 惩罚积分(%d)=%d小于0";
						uploadForm.recordError(String.format(s, i + 1, affiliate.getName(), totalPoints, newPoints, finalPoints));
					}
				}

				PimAdjustment adjustment = pimAdjustmentService.createPimAdjustment();
				adjustment.setAffiliate(affiliate);
				adjustment.setBonusValue(bonusValue * sign);
				adjustment.setDescription(StringUtils.trimToEmpty(rDescription));
				adjustments.add(adjustment);
			}
			if (!uploadForm.isValid()) {
				return this;
			}
			for (PimAdjustment pimAdjustment : adjustments) {
				pimAdjustmentService.addPimAdjustment(pimAdjustment.getAffiliate().getId(), pimAdjustment.getBonusValue(), pimAdjustment
						.getDescription());
			}
			addInfo("导入成功", false);
		} catch (Exception e) {
			addError(uploadForm, "导入失败，请检查EXCEL....", false);
		}
		return this;
	}

	Object onSubmitFromUploadEditForm() {
		if (file == null) {
			uploadEditForm.recordError("文件不能为空");
			return this;
		}
		try {
			String[] types = { ".xls" };
			String fileName = FileUploadUtils.upload(file, "proxy", types);
			List<Map<String, String>> rows = ExcelUtils.readExl("excel/pimAdjustmentEdit.properties", fileName);
			List<PimAdjustment> adjustments = new ArrayList<PimAdjustment>();
			for (int i = 0; i < rows.size(); i++) {
				Map<String, String> row = rows.get(i);
				String rId = row.get("id");
				String rAffiliateName = row.get("affiliateName");
				String rSign = row.get("sign");
				String rBonusValue = row.get("bonusValue");
				String rDescription = row.get("description");

				PimAdjustment adjustment = null;
				int id = 0;
				User affiliate = null;
				int sign = 0;
				long bonusValue = 0L;

				if (StringUtils.isEmpty(rId)) {
					uploadEditForm.recordError(String.format("第%d行:id必须存在", i + 1));
				} else {
					id = NumberUtils.toInt(rId, 0);
					if (id <= 0) {
						uploadEditForm.recordError(String.format("第%d行:ID必须是数字，且大于0", i + 1));
					} else {
						adjustment = pimAdjustmentService.get(id, 0);
						if (adjustment == null) {
							uploadEditForm.recordError(String.format("第%d行:无法根据ID找到对应奖惩记录", i + 1));
						}
					}
				}
				if (StringUtils.isEmpty(rAffiliateName)) {
					uploadEditForm.recordError(String.format("第%d行:网站主不能为空", i + 1));
				} else {
					affiliate = userService.getUserByName(rAffiliateName);
					if (affiliate == null || affiliate.getDeleted() == 1 || affiliate.getUserGroup().getId() != 2) {
						uploadEditForm.recordError(String.format("第%d行:该网站主不存在", i + 1));
						affiliate = null;
					}
				}
				if (!NumberUtils.isDigits(rBonusValue)) {
					uploadEditForm.recordError(String.format("第%d行:积分值必须为整数", i + 1));
				} else {
					bonusValue = NumberUtils.toLong(rBonusValue, 0L);
					if (bonusValue < 0) {
						uploadEditForm.recordError(String.format("第%d行:积分值必须大于等于0", i + 1));
					}
				}
				
				if(bonusValue != 0){
					if (StringUtils.isEmpty(rSign)) {
						uploadEditForm.recordError(String.format("第%d行:奖惩标志不能为空", i + 1));
					} else {
						sign = SIGN_MAP.get(rSign) == null ? 0 : SIGN_MAP.get(rSign);
						if (sign == 0) {
							uploadEditForm.recordError(String.format("第%d行:无法识别奖惩标志", i + 1));
						}
					}
				}

				if (adjustment == null || affiliate == null || Math.abs(sign) != 1 || bonusValue < 0) {
					continue;
				}

				// 验证修改后会不会出现负的总积分
				User oldAffiliate = adjustment.getAffiliate();
				if (!affiliate.equals(oldAffiliate)) {
					long totalPoints = 0L;
					long returnPoints = adjustment.getBonusValue();
					List<PimReportSummary> summaries = pimReportSummaryService.findBy("affiliate.id", oldAffiliate.getId());
					if (summaries.size() > 0) {
						totalPoints = summaries.get(0).getPoints();
					}
					long finalPoints = totalPoints - returnPoints;
					if (totalPoints - adjustment.getBonusValue() < 0) {
						String s = "第%d行:操作后，网站主%s,总积分(%d)-修改前积分(%d)=%d小于0";
						uploadEditForm.recordError(String.format(s, i + 1, oldAffiliate.getName(), totalPoints, returnPoints, finalPoints));
					}
				}
				if (sign == -1) {
					long totalPoints = 0L;
					long returnPoints = 0L;
					long newPoints = bonusValue * sign;
					List<PimReportSummary> summaries = pimReportSummaryService.findBy("affiliate.id", affiliate.getId());
					if (summaries.size() > 0) {
						totalPoints = summaries.get(0).getPoints();
					}
					if (affiliate.equals(oldAffiliate)) {
						returnPoints = adjustment.getBonusValue();
					}
					long finalPoints = totalPoints - returnPoints + newPoints;
					if (finalPoints < 0) {
						String s = "第%d行:操作后，网站主%s,总积分(%d)-修改前积分(%d)+修改后积分(%d)=%d小于0";
						uploadEditForm.recordError(String.format(s, i + 1, affiliate.getName(), totalPoints, returnPoints, newPoints,
								finalPoints));
					}
				}

				adjustment = pimAdjustmentService.createPimAdjustment();
				adjustment.setId(id);
				adjustment.setAffiliate(affiliate);
				adjustment.setBonusValue(bonusValue * sign);
				adjustment.setBonusWay(0);
				adjustment.setDescription(StringUtils.trimToEmpty(rDescription));
				adjustments.add(adjustment);
			}
			if (!uploadEditForm.isValid()) {
				return this;
			}
			for (PimAdjustment pimAdjustment : adjustments) {
				pimAdjustmentService.updatePimAdjustment(pimAdjustment.getId(), pimAdjustment.getAffiliate().getId(), pimAdjustment
						.getBonusValue(), pimAdjustment.getDescription());
			}
			addInfo("导入成功", false);
		} catch (Exception e) {
			addError(uploadEditForm, "导入失败，请检查EXCEL....", false);
		}
		return this;
	}

	Object onExportResult() {
		List<PimAdjustment> list = pimAdjustmentService.find(getQuery());
		if (list == null || list.size() == 0) {
			addInfo("没有需要导出的数据", false);
			return this;
		} else {
			String template = "pimAdjustment";
			String outName = template.concat(String.valueOf(System.currentTimeMillis()));
			try {
				String target = ExcelUtils.mergerXLS(list, template, outName);
				FileInputStream fileInputStream = new FileInputStream(target);
				return new XLSAttachment(fileInputStream, template);
			} catch (Exception e) {
				addError("导出错误，请联系技术", false);
				return this;
			}
		}
	}

	Object onExportTemplate() {
		return new XLSAttachment(this.getClass().getResourceAsStream("pimAdjust.xls"), "pimAdjust.xls");
	}

	public BeanModel<PimAdjustment> getBeanModel() {
		return beanModel;
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}

	void cleanupRender() {
		uploadForm.clearErrors();
	}
}
