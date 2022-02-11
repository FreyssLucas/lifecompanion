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
import org.lifecompanion.model.api.categorizedelement.useevent.DefaultUseEventSubCategories;
import org.lifecompanion.controller.configurationcomponent.dynamickey.KeyListController;
import org.lifecompanion.model.impl.categorizedelement.useevent.BaseUseEventGeneratorImpl;

public class OnPreviousAndStartReachedEventGenerator extends BaseUseEventGeneratorImpl {

    public OnPreviousAndStartReachedEventGenerator() {
        super();
        this.parameterizableAction = false;
        this.order = 80;
        this.category = DefaultUseEventSubCategories.KEYLIST_MOVES;
        this.nameID = "use.event.on.previous.and.start.reached.event.name";
        this.staticDescriptionID = "use.event.on.previous.and.start.reached.event.description";
        this.configIconPath = "keylist/icon_previous_keylist_start_reached.png";
        this.variableDescriptionProperty().set(this.getStaticDescription());
    }

    // Class part : "Mode start/stop"
    //========================================================================
    private Runnable listener;

    @Override
    public void modeStart(final LCConfigurationI configuration) {
        this.listener = () -> {
            this.useEventListener.fireEvent(this, null, null);
        };
        KeyListController.INSTANCE.getPreviousWithoutLoopStartReachedListener().add(listener);
    }

    @Override
    public void modeStop(final LCConfigurationI configuration) {
        KeyListController.INSTANCE.getPreviousWithoutLoopStartReachedListener().remove(listener);
    }
    //========================================================================
}
