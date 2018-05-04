package mmm.data;

/**
 * This enum has the various possible states of the logo maker app
 * during the editing process which helps us determine which controls
 * are usable or not and what specific user actions should affect.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public enum MetroMapState {
    SELECTING_SHAPE,
    ADDING_STATION,
    REMOVING_STATION_FROM_LINE,
    DRAGGING_SHAPE,
    STARTING_LINE,
    STARTING_IMAGE,
    STARTING_OVERLAY,
    STARTING_TEXT,
    STARTING_STATION,
    SELECTING,
    DRAGGING,
    ROTATING_LABEL,
    SIZING_SHAPE,
    DRAGGING_NOTHING,
    SIZING_NOTHING
}
