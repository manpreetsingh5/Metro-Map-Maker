/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.Transactions;

import djf.AppTemplate;
import mmm.data.Draggable;
import mmm.data.MetroMapData;
import mmm.gui.CanvasController;
import mmm.gui.MetroMapWorkspace;
import javafx.scene.shape.Shape;
import jtps.jTPS_Transaction;

/**
 *
 * @author Manny
 */
public class DragTransaction implements jTPS_Transaction   {
    AppTemplate app;
    MetroMapData dataManager;
    private double initial_x;
    private double initial_y;
    private double final_x;
    private double final_y;
    private Draggable shape;
    CanvasController canvasController;
    
    public DragTransaction(AppTemplate app, Draggable shape, double initial_x, double initial_y,double final_x, double final_y) {
        //app = MetroMapWorkspace.getApp();
        canvasController = new CanvasController(app);
        dataManager = (MetroMapData) app.getDataComponent();
        this.shape = shape;
        this.initial_x = initial_x;
        this.initial_y = initial_y;
        this.final_x = final_x;
        this.final_y = final_y;
        
    }
    @Override
    public void doTransaction() {
        shape.drag((int)final_x, (int)final_y);
        dataManager.setSelectedNode((Shape)shape);
        }
    
    @Override
    public void undoTransaction() {
        shape.drag((int)initial_x, (int)initial_y);
        dataManager.setSelectedNode((Shape)shape);
    }
}
