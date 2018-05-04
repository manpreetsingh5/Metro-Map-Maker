/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.Transactions;

import djf.AppTemplate;
import mmm.data.Draggable;
import mmm.data.MetroMapData;
import mmm.gui.MetroMapWorkspace;
import javafx.scene.shape.Shape;
import jtps.jTPS_Transaction;

/**
 *
 * @author Manny
 */
public class RemoveTransaction implements jTPS_Transaction   {
    AppTemplate app;
    MetroMapData dataManager;
    private Draggable shape;
    
    public RemoveTransaction(AppTemplate app,Draggable shape) {
        //app = MetroMapWorkspace.getApp();
        this.shape = shape;
        dataManager = (MetroMapData) app.getDataComponent();
    }
    @Override
    //public void doTransaction() {}
    public void doTransaction() {
        dataManager.removeShape((Shape)shape);
    }
    @Override
    //public void undoTransaction() {}
    public void undoTransaction() {
        dataManager.addNode((Shape)shape);
    }
}
