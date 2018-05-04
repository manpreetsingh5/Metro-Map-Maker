package mmm.data;

import static djf.settings.AppStartupConstants.FILE_PROTOCOL;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * This is a draggable rectangle for our goLogoLo application.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class DraggableImage extends Rectangle implements Draggable {
    public double startX;
    public double startY;
    public String imagePath;
    public boolean isBackground;
    
    public DraggableImage() {
        isBackground = false;
        setX(0.0);
	setY(0.0);
	setWidth(0.0);
	setHeight(0.0);
	setOpacity(1.0);
	startX = 0.0;
	startY = 0.0;
    }
    
    public DraggableImage(String imagePath) {
        isBackground = false;
	setX(0.0);
	setY(0.0);
	setWidth(0.0);
	setHeight(0.0);
	setOpacity(1.0);
	startX = 0.0;
	startY = 0.0;
        this.imagePath = imagePath;
        setFill(new ImagePattern(new Image(this.imagePath)));
    }
    
    @Override
    public MetroMapState getStartingState() {
	return MetroMapState.STARTING_IMAGE;
    }
    
    @Override
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
	double diffX = x - startX;
	double diffY = y - startY;
	double newX = getX() + diffX;
	double newY = getY() + diffY;
        xProperty().set(newX);
	yProperty().set(newY);
        startX = x;
        startY = y;
    }
    
    public String cT(double x, double y) {
	return "(x,y): (" + x + "," + y + ")";
    }
    
    @Override
    public void size(int x, int y) {
	double width = x - getX();
	widthProperty().set(width);
	double height = y - getY();
	heightProperty().set(height);	
    }
    
    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
	xProperty().set(initX);
	yProperty().set(initY);
	widthProperty().set(initWidth);
	heightProperty().set(initHeight);
    }
    
    @Override
    public String getShapeType() {
	return "Image";
    }
    
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public void setImage(String imagePath) {
        setFill(new ImagePattern(new Image(imagePath)));
    }
}
