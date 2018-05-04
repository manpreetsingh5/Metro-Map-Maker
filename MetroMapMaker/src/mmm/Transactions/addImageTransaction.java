/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.Transactions;

import djf.AppTemplate;
import mmm.data.DraggableImage;
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
public class addImageTransaction implements jTPS_Transaction   {
    AppTemplate app;
    MetroMapData dataManager;
    private DraggableImage newImage;
    
    public addImageTransaction(AppTemplate app,DraggableImage newImage) {
        //app = MetroMapWorkspace.getApp();
        this.newImage = newImage;
        dataManager = (MetroMapData) app.getDataComponent();
    }
    @Override
    //public void doTransaction() {}
    public void doTransaction() {
        dataManager.addNode(newImage);
    }
    @Override
    //public void undoTransaction() {}
    public void undoTransaction() {
        dataManager.removeShape(this.newImage);
    }
}
