/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.Transactions;

import djf.AppTemplate;
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
public class FontFamilyTransaction implements jTPS_Transaction {

    AppTemplate app;
    MetroMapData dataManager;
    MapEditController logoEditController;
    MetroMapWorkspace workspace;
    private String initialFont;
    private DraggableText shape;
    private String finalFont;

    public FontFamilyTransaction(AppTemplate app,DraggableText shape, String initialFont, String finalFont) {

        //app = MetroMapWorkspace.getApp();
        workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        //this.selectedShape = dataManager.getSelectedShape();
        this.shape = shape;
        this.finalFont = finalFont;
        this.initialFont = initialFont;
        dataManager = (MetroMapData) app.getDataComponent();
        logoEditController = new MapEditController(app);
    }

    @Override
    //public void doTransaction() {}
    public void doTransaction() {
        if (shape.getBold() && shape.getItalics()) {
            shape.setFont(Font.font(finalFont, FontWeight.BOLD, FontPosture.ITALIC, (double) shape.getFont().getSize()));
        } else if (shape.getBold()) {
            shape.setFont(Font.font(finalFont, FontWeight.BOLD, (double) shape.getFont().getSize()));
        } else if (shape.getItalics()) {
            shape.setFont(Font.font(finalFont, FontPosture.ITALIC, shape.getFont().getSize()));
        }
        else {
            shape.setFont(Font.font((String) workspace.getFontFamilyPicker().getSelectionModel().getSelectedItem(), (double) shape.getFont().getSize()));
        dataManager.setSelectedNode(shape);
        
    }
    }

    @Override
    //public void undoTransaction() {}
    public void undoTransaction() {
        if (shape instanceof DraggableText) {
            if (shape.getBold() && shape.getItalics()) {
                shape.setFont(Font.font(initialFont, FontWeight.BOLD, FontPosture.ITALIC, shape.getFont().getSize()));
            } else if (shape.getBold()) {
                shape.setFont(Font.font(initialFont, FontWeight.BOLD, shape.getFont().getSize()));
            } else if (shape.getItalics()) {
                shape.setFont(Font.font(initialFont, FontPosture.ITALIC, shape.getFont().getSize()));
            }
            else {
            shape.setFont(Font.font(initialFont, (double) shape.getFont().getSize()));
        }
        }
    }
}
