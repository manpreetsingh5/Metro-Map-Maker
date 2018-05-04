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
import mmm.data.DraggableLine;

/**
 *
 * @author Manny
 */
public class EditStationWindow extends Dialog{
    private DraggableStation station;
    String name;
    private Paint color;

    public EditStationWindow(DraggableStation station) {
        this.station = station;
        //init();
    }
    
    public void init() {
        //Dialog<Pair<String, ColorPicker>> lineMenu = new Dialog<>();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        //ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        this.getDialogPane().setPrefHeight(200);
        this.setHeaderText("Please enter a value for the color of the station:");
        this.setTitle("Edit Station");
        this.getDialogPane().setPadding(new Insets(10, 10, 10, 10));
        
        //TextField lineName = new TextField();
        //lineName.setText(station.name);
        //String name = lineName.getText();
        ColorPicker stationColor = new ColorPicker();
        stationColor.setOnAction(e -> {
            stationColor.setValue(stationColor.getValue());
            this.color = stationColor.getValue();
        });
        Paint color = (Paint)stationColor.getUserData();
        //grid.add(new Label("Name:"), 0, 0);
        //grid.add(lineName, 1, 0);
        grid.add(new Label("Color:"), 0, 1);
        grid.add(stationColor, 1, 1);
        this.setOnCloseRequest(e -> {
            this.close();
        });
        
        
        this.color = (Paint)stationColor.getValue();
        //line.setStroke(this.color);
        //line.setName(name);
        this.getDialogPane().setContent(grid);
       while (this.getResult() != ButtonType.OK && this.getResult() != ButtonType.CANCEL)  {
            
        this.showAndWait();
        //    this.name = lineName.getText();
        }
        //this.showAndWait();
    }
    
    public String getLineName() {
        return name;
    }
    
    public Paint getLineColor() {
        return color;
    }
}
