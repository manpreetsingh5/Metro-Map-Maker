/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.Transactions;

import djf.AppTemplate;
import mmm.data.DraggableText;
import mmm.data.MetroMapData;
import jtps.jTPS_Transaction;

/**
 *
 * @author Manny
 */
public class addTextTransaction implements jTPS_Transaction   {
    AppTemplate app;
    MetroMapData dataManager;
    private DraggableText newText;
    
    public addTextTransaction(AppTemplate app,DraggableText newText) {
        //app = MetroMapWorkspace.getApp();
        this.newText = newText;
        dataManager = (MetroMapData) app.getDataComponent();
    }
    @Override
    public void doTransaction() {
        dataManager.addNode(newText);
    }
    @Override
    public void undoTransaction() {
        dataManager.removeShape(newText);
    }
}
