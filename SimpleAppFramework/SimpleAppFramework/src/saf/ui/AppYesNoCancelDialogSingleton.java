package saf.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import properties_manager.PropertiesManager;
import static saf.settings.AppStartupConstants.SIMPLE_APP_PROPERTIES_FILE_NAME;
import static saf.ui.AppMessageDialogSingleton.singleton;

/**
 * This class serves to present a dialog with three options to
 * the user: Yes, No, or Cancel and lets one access which was
 * selected.
 * 
 */
public class AppYesNoCancelDialogSingleton extends Stage {
    // HERE'S THE SINGLETON
    static AppYesNoCancelDialogSingleton singleton;
    
    // GUI CONTROLS FOR OUR DIALOG
    VBox messagePane;
    Scene messageScene;
    Label messageLabel;
    Button yesButton;
    Button noButton;
    Button cancelButton;
    String selection;
    
    // CONSTANT CHOICES

    /**
     *
     */
        public static final String YES = "Yes";

    /**
     *
     */
    public static final String NO = "No";

    /**
     *
     */
    public static final String CANCEL = "Cancel";
    
    /**
     * Note that the constructor is private since it follows
     * the singleton design pattern.
     * 
     * @param primaryStage The owner of this modal dialog.
     */
    private AppYesNoCancelDialogSingleton() {messageLabel = new Label();}
    
    /**
     * The static accessor method for this singleton.
     * 
     * @return The singleton object for this type.
     */
    public static AppYesNoCancelDialogSingleton getSingleton() {
	if (singleton == null)
	    singleton = new AppYesNoCancelDialogSingleton();
	return singleton;
    }
	
    /**
     * This method initializes the singleton for use.
     * 
     * @param primaryStage The window above which this
     * dialog will be centered.
     */
    public void init(Stage primaryStage) {
        PropertiesManager props = PropertiesManager.getPropertiesManager(); // I ADDED THIS 
        
        
        // MAKE THIS DIALOG MODAL, MEANING OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);
        
        // LABEL TO DISPLAY THE CUSTOM MESSAGE
        messageLabel = new Label();        

        // YES, NO, AND CANCEL BUTTONS
        yesButton = new Button("OK");
        noButton = new Button(NO);
        cancelButton = new Button(CANCEL);
	
	// MAKE THE EVENT HANDLER FOR THESE BUTTONS
        EventHandler yesNoCancelHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            Button sourceButton = (Button)ae.getSource();
            AppYesNoCancelDialogSingleton.this.selection = sourceButton.getText();
            AppYesNoCancelDialogSingleton.this.hide();
        };
        
	// AND THEN REGISTER THEM TO RESPOND TO INTERACTIONS
        yesButton.setOnAction(yesNoCancelHandler);
        noButton.setOnAction(yesNoCancelHandler);
        cancelButton.setOnAction(yesNoCancelHandler);

        // NOW ORGANIZE OUR BUTTONS
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().add(yesButton);

        
        // WE'LL PUT EVERYTHING HERE
        messagePane = new VBox();
        messagePane.setAlignment(Pos.CENTER);
        messagePane.getChildren().add(messageLabel);
        messagePane.getChildren().add(buttonBox);
        
        // MAKE IT LOOK NICE
        messagePane.setPadding(new Insets(10, 20, 20, 20));
        messagePane.setSpacing(10);

        messagePane.getStylesheets().add("mapeditor/css/mv_style.css");
        messagePane.getStyleClass().add("cust");
        // AND PUT IT IN THE WINDOW
        messageScene = new Scene(messagePane);
        
        this.setScene(messageScene);
    }

    /**
     * Accessor method for getting the selection the user made.
     * 
     * @return Either YES, NO, or CANCEL, depending on which
     * button the user selected when this dialog was presented.
     */
    public String getSelection() {
        return selection;
    }
 
    /**
     * This method loads a custom message into the label
     * then pops open the dialog.
     * 
     * @param title The title to appear in the dialog window bar.
     * 
     * @param message Message to appear inside the dialog.
     */
    public void show(String title, String message) {
	// SET THE DIALOG TITLE BAR TITLE
	setTitle(title);
	
	// SET THE MESSAGE TO DISPLAY TO THE USER
        messageLabel.setText(message);
	
        
	// AND OPEN UP THIS DIALOG, MAKING SURE THE APPLICATION
	// WAITS FOR IT TO BE RESOLVED BEFORE LETTING THE USER
	// DO MORE WORK.
        showAndWait();
    }
}