/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.data;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import mmm.data.DraggableLine;

/**
 *
 * @author Manny
 */
public class EditLineWindow extends Dialog{
    private DraggableLine line;
    String name;
    private Paint color;

    public EditLineWindow(DraggableLine line) {
        this.line = line;
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
        this.setHeaderText("Please enter values for the name and color of the line:");
        this.setTitle("Create Line");
        this.getDialogPane().setPadding(new Insets(10, 10, 10, 10));
        
        TextField lineName = new TextField();
        lineName.setText(line.name);
        String name = lineName.getText();
        ColorPicker lineColor = new ColorPicker();
        lineColor.setOnAction(e -> {
            lineColor.setValue(lineColor.getValue());
            this.color = lineColor.getValue();
        });
        CheckBox check = new CheckBox();
        check.selectedProperty().addListener(e -> {
            if (this.line.circular == false) {
                this.line.circular = true;
                LineTo lineTo = new LineTo(this.line.moveTo.getX(),this.line.moveTo.getY());
                lineTo.xProperty().bindBidirectional(this.line.moveTo.xProperty());
                lineTo.yProperty().bindBidirectional(this.line.moveTo.yProperty());
                this.line.getElements().add(lineTo);
            }
            else if (this.line.circular == true) {
                this.line.circular = false;
                this.line.getElements().remove(this.line.getElements().size()-1);
            }
        });
        grid.setPadding(new Insets(10,10,10,10));
        Paint color = (Paint)lineColor.getUserData();
        grid.add(new Label("Name:"), 0, 0);
        grid.add(lineName, 1, 0);
        grid.add(new Label("Color:"), 0, 1);
        grid.add(lineColor, 1, 1);
        grid.add(new Label("Circular:"), 0, 2);
        grid.add(check, 1, 2);
        this.setOnCloseRequest(e -> {
            this.close();
        });
        
        
        this.color = (Paint)lineColor.getValue();
        //line.setStroke(this.color);
        //line.setName(name);
        this.getDialogPane().setContent(grid);
        while (this.getResult() != ButtonType.OK && this.getResult() != ButtonType.CANCEL)  {
            
            this.showAndWait();
            this.name = lineName.getText();
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
