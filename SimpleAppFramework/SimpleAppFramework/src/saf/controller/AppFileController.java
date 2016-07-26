package saf.controller;

import saf.ui.AppYesNoCancelDialogSingleton;
import saf.ui.AppMessageDialogSingleton;
import saf.ui.AppGUI;
import saf.components.AppFileComponent;
import saf.components.AppDataComponent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import saf.AppTemplate;
import saf.components.AppComponentsBuilder;
import static saf.settings.AppPropertyType.CANCEL;
import static saf.settings.AppPropertyType.CHOOSE_A_DATA_FILE;
import static saf.settings.AppPropertyType.CHOOSE_A_PARENT_DIRECTORY;
import static saf.settings.AppPropertyType.LOAD_ERROR_MESSAGE;
import static saf.settings.AppPropertyType.LOAD_ERROR_TITLE;
import static saf.settings.AppPropertyType.LOAD_WORK_TITLE;
import static saf.settings.AppPropertyType.WORK_FILE_EXT;
import static saf.settings.AppPropertyType.WORK_FILE_EXT_DESC;
import static saf.settings.AppPropertyType.NEW_COMPLETED_MESSAGE;
import static saf.settings.AppPropertyType.NEW_COMPLETED_TITLE;
import static saf.settings.AppPropertyType.NEW_ERROR_MESSAGE;
import static saf.settings.AppPropertyType.NEW_ERROR_TITLE;
import static saf.settings.AppPropertyType.SAVE_COMPLETED_MESSAGE;
import static saf.settings.AppPropertyType.SAVE_COMPLETED_TITLE;
import static saf.settings.AppPropertyType.SAVE_ERROR_MESSAGE;
import static saf.settings.AppPropertyType.SAVE_ERROR_TITLE;
import static saf.settings.AppPropertyType.SAVE_UNSAVED_WORK_MESSAGE;
import static saf.settings.AppPropertyType.SAVE_UNSAVED_WORK_TITLE;
import static saf.settings.AppPropertyType.SAVE_WORK_TITLE;
import static saf.settings.AppPropertyType.ENTER_A_NAME_FOR_THE_MAP;
import static saf.settings.AppPropertyType.EXPORT_FILE;
import static saf.settings.AppPropertyType.NEW_MAP;
import static saf.settings.AppPropertyType.OK;
import static saf.settings.AppPropertyType.RVM_FILE_EXT;
import static saf.settings.AppPropertyType.WORK_FILE_EXT;
import static saf.settings.AppPropertyType.WORK_FILE_EXT_DESC;
import static saf.settings.AppStartupConstants.PATH_WORK;
import static saf.settings.AppStartupConstants.SIMPLE_APP_PROPERTIES_FILE_NAME;

/**
 * This class provides the event programmed responses for the file controls that
 * are provided by this framework.
 *
 */
public class AppFileController {

    // HERE'S THE APP
    AppTemplate app;

    // WE WANT TO KEEP TRACK OF WHEN SOMETHING HAS NOT BEEN SAVED
    boolean saved;

    // THIS IS THE FILE FOR THE WORK CURRENTLY BEING WORKED ON
    File currentWorkFile;

    File selectedFile;

    File parentDir;
    
    File pathOfParentDir;

    String nameOfExportFile;
    
    public AppFileController() {

    }

    /**
     * This constructor just keeps the app for later.
     *
     * @param initApp The application within which this controller will provide
     * file toolbar responses.
     */
    public AppFileController(AppTemplate initApp) {
        // NOTHING YET
        saved = true;
        app = initApp;
    }

    /**
     * This method marks the appropriate variable such that we know that the
     * current Work has been edited since it's been saved. The UI is then
     * updated to reflect this.
     *
     * @param gui The user interface editing the Work.
     */
    public void markAsEdited(AppGUI gui) {
        // THE WORK IS NOW DIRTY
        saved = false;

        // LET THE UI KNOW
        gui.updateToolbarControls(saved);
    }

    /**
     * This method starts the process of editing new Work. If work is already
     * being edited, it will prompt the user to save it first.
     *
     */
    public void handleNewRequest() {

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        GridPane newMap = new GridPane();

        newMap.setAlignment(Pos.TOP_LEFT);
        newMap.setVgap(5.5);

        TextField name = new TextField();
        name.setMinWidth(700);
        newMap.add(new Label(props.getProperty(ENTER_A_NAME_FOR_THE_MAP)), 0, 0);
        newMap.add(name, 1, 0);

        HBox hbox = new HBox();
        newMap.add(new Label(props.getProperty(CHOOSE_A_DATA_FILE)), 0, 1);
        Button button1 = new Button();
        button1.setGraphic(new ImageView(new Image("file:./images/folder.png")));
        Label geoLabel = new Label();
        hbox.getChildren().add(button1);
        hbox.getChildren().add(geoLabel);
        newMap.add(hbox, 1, 1);

        HBox hbox2 = new HBox();
        newMap.add(new Label(props.getProperty(CHOOSE_A_PARENT_DIRECTORY)), 0, 2);
        Button button2 = new Button();
        button2.setGraphic(new ImageView(new Image("file:./images/folder.png")));
        Label parentLabel = new Label();
        hbox2.getChildren().add(button2);
        hbox2.getChildren().add(parentLabel);
        newMap.add(hbox2, 1, 2);

        Button ok = new Button(props.getProperty(OK));
        
        Button cancel = new Button(props.getProperty(CANCEL));

        newMap.add(ok, 0, 7);
        newMap.add(cancel, 1, 7);

        Stage stage = new Stage();

        Scene scene = new Scene(newMap, 900, 200);

        scene.getStylesheets().add("mapeditor/css/mv_style.css");
        newMap.getStyleClass().add("cust");

        stage.setTitle(props.getProperty(NEW_MAP));

        stage.setScene(scene);

        stage.show();

        button1.setOnAction(e -> {

            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File("./raw_map_data/"));
            selectedFile = fc.showOpenDialog(stage);
            geoLabel.setText(selectedFile.getPath());

        });

        button2.setOnAction(e -> {

            DirectoryChooser dir = new DirectoryChooser();

            dir.setInitialDirectory(new File("./export/"));

            parentDir = dir.showDialog(stage);

            parentLabel.setText(parentDir.getPath());
            
            pathOfParentDir = new File(parentDir.getPath() + "\\" + name.getText());
            
            if(!pathOfParentDir.exists()){
                
                pathOfParentDir.mkdir();
            }
        });
        

        ok.setOnAction(e -> {
            //handleLoadRequest();
            AppDataComponent dataManager = app.getDataComponent();
            AppFileComponent fileManager = app.getFileComponent();
            try {
                fileManager.newMap(name.getText(), dataManager, selectedFile.getPath(), parentDir.getPath());
                System.out.println(parentDir.getPath());
                app.getWorkspaceComponent().reloadWorkspace();
                app.getGUI().updateToolbarControls(false);
                
                nameOfExportFile = name.getText();
            } catch (IOException ex) {
                Logger.getLogger(AppFileController.class.getName()).log(Level.SEVERE, null, ex);
            }
            stage.close();
        });
        cancel.setOnAction(e -> stage.close());
        
        
    }

    /**
     * This method lets the user open a Course saved to a file. It will also
     * make sure data for the current Course is not lost.
     *
     * @param gui The user interface editing the course.
     */
    public void handleLoadRequest() {
        try {
            // WE MAY HAVE TO SAVE CURRENT WORK
            boolean continueToOpen = true;
            if (!saved) {
                // THE USER CAN OPT OUT HERE WITH A CANCEL
                continueToOpen = promptToSave();
            }

            // IF THE USER REALLY WANTS TO OPEN A Course
            if (continueToOpen) {
                // GO AHEAD AND PROCEED LOADING A Course
                promptToOpen();
                app.getGUI().updateToolbarControls(false);
            }
        } catch (IOException ioe) {
            // SOMETHING WENT WRONG
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            dialog.show(props.getProperty(LOAD_ERROR_TITLE), props.getProperty(LOAD_ERROR_MESSAGE));
        }
    }

    /**
     * This method will save the current course to a file. Note that we already
     * know the name of the file, so we won't need to prompt the user.
     *
     *
     * @param courseToSave The course being edited that is to be saved to a
     * file.
     */
    public void handleSaveRequest() {
        // WE'LL NEED THIS TO GET CUSTOM STUFF
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        try {
            // MAYBE WE ALREADY KNOW THE FILE
            if (currentWorkFile != null) {
                saveWork(currentWorkFile);
            } // OTHERWISE WE NEED TO PROMPT THE USER
            else {
                // PROMPT THE USER FOR A FILE NAME
                FileChooser fc = new FileChooser();
                fc.setInitialDirectory(new File(PATH_WORK));
                fc.setTitle(props.getProperty(SAVE_WORK_TITLE));
                fc.getExtensionFilters().addAll(
                        new ExtensionFilter(props.getProperty(WORK_FILE_EXT), props.getProperty(WORK_FILE_EXT)));

                File selectedFile = fc.showSaveDialog(app.getGUI().getWindow());

                if (selectedFile != null) {
                    saveWork(selectedFile);
                }
            }
        } catch (IOException ioe) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(LOAD_ERROR_TITLE), props.getProperty(LOAD_ERROR_MESSAGE));
        }
    }

    // HELPER METHOD FOR SAVING WORK
    private void saveWork(File selectedFile) throws IOException {
        // SAVE IT TO A FILE
        app.getFileComponent().saveData(app.getDataComponent(), selectedFile.getPath() + ".json");
        
        System.out.println(selectedFile.getPath());

        // MARK IT AS SAVED
        currentWorkFile = selectedFile;
        saved = true;

        // TELL THE USER THE FILE HAS BEEN SAVED
        AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        dialog.show(props.getProperty(SAVE_COMPLETED_TITLE), props.getProperty(SAVE_COMPLETED_MESSAGE));

        // AND REFRESH THE GUI, WHICH WILL ENABLE AND DISABLE
        // THE APPROPRIATE CONTROLS
        //app.getGUI().updateToolbarControls(false);          // comment on 07/17
    }

    /**
     * This method will exit the application, making sure the user doesn't lose
     * any data first.
     *
     */
    public void handleExitRequest() {
        try {
            // WE MAY HAVE TO SAVE CURRENT WORK
            boolean continueToExit = true;
            if (!saved) {
                // THE USER CAN OPT OUT HERE
                continueToExit = promptToSave();
            }

            // IF THE USER REALLY WANTS TO EXIT THE APP
            if (continueToExit) {
                // EXIT THE APPLICATION
                System.exit(0);
            }
        } catch (IOException ioe) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            dialog.show(props.getProperty(SAVE_ERROR_TITLE), props.getProperty(SAVE_ERROR_MESSAGE));
        }
    }

    public void handleExportRequest() throws IOException {

        PropertiesManager props = PropertiesManager.getPropertiesManager();

        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("./export/"));
        fc.setTitle(props.getProperty(EXPORT_FILE));
        //fc.getExtensionFilters().addAll(
        //new FileChooser.ExtensionFilter(props.getProperty(WORK_FILE_EXT_DESC), props.getProperty(RVM_FILE_EXT)));

//        File selectedFile = fc.showSaveDialog(app.getGUI().getWindow());
//
//        selectedFile.mkdir();
//
//        String filePath = selectedFile.getPath();
//        String nameOfFile = selectedFile.getName();
//        filePath += "/" + nameOfFile + ".rvm";

//        File file = new File(pathOfParentDir + "/" + nameOfExportFile + ".rvm");
//    
//        
//        file.createNewFile();

        AppDataComponent dataManager = app.getDataComponent();
        AppFileComponent fileManager = app.getFileComponent();
        try {

            //fileManager.exportData(dataManager, selectedFile.getAbsolutePath());
            //fileManager.exportData(dataManager, file.getPath());
            fileManager.exportData(dataManager);
            
        } catch (IOException ex) {

            Logger.getLogger(AppFileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This helper method verifies that the user really wants to save their
     * unsaved work, which they might not want to do. Note that it could be used
     * in multiple contexts before doing other actions, like creating new work,
     * or opening another file. Note that the user will be presented with 3
     * options: YES, NO, and CANCEL. YES means the user wants to save their work
     * and continue the other action (we return true to denote this), NO means
     * don't save the work but continue with the other action (true is
     * returned), CANCEL means don't save the work and don't continue with the
     * other action (false is returned).
     *
     * @return true if the user presses the YES option to save, true if the user
     * presses the NO option to not save, false if the user presses the CANCEL
     * option to not continue.
     */
    private boolean promptToSave() throws IOException {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // CHECK TO SEE IF THE CURRENT WORK HAS
        // BEEN SAVED AT LEAST ONCE
        // PROMPT THE USER TO SAVE UNSAVED WORK
        AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
        yesNoDialog.show(props.getProperty(SAVE_UNSAVED_WORK_TITLE), props.getProperty(SAVE_UNSAVED_WORK_MESSAGE));    // problem is here. 

        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoDialog.getSelection();

        // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (selection.equals(AppYesNoCancelDialogSingleton.YES)) {
            // SAVE THE DATA FILE
            AppDataComponent dataManager = app.getDataComponent();

            if (currentWorkFile == null) {
                // PROMPT THE USER FOR A FILE NAME
                FileChooser fc = new FileChooser();
                fc.setInitialDirectory(new File(PATH_WORK));
                fc.setTitle(props.getProperty(SAVE_WORK_TITLE));
                fc.getExtensionFilters().addAll(
                        new ExtensionFilter(props.getProperty(WORK_FILE_EXT_DESC), props.getProperty(WORK_FILE_EXT)));

                File selectedFile = fc.showSaveDialog(app.getGUI().getWindow());
                if (selectedFile != null) {
                    saveWork(selectedFile);
                    saved = true;
                }
            } else {
                saveWork(currentWorkFile);
                saved = true;
            }
        } // IF THE USER SAID CANCEL, THEN WE'LL TELL WHOEVER
        // CALLED THIS THAT THE USER IS NOT INTERESTED ANYMORE
        else if (selection.equals(AppYesNoCancelDialogSingleton.CANCEL)) {
            return false;
        }

        // IF THE USER SAID NO, WE JUST GO ON WITHOUT SAVING
        // BUT FOR BOTH YES AND NO WE DO WHATEVER THE USER
        // HAD IN MIND IN THE FIRST PLACE
        return true;
    }

    /**
     * This helper method asks the user for a file to open. The user-selected
     * file is then loaded and the GUI updated. Note that if the user cancels
     * the open process, nothing is done. If an error occurs loading the file, a
     * message is displayed, but nothing changes.
     */
    private void promptToOpen() {
        // WE'LL NEED TO GET CUSTOMIZED STUFF WITH THIS
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // AND NOW ASK THE USER FOR THE FILE TO OPEN
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_WORK));
        fc.setTitle(props.getProperty(LOAD_WORK_TITLE));
        File selectedFile = fc.showOpenDialog(app.getGUI().getWindow());

        // ONLY OPEN A NEW FILE IF THE USER SAYS OK
        if (selectedFile != null) {
            try {
                AppDataComponent dataManager = app.getDataComponent();
                AppFileComponent fileManager = app.getFileComponent();
                fileManager.loadData(dataManager, selectedFile.getAbsolutePath());
                //app.getWorkspaceComponent().reloadWorkspace();                                   //Comment out because the sequence of the thread. 

                // MAKE SURE THE WORKSPACE IS ACTIVATED
                app.getWorkspaceComponent().activateWorkspace(app.getGUI().getAppPane());      // put these to the workspace class. 
                saved = true;
                app.getGUI().updateToolbarControls(saved);
            } catch (Exception e) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(LOAD_ERROR_TITLE), props.getProperty(LOAD_ERROR_MESSAGE));
            }
        }
    }

    /**
     * This mutator method marks the file as not saved, which means that when
     * the user wants to do a file-type operation, we should prompt the user to
     * save current work first. Note that this method should be called any time
     * the course is changed in some way.
     */
    public void markFileAsNotSaved() {
        saved = false;
    }

    /**
     * Accessor method for checking to see if the current work has been saved
     * since it was last edited.
     *
     * @return true if the current work is saved to the file, false otherwise.
     */
    public boolean isSaved() {
        return saved;
    }

    public File getParentDirFile(){
        
        return pathOfParentDir;
    }
}
