package mmm.gui;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import mmm.data.MetroMapData;
import mmm.data.MetroMapState;
import djf.AppTemplate;
import java.io.File;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import static javafx.scene.paint.Color.BLACK;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import jtps.jTPS_Transaction;
import mmm.Transactions.BoldTransaction;
import mmm.Transactions.FontFamilyTransaction;
import mmm.Transactions.FontSizeTransaction;
import mmm.Transactions.ItalicizeTransaction;
import mmm.Transactions.addImageTransaction;
import mmm.Transactions.addTextTransaction;
import mmm.data.DraggableImage;
import mmm.data.DraggableLine;
import mmm.data.DraggableStation;
import mmm.data.DraggableText;
import static mmm.data.MetroMapData.WHITE_HEX;
import mmm.data.PathWindow;

/**
 * This class responds to interactions with other UI logo editing controls.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class MapEditController {
    AppTemplate app;
    MetroMapData dataManager;
    boolean gridOnOrOff;
    
    public MapEditController(AppTemplate initApp) {
	app = initApp;
	dataManager = (MetroMapData)app.getDataComponent();
        gridOnOrOff = false;
    }
   
    /**
     * This method handles a user request to remove the selected shape.
     */
    public void processRemoveSelectedStation() {
	// REMOVE THE SELECTED SHAPE IF THERE IS ONE
	dataManager.removeStation();
	
	// ENABLE/DISABLE THE PROPER BUTTONS
	MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false);
    }
    
    public void processRemoveStationFromLine() {
	// REMOVE THE SELECTED SHAPE IF THERE IS ONE
	dataManager.setState(MetroMapState.REMOVING_STATION_FROM_LINE);
	
	// ENABLE/DISABLE THE PROPER BUTTONS
	MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false);
    }
    
    public void processRemoveSelectedLine() {
        dataManager.removeLine();
        
        MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false);
    }
    
    public void processEditSelectedLine() {
        dataManager.editLine();
        
        MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false);
    }
    
    public void processEditSelectedStation() {
        dataManager.editStation();
        
        MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * This method processes a user request to start drawing a rectangle.
     */
    public void processSelectLineToDraw() {
	// CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.CROSSHAIR);
	
	// CHANGE THE STATE
	dataManager.setState(MetroMapState.STARTING_LINE);

	// ENABLE/DISABLE THE PROPER BUTTONS
	MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
    }
    
    public void processSelectLabelToDraw() {
        // CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.CROSSHAIR);
	
	// CHANGE THE STATE
	dataManager.setState(MetroMapState.STARTING_TEXT);

	// ENABLE/DISABLE THE PROPER BUTTONS
	MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
	
    }
    
    public void processSelectImageToDraw() {
        // CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.CROSSHAIR);
	
	// CHANGE THE STATE
	dataManager.setState(MetroMapState.STARTING_IMAGE);

	// ENABLE/DISABLE THE PROPER BUTTONS
	MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
	/**/
    }
    
    /**
     * This method provides a response to the user requesting to start
     * drawing an ellipse.
     */
    public void processSelectStationToDraw() {
	// CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.CROSSHAIR);
	
	// CHANGE THE STATE
	dataManager.setState(MetroMapState.STARTING_STATION);

	// ENABLE/DISABLE THE PROPER BUTTONS
	MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
    }
    
    /**
     * This method processes a user request to select the 
     * background color.
     */
    public void processSelectBackgroundColor() {
	MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
	Color selectedColor = workspace.getBackgroundColorPicker().getValue();
	if (selectedColor != null) {
	    dataManager.setBackgroundColor(selectedColor);
	    app.getGUI().updateToolbarControls(false);
	}
    }
    
    /**
     * This method processes a user request to select the outline
     * thickness for shape drawing.
     */
    public void processSelectOutlineThickness() {
	/*MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
	int outlineThickness = (int)workspace.getOutlineThicknessSlider().getValue();
	dataManager.setCurrentOutlineThickness(outlineThickness);
	app.getGUI().updateToolbarControls(false);
        */
    }
    
    /**
     * This method processes a user request to take a snapshot of the
     * current scene.
     */
    
    
    public void handleAboutRequest() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Authors: Richard McKenna, Ritwik Banerjee, Eugene Stark, and Manpreet Singh \n"
                + "Year: Fall 2017\n"
                + "Frameworks Used: Desktop Java Framework, jTPS, Properties Manager");
        //alert.getDialogPane().getChildren().add(n);
        
        alert.setHeaderText("Metro Map Maker");

        alert.setTitle("About");
        Optional<ButtonType> result = alert.showAndWait();
    }
    
    public void processAddStationToLine() {
        // CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.CROSSHAIR);
	
	// CHANGE THE STATE
	dataManager.setState(MetroMapState.ADDING_STATION);

	// ENABLE/DISABLE THE PROPER BUTTONS
	MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
    }
    
    public void processListStationsOnLine(DraggableLine selectedLine) {
        Alert alert = new Alert(AlertType.INFORMATION);
        String s = "Stations:\n";
        if (selectedLine.stations.isEmpty()) {
            alert.close();
        }
        else {
            for (DraggableStation station : selectedLine.stations) {
                s += station.name + "\n";
            }
        }
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(s);

        alert.showAndWait();
    }

    public void processChangeFontFamily() {
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        DraggableText selectedText = (DraggableText) dataManager.getSelectedNode();
        //dataManager.setSelectedShape(selectedText);
        FontFamilyTransaction transaction = new FontFamilyTransaction(app,selectedText,selectedText.getFont().getFamily(),(String)workspace.getFontFamilyPicker().getSelectionModel().getSelectedItem());
        workspace.jTPS.addTransaction(transaction);
    }
    
    public void processChangeFontSize() {
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        DraggableText selectedText = (DraggableText) dataManager.getSelectedNode();
        //dataManager.setSelectedShape(selectedText);
        double finalSize = /*Double.parseDouble((String)*/(double)workspace.getFontSizePicker().getSelectionModel().getSelectedItem();//);
        FontSizeTransaction transaction = new FontSizeTransaction(app,selectedText,selectedText.getFont().getSize(),finalSize);
        workspace.jTPS.addTransaction(transaction);
    }
    
    public void processBoldRequest() {
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        DraggableText selectedText = (DraggableText) dataManager.getSelectedNode();
        BoldTransaction transaction = new BoldTransaction(app,selectedText);
            workspace.jTPS.addTransaction(transaction);
            //System.out.println(selectedText);
            dataManager.setSelectedNode(selectedText);
        
    }

    public void processItalicsRequest() {
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        DraggableText selectedText = (DraggableText) dataManager.getSelectedNode();
        ItalicizeTransaction transaction = new ItalicizeTransaction(app,selectedText);
            workspace.jTPS.addTransaction(transaction);
        dataManager.setSelectedNode(selectedText);
        System.out.println(selectedText.getStyle());
    }
    
    public void processZoomIn() {
        MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
        workspace.getCanvas().setScaleX(workspace.getCanvas().getScaleX() * 1.1);
        workspace.getCanvas().setScaleY(workspace.getCanvas().getScaleY() * 1.1);
        workspace.reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false); 
    }
    
    public void processZoomOut() {
        MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
        workspace.getCanvas().setScaleX(workspace.getCanvas().getScaleX() / 1.1);
        workspace.getCanvas().setScaleY(workspace.getCanvas().getScaleY() / 1.1);
        workspace.reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false); 
    }
    
    public void processIncreaseSize() {
        MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
        workspace.getCanvas().setPrefWidth(workspace.getCanvas().getPrefWidth() * 1.1);
        workspace.getCanvas().setPrefHeight(workspace.getCanvas().getPrefHeight() * 1.1);
        workspace.reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false);
    }
    
    public void processDecreaseSize() {
        MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
        workspace.getCanvas().setPrefWidth(workspace.getCanvas().getPrefWidth() / 1.1);
        workspace.getCanvas().setPrefHeight(workspace.getCanvas().getPrefHeight() / 1.1);
        workspace.reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false);
    }

    void processKeyPress(KeyCode code) {
        //void processKeyPress(KeyCode fuck) {
        MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
        switch(code){
            case W:  
            {
                workspace.getCanvas().setTranslateY(workspace.getCanvas().getTranslateY() + 0.1 * workspace.getCanvas().getHeight());
                break;
            }
            case A:
            {
                workspace.getCanvas().setTranslateX(workspace.getCanvas().getTranslateX() + 0.1 * workspace.getCanvas().getWidth());
                break;
            }
            case S: 
            {
                workspace.getCanvas().setTranslateY(workspace.getCanvas().getTranslateY() - 0.1 * workspace.getCanvas().getHeight());
                break;
            }
            case D: 
            {
                workspace.getCanvas().setTranslateX(workspace.getCanvas().getTranslateX() - 0.1 * workspace.getCanvas().getWidth());
                break;
            }
            case UP: 
            {
                workspace.getCanvas().setTranslateY(workspace.getCanvas().getTranslateY() + 0.1 * workspace.getCanvas().getHeight());
                break;
            }
            case LEFT: 
            {
                workspace.getCanvas().setTranslateX(workspace.getCanvas().getTranslateX() + 0.1 * workspace.getCanvas().getWidth());
                break;
            }
            case DOWN: 
            {
                workspace.getCanvas().setTranslateY(workspace.getCanvas().getTranslateY() - 0.1 * workspace.getCanvas().getHeight());
                break;
            }
            case RIGHT: 
            {
                workspace.getCanvas().setTranslateX(workspace.getCanvas().getTranslateX() - 0.1 * workspace.getCanvas().getWidth());
                break;
            }
        }
        
         workspace.reloadWorkspace(dataManager);
         app.getGUI().updateToolbarControls(false);
    }

    public void processMoveLabel() {
        MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
        DraggableStation station = (DraggableStation) workspace.getMetroStation((String) workspace.getMetroStations().getSelectionModel().getSelectedItem());
        dataManager.changeOrientation(station);
    }

    public void processRotateLabel() {
        MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
        DraggableStation station = (DraggableStation) workspace.getMetroStation((String) workspace.getMetroStations().getSelectionModel().getSelectedItem());
        dataManager.changeAngle(station);
    }
    
    public void processgridOnOff() {

        MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
        boolean on = gridOnOrOff;
        
        if(!on){
        Pane gridShow = new Pane();
        double x = workspace.getCanvas().getPrefWidth();
        double y = workspace.getCanvas().getPrefHeight();
       
        x -= x % 25;
        y -= y % 25;
        
        gridShow.setPrefHeight(y);
        gridShow.setPrefWidth(x);
        
        workspace.getCanvas().getChildren().add(gridShow);
        
        while(y>0){
            while(x>0){
            Line grid = new Line();
            double height = workspace.getCanvas().getHeight();
            grid.startXProperty().set(x);
            grid.startYProperty().set(height);
            grid.endXProperty().set(x);
            grid.endYProperty().set(0);
            grid.setStrokeWidth(3);
            grid.setStroke(BLACK);
            gridShow.getChildren().add(grid);
            x=x-25;
            }
            x = workspace.getCanvas().getWidth();
            Line grid = new Line();
            grid.startXProperty().set(0);
            grid.startYProperty().set(y);
            grid.endXProperty().set(x);
            grid.endYProperty().set(y);
            grid.setStrokeWidth(3);
            grid.setStroke(BLACK);
            gridShow.getChildren().add(grid);
            x -= x % 25;
            y=y-25;
        }
        gridShow.setPickOnBounds(false);
        gridOnOrOff = true;
        }else{
           for(int i = 0; i < workspace.getCanvas().getChildren().size();i++){
            if(workspace.getCanvas().getChildren().get(i) instanceof Pane){
                workspace.getCanvas().getChildren().remove(i);
            }
           }
        gridOnOrOff = false;
        }
        workspace.reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false); 
    }

    void processSnapToGrid() {
        dataManager.snapCurrentStationToGrid();
       
       	MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
    }

    void processOriginToDest() {
        MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
        DraggableStation stat1 = workspace.getMetroStation((String)workspace.getOriginPicker().getSelectionModel().getSelectedItem());
        DraggableStation stat2 = workspace.getMetroStation((String)workspace.getDestPicker().getSelectionModel().getSelectedItem());
        PathWindow path = new PathWindow(stat1,stat2);
        path.init();
    }
    
    public void processRemoveElement() {
        dataManager.removeElement();
        
       	MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
    }
}
