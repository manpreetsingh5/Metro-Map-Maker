package mmm.gui;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.shape.Shape;
import mmm.data.MetroMapData;
import mmm.data.Draggable;
import mmm.data.MetroMapState;
import static mmm.data.MetroMapState.DRAGGING_NOTHING;
import static mmm.data.MetroMapState.DRAGGING_SHAPE;
import static mmm.data.MetroMapState.SELECTING_SHAPE;
import static mmm.data.MetroMapState.SIZING_SHAPE;
import djf.AppTemplate;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import mmm.Transactions.RemoveStation;
import mmm.Transactions.addStationToLineTransaction;
import mmm.Transactions.removeStationFromLineTransaction;
import mmm.data.DraggableLine;
import mmm.data.DraggableStation;

/**
 * This class responds to interactions with the rendering surface.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class CanvasController {

    AppTemplate app;

    public CanvasController(AppTemplate initApp) {
        app = initApp;
    }

    /**
     * Respond to mouse presses on the rendering surface, which we call canvas,
     * but is actually a Pane.
     */
    public void processCanvasMousePress(int x, int y) {
        MetroMapWorkspace workspace = (MetroMapWorkspace)app.getWorkspaceComponent();
        MetroMapData dataManager = (MetroMapData) app.getDataComponent();
        //System.out.println(dataManager.getState());
        if (dataManager.isInState(MetroMapState.REMOVING_STATION_FROM_LINE)) {
            Node node = dataManager.selectTopNode(x, y);
            Scene scene = app.getGUI().getPrimaryScene();
            if (node!= null) {
                if (node instanceof DraggableStation) {
                    dataManager.setSelectedNode(node);
                    removeStationFromLineTransaction remove = new removeStationFromLineTransaction(app, (DraggableStation) node);
                    workspace.getJTPS().addTransaction(remove);
                    
                    //dataManager.removeStationFromLine( (DraggableStation)node);
                }
                else {
                    dataManager.setState(SELECTING_SHAPE);
                    app.getGUI().updateToolbarControls(false);
                }
                
            }
            else {
                dataManager.setState(SELECTING_SHAPE);
            }
        }
        else if (dataManager.isInState(MetroMapState.ADDING_STATION)) {
            Node node = dataManager.getTopNode(x, y);
            Scene scene = app.getGUI().getPrimaryScene();
            if (node!= null) {
                if (node instanceof DraggableStation) {
                    dataManager.setSelectedNode(node);
                    //dataManager.addStationToLine((DraggableStation)node);
                    addStationToLineTransaction transaction = new addStationToLineTransaction(app,(DraggableStation)node);
                    workspace.getJTPS().addTransaction(transaction);
                }
                else {
                    dataManager.setState(SELECTING_SHAPE);
                    app.getGUI().updateToolbarControls(false);
                }
                
            }
            else {
                dataManager.setState(SELECTING_SHAPE);
            }
            
            
        }
         else if (dataManager.isInState(SELECTING_SHAPE)) {
            // SELECT THE TOP SHAPE
            Node shape = dataManager.selectTopNode(x, y);
            Scene scene = app.getGUI().getPrimaryScene();

            // AND START DRAGGING IT
            if (shape != null) {
                if (shape instanceof DraggableLine) {
                    ((MetroMapWorkspace)app.getWorkspaceComponent()).getMetroLines().setValue((((DraggableLine) shape).getName()));
                }
                else if (shape instanceof DraggableStation) {
                    ((MetroMapWorkspace)app.getWorkspaceComponent()).getMetroStations().setValue((((DraggableStation)shape).getName()));
                }
                
                scene.setCursor(Cursor.MOVE);
                dataManager.setState(MetroMapState.DRAGGING_SHAPE);
                app.getGUI().getFileController().markAsEdited(app.getGUI());
                
                //app.getGUI().updateToolbarControls(false);
            } else {
                scene.setCursor(Cursor.DEFAULT);
                dataManager.setState(DRAGGING_NOTHING);
                app.getWorkspaceComponent().reloadWorkspace(dataManager);
            }
        } 
        else if (dataManager.isInState(MetroMapState.STARTING_LINE)) {
            dataManager.startNewLine(x,y);
            dataManager.setState(MetroMapState.SELECTING_SHAPE);
        } 
        else if (dataManager.isInState(MetroMapState.STARTING_STATION)) {
            dataManager.startNewStation(x, y);
            dataManager.setState(MetroMapState.SELECTING_SHAPE);
        }
        else if (dataManager.isInState(MetroMapState.STARTING_TEXT)) {
            dataManager.startNewText(x, y);
            dataManager.setState(MetroMapState.SELECTING_SHAPE);
        }
        else if (dataManager.isInState(MetroMapState.STARTING_IMAGE)) {
            dataManager.startNewImage(x, y);
            dataManager.setState(MetroMapState.SELECTING_SHAPE);
        }
        
              
        //MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(dataManager);
    }

    /**
     * Respond to mouse dragging on the rendering surface, which we call canvas,
     * but is actually a Pane.
     */
    public void processCanvasMouseDragged(int x, int y) {
        MetroMapData dataManager = (MetroMapData) app.getDataComponent();
        if (dataManager.isInState(SIZING_SHAPE)) {
            Draggable newDraggableShape = (Draggable) dataManager.getNewNode();
            
            newDraggableShape.size(x, y);
        } else if (dataManager.isInState(DRAGGING_SHAPE)) {
            //if (dataManager.getSelectedNode() instanceof Rectangle) {}
            //else {
            Draggable selectedDraggableShape = (Draggable) dataManager.getSelectedNode();
            if (!(selectedDraggableShape instanceof DraggableLine))
                selectedDraggableShape.drag(x, y);
            app.getGUI().getFileController().markAsEdited(app.getGUI());
            //app.getGUI().updateToolbarControls(false);
            
        }
    }

    /**
     * Respond to mouse button release on the rendering surface, which we call canvas,
     * but is actually a Pane.
     */
    public void processCanvasMouseRelease(int x, int y) {
        MetroMapData dataManager = (MetroMapData) app.getDataComponent();
        if (dataManager.isInState(SIZING_SHAPE)) {
            dataManager.selectSizedNode();
            //app.getGUI().updateToolbarControls(false);
            app.getGUI().getFileController().markAsEdited(app.getGUI());
        } else if (dataManager.isInState(MetroMapState.DRAGGING_SHAPE)) {
            dataManager.setState(SELECTING_SHAPE);
            Scene scene = app.getGUI().getPrimaryScene();
            scene.setCursor(Cursor.DEFAULT);
            //app.getGUI().updateToolbarControls(false);
            app.getGUI().getFileController().markAsEdited(app.getGUI());
        } else if (dataManager.isInState(MetroMapState.DRAGGING_NOTHING)) {
            dataManager.setState(SELECTING_SHAPE);
        }
    }
    
}
