// Copyright 2008 Shing Hing Man
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on
// an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
// express or implied. See the License for the specific language
// governing permissions and limitations under the License.

package net.sf.lombok.components;

import java.util.ArrayList;
import java.util.List;

import net.sf.lombok.componenthelpers.DynamicSelectValueEncoder;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.SelectModelVisitor;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.BeforeRenderTemplate;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.corelib.mixins.RenderDisabled;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.internal.util.SelectModelRenderer;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.Request;

@IncludeJavaScriptLibrary(value = { "classpath:/net/sf/lombok/components/DynamicSelect.js" })
@SuppressWarnings("unchecked")
/**
 * A clone of the Select component. The drop down list of this select depends
 * on the selected value of the parent. This select gets the index of selected
 * option of the parent from Request. (Sign!) 
 * 
 * Note that this component must be placed before a ChildSelect in the html
 * template.
 * @author Shing Hing Man
 */
public class ChildSelect extends AbstractField {

	// Since option group is not catered for, we could have rendered the select html manually.
	private class Renderer extends SelectModelRenderer {

		public Renderer(MarkupWriter writer) {
			super(writer, getEncoder());
		}

		@Override
		protected boolean isOptionSelected(OptionModel optionModel, String clientValue) {
			return false;
		}
	}

	// A special encoder is required instead of user supply.
	private ValueEncoder encoder;

	/**
	 * A list models available to this select. The size of this list should be equal to the size of the parent list.
	 */
	@Parameter(required = true)
	private List<SelectModel> models;

	/**
	 * The select component that drives this selection list.
	 */
	@Parameter(name = "parent", required = true, defaultPrefix = "component")
	private ParentSelect parent;

	/**
	 * defaultOptions[i] is the default option for models[i].
	 */
	@Parameter(name = "defaultOptions", required = false, defaultPrefix = "prop")
	private List<OptionModel> defaultOptions;

	@Inject
	private Request request;

	@Inject
	private ComponentResources resources;

	/**
	 * The value to read or update. It will be a OptionModel.value.
	 */
	@Parameter(required = true, principal = true)
	private Object value;

	@SuppressWarnings("unused")
	@Mixin
	private RenderDisabled renderDisabled;

	@Inject
	private RenderSupport renderSupport;

	private Integer selectedIndexParent;

	@Override
	protected void processSubmission(String elementName) {
		String submittedValue = request.getParameter(elementName);

		if (InternalUtils.isBlank(submittedValue)) {
			value = null;
		} else {
			value = ((OptionModel) getEncoder().toValue(submittedValue)).getValue();
		}
	}

	void afterRender(MarkupWriter writer) {
		writer.end();
		// Generate the javascript to update model of this (child) select when
		// the parent change values.
		String childId = resources.getId();
		String parentId = parent.getClientId();
		String modelsId = parentId + childId;
		StringBuffer modelsSB = new StringBuffer("var " + modelsId + "=[");

		for (int i = 0; i < models.size(); i++) {
			SelectModel selectModel = models.get(i);
			String clientId = renderSupport.allocateClientId(childId);
			String arrayId = clientId + "A";
			String selectModelId = clientId + "Model";
			renderSupport.addScript(generateJS(selectModel, arrayId), new Object[0]);

			OptionModel defaultOption = null;

			if ((value != null) && (selectedIndexParent == i)) {
				defaultOption = getOptionModel(selectModel.getOptions(), value);
			} else {
				if (defaultOptions == null) {
					defaultOption = selectModel.getOptions().get(0);
				} else {
					defaultOption = defaultOptions.get(i);
				}
			}

			int indexOfDefaultOption = getIndexOf(selectModel.getOptions(), defaultOption);
			String str = selectModelId + "=new SelectionModel(" + arrayId + ",new OptionModel(\"" + indexOfDefaultOption + "\",\""
					+ defaultOption.getLabel() + "\"));";

			renderSupport.addScript(str, new Object[0]);

			modelsSB.append(selectModelId);
			if (i < models.size() - 1) {
				modelsSB.append(",");
			}

		}
		modelsSB.append("];");
		renderSupport.addScript(modelsSB.toString(), new Object[0]);
		String controllerStr = "new ChildListController(\"" + parentId + "\",\"" + childId + "\"," + modelsId + ").setChildList();";
		renderSupport.addScript(controllerStr, new Object[0]);
	}

	void beginRender(MarkupWriter writer) {
		writer.element("select", "name", getControlName(), "id", getClientId());

		resources.renderInformalParameters(writer);

	}

	Binding defaultValue() {
		return createDefaultParameterBinding("value");
	}

	/**
	 * Renders the options
	 */
	@BeforeRenderTemplate
	void options(MarkupWriter writer) {

		SelectModelVisitor renderer = new Renderer(writer);

		// Render an empty select. Let javascript to fill in options.
		SelectModel model = new SelectModelImpl(null, new ArrayList<OptionModel>());
		model.visit(renderer);

	}

	void setValue(Object value) {
		this.value = value;
	}

	protected ValueEncoder getEncoder() {
		// The encoder depends on the selected index in the parent.
		if (encoder == null) {

			selectedIndexParent = 0;
			Integer index = (Integer) request.getAttribute(parent.getSelectedIndexKey());
			if (index != null && index > -1) {
				selectedIndexParent = index;
			}

			SelectModel model = models.get(selectedIndexParent);

			encoder = new DynamicSelectValueEncoder(model);
		}
		return encoder;
	}

	/**
	 * 
	 * @param selectModel
	 * @param varName
	 * @return A javascript to create an array of OptionModel in selectModel. Eg var name=[new OptionModel("0", "X"), new OptionModel("1",
	 *         "Y")];
	 */
	protected String generateJS(SelectModel selectModel, String varName) {
		StringBuffer sb = new StringBuffer("var " + varName + "= new Array(");
		List<OptionModel> options = selectModel.getOptions();

		for (int i = 0; i < options.size(); i++) {
			OptionModel optionModel = options.get(i);
			sb.append("new OptionModel(\"" + i + "\",\"" + optionModel.getLabel() + "\")");
			if (i < options.size() - 1) {
				sb.append(",");
			}
		}
		sb.append(");");

		return sb.toString();

	}

	/**
	 * 
	 * @param options
	 *            A list of OptionModels
	 * @param value
	 * @return The OptionModel in options with OptionModel.value=value.
	 */
	protected OptionModel getOptionModel(List<OptionModel> options, Object value) {
		OptionModel optionModel = null;
		for (int i = 0; i < options.size(); i++) {
			OptionModel tempOption = options.get(i);
			if (value.equals(tempOption.getValue())) {
				optionModel = tempOption;
				break;
			}
		}
		return optionModel;

	}

	/**
	 * 
	 * @param options
	 *            A list of OptionModels
	 * @param optionModel
	 * @return i where options[i].value=optionModel.value
	 */
	protected int getIndexOf(List<OptionModel> options, OptionModel optionModel) {
		int index = -1;
		for (int i = 0; i < options.size(); i++) {
			OptionModel tempOption = options.get(i);
			if (optionModel.getValue().equals(tempOption.getValue())) {
				index = i;
				break;
			}
		}
		return index;

	}
}
