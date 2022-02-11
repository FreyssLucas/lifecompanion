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

package org.lifecompanion.config.view.pane.tabs.style2.part;

import org.fxmisc.easybind.EasyBind;
import org.lifecompanion.model.api.style.TextDisplayerStyleUserI;
import org.lifecompanion.config.data.control.SelectionController;
import org.lifecompanion.config.view.pane.tabs.style2.view.shape.ShapeStyleEditView;
import org.lifecompanion.config.view.reusable.ribbonmenu.RibbonBasePart;
import org.lifecompanion.framework.commons.translation.Translation;
import org.lifecompanion.framework.commons.ui.LCViewInitHelper;

public class TextDisplayerShapeStyleRibbonPart extends RibbonBasePart<TextDisplayerStyleUserI> implements LCViewInitHelper {

    private ShapeStyleEditView shapeStyleEditView;

    public TextDisplayerShapeStyleRibbonPart() {
        this.initAll();
    }

    @Override
    public void initUI() {
        this.shapeStyleEditView = new ShapeStyleEditView();
        this.setTitle(Translation.getText("style.ribbon.part.textdisplayer.shape.style"));
        this.setContent(this.shapeStyleEditView);
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initBinding() {
        EasyBind.subscribe(SelectionController.INSTANCE.selectedComponentBothProperty(), (c) -> {
            if (c instanceof TextDisplayerStyleUserI) {
                this.model.set((TextDisplayerStyleUserI) c);
            } else {
                this.model.set(null);
            }
        });
    }

    @Override
    public void bind(final TextDisplayerStyleUserI model) {
        this.shapeStyleEditView.modelProperty().set(model.getTextDisplayerShapeStyle());
    }

    @Override
    public void unbind(final TextDisplayerStyleUserI model) {
        this.shapeStyleEditView.modelProperty().set(null);
    }
}
