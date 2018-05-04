/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.Transactions;

import djf.AppTemplate;
import mmm.data.Draggable;
import mmm.data.DraggableText;
import mmm.data.MetroMapData;
import mmm.gui.MapEditController;
import mmm.gui.MetroMapWorkspace;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import jtps.jTPS_Transaction;

/**
 *
 * @author Manny
 */
public class FontSizeTransaction implements jTPS_Transaction   {
    AppTemplate app;
    MetroMapData dataManager;
    MapEditController logoEditController;
    MetroMapWorkspace workspace;
    private double initialSize;
    private double finalSize;
    private DraggableText shape;
    
    public FontSizeTransaction(AppTemplate app,Draggable shape, double initialSize,double finalSize) {
        
        //app = MetroMapWorkspace.getApp();
        workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        //this.selectedShape = dataManager.getSelectedShape();
        this.shape = (DraggableText)shape;
        this.initialSize = initialSize;
        this.finalSize = finalSize;
        dataManager = (MetroMapData) app.getDataComponent();
        logoEditController = new MapEditController(app);
    }
    @Override
    //public void doTransaction() {}
    public void doTransaction() {
        //DraggableText shape = (DraggableText) dataManager.getSelectedShape();
        shape.setFont(Font.font(shape.getFont().getFamily(), finalSize));
        if (shape.getBold() && shape.getItalics()) {
            shape.setFont(Font.font(shape.getFont().getFamily(), FontWeight.BOLD, FontPosture.ITALIC, finalSize));
        } else if (shape.getBold()) {
            shape.setFont(Font.font(shape.getFont().getFamily(), FontWeight.BOLD, finalSize));
        } else if (shape.getItalics()) {
            shape.setFont(Font.font(shape.getFont().getFamily(), FontPosture.ITALIC, finalSize));
        }
        dataManager.setSelectedNode(shape);
    }
    @Override
    //public void undoTransaction() {}
    public void undoTransaction() {
        if (dataManager.getSelectedNode()instanceof DraggableText){
            DraggableText shape = (DraggableText) dataManager.getSelectedNode();
            if (shape.getBold() && shape.getItalics()) {
            shape.setFont(Font.font(shape.getFont().getFamily(), FontWeight.BOLD, FontPosture.ITALIC, initialSize));
        } else if (shape.getBold()) {
            shape.setFont(Font.font(shape.getFont().getFamily(), FontWeight.BOLD, initialSize));
        } else if (shape.getItalics()) {
            shape.setFont(Font.font(shape.getFont().getFamily(), FontPosture.ITALIC, initialSize));
        }
        }
            
            ((DraggableText)shape).setFont(Font.font(initialSize));
    
    }
}
