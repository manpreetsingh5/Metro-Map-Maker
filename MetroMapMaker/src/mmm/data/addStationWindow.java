/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.data;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import mmm.data.DraggableStation;

/**
 *
 * @author Manny
 */
public class addStationWindow extends Dialog{
    private DraggableStation station;
    private String name;

    public addStationWindow(DraggableStation station) {
        this.station = station;
        init();
    }
    
    public void init() {
        //Dialog<Pair<String, ColorPicker>> lineMenu = new Dialog<>();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        //ButtonType loginButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        this.getDialogPane().setPrefHeight(200);
        this.setHeaderText("Please enter a value for the name of the station:");
        this.setTitle("Create Station");
        this.getDialogPane().setPadding(new Insets(10, 10, 10, 10));
        
        TextField lineName = new TextField();
        String name = lineName.getText();
        ColorPicker lineColor = new ColorPicker();
        lineColor.setOnAction(e -> {
            lineColor.setValue(lineColor.getValue());
        });
        Paint color = (Paint)lineColor.getUserData();
        grid.add(new Label("Name:"), 0, 0);
        grid.add(lineName, 1, 0);
        
        this.setOnCloseRequest(e -> {
            this.close();
        });
        
        this.name = lineName.getText();
        this.getDialogPane().setContent(grid);
        while (this.getResult() != ButtonType.OK && this.getResult() != ButtonType.CANCEL) {
            this.showAndWait();
            this.name = lineName.getText();
        }
    }
    
    public String getStationName() {
        return name;
    }
}
