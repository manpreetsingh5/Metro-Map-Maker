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
public class ItalicizeTransaction implements jTPS_Transaction   {
    AppTemplate app;
    MetroMapData dataManager;
    MapEditController logoEditController;
    private Draggable shape;
    
    public ItalicizeTransaction(AppTemplate app,Draggable shape) {
        //app = MetroMapWorkspace.getApp();
        dataManager = (MetroMapData) app.getDataComponent();
        this.shape = shape;
        logoEditController = new MapEditController(app);
    }
    @Override
    public void doTransaction() {
        DraggableText selectedText = (DraggableText) shape;
        if (selectedText.getItalics()) {

            if (selectedText.getBold()) {
                selectedText.setFont(Font.font(selectedText.getFont().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, selectedText.getFont().getSize()));
                selectedText.setItalics(false);
            } else {
                selectedText.setFont(Font.font(selectedText.getFont().getFamily(), FontWeight.NORMAL, FontPosture.REGULAR, selectedText.getFont().getSize()));
                selectedText.setItalics(false);
            }
        } else {
            if (selectedText.getBold()) {
                selectedText.setFont(Font.font(selectedText.getFont().getFamily(), FontWeight.BOLD, FontPosture.ITALIC, selectedText.getFont().getSize()));
                selectedText.setItalics(true);
            } else if (!selectedText.getBold()) {
                selectedText.setFont(Font.font(selectedText.getFont().getFamily(), FontPosture.ITALIC, selectedText.getFont().getSize()));
                selectedText.setItalics(true);
            }

        }
    }
    @Override
    public void undoTransaction() {
         DraggableText selectedText = (DraggableText) shape;
        if (selectedText.getItalics()) {

            if (selectedText.getBold()) {
                selectedText.setFont(Font.font(selectedText.getFont().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, selectedText.getFont().getSize()));
                selectedText.setItalics(false);
            } else {
                selectedText.setFont(Font.font(selectedText.getFont().getFamily(), FontWeight.NORMAL, FontPosture.REGULAR, selectedText.getFont().getSize()));
                selectedText.setItalics(false);
            }
            
        } 
        else {
            if (selectedText.getBold()) {
                selectedText.setFont(Font.font(selectedText.getFont().getFamily(), FontWeight.BOLD, FontPosture.ITALIC, selectedText.getFont().getSize()));
                selectedText.setItalics(true);
            } else if (!selectedText.getBold()) {
                selectedText.setFont(Font.font(selectedText.getFont().getFamily(), FontPosture.ITALIC, selectedText.getFont().getSize()));
                selectedText.setItalics(true);
            }

        }
    }
}
