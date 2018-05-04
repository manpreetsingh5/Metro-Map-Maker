/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.Transactions;

import djf.AppTemplate;
import mmm.data.DraggableLine;
import mmm.data.MetroMapData;
import mmm.gui.MapEditController;
import mmm.gui.MetroMapWorkspace;
import jtps.jTPS;
import jtps.jTPS_Transaction;

/**
 *
 * @author Manny
 */
public class removeLineTransaction implements jTPS_Transaction   {
    AppTemplate app;
    MetroMapData dataManager;
    private DraggableLine newLine;
    int index;
    
    public removeLineTransaction(AppTemplate app,DraggableLine newLine) {
        this.app = app;
        this.newLine = newLine;
        MetroMapWorkspace workspace = ((MetroMapWorkspace)app.getWorkspaceComponent());
        this.index = workspace.getMetroLines().getItems().indexOf(newLine.name);
        //dataManager = (golData) app.getDataComponent();
    }
    @Override
    //public void doTransaction() {}
    public void doTransaction() {
        //dataManager.addNode(newLine);
        MetroMapWorkspace workspace = ((MetroMapWorkspace)app.getWorkspaceComponent());
        workspace.getCanvas().getChildren().remove(newLine.frontStation);
        workspace.getCanvas().getChildren().remove(newLine.frontText);
        workspace.getCanvas().getChildren().remove(newLine.endStation);
        workspace.getCanvas().getChildren().remove(newLine.endText);
        workspace.getCanvas().getChildren().remove(newLine);
        workspace.getMetroLines().getItems().remove(newLine.name);
    }
    @Override
    //public void undoTransaction() {}
    public void undoTransaction() {
        MetroMapWorkspace workspace = ((MetroMapWorkspace)app.getWorkspaceComponent());
        workspace.getCanvas().getChildren().add(newLine.frontStation);
        workspace.getCanvas().getChildren().add(newLine.frontText);
        workspace.getCanvas().getChildren().add(newLine.endStation);
        workspace.getCanvas().getChildren().add(newLine.endText);
        workspace.getCanvas().getChildren().add(newLine);
        workspace.getMetroLines().getItems().add(newLine.name);
    }
}
