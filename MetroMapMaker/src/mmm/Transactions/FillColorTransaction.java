/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.Transactions;

import djf.AppTemplate;
import mmm.data.Draggable;
import mmm.data.DraggableStation;
import mmm.data.DraggableLine;
import mmm.data.DraggableText;
import mmm.data.MetroMapData;
import mmm.gui.MapEditController;
import mmm.gui.MetroMapWorkspace;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import jtps.jTPS_Transaction;

/**
 *
 * @author Manny
 */
public class FillColorTransaction implements jTPS_Transaction {

    AppTemplate app;
    MetroMapData dataManager;
    MapEditController logoEditController;
    MetroMapWorkspace workspace;
    private Color initialColor;
    private Color finalColor;
    private Draggable shape;

    public FillColorTransaction(AppTemplate app,Draggable shape, Color initialColor, Color finalColor) {

        //app = MetroMapWorkspace.getApp();
        workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        this.shape = shape;
        this.initialColor = initialColor;
        this.finalColor = finalColor;
        dataManager = (MetroMapData) app.getDataComponent();
        logoEditController = new MapEditController(app);
    }

    @Override
    public void doTransaction() {
        Draggable selectedShape = (Draggable) dataManager.getSelectedNode();

        if (shape instanceof DraggableLine) {
            ((DraggableLine) shape).setFill(finalColor);
        } else if (shape instanceof DraggableStation) {
            ((DraggableStation) shape).setFill(finalColor);
        } else if (shape instanceof DraggableText) {
            ((DraggableText) shape).setFill(finalColor);
        }

    }

    @Override
    public void undoTransaction() {
        Draggable selectedShape = (Draggable) dataManager.getSelectedNode();
        if (shape instanceof DraggableLine) {
            ((DraggableLine) shape).setFill(initialColor);
        } else if (shape instanceof DraggableStation) {
            ((DraggableStation) shape).setFill(initialColor);
        } else if (shape instanceof DraggableText) {
            ((DraggableText) shape).setFill(initialColor);
        }
    }
}
