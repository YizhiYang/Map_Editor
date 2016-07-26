/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drivertestapp;

import java.io.File;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mapeditor.workspaceController.WorkspaceController;
import properties_manager.PropertiesManager;
import saf.AppTemplate;
import saf.components.AppComponentsBuilder;
import saf.controller.AppFileController;
import static saf.settings.AppPropertyType.LOAD_WORK_TITLE;
import static saf.settings.AppPropertyType.SAVE_WORK_TITLE;
import static saf.settings.AppPropertyType.WORK_FILE_EXT;
import static saf.settings.AppPropertyType.WORK_FILE_EXT_DESC;
import static saf.settings.AppStartupConstants.PATH_WORK;
import static saf.settings.AppStartupConstants.SIMPLE_APP_PROPERTIES_FILE_NAME;
import static saf.settings.AppStartupConstants.WORKSPACE_PROPERTIES_FILE_NAME;
import saf.ui.AppYesNoCancelDialogSingleton;

/**
 *
 * @author Yizhi Yang
 */
public class SaveMap extends AppTemplate {

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        loadProperties(SIMPLE_APP_PROPERTIES_FILE_NAME);

        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_WORK));
        fc.setTitle(props.getProperty(SAVE_WORK_TITLE));
        fc.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter(props.getProperty(WORK_FILE_EXT_DESC), props.getProperty(WORK_FILE_EXT)));

        File selectedFile = fc.showSaveDialog(primaryStage);
    }

    @Override
    public AppComponentsBuilder makeAppBuilderHook() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
