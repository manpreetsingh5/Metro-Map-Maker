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
public class BoldTransaction implements jTPS_Transaction   {
    AppTemplate app;
    MetroMapData dataManager;
    MapEditController logoEditController;
    private Draggable shape;
    
    public BoldTransaction(AppTemplate app,Draggable shape) {
        //app = MetroMapWorkspace.getApp();
        //this.selectedShape = dataManager.getSelectedShape();
        this.shape = shape;
        dataManager = (MetroMapData) app.getDataComponent();
        logoEditController = new MapEditController(app);
    }
    @Override
    //public void doTransaction() {}
    public void doTransaction() {
        DraggableText selectedText = (DraggableText)shape;
        if (selectedText.getBold()) {

            if (selectedText.getItalics()) {
                selectedText.setFont(Font.font(selectedText.getFont().getFamily(), FontWeight.NORMAL, FontPosture.ITALIC, selectedText.getFont().getSize()));
                selectedText.setBold(false);
            } else {
                selectedText.setFont(Font.font(selectedText.getFont().getFamily(), FontWeight.NORMAL, FontPosture.REGULAR, selectedText.getFont().getSize()));
                selectedText.setBold(false);
            }
        } else {
            if (selectedText.getItalics()) {
                selectedText.setFont(Font.font(selectedText.getFont().getFamily(), FontWeight.BOLD, FontPosture.ITALIC, selectedText.getFont().getSize()));
                selectedText.setBold(true);
            } else if (!selectedText.getItalics()) {
                selectedText.setFont(Font.font(selectedText.getFont().getFamily(), FontWeight.BOLD, selectedText.getFont().getSize()));
                selectedText.setBold(true);
            }
            dataManager.setSelectedNode(selectedText);
        }
    }
    @Override
    //public void undoTransaction() {}
    public void undoTransaction() {
        DraggableText selectedText = (DraggableText)shape;
        if (selectedText.getBold()) {

            if (selectedText.getItalics()) {
                selectedText.setFont(Font.font(selectedText.getFont().getFamily(), FontWeight.NORMAL, FontPosture.ITALIC, selectedText.getFont().getSize()));
                selectedText.setBold(false);
            } else {
                selectedText.setFont(Font.font(selectedText.getFont().getFamily(), FontWeight.NORMAL, FontPosture.REGULAR, selectedText.getFont().getSize()));
                selectedText.setBold(false);
            }
        } else {
            if (selectedText.getItalics()) {
                selectedText.setFont(Font.font(selectedText.getFont().getFamily(), FontWeight.BOLD, FontPosture.ITALIC, selectedText.getFont().getSize()));
                selectedText.setBold(true);
            } else if (!selectedText.getItalics()) {
                selectedText.setFont(Font.font(selectedText.getFont().getFamily(), FontWeight.BOLD, selectedText.getFont().getSize()));
                selectedText.setBold(true);
            }
            //System.out.println(selectedText);
            dataManager.setSelectedNode(selectedText);
        }
    }
}
