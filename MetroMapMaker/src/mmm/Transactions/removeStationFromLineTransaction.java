/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.Transactions;

import djf.AppTemplate;
import mmm.data.DraggableStation;
import mmm.data.MetroMapData;
import mmm.gui.MetroMapWorkspace;
import jtps.jTPS_Transaction;
import mmm.data.MetroMapState;

/**
 *
 * @author Manny
 */
public class removeStationFromLineTransaction implements jTPS_Transaction   {
    AppTemplate app;
    MetroMapData dataManager;
    private DraggableStation newStation;
    
    public removeStationFromLineTransaction(AppTemplate app,DraggableStation newStation) {
        //app = MetroMapWorkspace.getApp();
        this.newStation = newStation;
        dataManager = (MetroMapData) app.getDataComponent();
    }
    @Override
    //public void doTransaction() {}
    public void doTransaction() {
        dataManager.removeStationFromLine(newStation);
        
    }
    @Override
    //public void undoTransaction() {}
    public void undoTransaction() {
        dataManager.addStationToLine(newStation);
        //dataManager.removeShape(this.newStation);
        //dataManager.removeShape(this.newStation.stationName);
    }
}
