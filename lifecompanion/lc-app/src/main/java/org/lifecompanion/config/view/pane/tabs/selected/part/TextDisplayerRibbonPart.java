/*
 * LifeCompanion AAC and its sub projects
 *
 * Copyright (C) 2014 to 2019 Mathieu THEBAUD
 * Copyright (C) 2020 to 2021 CMRRF KERPAPE (Lorient, France)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.lifecompanion.config.view.pane.tabs.selected.part;

import org.controlsfx.control.ToggleSwitch;

import org.lifecompanion.model.api.configurationcomponent.WriterDisplayerI;
import org.lifecompanion.framework.commons.translation.Translation;
import org.lifecompanion.framework.commons.ui.LCViewInitHelper;
import org.lifecompanion.config.data.action.impl.TextDisplayerActions.SetEnableImageAction;
import org.lifecompanion.config.data.action.impl.TextDisplayerActions.SetEnableWordWrapAction;
import org.lifecompanion.config.data.action.impl.TextDisplayerActions.SetImageHeightAction;
import org.lifecompanion.config.data.action.impl.TextDisplayerActions.SetLineSpacingAction;
import org.lifecompanion.config.data.common.LCConfigBindingUtils;
import org.lifecompanion.util.UIUtils;
import org.lifecompanion.model.api.configurationcomponent.DisplayableComponentI;
import org.lifecompanion.config.data.control.SelectionController;
import org.lifecompanion.model.api.ui.editmode.ConfigurationProfileLevelEnum;
import org.lifecompanion.config.view.common.ConfigUIUtils;
import org.lifecompanion.config.view.reusable.ribbonmenu.RibbonBasePart;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Pane to base properties of a {@link WriterDisplayerI}
 * @author Mathieu THEBAUD <math.thebaud@gmail.com>
 */
public class TextDisplayerRibbonPart extends RibbonBasePart<WriterDisplayerI> implements LCViewInitHelper {
	private Spinner<Double> spinnerLineSpacing, spinnerImageHeight;
	private ToggleSwitch toggleEnableImage, toggleEnableWordWrap;
	private ChangeListener<Number> changeListenerLineSpacing, changeListenerImageHeight;
	private ChangeListener<Boolean> changeListenerEnableImage, changeListenerEnableWordWrap;

	public TextDisplayerRibbonPart() {
		this.initAll();
	}

	@Override
	public void initUI() {
		this.setTitle(Translation.getText("pane.title.text.editor.lines"));

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10.0);
		gridPane.setVgap(3.0);
		//Line spacing
		Label labelLineSpacing = new Label(Translation.getText("text.displayer.line.spacing"));
		this.spinnerLineSpacing = UIUtils.createDoubleSpinner(0, 200, 2, 2, 75);
		UIUtils.createAndAttachTooltip(spinnerLineSpacing, "tooltip.explain.text.editor.line.spacing");
		gridPane.add(labelLineSpacing, 0, 0);
		gridPane.add(this.spinnerLineSpacing, 1, 0);
		GridPane.setHgrow(labelLineSpacing, Priority.ALWAYS);
		gridPane.add(new Separator(Orientation.HORIZONTAL), 0, 2, 2, 1);
		//Images
		this.toggleEnableImage = ConfigUIUtils.createToggleSwitch("text.displayer.enable.image", "tooltip.explain.text.editor.enable.image");
		gridPane.add(this.toggleEnableImage, 0, 3, 2, 1);
		this.spinnerImageHeight = UIUtils.createDoubleSpinner(0, 500, 2, 2, 75);
		UIUtils.createAndAttachTooltip(spinnerImageHeight, "tooltip.explain.text.editor.image.height");
		gridPane.add(new Label(Translation.getText("text.displayer.image.height")), 0, 4);
		gridPane.add(this.spinnerImageHeight, 1, 4);
		//Word wrap
		gridPane.add(new Separator(Orientation.HORIZONTAL), 0, 5, 2, 1);
		this.toggleEnableWordWrap = ConfigUIUtils.createToggleSwitch("text.displayer.enable.word.wrap", "tooltip.explain.text.editor.word.wrap");
		gridPane.add(this.toggleEnableWordWrap, 0, 6, 2, 1);

		this.setContent(gridPane);
	}

	@Override
	public void initListener() {
		this.changeListenerLineSpacing = LCConfigBindingUtils.createDoubleSpinnerBinding(this.spinnerLineSpacing, this.model,
				WriterDisplayerI::lineSpacingProperty, (model, nv) -> new SetLineSpacingAction(model, nv));
		this.changeListenerImageHeight = LCConfigBindingUtils.createDoubleSpinnerBindingWithCondition(this.spinnerImageHeight, this.model,
				WriterDisplayerI::imageHeightProperty, (model, nv) -> new SetImageHeightAction(model, nv), m -> m.enableImageProperty().get());
		this.changeListenerEnableImage = LCConfigBindingUtils.createSimpleBinding(this.toggleEnableImage.selectedProperty(), this.model,
				m -> m.enableImageProperty().get(), (model, nv) -> new SetEnableImageAction(model, nv));
		this.changeListenerEnableWordWrap = LCConfigBindingUtils.createSimpleBinding(this.toggleEnableWordWrap.selectedProperty(), this.model,
				m -> m.enableWordWrapProperty().get(), (model, nv) -> new SetEnableWordWrapAction(model, nv));
	}

	@Override
	public void initBinding() {
		SelectionController.INSTANCE.selectedComponentBothProperty()
				.addListener((ChangeListener<DisplayableComponentI>) (observableP, oldValueP, newValueP) -> {
					if (newValueP instanceof WriterDisplayerI) {
						this.model.set((WriterDisplayerI) newValueP);
					} else {
						this.model.set(null);
					}
				});
		this.spinnerImageHeight.disableProperty().bind(this.toggleEnableImage.selectedProperty().not());
		ConfigUIUtils.bindShowForLevelFrom(this, ConfigurationProfileLevelEnum.NORMAL);
	}

	@Override
	public void bind(final WriterDisplayerI component) {
		component.lineSpacingProperty().addListener(this.changeListenerLineSpacing);
		component.imageHeightProperty().addListener(this.changeListenerImageHeight);
		component.enableImageProperty().addListener(this.changeListenerEnableImage);
		component.enableWordWrapProperty().addListener(this.changeListenerEnableWordWrap);
		this.spinnerLineSpacing.getValueFactory().setValue(component.lineSpacingProperty().get());
		this.spinnerImageHeight.getValueFactory().setValue(component.imageHeightProperty().get());
		this.toggleEnableImage.setSelected(component.enableImageProperty().get());
		this.toggleEnableWordWrap.setSelected(component.enableWordWrapProperty().get());
	}

	@Override
	public void unbind(final WriterDisplayerI component) {
		component.lineSpacingProperty().removeListener(this.changeListenerLineSpacing);
		component.imageHeightProperty().removeListener(this.changeListenerImageHeight);
		component.enableImageProperty().removeListener(this.changeListenerEnableImage);
		component.enableWordWrapProperty().removeListener(this.changeListenerEnableWordWrap);
	}

}
