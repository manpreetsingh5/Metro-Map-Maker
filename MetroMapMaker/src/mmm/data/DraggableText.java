/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.data;

/**
 *
 * @author Manny
 */
import javafx.scene.text.Text;

/**
 *
 * @author Manny
 */
public class DraggableText extends Text implements Draggable {
    public double startX;
    public double startY;
    public boolean bold;
    public boolean italic;
    public boolean onLine;
    public DraggableText() {
        super("");
        setX(0.0);
	setY(0.0);
        this.onLine = false;
	setOpacity(1.0);
	startX = 0.0;
	startY = 0.0;
    }
    public DraggableText(double x, double y) {
	super("");
        setX(0.0);
	setY(0.0);
        this.onLine = false;
	setOpacity(1.0);
	startX = 0.0;
	startY = 0.0;
       
    }
    @Override
    public MetroMapState getStartingState() {
	return MetroMapState.STARTING_TEXT;
    }
    
    public void start(int x, int y) {
        setX(x);
        setY(y);
	
    }
    
    public void setStartXY(int x, int y) {
        startX = x;
        startY = y;
    }
    @Override
    public void drag(int x, int y) {
	/*double diffX = x - startX;
	double diffY = y - startY;
	double newX = getX() + diffX;
	double newY = getY() + diffY;
        xProperty().set(newX);
        yProperty().set(newY);
	startX = x;
	startY = y;*/
        this.xProperty().set(x);
        this.yProperty().set(y);
    }
    
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
	xProperty().set(initX);
	yProperty().set(initY);
    }
    
    public void size(int x, int y) {
	
    }
    
    public String getShapeType() {
	return "Text";
    }

    
    public boolean getBold() {
        return bold;
    }
    
    public boolean getItalics() {
        return italic;
    }
    
    public void setBold(boolean bold) {
        this.bold = bold;
    }
    
    public void setItalics(boolean italic) {
        this.italic = italic;
    }

    @Override
    public double getWidth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
