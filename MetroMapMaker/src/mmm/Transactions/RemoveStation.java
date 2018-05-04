/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.Transactions;

import djf.AppTemplate;
import jtps.jTPS_Transaction;
import mmm.data.DraggableLine;
import mmm.data.DraggableStation;
import mmm.data.MetroMapData;

/**
 *
 * @author Manny
 */
public class RemoveStation implements jTPS_Transaction {
    AppTemplate app;
    MetroMapData dataManager;
    private DraggableStation newStation;
    
    public RemoveStation(AppTemplate app, DraggableStation station) {
        this.app = app;
        dataManager = (MetroMapData)app.getDataComponent();
        this.newStation = station;
    }
    @Override
    public void doTransaction() {
        dataManager.removeStation();
        
    }

    @Override
    public void undoTransaction() {
        if (newStation.onLine = true)
            dataManager.addStationToLine(newStation);
        else
            dataManager.removeShape(newStation);
    }
    
}
