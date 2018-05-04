package mmm.data;

import java.util.ArrayList;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import jtps.jTPS_Transaction;
import mmm.Transactions.addStationTransaction;
import static mmm.data.Draggable.STATION;
import static mmm.data.MetroMapData.BLACK_HEX;
import mmm.gui.MetroMapWorkspace;

/**
 * This is a draggable ellipse for our goLogoLo application.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class DraggableStation extends Circle implements Draggable {
    public double startCenterX;
    public double startCenterY;
    public String name;
    public DraggableText stationName;
    public MetroMapData data;
    public String labelPosition;
    public double angle;
    public boolean onLine;
    public ArrayList<DraggableLine> lines = new ArrayList<DraggableLine>();
    
    public DraggableStation(MetroMapData data) {
        this.name = "";
        this.data = data;
    }
    
    public DraggableStation(MetroMapData data, double x, double y, double radius) {
        super(x,y,radius);
        this.angle = 0;
        this.name = "";
        this.labelPosition = "bottom_right";
        this.stationName = new DraggableText(x+20,y+20);
	setStroke(Paint.valueOf(BLACK_HEX));
        this.data = data;
        onLine = false;
        stationName.xProperty().bind(this.centerXProperty().add(20));
        stationName.yProperty().bind(this.centerYProperty().add(20));
        this.lines = new ArrayList<DraggableLine>();
    }
    
    public MetroMapState getStartingState() {
	return MetroMapState.STARTING_STATION;
    }
    
    
    
    public void start(int x, int y) {
	startCenterX = x;
	startCenterY = y;
        setCenterX(x);
        setCenterY(y);
        
    }
    
    @Override
    public void drag(int x, int y) {
	double diffX = x - getCenterX();
	double diffY = y - getCenterY();
	double newX = getCenterX() + diffX;
	double newY = getCenterY() + diffY;
	setCenterX(newX);
	setCenterY(newY);
	startCenterX = x;
	startCenterY = y;
    }
    
    public void size(int x, int y) {
	double width = x - startCenterX;
	double height = y - startCenterY;
	double centerX = startCenterX + (width / 2);
	double centerY = startCenterY + (height / 2);
	setCenterX(centerX);
	setCenterY(centerY);
	
    }
        
    public String getShapeType() {
	return STATION;
    }
    
    public boolean equals(DraggableStation o) {
        DraggableStation station = null;
        if (o instanceof DraggableStation) {
        station = (DraggableStation) o;
        
        }
        return ((this.getCenterX() == station.getCenterX())
                && (this.getCenterY() == station.getCenterY())
                && (this.name.equals(station.name)));
    }
    /*@Override
    public void start(int x, int y) {
	startCenterX = x;
	startCenterY = y;
    }
    
    @Override
    public void drag(int x, int y) {
	double diffX = x - startCenterX;
	double diffY = y - startCenterY;
	double newX = getCenterX() + diffX;
	double newY = getCenterY() + diffY;
	setCenterX(newX);
	setCenterY(newY);
	startCenterX = x;
	startCenterY = y;
    }
    
    @Override
    public void size(int x, int y) {
	double width = x - startCenterX;
	double height = y - startCenterY;
	double centerX = startCenterX + (width / 2);
	double centerY = startCenterY + (height / 2);
	setCenterX(centerX);
	setCenterY(centerY);
	setRadiusX(width / 2);
	setRadiusY(height / 2);	
	
    }*/
        
    @Override
    public double getX() {
	return getCenterX() - getRadius();
    }

    @Override
    public double getY() {
	return getCenterY() - getRadius();
    }

    @Override
    public double getWidth() {
	return getRadius() * 2;
    }

    @Override
    public double getHeight() {
	return getRadius() * 2;
    }
        
    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
	setCenterX(initX + (initWidth/2));
	setCenterY(initY + (initHeight/2));
	setRadius(initWidth/2);
	//setRadiusY(initHeight/2);
    }
    
    public String getName() {
        return name;
    }
    
    public DraggableText getStationName() {
        return stationName;
    }
}
