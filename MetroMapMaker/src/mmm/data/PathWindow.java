/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.data;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import mmm.data.DraggableLine;
import mmm.data.DraggableStation;

/**
 *
 * The resulting Singleton that would show how
 * @author Ben Michalowicz
 */
public class PathWindow extends Dialog{
    
    DraggableStation statName1, statName2;
    
    TextArea result;
    
    Label routeName;
    
    HBox hbox;
    
    
    public PathWindow(DraggableStation startName, DraggableStation endName){
        this.statName1 = startName;
        this.statName2 = endName;
        
        //init();
        
    }
    
    public void init() {
        //Dialog<Pair<String, ColorPicker>> lineMenu = new Dialog<>();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        //ButtonType okButton = new ButtonType("Ok", ButtonType.OK);
        this.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        this.getDialogPane().setPrefHeight(200);
        this.setHeaderText("Route from " + this.statName1.name + " to " + this.statName2.name);
        this.setTitle("Route");
        this.getDialogPane().setPadding(new Insets(10, 10, 10, 10));
        
        DraggableLine line1 = this.statName1.lines.get(0);
        DraggableLine line2 = this.statName2.lines.get(0);
        DraggableStation transfer = line1.stations.get(1);
        for (DraggableStation s : line2.stations) {
            if (line1.stations.contains(s))
                transfer = s;
        }
        
        int firstTransfer = Math.abs(this.statName1.lines.get(0).stations.indexOf(statName1)-this.statName1.lines.get(0).stations.indexOf(transfer));
        int secondTransfer = Math.abs(this.statName2.lines.get(0).stations.indexOf(statName2)-this.statName2.lines.get(0).stations.indexOf(transfer)) + 1;
        if (!line1.name.equals(line2.name)) {
            firstTransfer = Math.abs(this.statName1.lines.get(0).stations.indexOf(statName1)-this.statName1.lines.get(0).stations.indexOf(transfer));
            secondTransfer = Math.abs(this.statName2.lines.get(0).stations.indexOf(statName2)-this.statName2.lines.get(0).stations.indexOf(transfer)) + 1;
        }
        else {
            firstTransfer = Math.abs(this.statName1.lines.get(0).stations.indexOf(statName1)-this.statName1.lines.get(0).stations.indexOf(statName2));
            //secondTransfer = Math.abs(this.statName2.lines.get(0).stations.indexOf(statName2)-this.statName2.lines.get(0).stations.indexOf(transfer)) + 1;
        }
        String s = "Origin: " + this.statName1.name + "\n"
                        + "Destination: " + this.statName2.name + "\n"
                        + this.statName1.lines.get(0).name + " (" + (firstTransfer) + " stops)\n";
        if (!line1.name.equals(line2.name)) {
                        s += this.statName2.lines.get(0).name + " (" + (secondTransfer) + " stops)\n";
                        s += "Total Stops: " + (firstTransfer + secondTransfer)+"\n";
                        s += "Estimated Time: " + ((firstTransfer + secondTransfer)*3) + " minutes\n";
        }
        else {
            s += "Total Stops: " + (firstTransfer)+"\n";
            s += "Estimated Time: " + ((firstTransfer)*3) + " minutes\n";
        }
                        
                        //+ "Estimated Time: " + ((firstTransfer + secondTransfer)*3) + " minutes\n";
        s += "Board " + this.statName1.lines.get(0).name + " at " + this.statName1.name + "\n";
        if (!line1.name.equals(line2.name)) {
            s += "Transfer to " + this.statName2.lines.get(0).name + " at " + transfer.name + "\n";
        }
        s += "Disembark " + this.statName2.lines.get(0).name + " at " + this.statName2.name;
        TextArea lineName = new TextArea();
        lineName.setPrefRowCount(20);
        lineName.setPrefColumnCount(20);
        //grid.setPrefSize(500, 500);
        lineName.setText(s);
        String name = lineName.getText();
        
        //grid.add(new Label("Name:"), 0, 0);
        grid.add(lineName, 1, 1);
        
        this.getDialogPane().setContent(grid);
        this.setOnCloseRequest(e -> {
            this.close();
        });
        this.getDialogPane().setContent(grid);
        while (this.getResult() != ButtonType.OK && this.getResult() != ButtonType.CANCEL)  {
            
            this.showAndWait();
        }
        //this.showAndWait();
    }
    
    
    
}