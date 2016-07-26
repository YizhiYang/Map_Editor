package saf.settings;

/**
 * This enum provides properties that are to be loaded via
 * XML files to be used for setting up the application.
 * 
 */
public enum AppPropertyType {
        // LOADED FROM simple_app_properties.xml

    /**
     *
     */
            APP_TITLE,

    /**
     *
     */
    APP_LOGO,

    /**
     *
     */
    APP_CSS,

    /**
     *
     */
    APP_PATH_CSS,
        
        // APPLICATION ICONS

    EXPORT_ICON,
    /**
     *
     */
            NEW_ICON,

    /**
     *
     */
    LOAD_ICON,

    /**
     *
     */
    SAVE_ICON,

    /**
     *
     */
    SAVE_AS_ICON,

    /**
     *
     */
    EXIT_ICON,
    
    CHANGE_MAP_NAME,
    ADD_IMAGE_TO_MAP,
    MOVE_IMAGE_ON_MAP,
    REMOVE_IMAGE_ON_MAP,
    CHANGE_MAP_BACKGROUND_COLOR,
    CHANGE_BORDER_COLOR,
    CHANGE_BORDER_THICKNESS,
    REASSIGN_MAP_COLORS,
    SELECT_SUB_REGION,
    VIEW_SUB_REGION_DATA,
    GO_TO_NEXT_SUB_REGION,
    GO_TO_PREVIOUS_SUB_REGION,
    EDIT_SUB_REGION_DETAILS,
    PLAY_SUB_REGION_ANTHEM,
    PAUSE_ANTHEM,
    CLOSE_SUB_REGION_DIALOG,
    EXPAND_MAP_DATA_VIEW,
    ZOOM_MAP_IN_OUT,
    SCROLL_MAP,
    CHANGE_MAP_DIMENSIONS,
        
        // APPLICATION TOOLTIPS FOR BUTTONS

    /**
     *
     */
            NEW_TOOLTIP,

    /**
     *
     */
    LOAD_TOOLTIP,

    /**
     *
     */
    SAVE_TOOLTIP,

    /**
     *
     */
    SAVE_AS_TOOLTIP,

    /**
     *
     */
    EXPORT_TOOLTIP,

    /**
     *
     */
    EXIT_TOOLTIP,
    
    CHANGEMAPNAME_TOOLTIP,
    ADDIMAGETOMAP_TOOLTIP,
    REMOVEIMAGEFROMMAP_TOOLTIP,
    CHANGEMAPBACKGROUNDCOLOR_TOOLTIP,
    CHANGEBORDERCOLOR_TOOLTIP,
    CHANGEBORDERTHICKNESS_TOOLTIP,
    REASSIGNMAPCOLORS_TOOLTIP,
    PLAYSUBREGIONANTHEM_TOOLTIP,
    PAUSEANTHEM_TOOLTIP,
    ZOOMMAPINOUT_TOOLTIP,
    CHANGEMAPDIMENSIONS_TOOLTIP,
            
	
	// ERROR MESSAGES

    /**
     *
     */
    	NEW_ERROR_MESSAGE,

    /**
     *
     */
    LOAD_ERROR_MESSAGE,

    /**
     *
     */
    SAVE_ERROR_MESSAGE,

    /**
     *
     */
    PROPERTIES_LOAD_ERROR_MESSAGE,
	
	// ERROR TITLES

    /**
     *
     */
    	NEW_ERROR_TITLE,

    /**
     *
     */
    LOAD_ERROR_TITLE,

    /**
     *
     */
    SAVE_ERROR_TITLE,

    /**
     *
     */
    PROPERTIES_LOAD_ERROR_TITLE,
	
	// AND VERIFICATION MESSAGES AND TITLES

    /**
     *
     */
            NEW_COMPLETED_MESSAGE,

    /**
     *
     */
    NEW_COMPLETED_TITLE,

    /**
     *
     */
    LOAD_COMPLETED_MESSAGE,

    /**
     *
     */
    LOAD_COMPLETED_TITLE,

    /**
     *
     */
    SAVE_COMPLETED_MESSAGE,
	
    /**
     *
     */
    SAVE_COMPLETED_TITLE,	

    /**
     *
     */
    SAVE_UNSAVED_WORK_TITLE,

    /**
     *
     */
    SAVE_UNSAVED_WORK_MESSAGE,
	
    /**
     *
     */
    SAVE_WORK_TITLE,
    
    EXPORT_FILE,

    /**
     *
     */
    LOAD_WORK_TITLE,

    /**
     *
     */
    WORK_FILE_EXT,

    /**
     *
     */
    WORK_FILE_EXT_DESC,

    /**
     *
     */
    PROPERTIES_,
    
    RVM_FILE_EXT,
    
    
    
    ENTER_A_NAME_FOR_THE_MAP,
    OK,
    CANCEL,
    NAME_OF_THE_MAP,
    PICK_A_BACKGROUND_COLOR,
    BACKGROUND_COLOR,
    PICK_A_BORDERS_COLOR,
    BORDERS_COLOR,
    
    X_COORDINATE,
    Y_COORDINATE,
    CHANGE_MAPS_DIMENSIONS,
    
    CHOOSE_A_DATA_FILE,
    CHOOSE_A_PARENT_DIRECTORY,
    NEW_MAP,
    
    NAME,
    CAPITAL,
    LEADER,
    NEXT,
    PREVIOUS,
    
    EDIT_MAP
}
