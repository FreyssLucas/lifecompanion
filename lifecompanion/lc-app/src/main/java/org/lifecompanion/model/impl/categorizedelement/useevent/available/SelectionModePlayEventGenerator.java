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

package org.lifecompanion.model.impl.categorizedelement.useevent.available;

import org.lifecompanion.model.api.configurationcomponent.LCConfigurationI;
import org.lifecompanion.controller.selectionmode.SelectionModeController;
import org.lifecompanion.model.impl.categorizedelement.useevent.BaseUseEventGeneratorImpl;
import org.lifecompanion.model.api.categorizedelement.useevent.DefaultUseEventSubCategories;
import javafx.beans.value.ChangeListener;

public class SelectionModePlayEventGenerator extends BaseUseEventGeneratorImpl {

	public SelectionModePlayEventGenerator() {
		super();
		this.parameterizableAction = false;
		this.order = 6;
		this.category = DefaultUseEventSubCategories.STATUS;
		this.nameID = "use.event.configuration.selection.mode.play.name";
		this.staticDescriptionID = "use.event.configuration.selection.mode.play.description";
		this.configIconPath = "configuration/icon_scanning_played.png";
		this.variableDescriptionProperty().set(this.getStaticDescription());
	}

	// Class part : "Mode start/stop"
	//========================================================================
	private ChangeListener<? super Boolean> changeListener;

	@Override
	public void modeStart(final LCConfigurationI configuration) {
		this.changeListener = (obs, ov, nv) -> {
			if (nv) {
				this.useEventListener.fireEvent(this, null, null);
			}
		};
		SelectionModeController.INSTANCE.playingProperty().addListener(this.changeListener);
	}

	@Override
	public void modeStop(final LCConfigurationI configuration) {
		SelectionModeController.INSTANCE.playingProperty().removeListener(this.changeListener);
	}
	//========================================================================

}
