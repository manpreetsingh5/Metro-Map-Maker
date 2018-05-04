package mmm.data;

import mmm.data.EditLineWindow;
import mmm.data.addStationWindow;
import mmm.data.addLineWindow;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import mmm.gui.MetroMapWorkspace;
import djf.components.AppDataComponent;
import djf.AppTemplate;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.imageio.ImageIO;
import jtps.jTPS_Transaction;
import mmm.Transactions.FillColorTransaction;
import mmm.Transactions.RemoveTransaction;
import mmm.Transactions.addImageTransaction;
import mmm.Transactions.addLineTransaction;
import mmm.Transactions.addStationTransaction;
import mmm.Transactions.addTextTransaction;
import mmm.Transactions.removeLineTransaction;
import static mmm.data.MetroMapState.SELECTING_SHAPE;
import static mmm.data.MetroMapState.SIZING_SHAPE;
import mmm.file.MetroMapFiles;

/**
 * This class serves as the data management component for this application.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class MetroMapData implements AppDataComponent {
    // FIRST THE THINGS THAT HAVE TO BE SAVED TO FILES

    // THESE ARE THE SHAPES TO DRAW
    ObservableList<Node> nodes;
    ObservableList<DraggableLine> lines;
    ObservableList<DraggableStation> stations;

    // THE BACKGROUND COLOR
    Color backgroundColor;

    // AND NOW THE EDITING DATA
    // THIS IS THE SHAPE CURRENTLY BEING SIZED BUT NOT YET ADDED
    Node newNode;

    // THIS IS THE SHAPE CURRENTLY SELECTED
    Node selectedNode;

    // SHAPE USED TO SAVE LAST CUT/COPIED OBJECT
    Node savedNode;
    // FOR FILL AND OUTLINE
    Color currentFillColor;
    Color currentOutlineColor;
    double currentBorderWidth;

    // CURRENT STATE OF THE APP
    MetroMapState state;

    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;

    // USE THIS WHEN THE SHAPE IS SELECTED
    Effect highlightedEffect;

    public static final String WHITE_HEX = "#FFFFFF";
    public static final String BLACK_HEX = "#000000";
    public static final String YELLOW_HEX = "#EEEE00";
    public static final Paint DEFAULT_BACKGROUND_COLOR = Paint.valueOf(WHITE_HEX);
    public static final Paint HIGHLIGHTED_COLOR = Paint.valueOf(YELLOW_HEX);
    public static final int HIGHLIGHTED_STROKE_THICKNESS = 3;

    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public MetroMapData(AppTemplate initApp) {
        // KEEP THE APP FOR LATER
        app = initApp;

        // NO SHAPE STARTS OUT AS SELECTED
        newNode = null;
        selectedNode = null;

        // INIT THE COLORS
        currentFillColor = Color.web(WHITE_HEX);
        currentOutlineColor = Color.web(BLACK_HEX);
        currentBorderWidth = 1;

        // THIS IS FOR THE SELECTED SHAPE
        DropShadow dropShadowEffect = new DropShadow();
        dropShadowEffect.setOffsetX(0.0f);
        dropShadowEffect.setOffsetY(0.0f);
        dropShadowEffect.setSpread(1.0);
        dropShadowEffect.setColor(Color.YELLOW);
        dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
        dropShadowEffect.setRadius(15);
        highlightedEffect = dropShadowEffect;
    }

    public ObservableList<Node> getNodes() {
        return nodes;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getCurrentFillColor() {
        return currentFillColor;
    }

    public Color getCurrentOutlineColor() {
        return currentOutlineColor;
    }

    public double getCurrentBorderWidth() {
        return currentBorderWidth;
    }

    public void setNodes(ObservableList<Node> initNodes) {
        nodes = initNodes;
    }

    public void setBackgroundColor(Color initBackgroundColor) {
        backgroundColor = initBackgroundColor;
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        BackgroundFill fill = new BackgroundFill(backgroundColor, null, null);
        
        Background background = new Background(fill);
        canvas.setBackground(background);
    }
    
    public void setTextColor(Color initColor) {
        MetroMapData dataManager = (MetroMapData)app.getDataComponent();
        if (dataManager.getSelectedNode() instanceof DraggableText) {
            DraggableText text = (DraggableText)dataManager.getSelectedNode();
            FillColorTransaction transaction = new FillColorTransaction(app,text,(Color)text.getFill(),initColor);
        }
    }

    public void setCurrentFillColor(Color initColor) {
        currentFillColor = initColor;
        if (selectedNode != null) {
            ((Shape) selectedNode).setFill(currentFillColor);
        }
    }

    public void setCurrentOutlineColor(Color initColor) {
        currentOutlineColor = initColor;
        if (selectedNode != null) {
            ((Shape) selectedNode).setStroke(initColor);
        }
    }

    public void setCurrentOutlineThickness(int initBorderWidth) {
        currentBorderWidth = initBorderWidth;
        if (selectedNode != null) {
            ((Shape) selectedNode).setStrokeWidth(initBorderWidth);
        }
    }

    public void removeSelectedShape() {
        if (selectedNode != null) {
            nodes.remove(selectedNode);
            selectedNode = null;
        }
    }

    public void moveSelectedShapeToBack() {
        if (selectedNode != null) {
            nodes.remove(selectedNode);
            if (nodes.isEmpty()) {
                nodes.add(selectedNode);
            } else {
                ArrayList<Node> temp = new ArrayList<>();
                temp.add(selectedNode);
                for (Node node : nodes) {
                    temp.add(node);
                }
                nodes.clear();
                for (Node node : temp) {
                    nodes.add(node);
                }
            }
        }
    }

    public void moveSelectedNodeToFront() {
        if (selectedNode != null) {
            nodes.remove(selectedNode);
            nodes.add(selectedNode);
        }
    }

    /**
     * This function clears out the HTML tree and reloads it with the minimal
     * tags, like html, head, and body such that the user can begin editing a
     * page.
     */
    @Override
    public void resetData() {
        setState(SELECTING_SHAPE);
        newNode = null;
        selectedNode = null;

        // INIT THE COLORS
        currentFillColor = Color.web(WHITE_HEX);
        currentOutlineColor = Color.web(BLACK_HEX);
        backgroundColor = Color.web(WHITE_HEX);

        nodes.clear();
        ((MetroMapWorkspace) app.getWorkspaceComponent()).getCanvas().getChildren().clear();
    }

    public void selectSizedNode() {
        if (selectedNode instanceof Shape) {
            if (selectedNode != null) {
                unhighlightNode(selectedNode);
            }
            selectedNode = newNode;
            highlightNode(selectedNode);
            newNode = null;
            if (state == SIZING_SHAPE) {
                state = ((Draggable) selectedNode).getStartingState();
            }
        }
    }

    public void unhighlightNode(Node node) {
        selectedNode.setEffect(null);
    }

    public void highlightNode(Node node) {
        node.setEffect(highlightedEffect);
    }

    public void startNewStation(int x, int y) {
        DraggableStation newStation = new DraggableStation((MetroMapData) app.getDataComponent());

        addStationWindow window = new addStationWindow(newStation);

        newStation.setFill(Paint.valueOf(WHITE_HEX));
        newStation.setStroke(Paint.valueOf(BLACK_HEX));
        newStation.setStrokeWidth(2.5);
        newStation.name = window.getStationName();
        
        newStation.labelPosition = "bottom_right";
        newNode = newStation;
        //newStation.stationName.setText(window.getStationName());
        
        newStation.setRadius(10);
        initStationEnd(newStation, x, y, window);
        newStation.start(x, y);
        
        addStationTransaction transaction = new addStationTransaction(app, newStation);
        ((MetroMapWorkspace)app.getWorkspaceComponent()).getJTPS().addTransaction(transaction);
        setState(MetroMapState.SELECTING_SHAPE);
        
        if (selectedNode != null) {
            unhighlightNode(selectedNode);
            selectedNode = null;
        }
        // USE THE CURRENT SETTINGS FOR THIS NEW SHAPE
        //this.addNode(newStation);
        //this.addNode(newStation.stationName);
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        workspace.getMetroStations().getItems().add(newStation.name);
        workspace.getMetroStations().setValue(newStation.name);
        
        state = MetroMapState.SELECTING_SHAPE;
        
        //transaction.doTransaction();
        //return newStation;
    }

    public DraggableLine startNewLine(int x, int y) {
        //DraggableLine newLine = new DraggableLine();
        DraggableLine newLine = new DraggableLine((double) x, (double) y, (double) x + 200, (double) y);
        newLine.setStroke(Paint.valueOf(BLACK_HEX));
        //newLine.setStrokeWidth(10);

        newNode = newLine;
        addLineWindow window = new addLineWindow(newLine);

        
        initLineEnds(newLine, x, y, window);

        newLine.setStroke(window.getLineColor());
        newLine.setName(window.getLineName());
        newLine.frontText.setStroke(window.getLineColor());
        newLine.frontText.onLine = true;
        newLine.endText.setStroke(window.getLineColor());
        newLine.endText.onLine = true;
        newLine.init();
        //bindLines(newLine);
        initLineHandlers(newLine);
        

        newLine.setStrokeWidth(5.0);
        //initNewShape();
        if (selectedNode != null) {
            unhighlightNode(selectedNode);
            selectedNode = null;
        }
        
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        addLineTransaction addLine = new addLineTransaction(app,newLine);
        workspace.jTPS.addTransaction(addLine);
        // USE THE CURRENT SETTINGS FOR THIS NEW SHAPE
        
        workspace.getMetroLines().getItems().add(newLine.name.toString());
        workspace.getMetroLines().setValue(newLine.name.toString());
        //nodes.add(newNode);
        //state = MetroMapState.SELECTING_SHAPE;
        this.setState(MetroMapState.SELECTING_SHAPE);
        //System.out.println(nodes.size());
        return newLine;
    }

    public void editLine() {
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        DraggableLine line = (DraggableLine) workspace.getMetroLine((String) workspace.getMetroLines().getValue());
        EditLineWindow editLineWindow = new EditLineWindow(line);
        editLineWindow.init();
        //editLineWindow.showAndWait();
        line.setStroke(editLineWindow.getLineColor());
        line.setName(editLineWindow.getLineName());
        line.frontStation.setFill(editLineWindow.getLineColor());
        line.frontText.setText(editLineWindow.getLineName());
        line.frontText.setFill(editLineWindow.getLineColor());
        line.frontText.setStroke(editLineWindow.getLineColor());
        line.endStation.setFill(editLineWindow.getLineColor());
        line.endText.setText(editLineWindow.getLineName());
        line.endText.setFill(editLineWindow.getLineColor());
        line.endText.setStroke(editLineWindow.getLineColor());
        ((Circle) workspace.getEditLine().getChildren().get(0)).setFill(editLineWindow.getLineColor());
        ((Text) workspace.getEditLine().getChildren().get(1)).setText(editLineWindow.getLineColor().toString());

    }

    public void editStation() {
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        DraggableStation station = (DraggableStation) workspace.getMetroStation((String) workspace.getMetroStations().getSelectionModel().getSelectedItem());
        EditStationWindow editStationWindow = new EditStationWindow(station);
        editStationWindow.init();
        station.setFill(editStationWindow.getLineColor());
        ((Circle) workspace.getEditStation().getChildren().get(0)).setFill(editStationWindow.getLineColor());
        ((Text) workspace.getEditStation().getChildren().get(1)).setText(editStationWindow.getLineColor().toString());

    }
    
    public void changeOrientation(DraggableStation station) {
        switch(station.labelPosition) {
            case "bottom_right": {
                station.stationName.xProperty().bind(station.centerXProperty().subtract(25));
                station.stationName.yProperty().bind(station.centerYProperty().add(25));
                station.labelPosition = "bottom_left";
                break;
            }
            case "bottom_left": {
                station.stationName.xProperty().bind(station.centerXProperty().subtract(25));
                station.stationName.yProperty().bind(station.centerYProperty().subtract(25));
                station.labelPosition = "top_left";
                break;
            }
            case "top_left": {
                station.stationName.xProperty().bind(station.centerXProperty().add(25));
                station.stationName.yProperty().bind(station.centerYProperty().subtract(25));
                station.labelPosition = "top_right";
                break;
            }
            case "top_right": {
                station.stationName.xProperty().bind(station.centerXProperty().add(25));
                station.stationName.yProperty().bind(station.centerYProperty().add(25));
                station.labelPosition = "bottom_right";
                break;
            }
        }
            
    }
    
    public void changeAngle(DraggableStation station) {
        station.angle += 30;
        station.stationName.setRotate(station.angle);
        //station.stationName.rotateProperty().add(30);
            
    }

    public void removeLine() {

        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        DraggableLine line = (DraggableLine) workspace.getMetroLine((String) workspace.getMetroLines().getValue());
        if (workspace.getCanvas().getChildren().contains(line)) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setHeaderText("Are you sure you wish to delete this line? ");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {

                removeLineTransaction transaction = new removeLineTransaction(app,line);
                workspace.getJTPS().addTransaction(transaction);
            }
        }

    }

    public void removeStation() {
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        DraggableStation station = (DraggableStation) workspace.getMetroStation((String) workspace.getMetroStations().getValue());
        if (workspace.getCanvas().getChildren().contains(station)) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setHeaderText("Are you sure you wish to delete this station? ");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                workspace.getMetroStations().getItems().remove(workspace.getMetroStations().getItems().indexOf(station.name));
                if (station.lines.size() == 0) {
                    workspace.getCanvas().getChildren().remove(station.stationName);
                    workspace.getCanvas().getChildren().remove(station);
                } else {
                    for (DraggableLine l : station.lines) {
                        int index = l.stations.indexOf(station);
                        l.getElements().remove(l.getElements().get(index));
                    }
                    workspace.getCanvas().getChildren().remove(station.stationName);
                    workspace.getCanvas().getChildren().remove(station);
                }
            }
        }
    }

    public void addStationToLine(DraggableStation station) {
        LineTo newLineTo = new LineTo(station.getX(), station.getY());
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        DraggableLine line = ((MetroMapWorkspace)app.getWorkspaceComponent()).getMetroLine((String)((MetroMapWorkspace)app.getWorkspaceComponent()).getMetroLines().getSelectionModel().getSelectedItem());
        Point2D p1 = new Point2D(station.getX(), station.getY());
        Point2D p2 = new Point2D(((MoveTo) line.getElements().get(0)).getX(), ((MoveTo) line.getElements().get(0)).getY());
        double minDist = p1.distance(p2);
        int indexOfMin = 0;
        for (PathElement p : line.getElements().subList(1, line.getElements().size() - 1)) {
            p2 = new Point2D(((LineTo) p).getX(), ((LineTo) p).getY());
            if (p1.distance(p2) < minDist) {
                minDist = p1.distance(p2);
                indexOfMin = line.getElements().indexOf(p);

            }
        }
//        p2 = new Point2D(((LineTo) line.getElements().get(indexOfMin)).getX(), ((LineTo) line.getElements().get(indexOfMin)).getY());
        if (indexOfMin == 0) {
            line.stations.add(1, station);
            station.lines.add(line);
            newLineTo.xProperty().bindBidirectional(station.centerXProperty());
            newLineTo.yProperty().bindBidirectional(station.centerYProperty());
            line.getElements().add(1, newLineTo);
        } else if (indexOfMin == line.getElements().size()-1) {
            line.stations.add(line.stations.size() - 2, station);
            station.lines.add(line);
            newLineTo.xProperty().bindBidirectional(station.centerXProperty());
            newLineTo.yProperty().bindBidirectional(station.centerYProperty());
            line.getElements().add(line.getElements().size() - 2, newLineTo);
        } else {
            Point2D minOne;
            if (indexOfMin == 1)
                minOne = new Point2D(((MoveTo) line.getElements().get(indexOfMin - 1)).getX(), ((MoveTo) line.getElements().get(indexOfMin - 1)).getY());
            else
                minOne = new Point2D(((LineTo) line.getElements().get(indexOfMin - 1)).getX(), ((LineTo) line.getElements().get(indexOfMin - 1)).getY());
            Point2D minTwo = new Point2D(((LineTo) line.getElements().get(indexOfMin + 1)).getX(), ((LineTo) line.getElements().get(indexOfMin + 1)).getY());
            if (minOne.distance(p2) >= minTwo.distance(p2)) {
                line.stations.add(indexOfMin, station);
                station.lines.add(line);
                newLineTo.xProperty().bindBidirectional(station.centerXProperty());
                newLineTo.yProperty().bindBidirectional(station.centerYProperty());
                line.getElements().add(indexOfMin, newLineTo);
            } else {
                line.stations.add(indexOfMin + 1, station);
                station.lines.add(line);
                newLineTo.xProperty().bindBidirectional(station.centerXProperty());
                newLineTo.yProperty().bindBidirectional(station.centerYProperty());
                line.getElements().add(indexOfMin + 1, newLineTo);
            }
        }
        station.onLine = true;
        if (!(workspace.getOriginPicker().getItems().contains(station.name) && workspace.getDestPicker().getItems().contains(station.name))) {
            workspace.getOriginPicker().getItems().add(station.name);
            workspace.getDestPicker().getItems().add(station.name);
        }

    }

    public void removeStationFromLine(DraggableStation station) {
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        DraggableLine line = (DraggableLine) workspace.getMetroLine((String) workspace.getMetroLines().getValue());

        if (line.stations.contains(station)) {
            int index = line.stations.indexOf(station);
            if (line.getElements().size() == 3) {
                if (index != 0 || index != line.getElements().size()) {
                    line.getElements().remove(index);
                }
            } else {
                if (index != 0 || index != line.getElements().size()) {
                    line.getElements().remove(index);
                }
            }
            station.lines.remove(line);
            line.stations.remove(station);

        }
        if (station.lines.size()==0) {
            station.onLine = false;
            workspace.getOriginPicker().getItems().remove(station.name);
            workspace.getDestPicker().getItems().remove(station.name);
        }
    }

    public void bindLines(DraggableLine line2) {
        line2.frontText.xProperty().bindBidirectional(line2.frontStation.centerXProperty());
        line2.frontText.yProperty().bindBidirectional(line2.frontStation.centerYProperty());
        line2.endText.xProperty().bindBidirectional(line2.endStation.centerXProperty());
        line2.endText.yProperty().bindBidirectional(line2.endStation.centerYProperty());
    }

    // Returns the angle measure between a line and the horizontal due east. Returns a negative value for clockwise values.
    public double lineAngle(DraggableLine line) {
        DraggableLine horizontal = new DraggableLine(line.startX, line.startY, line.startX + 200, line.startY);
        Point2D p1 = new Point2D(line.startX, line.startY);
        Point2D p2 = new Point2D(line.endX, line.endY);
        double dist = Math.abs(p1.distance(p2));
        double dist2 = line.endY - horizontal.endY;
        double leg1 = Math.sqrt(Math.pow(dist, 2) - Math.pow(dist2, 2));
        double angle = Math.atan(((dist2) / leg1));
        return angle;
    }

    public void initLineEnds(DraggableLine newLine, int x, int y, addLineWindow window) {
        DraggableText front = new DraggableText(x - 10, y);
        //front.setText(window.getLineName());
        front.setText(window.getLineName());
        front.setStroke(Paint.valueOf(BLACK_HEX));
        front.setStrokeWidth(1.0);
        newLine.frontText = front;

        DraggableText end = new DraggableText(x + 210, y);
        //end.setText(window.getLineName());
        end.setText(window.getLineName());
        end.setStroke(Paint.valueOf(BLACK_HEX));
        end.setStrokeWidth(1.0);
        newLine.endText = end;

        DraggableStation frontStation = new DraggableStation(this, (double) x, (double) y, 10.0);
        frontStation.setFill(window.getLineColor());
        frontStation.setStroke(Paint.valueOf(BLACK_HEX));
        frontStation.setStrokeWidth(2.5);
        newLine.frontStation = frontStation;
        newLine.frontStation.onLine = true;
        newLine.frontStation.labelPosition = "top_right";

        DraggableStation endStation = new DraggableStation(this, (double) x + 200, (double) y, 10.0);
        endStation.setFill(window.getLineColor());
        endStation.setStroke(Paint.valueOf(BLACK_HEX));
        endStation.setStrokeWidth(2.5);
        newLine.endStation = endStation;
        newLine.endStation.onLine = true;
        newLine.endStation.labelPosition = "top_right";
        newLine.stations.add(frontStation);
        newLine.stations.add(endStation);
        
        newLine.frontText.xProperty().bindBidirectional(newLine.frontStation.centerXProperty());
        newLine.frontText.yProperty().bindBidirectional(newLine.frontStation.centerYProperty());
        newLine.endText.xProperty().bindBidirectional(newLine.endStation.centerXProperty());
        newLine.endText.yProperty().bindBidirectional(newLine.endStation.centerYProperty());

        //nodes.addAll(newLine.frontStation, newLine.endStation);
        newLine.frontStation.setVisible(false);
        newLine.endStation.setVisible(false);
    }

    public void initStationEnd(DraggableStation newStation, int x, int y, addStationWindow window) {
        DraggableText name = new DraggableText(x, y);

        name.setText(window.getStationName());
        name.setStroke(Paint.valueOf(WHITE_HEX));
        //name.setStrokeWidth(2.5);
        newStation.stationName = name;
        newStation.stationName.onLine = true;
        newStation.stationName.xProperty().bind(newStation.centerXProperty().add(20));
        newStation.stationName.yProperty().bind(newStation.centerYProperty().add(20));

        //nodes.add(name);

    }

    public void initLineHandlers(DraggableLine newLine) {
        newLine.frontText.setOnMousePressed(e -> {
            newLine.frontStation.setVisible(true);
            newLine.frontText.setVisible(false);
        });
        newLine.frontText.setOnMouseReleased(e -> {
            newLine.frontStation.setVisible(false);
            newLine.frontText.setVisible(true);
        });

        newLine.endText.setOnMousePressed(e -> {
            newLine.endStation.setVisible(true);
            newLine.endText.setVisible(false);
        });
        newLine.endText.setOnMouseReleased(e -> {
            newLine.endStation.setVisible(false);
            newLine.endText.setVisible(true);
        });

        newLine.frontStation.setOnMousePressed(e -> {
            newLine.frontStation.setVisible(true);
            newLine.frontText.setVisible(false);
        });
        newLine.frontStation.setOnMouseReleased(e -> {
            newLine.frontStation.setVisible(false);
            newLine.frontText.setVisible(true);
        });

        newLine.endStation.setOnMousePressed(e -> {
            newLine.endStation.setVisible(false);
            newLine.endText.setVisible(true);
        });
        newLine.endStation.setOnMouseReleased(e -> {
            newLine.endStation.setVisible(true);
            newLine.endText.setVisible(false);
        });
    }

     public DraggableText startNewText(int x, int y) {
         
       MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        TextInputDialog dialog = new TextInputDialog("Text");
        dialog.setTitle("Text Input Dialog");
        dialog.setHeaderText("");
        dialog.setContentText("Enter Text:");
        Optional<String> result = dialog.showAndWait();
        DraggableText text = new DraggableText();
        text.setText(result.get());
        text.setFont(Font.font("Times New Roman",18));
        text.setFill(Paint.valueOf(WHITE_HEX));
        //text.start((int) text.getX(), (int) text.getY());
        text.setX(x);
        text.setY(y);
        addTextTransaction transaction = new addTextTransaction(app,text);
        workspace.jTPS.addTransaction(transaction);
        state = MetroMapState.SELECTING_SHAPE;
        return text;
        
    }
    public DraggableImage startNewImage(int x , int y) {
        
        
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        String filePath = file.toURI().toString();
        Image image = new Image(file.toURI().toString());
        DraggableImage draggableImage = new DraggableImage(filePath);
        draggableImage.setWidth(image.getWidth());
        draggableImage.setHeight(image.getHeight());
        draggableImage.setFill(new ImagePattern(image));
        addImageTransaction transaction = new addImageTransaction(app,draggableImage);
        workspace.getJTPS().addTransaction((jTPS_Transaction)transaction);
        state = MetroMapState.SELECTING_SHAPE;
        return draggableImage;
        
    }
    public void initNewShape() {
        // DESELECT THE SELECTED SHAPE IF THERE IS ONE
        if (selectedNode instanceof Node) {
            if (selectedNode != null) {
                unhighlightNode(selectedNode);
                selectedNode = null;
            }

            // USE THE CURRENT SETTINGS FOR THIS NEW SHAPE
            MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
            //((Shape)newShape).setFill(workspace.getFillColorPicker().getValue());
            //((Shape)newShape).setStroke(workspace.getOutlineColorPicker().getValue());
            //((Shape)newShape).setStrokeWidth(workspace.getOutlineThicknessSlider().getValue());
        }
        // ADD THE SHAPE TO THE CANVAS
        // if (!(newShape instanceof DraggableText))
        //   shapes.add(newShape);

        // GO INTO SHAPE SIZING MODE
        state = MetroMapState.SIZING_SHAPE;
    }

    public Node getNewNode() {
        return newNode;
    }

    public Node getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(Node initSelectedNode) {
        selectedNode = initSelectedNode;
    }

    public Node selectTopNode(int x, int y) {
        Node node = getTopNode(x, y);

        if (selectedNode != null) {
            unhighlightNode(selectedNode);
        }
        if (node != null && ((node instanceof DraggableImage ) || (node instanceof DraggableStation) || (node instanceof DraggableText))) {
            highlightNode(node);
            MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
            workspace.loadSelectedShapeSettings((Shape) node);
        }
        selectedNode = node;

        if (node != null) {
            //if (shape instanceof DraggableLine)
            //((DraggableLine)shape).start(x, y);
            /*else if (shape instanceof DraggableImage)
                ((DraggableImage)shape).setStartXY(x, y);
            else if (shape instanceof DraggableText)
                ((DraggableText)shape).setStartXY(x, y);*/
            //else
            // ((Draggable)shape).start(x, y);
        }
        if (node == selectedNode) {
            return node;
        }
        return node;
    }

    public Node getTopNode(int x, int y) {
        for (int i = nodes.size() - 1; i >= 0; i--) {
            Node node = (Node) nodes.get(i);
            if (node.contains(x, y)) {
                return node;
            }
        }
        return null;
    }

    public void addNode(Node nodeToAdd) {
        nodes.add(nodeToAdd);
    }
    
    

    public void removeShape(Node nodeToRemove) {
        nodes.remove(nodeToRemove);
    }

    public MetroMapState getState() {
        return state;
    }

    public void setState(MetroMapState initState) {
        state = initState;
    }

    public boolean isInState(MetroMapState testState) {
        return state == testState;
    }

    public Node getSavedShape() {
        return savedNode;
    }

    public void setSavedShape(Node savedNode) {
        this.savedNode = savedNode;
    }

    public void setNewShape(Node newNode) {
        this.newNode = newNode;
    }

    public File getCurrentWorkFile() {
        return app.getGUI().getFileController().getCurrentWorkFile();
    }

    public void processExport() throws IOException {
        ///System.out.println(this.nodes.size());
        ((MetroMapFiles) app.getFileComponent()).exportData(app.getDataComponent(), "./Exports" + "/" + this.getCurrentWorkFile().getName() + "/" + this.getCurrentWorkFile().getName());
        this.processSnapshot();
        try {
            ((MetroMapFiles) app.getFileComponent()).exportData(this, this.getCurrentWorkFile().getAbsolutePath());
        } catch (IOException ex) {
            Logger.getLogger(MetroMapWorkspace.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void processSnapshot() throws IOException {
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
        File file = new File("./Exports/" + app.getGUI().getFileController().getCurrentWorkFile().getName() + "/" + app.getGUI().getFileController().getCurrentWorkFile().getName() + ".png");
        file.createNewFile();
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText("The file has been exported.");
        alert.showAndWait();
    }

    public void addElementToComboBox(Draggable node) {
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        if (node instanceof DraggableLine) {
            workspace.getMetroLines().getItems().add(((DraggableLine) node).name);
        } else if (node instanceof DraggableStation) {
            workspace.getMetroStations().getItems().add(((DraggableStation) node).name);
            workspace.getOriginPicker().getItems().add(((DraggableStation) node).name);
        workspace.getDestPicker().getItems().add(((DraggableStation) node).name);
        }
    }

    public void setImageBackground() {
        MetroMapData dataManager = (MetroMapData)app.getDataComponent();
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setHeaderText("Are you sure you would like to set an Image Background?");
        confirm.showAndWait().ifPresent((ButtonType response) -> {
            if (response == ButtonType.OK) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().addAll(
                        //new ExtensionFilter("Text Files", "*.txt"),
                        new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
                        //new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"));
                        //new ExtensionFilter("All Files", "*.*"));
                File selectedFile = fileChooser.showOpenDialog(app.getGUI().getWindow());
                if (selectedFile != null) {
                    //app.getGUI().getWindow().display(selectedFile);
                    Rectangle background = new Rectangle();
                    background.widthProperty().bind(((MetroMapWorkspace)app.getWorkspaceComponent()).getCanvas().widthProperty());
                    background.heightProperty().bind(((MetroMapWorkspace)app.getWorkspaceComponent()).getCanvas().heightProperty());
                    background.setFill(new ImagePattern(new Image("file:" + selectedFile.getAbsolutePath())));
                    dataManager.getNodes().add(0,background);
                  
                }
            }
            else if (response == ButtonType.CANCEL) {
                confirm.close();
            }
        });
    }
    
    public void removeMoveElement() {
        MetroMapData dataManager = (MetroMapData)app.getDataComponent();
        Draggable node = (Draggable)dataManager.getSelectedNode();
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        if (node instanceof DraggableText || node instanceof DraggableImage){
            RemoveTransaction remove = new RemoveTransaction(app,node);
            workspace.jTPS.addTransaction(remove);
        }  
    }

    public void snapCurrentStationToGrid() {
         //public void snapCurrentStationToGrid() {
        if(selectedNode != null){
            double x = 0;
            double y = 0;
           if (selectedNode instanceof DraggableStation) {
                x = ((DraggableStation)selectedNode).getCenterX();
                y = ((DraggableStation)selectedNode).getCenterY();
           }
         
           
           if(x % 25 <= 12){
               x -= x % 25;
           }else{
               x += x % 25;
           }
           
           if(y % 25 > 12){
               y -= y % 25;
           }else{
               y += y % 25;
           }
           
           ((DraggableStation)selectedNode).setCenterX(x);
           ((DraggableStation)selectedNode).setCenterY(y);
       }
    }

    public void removeElement() {
        if (this.getSelectedNode() instanceof DraggableText || this.getSelectedNode() instanceof DraggableImage) {
            this.removeShape(this.getSelectedNode());
        }
    }
    
}
