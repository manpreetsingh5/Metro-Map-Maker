/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.Transactions;

import djf.AppTemplate;
import mmm.data.Draggable;
import mmm.data.MetroMapData;
import mmm.gui.MapEditController;
import mmm.gui.MetroMapWorkspace;
import javafx.scene.paint.Color;
import jtps.jTPS_Transaction;

/**
 *
 * @author Manny
 */
public class BackgroundColorTransaction implements jTPS_Transaction {

    AppTemplate app;
    MetroMapData dataManager;
    MapEditController logoEditController;
    MetroMapWorkspace workspace;
    private Color initialColor;
    private Color finalColor;
    private Draggable shape;

    public BackgroundColorTransaction(AppTemplate app, Color initialColor, Color finalColor) {

        workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        this.shape = shape;
        this.initialColor = initialColor;
        this.finalColor = finalColor;
        dataManager = (MetroMapData) app.getDataComponent();
        logoEditController = new MapEditController(app);
    }

    @Override
    public void doTransaction() {
        dataManager.setBackgroundColor(finalColor);
        workspace.getBackgroundColorPicker().setValue(finalColor);

    }

    @Override
    public void undoTransaction() {
        dataManager.setBackgroundColor(initialColor);
        workspace.getBackgroundColorPicker().setValue(initialColor);
    }
}
