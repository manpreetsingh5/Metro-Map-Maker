package mmm.file;

import djf.AppTemplate;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import djf.controller.AppFileController;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.StageStyle;
import javax.json.JsonString;
import mmm.data.MetroMapData;
import mmm.data.DraggableStation;
import mmm.data.DraggableLine;
import mmm.data.Draggable;
import static mmm.data.Draggable.LINE;
import static mmm.data.Draggable.STATION;
import mmm.data.DraggableImage;
import mmm.data.DraggableText;
import static mmm.data.MetroMapData.WHITE_HEX;
import mmm.gui.MetroMapWorkspace;

/**
 * This class serves as the file management component for this application,
 * providing all I/O services.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class MetroMapFiles implements AppFileComponent {

    // FOR JSON LOADING
    static final String JSON_BG_COLOR = "background_color";
    static final String JSON_RED = "red";
    static final String JSON_GREEN = "green";
    static final String JSON_BLUE = "blue";
    static final String JSON_ALPHA = "alpha";
    static final String JSON_NAME = "name";
    static final String JSON_SHAPES = "Other";
    static final String JSON_SHAPE = "shape";
    static final String JSON_TYPE = "type";
    static final String JSON_X = "x";
    static final String JSON_Y = "y";
    static final String JSON_RADIUS = "radius";
    static final String JSON_WIDTH = "width";
    static final String JSON_HEIGHT = "height";
    static final String JSON_FILL_COLOR = "fill_color";
    static final String JSON_OUTLINE_COLOR = "outline_color";
    static final String JSON_OUTLINE_THICKNESS = "outline_thickness";
    static final String JSON_IMAGE_PATH = "image_path";
    static final String JSON_CIRCULAR = "circular";
    static final String JSON_TEXT = "text";
    static final String JSON_FONT = "font";
    static final String JSON_FONT_SIZE = "font_size";
    static final String JSON_ONLINE = "onLine";

    static final String JSON_BOLD = "bold";
    static final String JSON_ITALIC = "italic";
    static final String JSON_LINES = "lines";
    static final String JSON_STATION_NAMES = "station_names";
    static final String JSON_STATIONS = "stations";
    static final String JSON_OTHER_STATIONS = "stations";
    static final String JSON_IMAGES = "images";
    static final String JSON_TEXTS = "texts";
    static final String JSON_BOOLEAN_BACKGROUND = "isBackground";

    static final String DEFAULT_DOCTYPE_DECLARATION = "<!doctype html>\n";
    static final String DEFAULT_ATTRIBUTE_VALUE = "";

    /**
     * This method is for saving user work, which in the case of this
     * application means the data that together draws the logo.
     *
     * @param data The data management component for this application.
     *
     * @param filePath Path (including file name/extension) to where to save the
     * data to.
     *
     * @throws IOException Thrown should there be an error writing out data to
     * the file.
     */
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        // GET THE DATA
        MetroMapData dataManager = (MetroMapData) data;

        // FIRST THE BACKGROUND COLOR
        Color bgColor = dataManager.getBackgroundColor();
        JsonObject bgColorJson = makeJsonColorObject(bgColor);
        // NOW BUILD THE JSON OBJCTS TO SAVE
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder lineBuilder = Json.createArrayBuilder();
        JsonArrayBuilder textBuilder = Json.createArrayBuilder();
        JsonArrayBuilder imageBuilder = Json.createArrayBuilder();
        JsonArrayBuilder stationBuilder = Json.createArrayBuilder();
        ObservableList<Node> shapes = dataManager.getNodes();
        for (Node node : shapes) {
            if (node instanceof DraggableLine) {
                DraggableLine line = (DraggableLine) node;
                //JsonArrayBuilder lineBuilder = Json.createArrayBuilder();
                String type = line.getShapeType();
                //double outlineThickness = line.getStrokeWidth();
                boolean circular = line.circular;
                //JsonObject fillColorJson = makeJsonColorObject((Color)line.getFill());
                //JsonObject outlineColorJson = makeJsonColorObject((Color) line.getStroke());
                JsonObject outlineColorJson;
                Color outlineColor = (Color) line.getStroke();
                if (outlineColor == null) {
                    outlineColorJson = makeJsonColorObject(Color.valueOf(WHITE_HEX));
                } else {
                    outlineColorJson = makeJsonColorObject(line.getStroke());
                }
                double outlineThickness = line.getStrokeWidth();
                //JsonObject fillColorJson = makeJsonColorObject(line.getFill());
                JsonArrayBuilder stationsInLineBuilder = Json.createArrayBuilder();
                for (DraggableStation station : line.stations) {
                    //stationsInLineBuilder.add(station.name);
                    JsonObject outlineColorJson2;
                    Paint fillOutlineColor2 = station.getFill();
                    if (station.name.equals("") || station.name.equals(line.name)) {
                        JsonObject stationJson = Json.createObjectBuilder()
                                //.add(JSON_NAME, station.name).build();
                                .add(JSON_FILL_COLOR, fillOutlineColor2.toString())
                                .add(JSON_ONLINE, false)
                                .add(JSON_X, station.getCenterX())
                                .add(JSON_Y, station.getCenterY())
                                .add(JSON_RADIUS, station.getRadius())
                                .add(JSON_NAME, line.name).build();
                        stationsInLineBuilder.add(stationJson);
                    } else {
                        JsonObject stationJson = Json.createObjectBuilder()
                                //.add(JSON_NAME, station.name).build();
                                //.add(JSON_FILL_COLOR, fillColorJson2)
                                //.add(JSON_OUTLINE_COLOR, this.makeJsonColorObject(station.getStroke()))
                                .add(JSON_ONLINE, station.onLine)
                                .add(JSON_X, station.getCenterX())
                                .add(JSON_Y, station.getCenterY())
                                .add(JSON_RADIUS, station.getRadius())
                                .add(JSON_NAME, station.name).build();
                        stationsInLineBuilder.add(stationJson);
                    }
                }
                JsonObject lineJson = Json.createObjectBuilder()
                        .add(JSON_NAME, line.name)
                        .add(JSON_TYPE, type)
                        //.add(JSON_FILL_COLOR, fillColorJson)
                        .add(JSON_OUTLINE_COLOR, outlineColorJson)
                        .add(JSON_CIRCULAR, circular)
                        .add(JSON_OUTLINE_THICKNESS, outlineThickness)
                        .add(JSON_STATIONS, stationsInLineBuilder).build();
                lineBuilder.add(lineJson);
            } else if (node instanceof DraggableStation) {
                DraggableStation station = (DraggableStation) node;
                //JsonArrayBuilder stationBuilder = Json.createArrayBuilder();
                String name = station.name;
                String type = station.getShapeType();
                double x = station.getCenterX();
                double y = station.getCenterY();
                double width = station.getWidth();
                double height = station.getHeight();
                //JsonObject fillColorJson = makeJsonColorObject((Color)station.getFill());
                //JsonObject outlineColorJson = makeJsonColorObject((Color)station.getStroke());
                //double outlineThickness = station.getStrokeWidth();
                double radius = station.getRadius();

                JsonObject outlineColorJson;
                Color outlineColor = (Color) station.getStroke();
                if (outlineColor == null) {
                    outlineColorJson = makeJsonColorObject(Color.valueOf(WHITE_HEX));
                } else {
                    outlineColorJson = makeJsonColorObject((Color) station.getStroke());
                }
                double outlineThickness = station.getStrokeWidth();
                JsonObject fillColorJson = makeJsonColorObject((Color) station.getFill());

                JsonObject stationJson = Json.createObjectBuilder()
                        .add(JSON_NAME, name)
                        .add(JSON_TYPE, type)
                        .add(JSON_X, x)
                        .add(JSON_Y, y)
                        .add(JSON_WIDTH, width)
                        .add(JSON_HEIGHT, height)
                        //.add(JSON_FILL_COLOR, fillColorJson)
                        .add(JSON_ONLINE, station.onLine)
                        .add(JSON_OUTLINE_COLOR, outlineColorJson)
                        .add(JSON_RADIUS, radius)
                        .add(JSON_OUTLINE_THICKNESS, outlineThickness).build();
                stationBuilder.add(stationJson);
            } else if (node instanceof DraggableImage) {
                DraggableImage shape = (DraggableImage) node;
                Draggable draggableShape = ((Draggable) shape);
                String type = draggableShape.getShapeType();
                String path = ((DraggableImage) draggableShape).getImagePath();
                double x = draggableShape.getX();
                double y = draggableShape.getY();
                double width = draggableShape.getWidth();
                double height = draggableShape.getHeight();

                JsonObject imageJson = Json.createObjectBuilder()
                        .add(JSON_TYPE, type)
                        .add(JSON_X, x)
                        .add(JSON_Y, y)
                        .add(JSON_WIDTH, width)
                        .add(JSON_HEIGHT, height)
                        .add(JSON_IMAGE_PATH, path.substring(5)).build();

                imageBuilder.add(imageJson);
            } else if (node instanceof DraggableText) {
                Shape shape = (Shape) node;
                Draggable draggableShape = ((Draggable) shape);
                String type = draggableShape.getShapeType();
                double x = draggableShape.getX();
                double y = draggableShape.getY();
                boolean bold = ((DraggableText) draggableShape).getBold();
                boolean italic = ((DraggableText) draggableShape).getItalics();
                String fontFamily = ((DraggableText) draggableShape).getFont().getFamily();
                double fontSize = ((DraggableText) draggableShape).getFont().getSize();
                String text = ((DraggableText) draggableShape).getText();

                JsonObject outlineColorJson;
                Color outlineColor = (Color) shape.getStroke();
                if (outlineColor == null) {
                    outlineColorJson = makeJsonColorObject(Color.valueOf(WHITE_HEX));
                } else {
                    outlineColorJson = makeJsonColorObject((Color) shape.getStroke());
                }
                double outlineThickness = shape.getStrokeWidth();
                JsonObject fillColorJson = makeJsonColorObject((Color) shape.getFill());

                JsonObject shapeJson = Json.createObjectBuilder()
                        .add(JSON_TYPE, type)
                        .add(JSON_X, x)
                        .add(JSON_Y, y)
                        .add(JSON_TEXT, text)
                        .add(JSON_ONLINE, ((DraggableText) draggableShape).onLine)
                        .add(JSON_FILL_COLOR, fillColorJson)
                        .add(JSON_BOLD, bold)
                        .add(JSON_ITALIC, italic)
                        .add(JSON_FONT_SIZE, fontSize)
                        .add(JSON_OUTLINE_COLOR, outlineColorJson)
                        .add(JSON_OUTLINE_THICKNESS, outlineThickness)
                        .add(JSON_FONT, fontFamily).build();
                textBuilder.add(shapeJson);
            }
        }

        JsonArray otherNodes = arrayBuilder.build();
        JsonArray metroLines = lineBuilder.build();
        JsonArray metroStat = stationBuilder.build();
        JsonArray metroText = textBuilder.build();
        JsonArray metroImage = imageBuilder.build();

        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_BG_COLOR,bgColorJson)
                .add(JSON_LINES, metroLines)
                .add(JSON_OTHER_STATIONS, metroStat)
                .add(JSON_SHAPES, otherNodes)
                .add(JSON_IMAGES, metroImage)
                .add(JSON_TEXTS, metroText)
                .build();

        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();

        // INIT THE WRITER
        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(filePath);
        pw.write(prettyPrinted);
        pw.close();

    }

    private JsonObject makeJsonColorObject(Paint paint) {
        Color color = (Color) paint;
        JsonObject colorJson = Json.createObjectBuilder()
                .add(JSON_RED, ((Color) color).getRed())
                .add(JSON_GREEN, ((Color) color).getGreen())
                .add(JSON_BLUE, ((Color) color).getBlue())
                .add(JSON_ALPHA, ((Color) color).getOpacity()).build();
        return colorJson;
    }

    private JsonObject makeJsonPathElement(LineTo p) {
        JsonObject colorJson = Json.createObjectBuilder()
                .add(JSON_TYPE, p.toString())
                .add(JSON_X, p.getX())
                .add(JSON_Y, p.getY()).build();
        return colorJson;
    }

    /**
     * This method loads data from a JSON formatted file into the data
     * management component and then forces the updating of the workspace such
     * that the user may edit the data.
     *
     * @param data Data management component where we'll load the file into.
     *
     * @param filePath Path (including file name/extension) to where to load the
     * data from.
     *
     * @throws IOException Thrown should there be an error reading in data from
     * the file.
     */
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        // CLEAR THE OLD DATA OUT
        MetroMapData dataManager = (MetroMapData) data;
        dataManager.resetData();

        // LOAD THE JSON FILE WITH ALL THE DATA
        JsonObject json = loadJSONFile(filePath);

        // LOAD THE BACKGROUND COLOR
        Color bgColor = loadColor(json, JSON_BG_COLOR);
        dataManager.setBackgroundColor(bgColor);
        // AND NOW LOAD ALL THE SHAPES
        JsonArray jsonLineArray = json.getJsonArray(JSON_LINES);
        for (int i = 0; i < jsonLineArray.size(); i++) {
            JsonObject jsonShape = jsonLineArray.getJsonObject(i);
            Node node = loadShape((MetroMapData) data, jsonShape);
            if (node instanceof Draggable) {
                dataManager.addElementToComboBox((Draggable) node);
            }
        }

        JsonArray jsonStationArray = json.getJsonArray(JSON_STATIONS);
        for (int i = 0; i < jsonStationArray.size(); i++) {
            JsonObject jsonShape = jsonStationArray.getJsonObject(i);
            Node node = loadShape((MetroMapData) data, jsonShape);
            if (node instanceof DraggableStation) {
                if (!((DraggableStation)node).name.equals("")) {
                    dataManager.addElementToComboBox((Draggable) node);
                }
                
            }
        }

        JsonArray jsonImageArray = json.getJsonArray(JSON_IMAGES);
        for (int i = 0; i < jsonImageArray.size(); i++) {
            JsonObject jsonShape = jsonImageArray.getJsonObject(i);
            Node node = loadShape((MetroMapData) data, jsonShape);
            //if (node instanceof Draggable) {
            //    dataManager.addElementToComboBox((Draggable) node);
            //}
        }

        JsonArray jsonTextArray = json.getJsonArray(JSON_TEXTS);
        for (int i = 0; i < jsonTextArray.size(); i++) {
            JsonObject jsonShape = jsonTextArray.getJsonObject(i);
            Node node = loadShape((MetroMapData) data, jsonShape);
            //if (node instanceof Draggable) {
            //    dataManager.addElementToComboBox((Draggable) node);
            //}
        }
        
        for (Node node: dataManager.getNodes()) {
            if (node instanceof DraggableStation) {
                for (Node node2 : dataManager.getNodes()) {
                    if (node2 instanceof DraggableLine) {
                        if (((DraggableLine)node2).stations.contains((DraggableStation)node)) {
                            ((DraggableStation)node).lines.add(((DraggableLine)node2));
                        }
                    }
                }
            }
        }

    }

    private double getDataAsDouble(JsonObject json, String dataName) {
        JsonValue value = json.get(dataName);
        JsonNumber number = (JsonNumber) value;
        return number.bigDecimalValue().doubleValue();
    }

    private String getDataAsString(JsonObject json, String dataName) {
        JsonValue value = json.get(dataName);
        JsonString string = (JsonString) value;
        return string.getString();
    }

    private Node loadShape(MetroMapData data, JsonObject jsonShape) {
        // FIRST BUILD THE PROPER SHAPE TYPE
        MetroMapData dataManager = (MetroMapData) data;
        String type = jsonShape.getString(JSON_TYPE);
        Node shape = null;
        if (type.equals(LINE)) {
            shape = loadLine((MetroMapData) data, jsonShape);

        } else if (type.equals(STATION)) {
            shape = loadStation((MetroMapData) data, jsonShape);
        } else if (type.equals("Image")) {
            shape = loadImage((MetroMapData) data, jsonShape);
        } else if (type.equals("Text")) {
            shape = loadText((MetroMapData) data, jsonShape);
        }

        return shape;
    }

    private Node loadLine(MetroMapData data, JsonObject jsonShape) {
        DraggableLine line = new DraggableLine(data);
        line.stations = new ArrayList<DraggableStation>();

        //Color fillColor = loadColor(jsonShape, JSON_FILL_COLOR);
        Color outlineColor = loadColor(jsonShape, JSON_OUTLINE_COLOR);
        Double outlineThickness = getDataAsDouble(jsonShape, JSON_OUTLINE_THICKNESS);
        String name = getDataAsString(jsonShape, JSON_NAME);
        line.name = name;

        //double outlineThickness = getDataAsDouble(jsonShape, JSON_OUTLINE_THICKNESS);
        //line.setFill(fillColor);
        line.setStroke(outlineColor);
        line.setStrokeWidth(outlineThickness);
        //line.setStrokeWidth(outlineThickness);

        JsonArray stations = jsonShape.getJsonArray(JSON_STATIONS);
        for (int n = 0; n < stations.size(); n++) {
            JsonObject jsonNode = stations.getJsonObject(n);
            DraggableStation station = new DraggableStation(data);
            String name2 = getDataAsString(jsonNode, JSON_NAME);
            double x = getDataAsDouble(jsonNode, JSON_X);
            double y = getDataAsDouble(jsonNode, JSON_Y);
            double radius = getDataAsDouble(jsonNode, JSON_RADIUS);
            boolean onLine = this.getDataAsBoolean(jsonNode, JSON_ONLINE);

            station.name = name2;
            station.onLine = onLine;
            station.setCenterX(x);
            station.setCenterY(y);
            station.setRadius(radius);
            station.setFill(Paint.valueOf(WHITE_HEX));
            DraggableText front = new DraggableText(station.getCenterX(), station.getCenterY());
            front.setText(name2);
            front.setStroke(station.getFill());
            
            station.stationName = front;
            if (n == 0) {
                line.stations.add(station);
                MoveTo moveTo = new MoveTo(station.getCenterX(), station.getCenterY());
                line.getElements().add(moveTo);
                line.frontStation = station;
                line.frontStation.setVisible(false);
                line.frontText = new DraggableText();
                line.frontText.setText(name);
                line.frontText.setFill(outlineColor);
                //line.frontText.xProperty().bind(station.centerXProperty());
                //line.frontText.yProperty().bind(station.centerYProperty());
                line.moveTo = moveTo;
                line.moveTo.xProperty().bindBidirectional(line.frontStation.centerXProperty());
                line.moveTo.yProperty().bindBidirectional(line.frontStation.centerYProperty());
                line.frontText.xProperty().bindBidirectional(line.frontStation.centerXProperty());
                line.frontText.yProperty().bindBidirectional(line.frontStation.centerYProperty());
                if (!data.getNodes().contains(station)) {
                    data.addNode(line.frontStation);
                    data.addNode(line.frontText);
                }
                //line.frontText.xProperty().bindBidirectional(station.centerXProperty());
                //line.frontText.yProperty().bindBidirectional(station.centerYProperty());
            } else if (n == stations.size() - 1) {
                line.stations.add(station);
                LineTo lineTo = new LineTo(station.getCenterX(), station.getCenterY());
                line.getElements().add(lineTo);
                line.endStation = station;
                //line.endStation.setFill(line.getStroke());
                line.endStation.setVisible(false);
                line.endText = new DraggableText();
                line.endText.setText(name);
                line.endText.setFill(outlineColor);
                //line.endText.xProperty().bind(line.endStation.centerXProperty());
                //line.endText.yProperty().bind(line.endStation.centerYProperty());
                line.lineTo = lineTo;
                line.lineTo.xProperty().bindBidirectional(line.endStation.centerXProperty());
                line.lineTo.yProperty().bindBidirectional(line.endStation.centerYProperty());
                
                line.endText.xProperty().bindBidirectional(station.centerXProperty());
                line.endText.yProperty().bindBidirectional(station.centerYProperty());
                data.addNode(line.endStation);
                data.addNode(line.endText);
                
            } else {
                line.stations.add(n, station);
                LineTo lineTo = new LineTo(station.getCenterX(), station.getCenterY());
                line.getElements().add(n, lineTo);
                front.xProperty().bind(station.centerXProperty().add(20));
                front.yProperty().bind(station.centerYProperty().add(20));
                lineTo.xProperty().bindBidirectional(station.centerXProperty());
                lineTo.yProperty().bindBidirectional(station.centerYProperty());
                ArrayList<Circle> stationsToAdd = new ArrayList<>();
                ArrayList<DraggableStation> stationsToBind = new ArrayList<>();

                //for (Node node : data.getNodes()) {
                //    if (node instanceof DraggableStation) {
                //        if (!(((DraggableStation) node).getCenterX() == station.getCenterX() && ((DraggableStation) node).getCenterY() == station.getCenterY())) {
                            data.addNode(station);
                            data.addNode(front);
                //        }
                //    }
                //}
                
               
            }

        }
        line.frontText.setOnMousePressed(e -> {
            line.frontStation.setVisible(true);
            line.frontText.setVisible(false);
        });

        line.frontText.setOnMouseReleased(e -> {
            line.frontStation.setVisible(false);
            line.frontText.setVisible(true);
        });

        line.endText.setOnMousePressed(e -> {
            line.endStation.setVisible(true);
            line.endText.setVisible(false);
        });

        line.endText.setOnMouseReleased(e -> {
            line.endStation.setVisible(false);
            line.endText.setVisible(true);
        });

        line.frontStation.setOnMousePressed(e -> {
            line.frontStation.setVisible(true);
            line.frontText.setVisible(false);
        });

        line.frontStation.setOnMouseReleased(e -> {
            line.frontStation.setVisible(false);
            line.frontText.setVisible(true);
        });

        line.endStation.setOnMousePressed(e -> {
            line.endStation.setVisible(false);
            line.endText.setVisible(true);
        });

        line.endStation.setOnMouseReleased(e -> {
            line.endStation.setVisible(true);
            line.endText.setVisible(false);
        });
        data.addNode(line);
        return line;
    }

    private Node loadStation(MetroMapData data, JsonObject jsonNode) {
        DraggableStation station = new DraggableStation(data);
        String name = getDataAsString(jsonNode, JSON_NAME);
        String type = getDataAsString(jsonNode, JSON_TYPE);
        double x = getDataAsDouble(jsonNode, JSON_X);
        double y = getDataAsDouble(jsonNode, JSON_Y);
        double width = getDataAsDouble(jsonNode, JSON_WIDTH);
        double height = getDataAsDouble(jsonNode, JSON_HEIGHT);
        double radius = getDataAsDouble(jsonNode, JSON_RADIUS);
        boolean onLine = this.getDataAsBoolean(jsonNode, JSON_ONLINE);
        /*Color stationFillColor = loadColor(jsonNode, JSON_FILL_COLOR);
        Color stationOutlineColor = loadColor(jsonNode, JSON_OUTLINE_COLOR);
        double stationOutlineThickness = getDataAsDouble(jsonNode, JSON_OUTLINE_THICKNESS);*/

        station.name = name;
        station.setCenterX(x);
        station.setCenterY(y);
        station.onLine = onLine;
        //station.name = name;
        station.setRadius(radius);
        /*station.setFill(stationFillColor);
        station.setStroke(stationOutlineColor);
        station.setStrokeWidth(stationOutlineThickness);*/
        DraggableText front = new DraggableText(station.getCenterX(), station.getCenterY());
        front.setText(name);
        front.xProperty().bind(station.centerXProperty().add(20));
        front.yProperty().bind(station.centerYProperty().add(20));
        if (station.onLine == false) {
            data.addNode(station);
            data.addNode(front);
        }
        return station;

    }

    private Node loadText(MetroMapData data, JsonObject jsonShape) {
        DraggableText text = new DraggableText();
        Color fillColor = loadColor(jsonShape, JSON_FILL_COLOR);
        Color outlineColor = loadColor(jsonShape, JSON_OUTLINE_COLOR);
        String text1 = getDataAsString(jsonShape, JSON_TEXT);
        double x = getDataAsDouble(jsonShape, JSON_X);
        double y = getDataAsDouble(jsonShape, JSON_Y);
        //double outlineThickness = getDataAsDouble(jsonShape, JSON_OUTLINE_THICKNESS);
        //double height = getDataAsDouble(jsonShape, JSON_HEIGHT);
        //Draggable draggableShape = (Draggable) jsonShape;
        String fontFamily = getDataAsString(jsonShape, JSON_FONT);
        double fontSize = getDataAsDouble(jsonShape, JSON_FONT_SIZE);
        boolean bold = getDataAsBoolean(jsonShape, JSON_BOLD);
        boolean italic = getDataAsBoolean(jsonShape, JSON_ITALIC);
        text.setFill(fillColor);
        ((DraggableText) text).setBold(bold);
        ((DraggableText) text).setItalics(italic);

        if (bold && italic) {
            ((DraggableText) text).setFont(Font.font(fontFamily, FontWeight.BOLD, FontPosture.ITALIC, fontSize));
        } else if (bold) {
            ((DraggableText) text).setFont(Font.font(fontFamily, FontWeight.BOLD, fontSize));
        } else if (italic) {
            ((DraggableText) text).setFont(Font.font(fontFamily, FontPosture.ITALIC, fontSize));
        } else if (bold && italic) {
            ((DraggableText) text).setFont(Font.font(fontFamily, FontWeight.BOLD, FontPosture.ITALIC, fontSize));
        }
        text.setStroke(outlineColor);
        //text.setStrokeWidth(outlineThickness);
        ((DraggableText) text).setText(text1);

        ((DraggableText) text).setLocationAndSize(x, y, 0, 0);
        if (text.onLine = false)
            data.addNode(text);

        return text;
    }

    private Node loadImage(MetroMapData data, JsonObject jsonNode) {
        DraggableImage image = new DraggableImage();
        String type = getDataAsString(jsonNode, JSON_TYPE);
        String imagePath = getDataAsString(jsonNode, JSON_IMAGE_PATH);
        double x = getDataAsDouble(jsonNode, JSON_X);
        double y = getDataAsDouble(jsonNode, JSON_Y);
        double width = getDataAsDouble(jsonNode, JSON_WIDTH);
        double height = getDataAsDouble(jsonNode, JSON_HEIGHT);
        image.setLocationAndSize(x, y, width, height);
        image.imagePath = imagePath;
        //((DraggableImage) image).setImage(imagePath);
        image.setFill(new ImagePattern(new Image(image.imagePath)));
        data.addNode(image);

        return image;
    }

    //private Node loadImage(MetroMapData data, JsonObject jsonNode) {
    //  }
    private Color loadColor(JsonObject json, String colorToGet) {
        JsonObject jsonColor = json.getJsonObject(colorToGet);
        double red = getDataAsDouble(jsonColor, JSON_RED);
        double green = getDataAsDouble(jsonColor, JSON_GREEN);
        double blue = getDataAsDouble(jsonColor, JSON_BLUE);
        double alpha = getDataAsDouble(jsonColor, JSON_ALPHA);
        Color loadedColor = new Color(red, green, blue, alpha);
        return loadedColor;
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        InputStream is = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }

    /**
     * This method is provided to satisfy the compiler, but it is not used by
     * this application.
     */
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        MetroMapData dataManager = (MetroMapData) data;

        // FIRST THE BACKGROUND COLOR
        Color bgColor = dataManager.getBackgroundColor();
        JsonObject bgColorJson = makeJsonColorObject(bgColor);

        // NOW BUILD THE JSON OBJCTS TO SAVE
        JsonArrayBuilder lineBuilder = Json.createArrayBuilder();
        JsonArrayBuilder stationBuilder = Json.createArrayBuilder();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        ObservableList<Node> shapes = dataManager.getNodes();
        for (Node node : shapes) {
            if (node instanceof DraggableLine) {
                DraggableLine line = (DraggableLine) node;
                //JsonArrayBuilder lineBuilder = Json.createArrayBuilder();
                String type = line.getShapeType();
                boolean circular = line.circular;
                //double outlineThickness = line.getStrokeWidth();
                //JsonObject fillColorJson = makeJsonColorObject((Color) line.getFill());
                JsonObject outlineColorJson = makeJsonColorObject((Color) line.getStroke());
                JsonArrayBuilder stationsInLineBuilder = Json.createArrayBuilder();
                for (DraggableStation station : line.stations) {
                    /*JsonObject stationJson = Json.createObjectBuilder()
                            .add(JSON_NAME, station.name).build();
                            //.add(JSON_X, station.startCenterX)
                            //.add(JSON_Y, station.startCenterY).build();
                    stationsInLineBuilder.add(stationJson);*/
                    stationsInLineBuilder.add(station.name);
                }
                JsonObject lineJson = Json.createObjectBuilder()
                        .add(JSON_NAME, line.name)
                        .add(JSON_TYPE, type)
                        .add(JSON_OUTLINE_COLOR, outlineColorJson)
                        .add(JSON_CIRCULAR, line.circular)
                        .add(JSON_STATION_NAMES, stationsInLineBuilder).build();
                lineBuilder.add(lineJson);
            } else if (node instanceof DraggableStation) {
                DraggableStation station = (DraggableStation) node;
                //JsonArrayBuilder stationBuilder = Json.createArrayBuilder();
                String name = station.name;
                String type = station.getShapeType();
                double x = station.getCenterX();
                double y = station.getCenterY();

                JsonObject stationJson = Json.createObjectBuilder()
                        .add(JSON_NAME, name)
                        .add(JSON_TYPE, type)
                        .add(JSON_X, x)
                        .add(JSON_Y, y).build();
                stationBuilder.add(stationJson);
            }
        }
        /*JsonArray nodesArray = arrayBuilder.build();

        // THEN PUT IT ALL TOGETHER IN A JsonObject
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_BG_COLOR, bgColorJson)
                .add(JSON_SHAPES, nodesArray)
                .build();*/

        JsonArray otherNodes = arrayBuilder.build();
        JsonArray metroLines = lineBuilder.build();
        JsonArray metroStat = stationBuilder.build();

        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_LINES, metroLines)
                .add(JSON_STATIONS, metroStat)
                //.add(JSON_SHAPES, otherNodes)
                .build();

        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();

        // INIT THE WRITER
        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(filePath);
        pw.write(prettyPrinted);
        pw.close();
    }

    /**
     * This method is provided to satisfy the compiler, but it is not used by
     * this application.
     */
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        // AGAIN, WE ARE NOT USING THIS IN THIS ASSIGNMENT
    }

    public void newRequest(AppDataComponent data, AppTemplate app) throws IOException {
        //AppFileController.handleNewRequest();
        boolean name = true;
        TextInputDialog newFileNameDialog = new TextInputDialog("Enter a name");
        newFileNameDialog.initStyle(StageStyle.UNDECORATED);
        String fileName = "";
        boolean valid = false;
        while (!valid) {
            name = true;
            Optional<String> result = newFileNameDialog.showAndWait();
            fileName = result.get();
            File directory = new File("work");

            File[] fileList = directory.listFiles();
            Arrays.sort(fileList, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return -Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                }
            });
            for (File f : fileList) {
                if (fileName.equals(f.getName())) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setContentText("That file name is already being used! \n\nPlease select another name.");
                    alert.showAndWait();
                    name = false;

                }

            }
            if (name == true) {
                valid = true;
            }

        }

        File f = new File("./work/" + fileName);
        File exportDir = new File("./Exports/" + fileName);
        exportDir.mkdir();
        f.createNewFile();
        app.getGUI().getFileController().setCurrentWorkFile(f);
        MetroMapData dataManager = (MetroMapData) data;
        dataManager.setBackgroundColor(Color.valueOf(WHITE_HEX));
        AppFileController fileComponent = new AppFileController(app);
        app.getGUI().getAppPane().setStyle("-fx-background-color:#323232;");
        fileComponent.handleNewRequest();
        app.getGUI().getAppPane().setStyle("-fx-background-color:#323232;");
        MetroMapWorkspace workspace = ((MetroMapWorkspace) app.getWorkspaceComponent());
        workspace.reloadWorkspace(data);
        dataManager.resetData();
        //this.saveData(data, f.getPath());
    }

    private boolean getDataAsBoolean(JsonObject jsonShape, String bool) {
        return jsonShape.getBoolean(bool);
    }

}
