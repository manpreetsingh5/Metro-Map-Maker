package mmm.gui;

import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import static mmm.MetroMapLanguageProperty.*;
import mmm.data.MetroMapData;
import static mmm.data.MetroMapData.WHITE_HEX;
import mmm.data.MetroMapState;
import djf.ui.AppYesNoCancelDialogSingleton;
import djf.ui.AppMessageDialogSingleton;
import djf.ui.AppGUI;
import djf.AppTemplate;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import static djf.ui.AppGUI.CLASS_BORDERED_PANE;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import static mmm.css.MetroMapStyle.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import jtps.jTPS;
import mmm.data.DraggableImage;
import mmm.data.DraggableLine;
import mmm.data.DraggableStation;
import static mmm.data.MetroMapData.BLACK_HEX;
import mmm.file.MetroMapFiles;

/**
 * This class serves as the workspace component for this application, providing
 * the user interface controls for editing work.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class MetroMapWorkspace extends AppWorkspaceComponent {

    
    // Transaction manager
    public static jTPS jTPS;
    
    // HERE'S THE APP
    AppTemplate app;

    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;

    // HAS ALL THE CONTROLS FOR EDITING
    VBox editToolbar;
    
    /////////////////////////////////
    Button saveAs;
    Button export;
    
    FlowPane undoRedo;
    Button undo;
    Button redo;
    FlowPane aboutBar;
    Button about;
    
    // FIRST ROW
    VBox row1Box;
    
    // First row of first row
    HBox row1_1;
    Label metroLinesLabel;
    ComboBox metroLines;
    ArrayList<String> metroLinesList;
    Button editLineButton;
    StackPane editLine;
    Circle c1;
    
    // Second row of first row
    HBox row1_2;
    Button addLineButton;
    Button removeLineButton;
    Button addStationToLine;
    Button removeStationFromLine;
    Button listStations;
    // Third part of first row
    Slider lineThickness;
    
    ///////////////////////////////////////////////
    
    
    //////////////////////////////////////////////
    
    // SECOND ROW
    VBox row2Box;
    
    // First row of second row
    HBox row2_1;
    Label metroStationsLabel;
    ComboBox metroStations;
    ColorPicker stationColorPicker;
    StackPane editStation;
    Circle c2;
    
    // Second row of second row
    HBox row2_2;
    Button snapToGrid;
    Button moveLabelButton;
    Button rotateLabelButton;
    Button addStation;
    Button removeStation;
    // Third part of second row
    Slider stationRadiusSlider;
    
    /////////////////////////////////////////////
    
    
    // THIRD ROW
    HBox row3Box;
    VBox row3_1;
    VBox row3_2;
    ComboBox originStationPicker;
    ComboBox destStationPicker;
    Button originToDestButton;

    ////////////////////////////////////////////////////
    
    // FORTH ROW
    VBox row4Box;
    
    HBox row4_1;
    Label decorLabel;
    ColorPicker backgroundColorPicker;
    
    HBox row4_2;
    Button setImageBackgroundButton;
    Button addImageButton;
    Button addLabelButton;
    Button removeElementButton;
    
    ////////////////////////////////////////////////////
    
    
    ////////////////////////////////////////////////////
    
    // FIFTH ROW
    VBox row5Box;
    
    HBox row5_1;
    Label fontLabel;
    ColorPicker fontColorPicker;
    
    HBox row5_2;
    Button boldButton;
    Button italicizeButton;
    ComboBox<Double> fontSizePicker;
    ComboBox fontFamilyPicker;
    //
        
    ////////////////////////////////////////////////////
    
    ////////////////////////////////////////////////////
    
    // SIXTH ROW
    VBox row6Box;
    
    HBox row6_1;
    Label navigationLabel;
    CheckBox gridOnOffBox;
    Label showBoxLabel;
    
    HBox row6_2;
    Button zoomInButton;
    Button zoomOutButton;
    Button increaseMapSizeButton;
    Button decreaseMapSizeButton;
    
    
    ScrollPane canvasHolder;
    // THIS IS WHERE WE'LL RENDER OUR DRAWING, NOTE THAT WE
    // CALL THIS A CANVAS, BUT IT'S REALLY JUST A Pane
    Pane canvas;
    
    // HERE ARE THE CONTROLLERS
    CanvasController canvasController;
    MapEditController mapEditController;    

    // HERE ARE OUR DIALOGS
    AppMessageDialogSingleton messageDialog;
    AppYesNoCancelDialogSingleton yesNoCancelDialog;
    
    // FOR DISPLAYING DEBUG STUFF
    Text debugText;

    /**
     * Constructor for initializing the workspace, note that this constructor
     * will fully setup the workspace user interface for use.
     *
     * @param initApp The application this workspace is part of.
     *
     * @throws IOException Thrown should there be an error loading application
     * data for setting up the user interface.
     */
    public MetroMapWorkspace(AppTemplate initApp) {
        
	// KEEP THIS FOR LATER
	app = initApp;
        
        jTPS = new jTPS();

	// KEEP THE GUI FOR LATER
	gui = app.getGUI();
        
        
        // LAYOUT THE APP
        initLayout();
        
        // HOOK UP THE CONTROLLERS
        initControllers();
        
        // AND INIT THE STYLE FOR THE WORKSPACE
        initStyle();    
        
        //canvas.setStyle("-fx-background-color: #323232");
    }
    
    /**
     * Note that this is for displaying text during development.
     */
    public void setDebugText(String text) {
	debugText.setText(text);
    }
    
    // ACCESSOR METHODS FOR COMPONENTS THAT EVENT HANDLERS
    // MAY NEED TO UPDATE OR ACCESS DATA FROM
    
    public Pane getCanvas() {
	return canvas;
    }
        
    // HELPER SETUP METHOD
    private void initLayout() {
	// THIS WILL GO IN THE LEFT SIDE OF THE WORKSPACE
	editToolbar = new VBox();
        app.getGUI().getWindow().setMinHeight(900);
        app.getGUI().getWindow().setMinWidth(1500);
        
	
        app.getGUI().getFileToolbar().getChildren().get(0).setOnMouseClicked(e-> {
            try {
                ((MetroMapFiles)app.getFileComponent()).newRequest(((MetroMapData)app.getDataComponent()), app);
            } catch (IOException ex) {
                Logger.getLogger(MetroMapWorkspace.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        app.getGUI().getTopToolbarPane().setAlignment(Pos.CENTER);
        gui.getFileToolbar().getChildren().remove(3);
        gui.getFileToolbar().setAlignment(Pos.CENTER);
        app.getGUI().getFileToolbar().setPrefWidth(gui.getFileToolbar().getChildren().size() * BUTTON_TAG_WIDTH + 20);
        saveAs = gui.initChildButton(gui.getFileToolbar(), SAVE_AS_ICON.toString(), SAVE_AS_TOOLTIP.toString(), false);
        export = gui.initChildButton(gui.getFileToolbar(), EXPORT_ICON.toString(), EXPORT_TOOLTIP.toString(), false);
    
        undoRedo = new FlowPane();
        undoRedo.setPrefWidth(2 * BUTTON_TAG_WIDTH + 20);
        undoRedo.setAlignment(Pos.CENTER);
        undo = gui.initChildButton(undoRedo, UNDO_ICON.toString(), UNDO_TOOLTIP.toString(), false);
        redo = gui.initChildButton(undoRedo, REDO_ICON.toString(), REDO_TOOLTIP.toString(), false);
        
        aboutBar = new FlowPane();
        aboutBar.setAlignment(Pos.CENTER);
        aboutBar.setPrefWidth(BUTTON_TAG_WIDTH + 20);
        about = gui.initChildButton(aboutBar, ABOUT_ICON.toString(), ABOUT_TOOLTIP.toString(), false);
        
        app.getGUI().getTopToolbarPane().getChildren().addAll(undoRedo,aboutBar);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ROW 1
	row1Box = new VBox();
        
        row1_1 = new HBox(35);
        metroLines = new ComboBox();
        metroLines.setValue("Metro Lines");
        metroLines.setPrefWidth(150);
        metroLinesLabel = new Label("Metro Lines");
        metroLinesLabel.setStyle("-fx-text-fill: white;");
        editLineButton = new Button();
        editLineButton.setVisible(false);
        editLine = new StackPane();
        c1 = new Circle(30);
        c1.setStroke(Paint.valueOf(BLACK_HEX));
        c1.setStrokeWidth(1.0);
        c1.setFill(Paint.valueOf(WHITE_HEX));
        Text color = new Text(WHITE_HEX);
        color.setFont(Font.font(10));
        //editLineButton.setGraphic(c1);
        editLine.getChildren().addAll(c1,color);
        
        
        
        row1_1.getChildren().addAll(metroLinesLabel,metroLines,editLine);
        
        row1_2 = new HBox(10);
	addLineButton = gui.initChildButton(row1_2, ADD_ICON.toString(), ADD_TOOLTIP.toString(), false);
        addLineButton.setPrefSize(40, 40);
	removeLineButton = gui.initChildButton(row1_2, REMOVE_ICON.toString(), REMOVE_TOOLTIP.toString(), false);
	removeLineButton.setPrefSize(40, 40);
        addStationToLine = new Button("  Add  \nStation");
        addStationToLine.setPrefHeight(40);
        addStationToLine.setDisable(false);
        addStationToLine.setTooltip(new Tooltip("Add a station to an existing line"));
        
	removeStationFromLine = new Button("  Remove  \n   Station");
        removeStationFromLine.setPrefHeight(40);
        removeStationFromLine.setDisable(false);
        removeStationFromLine.setTooltip(new Tooltip("Remove a station from an existing line"));
        row1_2.getChildren().addAll(addStationToLine,removeStationFromLine);
        listStations = gui.initChildButton(row1_2, LIST_STATIONS_ICON.toString(), LIST_STATIONS_TOOLTIP.toString(), false);
        listStations.setPrefSize(40, 40);
        //row1_2.getChildren().addAll(addLineButton,removeLineButton,addStationToLine,removeStationFromLine,listStations); 
        
        lineThickness = new Slider();
        lineThickness.setMin(0);
        lineThickness.setMax(100);
        lineThickness.setValue(5);
        lineThickness.setShowTickLabels(true);
        lineThickness.setShowTickMarks(true);
        lineThickness.setMajorTickUnit(50);
        lineThickness.setMinorTickCount(5);
        lineThickness.setBlockIncrement(10);
        //lineThickness.setValue(5);
        
        row1Box.getChildren().addAll(row1_1,row1_2,lineThickness);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        
        
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ROW 2
	row2Box = new VBox();
        
        row2_1 = new HBox(20); 
        metroLinesList = new ArrayList<>();
        metroStationsLabel = new Label("Metro Stations");
        metroStationsLabel.setStyle("-fx-text-fill: white;");
        metroStations = new ComboBox();
        metroStations.setPrefWidth(150);
        metroStations.setValue("Metro Stations");
        //stationColorPicker = new ColorPicker();
        //stationColorPicker.setPrefWidth(50);
        editStation = new StackPane();
        c2 = new Circle(30);
        c2.setStroke(Paint.valueOf(BLACK_HEX));
        c2.setStrokeWidth(1.0);
        c2.setFill(Paint.valueOf(WHITE_HEX));
        Text color2 = new Text(WHITE_HEX);
        color2.setFont(Font.font(10));
        
        editStation.getChildren().addAll(c2,color2);
        
        row2_1.getChildren().addAll(metroStationsLabel,metroStations,editStation);
        
        row2_2 = new HBox(10);
        addStation = gui.initChildButton(row2_2, ADD_ICON.toString(), ADD_TOOLTIP.toString(), false);
        removeStation = gui.initChildButton(row2_2, REMOVE_ICON.toString(), REMOVE_TOOLTIP.toString(), false);
        snapToGrid = new Button("Snap");
        snapToGrid.setPrefHeight(40);
        snapToGrid.setDisable(false);
        snapToGrid.setTooltip(new Tooltip("Snap to grid"));
        moveLabelButton = new Button(" Move \nLabel ");
        moveLabelButton.setPrefHeight(40);
        moveLabelButton.setDisable(false);
        moveLabelButton.setTooltip(new Tooltip("Move Station \n    Label"));
        row2_2.getChildren().addAll(snapToGrid,moveLabelButton);
        rotateLabelButton = gui.initChildButton(row2_2, ROTATE_ICON.toString(), ROTATE_TOOLTIP.toString(), false);
        rotateLabelButton.setPrefHeight(40);
        
        
        stationRadiusSlider = new Slider();
        stationRadiusSlider = new Slider();
        stationRadiusSlider.setMin(0);
        stationRadiusSlider.setMax(25);
        stationRadiusSlider.setValue(5);
        stationRadiusSlider.setShowTickLabels(true);
        stationRadiusSlider.setShowTickMarks(true);
        stationRadiusSlider.setMajorTickUnit(12.5);
        stationRadiusSlider.setMinorTickCount(1);
        stationRadiusSlider.setBlockIncrement(10);
        
        row2Box.getChildren().addAll(row2_1,row2_2,stationRadiusSlider);
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        
        
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ROW 3
	row3Box = new HBox();
        row3_1 = new VBox();
        row3_2 = new VBox();
        row3Box.setPadding(new Insets(50,50,50,50));
        originStationPicker = new ComboBox();
        originStationPicker.setValue("Origin");
        originStationPicker.setPrefWidth(250);
        destStationPicker = new ComboBox();
        destStationPicker.setValue("Destination");
        destStationPicker.setPrefWidth(250);
        row3_1.getChildren().addAll(originStationPicker,new Text(""),destStationPicker);
        row3Box.getChildren().addAll(row3_1, row3_2);
        row3_2.getChildren().add(new Text(""));
        originToDestButton = gui.initChildButton(row3_2, ORIG_TO_DEST_ICON.toString(), ORIG_TO_DEST_TOOLTIP.toString(), false);
        originToDestButton.setAlignment(Pos.CENTER_RIGHT);
        
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        
        
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ROW 4
	row4Box = new VBox();
        row4_1 = new HBox(240);
        decorLabel = new Label("Decor");
        decorLabel.setStyle("-fx-text-fill: white;");
        backgroundColorPicker = new ColorPicker();
        backgroundColorPicker.setPrefWidth(50);
        backgroundColorPicker.setValue(Color.valueOf("0x333333ff"));
        
        row4_1.getChildren().addAll(decorLabel,backgroundColorPicker);
    
        row4_2 = new HBox();
        //setImageBackgroundButton = gui.initChildButton(row4_2, BLACK_HEX, BLACK_HEX, workspaceActivated);
        setImageBackgroundButton = new Button("   Set Image \n Background ");
        setImageBackgroundButton.setDisable(false);
        setImageBackgroundButton.setTooltip(new Tooltip("Set Image Background"));
        
        //addImageButton = gui.initChildButton(row4_2, BLACK_HEX, BLACK_HEX, workspaceActivated);
        addImageButton = new Button("  Add \n Image ");
        addImageButton.setDisable(false);
        addImageButton.setTooltip(new Tooltip("Add Image"));
        
        //addLabelButton = gui.initChildButton(row4_2, BLACK_HEX, BLACK_HEX, workspaceActivated);
        addLabelButton = new Button("  Add \n Label ");
        addLabelButton.setDisable(false);
        addLabelButton.setTooltip(new Tooltip("Move Station \n    Label"));
        
        //removeElementButton = gui.initChildButton(row4_2, BLACK_HEX, BLACK_HEX, workspaceActivated);
        removeElementButton = new Button("  Remove \n Element ");
        removeElementButton.setDisable(false);
        removeElementButton.setTooltip(new Tooltip("Remove Element"));
        
        row4_2.getChildren().addAll(setImageBackgroundButton,addImageButton,addLabelButton,removeElementButton);
        
        row4Box.getChildren().addAll(row4_1,row4_2);
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
        
        
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ROW 5
	row5Box = new VBox();
        row5_1 = new HBox(241);
	fontLabel = new Label("Font");
        fontLabel.setStyle("-fx-text-fill: white;");
        fontColorPicker = new ColorPicker();
        fontColorPicker.setPrefWidth(50);
        row5_1.getChildren().addAll(fontLabel,fontColorPicker);
    
        row5_2 = new HBox(10);
        boldButton = gui.initChildButton(row5_2, BOLD_ICON.toString(), BOLD_TOOLTIP.toString(), false);
        italicizeButton = gui.initChildButton(row5_2, ITALICIZE_ICON.toString(), ITALICIZE_TOOLTIP.toString(), false);
        fontSizePicker = new ComboBox();
        for (double i = 10; i < 50; i += 2) {
            fontSizePicker.getItems().add(i);
        }
        fontFamilyPicker = new ComboBox();
        fontFamilyPicker.getItems().add("Arial");
        fontFamilyPicker.getItems().add("Courier");
        fontFamilyPicker.getItems().add("Futura");
        fontFamilyPicker.getItems().add("Gill Sans");
        fontFamilyPicker.getItems().add("Phosphate");
        fontFamilyPicker.getItems().add("Times New Roman");
	
        row5_2.getChildren().addAll(fontSizePicker,fontFamilyPicker);
        
        row5Box.getChildren().addAll(row5_1,row5_2);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        
        
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ROW 6
	row6Box = new VBox(10);
	
        row6_1 = new HBox(175);
        navigationLabel = new Label("Navigation");
        navigationLabel.setStyle("-fx-text-fill: white;");
        gridOnOffBox = new CheckBox("Show Grid");
        gridOnOffBox.setTextFill(Paint.valueOf(WHITE_HEX));
        row6_1.getChildren().addAll(navigationLabel,gridOnOffBox);
        
    
        row6_2 = new HBox(10);
        zoomInButton = gui.initChildButton(row6_2, ZOOM_IN_ICON.toString(), ZOOM_IN_TOOLTIP.toString(), false);
        zoomOutButton = gui.initChildButton(row6_2, ZOOM_OUT_ICON.toString(), ZOOM_OUT_TOOLTIP.toString(), false);
        increaseMapSizeButton = gui.initChildButton(row6_2, INCREASE_MAP_SIZE_ICON.toString(), INCREASE_MAP_SIZE_TOOLTIP.toString(), false);
        decreaseMapSizeButton = gui.initChildButton(row6_2, DECREASE_MAP_SIZE_ICON.toString(), DECREASE_MAP_SIZE_TOOLTIP.toString(), false);
        
        row6Box.getChildren().addAll(row6_1,row6_2);
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
        
        
	// NOW ORGANIZE THE EDIT TOOLBAR
	editToolbar.getChildren().add(row1Box);
	editToolbar.getChildren().add(row2Box);
	editToolbar.getChildren().add(row3Box);
	editToolbar.getChildren().add(row4Box);
	editToolbar.getChildren().add(row5Box);
	editToolbar.getChildren().add(row6Box);
	
        
        
	// WE'LL RENDER OUR STUFF HERE IN THE CANVAS
	canvas = new Pane();
	debugText = new Text();
	canvas.getChildren().add(debugText);
	debugText.setX(100);
	debugText.setY(100);
        
        Scale scale = new Scale();
        canvas.getTransforms().add(scale);
        
        canvasHolder = new ScrollPane();
        canvasHolder.setContent(canvas);
        
        canvas.setPrefWidth(1340);
        canvas.setPrefHeight(930);
	
	// AND MAKE SURE THE DATA MANAGER IS IN SYNCH WITH THE PANE
	MetroMapData data = (MetroMapData)app.getDataComponent();
	data.setNodes(canvas.getChildren());

	// AND NOW SETUP THE WORKSPACE
	workspace = new BorderPane();
	((BorderPane)workspace).setLeft(editToolbar);
	((BorderPane)workspace).setCenter(canvasHolder);
    }
    
    // HELPER SETUP METHOD
    private void initControllers() {
	// MAKE THE EDIT CONTROLLER
	mapEditController = new MapEditController(app);
        MetroMapData dataManager = new MetroMapData(app);
        canvasController = new CanvasController(app);
        undo.setOnMousePressed(e -> {
            jTPS.undoTransaction();
            reloadWorkspace(app.getDataComponent());
        });
        redo.setOnMousePressed(e -> {
            jTPS.doTransaction();
            reloadWorkspace(app.getDataComponent());
        });
	about.setOnAction(e -> {
            mapEditController.handleAboutRequest();
        });
        export.setOnAction(e -> {
            try {
                dataManager.processExport();
            } catch (IOException ex) {
                Logger.getLogger(MetroMapWorkspace.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        c1.setOnMouseClicked(e -> {
            mapEditController.processEditSelectedLine();
        });
        
        c2.setOnMouseClicked(e -> {
            mapEditController.processEditSelectedStation();
        });
        
        addLineButton.setOnMouseClicked(e -> {
            mapEditController.processSelectLineToDraw();
        });
        
        removeLineButton.setOnAction(e -> {
            mapEditController.processRemoveSelectedLine();
        });
        
        addStation.setOnMouseClicked(e -> {
           mapEditController.processSelectStationToDraw();
        });
        removeStation.setOnMouseClicked(e -> {
            mapEditController.processRemoveSelectedStation();
        });
        
        moveLabelButton.setOnAction(e -> {
            mapEditController.processMoveLabel();
        });
        
        rotateLabelButton.setOnAction(e -> {
            mapEditController.processRotateLabel();
        });
        
        addStationToLine.setOnMousePressed(e -> {
            mapEditController.processAddStationToLine();
        });
        
        setImageBackgroundButton.setOnMousePressed(e -> {
            dataManager.setImageBackground();
        });
        
        addImageButton.setOnAction(e -> {
            mapEditController.processSelectImageToDraw();
        });
        
        addLabelButton.setOnAction(e -> {
            mapEditController.processSelectLabelToDraw();
        });
        
        removeStationFromLine.setOnMousePressed(e -> {
            mapEditController.processRemoveStationFromLine();
        });
        
        listStations.setOnAction(e -> {
            mapEditController.processListStationsOnLine(this.getMetroLine((String) this.getMetroLines().getSelectionModel().getSelectedItem()));
        });
        
        lineThickness.valueProperty().addListener(e -> {
            this.getMetroLine((String) this.getMetroLines().getSelectionModel().getSelectedItem()).setStrokeWidth(lineThickness.valueProperty().doubleValue());
        });
        
        stationRadiusSlider.valueProperty().addListener(e -> {
            this.getMetroStation((String) this.getMetroStations().getSelectionModel().getSelectedItem()).setRadius(stationRadiusSlider.valueProperty().doubleValue());
        });
        
        backgroundColorPicker.setOnAction(e -> {
            mapEditController.processSelectBackgroundColor();
        });
        
        fontColorPicker.valueProperty().addListener(e -> {
            dataManager.setTextColor(fontColorPicker.getValue());
            this.getFontColorPicker().setValue(fontColorPicker.getValue());
        });
        
        fontFamilyPicker.setOnAction(e -> {
            mapEditController.processChangeFontFamily();
        });
        
        fontSizePicker.setOnAction(e -> {
            mapEditController.processChangeFontSize();
        });
        
        boldButton.setOnAction(e -> {
            mapEditController.processBoldRequest();
        });
        
        italicizeButton.setOnAction(e -> {
            mapEditController.processItalicsRequest();
        });
        
        gridOnOffBox.selectedProperty().addListener(e -> {
            mapEditController.processgridOnOff();                                                               
        });
        
        snapToGrid.setOnAction(e -> {
            mapEditController.processSnapToGrid();
        });
        
        originToDestButton.setOnAction(e -> {
            mapEditController.processOriginToDest();
        });
        
        saveAs.setOnAction(e-> {
            try {
                ((MetroMapFiles)app.getFileComponent()).newRequest(dataManager, app);
                ((MetroMapFiles)app.getFileComponent()).saveData(dataManager, app.getGUI().getFileController().getCurrentWorkFile().getName());
            } catch (IOException ex) {
                Logger.getLogger(MetroMapWorkspace.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        zoomInButton.setOnAction(e -> {
            mapEditController.processZoomIn();
        });
        
        zoomOutButton.setOnAction(e -> {
            mapEditController.processZoomOut();
        });
        
        increaseMapSizeButton.setOnAction(e -> {
            mapEditController.processIncreaseSize();
        });
        
        decreaseMapSizeButton.setOnAction(e -> {
            mapEditController.processDecreaseSize();
        });
        
        canvasHolder.setOnKeyPressed(e -> {
            mapEditController.processKeyPress(e.getCode());
        });
        
        removeElementButton.setOnAction(e -> {
            mapEditController.processRemoveElement();
        });
        gui.getWindow().setOnCloseRequest(e -> {
            app.getGUI().getFileController().handleExitRequest();
        });
	
	canvas.setOnMousePressed(e->{
	    canvasController.processCanvasMousePress((int)e.getX(), (int)e.getY());
	});
	canvas.setOnMouseReleased(e->{
	    canvasController.processCanvasMouseRelease((int)e.getX(), (int)e.getY());
	});
	canvas.setOnMouseDragged(e->{
	    canvasController.processCanvasMouseDragged((int)e.getX(), (int)e.getY());
	});
    }

    // HELPER METHOD
    public void loadSelectedShapeSettings(Node shape) {
	/*if (shape != null) {
            if (!(shape instanceof DraggableImage)) {
	    Color fillColor = (Color)shape.getFill();
	    Color strokeColor = (Color)shape.getStroke();
	    double lineThickness = shape.getStrokeWidth();
            }
	}*/
    }

    /**
     * This function specifies the CSS style classes for all the UI components
     * known at the time the workspace is initially constructed. Note that the
     * tag editor controls are added and removed dynamicaly as the application
     * runs so they will have their style setup separately.
     */
    public void initStyle() {
	// NOTE THAT EACH CLASS SHOULD CORRESPOND TO
	// A STYLE CLASS SPECIFIED IN THIS APPLICATION'S
	// CSS FILE
	canvas.getStyleClass().add("/src/css/metroMap_style.css");
        canvas.setStyle("-fx-background-color: #323232");
        gui.getAppPane().setStyle("-fx-background-color:#323232");
        
        saveAs.getStyleClass().add(CLASS_BUTTON);
        undo.getStyleClass().add(CLASS_BUTTON);
        redo.getStyleClass().add(CLASS_BUTTON);
        export.getStyleClass().add(CLASS_BUTTON);
        about.getStyleClass().add(CLASS_BUTTON);
        
        aboutBar.getStyleClass().add(CLASS_BORDERED_PANE);
        undoRedo.getStyleClass().add(CLASS_BORDERED_PANE);
        
	
	// COLOR PICKER STYLE
	metroLines.getStyleClass().add(CLASS_BUTTON);
	editLineButton.getStyleClass().add(CLASS_BUTTON);
        addLineButton.getStyleClass().add(CLASS_BUTTON);
        removeLineButton.getStyleClass().add(CLASS_BUTTON);
        addStationToLine.getStyleClass().add(CLASS_BUTTON);
        removeStationFromLine.getStyleClass().add(CLASS_BUTTON);
        listStations.getStyleClass().add(CLASS_BUTTON);
        //1lineThickness.getStyleClass().add(CLASS_BUTTON);
        
        metroStations.getStyleClass().add(CLASS_BUTTON);
        //stationColorPicker.getStyleClass().add(CLASS_BUTTON);
        snapToGrid.getStyleClass().add(CLASS_BUTTON);
        moveLabelButton.getStyleClass().add(CLASS_BUTTON);
        rotateLabelButton.getStyleClass().add(CLASS_BUTTON);
        addStation.getStyleClass().add(CLASS_BUTTON);
        removeStation.getStyleClass().add(CLASS_BUTTON);
        
     
        originStationPicker.getStyleClass().add(CLASS_BUTTON);
        destStationPicker.getStyleClass().add(CLASS_BUTTON);
        originToDestButton.getStyleClass().add(CLASS_BUTTON);
        
	backgroundColorPicker.getStyleClass().add(CLASS_BUTTON);
	setImageBackgroundButton.getStyleClass().add(CLASS_BUTTON);
        addImageButton.getStyleClass().add(CLASS_BUTTON);
        addLabelButton.getStyleClass().add(CLASS_BUTTON);
        removeElementButton.getStyleClass().add(CLASS_BUTTON);
        
        fontColorPicker.getStyleClass().add(CLASS_BUTTON);
        boldButton.getStyleClass().add(CLASS_BUTTON);
        italicizeButton.getStyleClass().add(CLASS_BUTTON);
        fontSizePicker.getStyleClass().add(CLASS_BUTTON);
        fontFamilyPicker.getStyleClass().add(CLASS_BUTTON);
        
        zoomInButton.getStyleClass().add(CLASS_BUTTON);
        zoomOutButton.getStyleClass().add(CLASS_BUTTON);
        increaseMapSizeButton.getStyleClass().add(CLASS_BUTTON);
        decreaseMapSizeButton.getStyleClass().add(CLASS_BUTTON);
        
        
	editToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR);
	row1Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	row2Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	row3Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	//backgroundColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	
	row4Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	//fillColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	row5Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	//outlineColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	row6Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	//outlineThicknessLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	//row7Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
    }

    /**
     * This function reloads all the controls for editing logos
     * the workspace.
     */
    @Override
    public void reloadWorkspace(AppDataComponent data) {
	MetroMapData dataManager = (MetroMapData)data;
	if (dataManager.isInState(MetroMapState.STARTING_LINE)) {
            addLineButton.setDisable(true);
            removeLineButton.setDisable(false);
            addStationToLine.setDisable(false);
            //zremoveStationFromLine.setDisable(metroLines.getItems().size() > 0);
            listStations.setDisable(false);
            addStation.setDisable(false);
            //removeStation.setDisable(false);
            snapToGrid.setDisable(false);
            moveLabelButton.setDisable(false);
            rotateLabelButton.setDisable(false);
            originToDestButton.setDisable(false);
            setImageBackgroundButton.setDisable(false);
            addImageButton.setDisable(false);
            addLabelButton.setDisable(false);

            //removeElementButton.setDisable(dataManager.getSelectedNode() == null);

        } else if (dataManager.isInState(MetroMapState.STARTING_OVERLAY)) {
            addLineButton.setDisable(false);
            removeLineButton.setDisable(false);
            addStationToLine.setDisable(false);
            //removeStationFromLine.setDisable(metroLines.getItems().size() > 0);
            listStations.setDisable(false);
            addStation.setDisable(false);
            //removeStation.setDisable(false);
            snapToGrid.setDisable(false);
            moveLabelButton.setDisable(false);
            rotateLabelButton.setDisable(false);
            originToDestButton.setDisable(false);
            setImageBackgroundButton.setDisable(false);
            addImageButton.setDisable(true);
            addLabelButton.setDisable(false);
            //removeElementButton.setDisable(dataManager.getSelectedNode() == null);

        }else if (dataManager.isInState(MetroMapState.SELECTING_SHAPE)) {
            addLineButton.setDisable(false);
            removeLineButton.setDisable(false);
            addStationToLine.setDisable(false);
            removeStationFromLine.setDisable(metroLines.getItems().size() > 0);
            listStations.setDisable(false);
            addStation.setDisable(false);
            //removeStation.setDisable(false);
            snapToGrid.setDisable(false);
            moveLabelButton.setDisable(false);
            rotateLabelButton.setDisable(false);
            originToDestButton.setDisable(false);
            setImageBackgroundButton.setDisable(false);
            addImageButton.setDisable(true);
            addLabelButton.setDisable(false);
            //removeElementButton.setDisable(dataManager.getSelectedNode() == null);

        } 
        else if (dataManager.isInState(MetroMapState.STARTING_TEXT)) {
            addLineButton.setDisable(false);
            removeLineButton.setDisable(false);
            addStationToLine.setDisable(false);
            removeStationFromLine.setDisable(metroLines.getItems().size() > 0);
            listStations.setDisable(false);
            addStation.setDisable(false);
            //removeStation.setDisable(false);
            snapToGrid.setDisable(false);
            moveLabelButton.setDisable(false);
            rotateLabelButton.setDisable(false);
            originToDestButton.setDisable(false);
            setImageBackgroundButton.setDisable(false);
            addImageButton.setDisable(false);
            addLabelButton.setDisable(true);
            //removeElementButton.setDisable(dataManager.getSelectedNode() == null);

        } else if (dataManager.isInState(MetroMapState.STARTING_STATION)) {
            addLineButton.setDisable(false);
            removeLineButton.setDisable(false);
            addStationToLine.setDisable(true);
            removeStationFromLine.setDisable(metroLines.getItems().size() > 0);
            listStations.setDisable(false);
            addStation.setDisable(false);
            //removeStation.setDisable(true);
            snapToGrid.setDisable(false);
            moveLabelButton.setDisable(false);
            rotateLabelButton.setDisable(false);
            originToDestButton.setDisable(false);
            setImageBackgroundButton.setDisable(false);
            addImageButton.setDisable(false);
            addLabelButton.setDisable(false);
            //removeElementButton.setDisable(dataManager.getSelectedNode() == null);
            
        } else if (dataManager.isInState(MetroMapState.SELECTING)
                || dataManager.isInState(MetroMapState.DRAGGING)
                || dataManager.isInState(MetroMapState.DRAGGING_NOTHING)) {

            boolean notSelected = dataManager.getSelectedNode() == null;
            addLineButton.setDisable(false);
            removeLineButton.setDisable(false);
            addStationToLine.setDisable(false);
            removeStationFromLine.setDisable(metroLines.getItems().size() > 0);
            listStations.setDisable(false);
            addStation.setDisable(false);
            //removeStation.setDisable(false);
            snapToGrid.setDisable(false);
            moveLabelButton.setDisable(false);
            rotateLabelButton.setDisable(false);
            originToDestButton.setDisable(false);
            setImageBackgroundButton.setDisable(false);
            addImageButton.setDisable(false);
            addLabelButton.setDisable(true);
            //removeElementButton.setDisable(dataManager.getSelectedNode() == null);

        } else if (dataManager.isInState(MetroMapState.ROTATING_LABEL)) {
            addLineButton.setDisable(false);
            removeLineButton.setDisable(false);
            addStationToLine.setDisable(false);
            removeStationFromLine.setDisable(metroLines.getItems().size() > 0);
            listStations.setDisable(false);
            addStation.setDisable(true);
            //removeStation.setDisable(true);
            snapToGrid.setDisable(false);
            moveLabelButton.setDisable(false);
            rotateLabelButton.setDisable(false);
            originToDestButton.setDisable(false);
            setImageBackgroundButton.setDisable(false);
            addImageButton.setDisable(false);
            addLabelButton.setDisable(false);
            //removeElementButton.setDisable(dataManager.getSelectedNode() == null);
            
        }
        //removeStationFromLine.setDisable(false);
	//backgroundColorPicker.setValue(Color.valueOf(WHITE_HEX));

    }
    
    @Override
    public void resetWorkspace() {
        // WE ARE NOT USING THIS, THOUGH YOU MAY IF YOU LIKE
    }
    
    // GETTERS FOR ALL COMBOBOXES, COLOR PICKERS, AND SLIDERS
    
    public ComboBox getMetroLines() {
        return metroLines;
    }
    
    public StackPane getEditLine() {
        return editLine;
    }
    
    public Slider getLineThickness() {
        return lineThickness;
    }
    
    public ComboBox getMetroStations() {
        return metroStations;
    }
    
    public ColorPicker getStationColorPicker() {
        return stationColorPicker;
    }
    
    public Slider getStationThickness() {
        return stationRadiusSlider;
    }
    
    public ComboBox getOriginPicker() {
        return originStationPicker;
    }
    
    public ComboBox getDestPicker() {
        return destStationPicker;
    }
    
    public ColorPicker getBackgroundColorPicker() {
        return backgroundColorPicker;
    }
    
    public ColorPicker getFontColorPicker() {
        return fontColorPicker;
    }
    
    public ComboBox getFontSizePicker() {
        return fontSizePicker;
    }
    
    public ComboBox getFontFamilyPicker() {
        return fontFamilyPicker;
    }
    
    public CheckBox getGridCheckBox() {
        return gridOnOffBox;
    }
    
    public DraggableLine getMetroLine(String name) {
        DraggableLine line = new DraggableLine((MetroMapData)app.getDataComponent());
            for (Node n:canvas.getChildren()) {
                if (n instanceof DraggableLine) {
                    if (((DraggableLine)n).getName().equals(name)) {
                        line = (DraggableLine) n;
                    }
                }
            }
        return line;
    }
    
    public DraggableStation getMetroStation(String name) {
        DraggableStation station = new DraggableStation((MetroMapData)app.getDataComponent());
            for (Node n:canvas.getChildren()) {
                if (n instanceof DraggableStation) {
                    
                    if (((DraggableStation)n).getName().equals(name)) {
                        station = (DraggableStation)n;
                    }
                }
            }
        return station;
    }
    
    public StackPane getEditStation() {
        return editStation;
    }
    
    public AppTemplate getApp() {
        return app;
    }
    
    public jTPS getJTPS() {
        return jTPS;
    }
    
}