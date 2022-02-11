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
package org.lifecompanion.config.view.pane.config;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.lifecompanion.util.UIUtils;
import org.lifecompanion.base.data.config.LCGraphicStyle;
import org.lifecompanion.model.impl.plugin.PluginInfo;
import org.lifecompanion.controller.plugin.PluginManager;
import org.lifecompanion.base.view.reusable.GeneralConfigurationStepViewI;
import org.lifecompanion.config.data.action.impl.PluginActions;
import org.lifecompanion.config.data.config.LCGlyphFont;
import org.lifecompanion.config.data.control.ConfigActionController;
import org.lifecompanion.framework.commons.translation.Translation;
import org.lifecompanion.framework.commons.ui.LCViewInitHelper;

public class PluginConfigSubmenu extends BorderPane implements LCViewInitHelper, UserConfigSubmenuI {
    private Button buttonAddPlugin, buttonAddPluginFromWeb;
    private ListView<PluginInfo> listViewPlugins;

    public PluginConfigSubmenu() {
        initAll();
    }

    @Override
    public void initUI() {
        Label labelPluginList = UIUtils.createTitleLabel("general.configuration.view.step.plugin.list.plugin.label");

        listViewPlugins = new ListView<>(PluginManager.INSTANCE.getPluginInfoList());
        listViewPlugins.setCellFactory(lv -> new PluginInfoListCell(lv));

        VBox boxCenter = new VBox(10.0, labelPluginList, listViewPlugins);

        buttonAddPlugin = UIUtils.createRightTextButton(Translation.getText("general.configuration.view.step.plugin.add.button"),
                LCGlyphFont.FONT_AWESOME.create(FontAwesome.Glyph.PLUS_CIRCLE).size(22).color(LCGraphicStyle.MAIN_DARK), "TODO");
        buttonAddPlugin.getStyleClass().add("button-icon-text-bigger");

        buttonAddPluginFromWeb = UIUtils.createRightTextButton(Translation.getText("general.configuration.view.step.plugin.add.from.repo.button"),
                LCGlyphFont.FONT_AWESOME.create(FontAwesome.Glyph.PLUS_CIRCLE).size(22).color(LCGraphicStyle.MAIN_DARK), "TODO");
        buttonAddPluginFromWeb.getStyleClass().add("button-icon-text-bigger");

        VBox boxButtons = new VBox(10.0, buttonAddPlugin, buttonAddPluginFromWeb);
        boxButtons.setAlignment(Pos.CENTER);
        BorderPane.setMargin(boxButtons, new Insets(0, 10.0, 10.0, 10.0));

        this.setPadding(new Insets(GeneralConfigurationStepViewI.PADDING));
        this.setCenter(boxCenter);
        this.setBottom(boxButtons);
    }

    @Override
    public void initListener() {
        buttonAddPlugin.setOnAction(e -> ConfigActionController.INSTANCE.executeAction(new PluginActions.AddPluginAction(buttonAddPlugin)));
        buttonAddPluginFromWeb.setOnAction(e -> ConfigActionController.INSTANCE.executeAction(new PluginActions.AddPluginFromWeb(buttonAddPluginFromWeb)));
    }

    @Override
    public void initBinding() {
    }

    @Override
    public String getTabTitleId() {
        return "general.configuration.view.step.plugin.list.title";
    }

    @Override
    public void updateFields() {
    }

    @Override
    public void updateModel() {
    }

    @Override
    public Region getView() {
        return this;
    }
}
