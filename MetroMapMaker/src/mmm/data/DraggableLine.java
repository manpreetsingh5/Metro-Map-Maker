package mmm.data;

//import javafx.scene.shape.PolyLine;
import java.util.ArrayList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import static mmm.data.Draggable.LINE;

/**
 * This is a draggable rectangle for our goLogoLo application.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class DraggableLine extends Path implements Draggable {
    public MetroMapData data;
    public double startX;
    public double startY;
    public double endX;
    public double endY;
    public String name;
    public Paint color;
    public DraggableStation frontStation;
    public DraggableStation endStation;
    public DraggableText frontText;
    public DraggableText endText;
    public ArrayList<DraggableStation> stations;
    public boolean circular;
    //Line line;
    public MoveTo moveTo;
    public LineTo lineTo;
    
    public DraggableLine(MetroMapData data) {
        this.data = data;
    }

    public DraggableLine(double startX,double startY,double endX, double endY) {
        //line = new Line(startX,startY,endX,endY);
        this.circular = false;
        //this.data = data;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        moveTo = new MoveTo(startX,startY);
        lineTo = new LineTo(endX,endY);
        frontStation = new DraggableStation(data,this.moveTo.getX(), this.moveTo.getY(), 20);
        frontStation.onLine = true;
        endStation = new DraggableStation(data,lineTo.getX(), lineTo.getY(), 20);
        endStation.onLine = true;
        frontText = new DraggableText(moveTo.getX(),moveTo.getY());
        endText = new DraggableText(lineTo.getX(),lineTo.getY());
        this.getElements().addAll(moveTo,lineTo);
        //this.line = new Line(startX,startY,endX,endY);
        //init();
        
        setOpacity(1.0);
        startX = 0.0;
        startY = 0.0;
        stations = new ArrayList<DraggableStation>();

    }

    public MetroMapState getStartingState() {
        return MetroMapState.STARTING_LINE;
    }
    
    public void init() {
        moveTo.xProperty().bindBidirectional(frontStation.centerXProperty());
        moveTo.yProperty().bindBidirectional(frontStation.centerYProperty());
        lineTo.xProperty().bindBidirectional(endStation.centerXProperty());
        lineTo.yProperty().bindBidirectional(endStation.centerYProperty());
        
        //frontText.xProperty().bindBidirectional(frontStation.centerXProperty());
        //frontText.yProperty().bindBidirectional(frontStation.centerXProperty());
        //endText.xProperty().bindBidirectional(endStation.centerXProperty());
        //endText.yProperty().bindBidirectional(endStation.centerXProperty());
        
        /*frontStation.centerXProperty().bind(frontText.xProperty());
        frontStation.centerYProperty().bind(frontText.yProperty());
        endStation.centerXProperty().bind(endText.xProperty());
        endStation.centerXProperty().bind(endText.xProperty());
        */
    }
    
    /*
    public void start(int x, int y) {
        
        startX = x;
        startY = y;
        endX = x + 200;
        endY = y;

        
        getPoints().addAll(startX, startY, endX, endY);
        //double x2 = ((getPoints().get(0) + getPoints().get(getPoints().size() - 2))) / 2;
        //double y2 = ((getPoints().get(1) + getPoints().get(getPoints().size() - 1))) / 2;
        
        getPoints().set(2, endX);
        getPoints().set(3, endY);
        
        initLabels();
        reloadLine();

    }
*/
  /*
    public void initLabels() {
        
        this.front.setFont(Font.font("Arial",FontWeight.BOLD,24.0));
        
        this.end.setFont(Font.font("Arial",FontWeight.BOLD,24.0));
        
        this.front.setX(getPoints().get(0) - 25);
        this.front.setY(getPoints().get(1));
        this.end.setX(getPoints().get(getPoints().size() - 2) + 5);
        this.end.setY(getPoints().get(getPoints().size() - 1));
        
        //this.front.xProperty().bind(this.);
        
        //data.getShapes().add(front);
        System.out.println(data.getShapes().size());
        //data.getShapes().add(end);
        System.out.println(data.getShapes().size());
    }
*/
    
    public void addStationToLine(DraggableStation station) {
        this.stations.add(station);
        
    }
    
    public String getShapeType() {
        return LINE;
    }

    public String getName() {
        return this.name;
    }
    /*
    public void reloadLine() {
        int count = this.getPoints().size();
        int j = 0;
        
        for(int i = 0; i <= this.getPoints().size()/2; i++) {
            
            DraggableStation station = new DraggableStation(data);
            
            station.start(this.getPoints().get(j).intValue(), this.getPoints().get(j+1).intValue());
            station.setRadiusX(25);
            station.setRadiusY(25);
            station.setFill(Paint.valueOf(WHITE_HEX));
            station.setStroke(Paint.valueOf(BLACK_HEX));
            station.setStrokeWidth(5);
            station.setCenterX(this.getPoints().get(j));
            station.setCenterY(this.getPoints().get(j+1));
            
            DoubleProperty x = new SimpleDoubleProperty(this.getPoints().get(j));
            DoubleProperty y = new SimpleDoubleProperty(this.getPoints().get(j+1));
            x.bind(new SimpleDoubleProperty(station.getCenterX()));
            y.bind(new SimpleDoubleProperty(station.getCenterY()));
            
            j++;
        }
    }
    */
    public void setName(String name) {
        this.name = name;
    }
    
    public void setColor(Paint color) {
        this.color = color;
    }
    
    

    @Override
    public void start(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void drag(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void size(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getX() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getY() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getWidth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
