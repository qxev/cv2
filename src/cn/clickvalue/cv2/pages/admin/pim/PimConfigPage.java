package cn.clickvalue.cv2.pages.admin.pim;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.util.DefaultPrimaryKeyEncoder;

import cn.clickvalue.cv2.DTO.PimAutoAdjustRuleDTO;
import cn.clickvalue.cv2.DTO.PimGradeDTO;
import cn.clickvalue.cv2.common.util.UUIDUtil;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.PimAutoAdjustRule;
import cn.clickvalue.cv2.model.PimGrade;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.PimAutoAdjustRuleService;
import cn.clickvalue.cv2.services.logic.PimGradeService;

public class PimConfigPage extends BasePage {

	@SuppressWarnings("unused")
	@Property
	private PimGradeDTO grade;

	@SuppressWarnings("unused")
	@Property
	private PimAutoAdjustRuleDTO rule;

	@Property
	@Persist
	private DefaultPrimaryKeyEncoder<String, PimGradeDTO> gradeEncoder;

	@Property
	@Persist
	private DefaultPrimaryKeyEncoder<String, PimAutoAdjustRuleDTO> ruleEncoder;

	@SuppressWarnings("unused")
	@Component(parameters = { "value=rule.actionType", "model=literal:-1=惩罚,0=清零,1=奖励", "blankOption=NEVER " })
	private Select action;

	@SuppressWarnings("unused")
	@Component(parameters = { "value=rule.actionValueType", "model=literal:0=数值,1=百分比", "blankOption=NEVER" })
	private Select valueType;

	@SuppressWarnings("unused")
	@Component(parameters = { "value=rule.condition", "model=literal:0=连续未活跃月,1=连续活跃月", "blankOption=NEVER" })
	private Select condition;

	@Inject
	private PimGradeService pimGradeService;

	@Inject
	private PimAutoAdjustRuleService pimAutoAdjustRuleService;

	@InjectPage
	private MessagePage messagePage;

	void setupRender() {
		gradeEncoder = new DefaultPrimaryKeyEncoder<String, PimGradeDTO>();
		List<PimGrade> grades = pimGradeService.findAll();
		for (PimGrade grade : grades) {
			PimGradeDTO grateDTO = new PimGradeDTO();
			grateDTO.setId(grade.getId());
			grateDTO.setGrade(grade.getGrade());
			grateDTO.setPoint(grade.getPoint());
			grateDTO.setRate(grade.getRate());
			grateDTO.setGradeName(StringUtils.trimToEmpty(grade.getGradeName()));
			grateDTO.setCreatedAt(grade.getCreatedAt());
			gradeEncoder.add(UUIDUtil.getUUID(), grateDTO);
		}

		ruleEncoder = new DefaultPrimaryKeyEncoder<String, PimAutoAdjustRuleDTO>();
		List<PimAutoAdjustRule> adjustRules = pimAutoAdjustRuleService.findAll();
		for (PimAutoAdjustRule adjustRule : adjustRules) {
			PimAutoAdjustRuleDTO rule = new PimAutoAdjustRuleDTO();
			rule.setId(adjustRule.getId());
			rule.setActionDesc(adjustRule.getActionDesc());
			rule.setActionType(adjustRule.getActionType());
			rule.setActionValue(adjustRule.getActionValue());
			rule.setActionValueType(adjustRule.getActionValueType());
			rule.setCreatedAt(adjustRule.getCreatedAt());
			if (adjustRule.getStateActiveMonth() != null && adjustRule.getStateActiveMonth() > 0) {
				rule.setCondition(1);
				rule.setConditionValue(adjustRule.getStateActiveMonth());
			} else {
				rule.setCondition(0);
				rule.setConditionValue(adjustRule.getStateUnActiveMonth());
			}
			ruleEncoder.add(UUIDUtil.getUUID(), rule);
		}
	}

	Object onAddRowFromGrade() {
		PimGradeDTO grade = new PimGradeDTO();
		grade.setGrade(gradeEncoder.getValues().size() + 1);
		grade.setPoint(0L);
		grade.setRate(0.00f);
		gradeEncoder.add(UUIDUtil.getUUID(), grade);
		return grade;
	}

	void onRemoveRowFromGrade(PimGradeDTO grade) {
		gradeEncoder.setDeleted(true);
	}

	Object onAddRowFromRule() {
		PimAutoAdjustRuleDTO rule = new PimAutoAdjustRuleDTO();
		rule.setActionDesc("");
		rule.setActionType(-1);
		rule.setActionValue(0L);
		rule.setActionValueType(0);
		rule.setCondition(0);
		rule.setConditionValue(0);
		ruleEncoder.add(UUIDUtil.getUUID(), rule);
		return rule;
	}

	void onRemoveRowFromRule(PimAutoAdjustRuleDTO rule) {
		ruleEncoder.setDeleted(true);
	}

	Object onSubmitFromGradeForm() {
		for (PimGradeDTO grade : gradeEncoder.getAllValues()) {
			PimGrade pGrade = new PimGrade();
			pGrade.setId(grade.getId());
			pGrade.setGrade(grade.getGrade());
			pGrade.setGradeName(StringUtils.trimToEmpty(grade.getGradeName()));
			pGrade.setPoint(grade.getPoint());
			pGrade.setRate(grade.getRate());
			pGrade.setCreatedAt(grade.getCreatedAt());
			gradeEncoder.toKey(grade);
			if (!gradeEncoder.isDeleted()) {
				pimGradeService.save(pGrade);
			} else {
				if (grade.getId() != null) {
					pimGradeService.delete(pGrade);
				}
			}
		}
		messagePage.setMessage("积分等级保存成功！");
		messagePage.setNextPage("admin/pim/configpage");
		return messagePage;
	}

	Object onSubmitFromRuleForm() {
		for (PimAutoAdjustRuleDTO rule : ruleEncoder.getAllValues()) {
			PimAutoAdjustRule adjustRule = new PimAutoAdjustRule();
			adjustRule.setId(rule.getId());
			adjustRule.setActionDesc(rule.getActionDesc());
			adjustRule.setActionType(rule.getActionType());
			adjustRule.setActionValue(rule.getActionValue());
			adjustRule.setActionValueType(rule.getActionValueType());
			adjustRule.setCreatedAt(rule.getCreatedAt());
			if (rule.getCondition() == 0) {
				adjustRule.setStateActiveMonth(0);
				adjustRule.setStateUnActiveMonth(rule.getConditionValue());
			} else {
				adjustRule.setStateActiveMonth(rule.getConditionValue());
				adjustRule.setStateUnActiveMonth(0);
			}
			ruleEncoder.toKey(rule);
			if (!ruleEncoder.isDeleted()) {
				pimAutoAdjustRuleService.save(adjustRule);
			} else {
				if (rule.getId() != null) {
					pimAutoAdjustRuleService.delete(adjustRule);
				}
			}
		}
		messagePage.setMessage("自动奖惩规则保存成功！");
		messagePage.setNextPage("admin/pim/configpage");
		return messagePage;
	}
}
