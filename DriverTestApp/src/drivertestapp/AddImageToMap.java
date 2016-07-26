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
import mapeditor.MapEditorApp;
import mapeditor.workspaceController.WorkspaceController;
import static saf.settings.AppPropertyType.LOAD_WORK_TITLE;
import static saf.settings.AppStartupConstants.PATH_WORK;

/**
 *
 * @author Yizhi Yang
 */
public class AddImageToMap extends Application{

    public static void main(String[] args) {
        
        launch(args);
    }

    
    @Override
    public void start(Stage primaryStage){
        
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_WORK));
        //fc.setTitle(props.getProperty(LOAD_WORK_TITLE));
        fc.setTitle("Select a image...");
        File selectedFile = fc.showOpenDialog(primaryStage);
        
        
    }
}