package org.lifecompanion.plugin.ppp.actions;

import org.lifecompanion.controller.lifecycle.AppModeController;
import org.lifecompanion.model.api.categorizedelement.useaction.UseActionEvent;
import org.lifecompanion.model.api.categorizedelement.useaction.UseActionTriggerComponentI;
import org.lifecompanion.model.api.usevariable.UseVariableI;
import org.lifecompanion.model.impl.categorizedelement.useaction.SimpleUseActionImpl;
import org.lifecompanion.plugin.ppp.actions.categories.PPPActionSubCategories;
import org.lifecompanion.plugin.ppp.services.RecordsService;
import org.lifecompanion.plugin.ppp.services.UserDatabaseService;

import java.util.Map;

public class SyncDatabasesAction extends SimpleUseActionImpl<UseActionTriggerComponentI> {
    public SyncDatabasesAction() {
        super(UseActionTriggerComponentI.class);
        this.nameID = "sync_database";
        this.staticDescriptionID = "ppp.plugin.actions.show_records.description";
        this.category = PPPActionSubCategories.VARIOUS;
        this.order = 20;
        this.parameterizableAction = false;
        this.variableDescriptionProperty().set(this.getStaticDescription());
    }

    @Override
    public String getConfigIconPath() {
        return "actions/icon_show_records.png";
    }

    @Override
    public void execute(final UseActionEvent event, final Map<String, UseVariableI<?>> variables) {
        UserDatabaseService.INSTANCE.syncDatabases();
    }
}
