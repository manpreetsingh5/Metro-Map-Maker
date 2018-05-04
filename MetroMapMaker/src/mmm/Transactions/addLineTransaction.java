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
public class addLineTransaction implements jTPS_Transaction   {
    AppTemplate app;
    MetroMapData dataManager;
    private DraggableLine newLine;
    
    public addLineTransaction(AppTemplate app,DraggableLine newLine) {
        this.app = app;
        this.newLine = newLine;
        dataManager = (MetroMapData) app.getDataComponent();
    }
    @Override
    public void doTransaction() {
        MetroMapWorkspace workspace = ((MetroMapWorkspace)app.getWorkspaceComponent());
        //d//ataManager
        dataManager.addNode(newLine);
        dataManager.addNode(newLine.frontStation);
        dataManager.addNode(newLine.frontText);
        dataManager.addNode(newLine.endStation);
        dataManager.addNode(newLine.endText);
        //workspace.getMetroLines().getItems().add(newLine.name);
    }
    @Override
    public void undoTransaction() {
        MetroMapWorkspace workspace = ((MetroMapWorkspace)app.getWorkspaceComponent());
        dataManager.removeShape(newLine.frontStation);
        dataManager.removeShape(newLine.frontText);
        dataManager.removeShape(newLine.endStation);
        dataManager.removeShape(newLine.endText);
        dataManager.removeShape(newLine);
        //workspace.getMetroLines().getItems().remove(newLine.name);
    }
}
