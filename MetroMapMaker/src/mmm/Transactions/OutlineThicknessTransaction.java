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
import javafx.scene.shape.Shape;
import jtps.jTPS_Transaction;

/**
 *
 * @author Manny
 */
public class OutlineThicknessTransaction implements jTPS_Transaction {

    AppTemplate app;
    MetroMapData dataManager;
    MapEditController logoEditController;
    MetroMapWorkspace workspace;
    private int initialThickness;
    private int finalThickness;
    private Draggable shape;

    public OutlineThicknessTransaction(AppTemplate app,Draggable shape, int initialThickness, int finalThickness) {
        //app = MetroMapWorkspace.getApp();
        workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        this.shape = shape;
        this.initialThickness = initialThickness;
        this.finalThickness = initialThickness;
        dataManager = (MetroMapData) app.getDataComponent();
        logoEditController = new MapEditController(app);
    }

    @Override
    public void doTransaction() {
	    ((Shape)shape).setStrokeWidth(finalThickness);
            //workspace.getOutlineThicknessSlider().setValue(finalThickness);
        //((Shape)shape).setStrokeWidth(finalThickness);

    }

    @Override
    public void undoTransaction() {
        ((Shape)shape).setStrokeWidth(initialThickness);
        //workspace.getOutlineThicknessSlider().setValue(initialThickness);
    }
}
