/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drivertestapp;

import java.io.File;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mapeditor.MapEditorApp;
import mapeditor.workspaceController.WorkspaceController;
import properties_manager.PropertiesManager;
import saf.AppTemplate;
import saf.components.AppComponentsBuilder;
import static saf.settings.AppPropertyType.CANCEL;
import static saf.settings.AppPropertyType.CHANGE_MAPS_DIMENSIONS;
import static saf.settings.AppPropertyType.LOAD_WORK_TITLE;
import static saf.settings.AppPropertyType.OK;
import static saf.settings.AppPropertyType.X_COORDINATE;
import static saf.settings.AppPropertyType.Y_COORDINATE;
import static saf.settings.AppStartupConstants.PATH_WORK;
import static saf.settings.AppStartupConstants.SIMPLE_APP_PROPERTIES_FILE_NAME;

/**
 *
 * @author Yizhi Yang
 */
public class ChangeMapDimensions extends AppTemplate{


    public static void main(String[] args) {
        
        launch(args);
    }

    public void start(Stage primaryStage){
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        loadProperties(SIMPLE_APP_PROPERTIES_FILE_NAME);
        
        GridPane promptForChangeDimensions = new GridPane();

        promptForChangeDimensions.setAlignment(Pos.CENTER);
        promptForChangeDimensions.setVgap(5.5);

        TextField X = new TextField();
        promptForChangeDimensions.add(new Label(props.getProperty(X_COORDINATE)), 0, 0);
        promptForChangeDimensions.add(X, 1, 0);
        
         TextField Y = new TextField();
        promptForChangeDimensions.add(new Label(props.getProperty(Y_COORDINATE)), 0, 1);
        promptForChangeDimensions.add(Y, 1, 1);

        Button ok = new Button(props.getProperty(OK));
        Button cancel = new Button(props.getProperty(CANCEL));

        promptForChangeDimensions.add(ok, 0, 7);
        promptForChangeDimensions.add(cancel, 1, 7);

        

        Scene scene = new Scene(promptForChangeDimensions, 350, 300);

        primaryStage.setTitle(props.getProperty(CHANGE_MAPS_DIMENSIONS));

        scene.getStylesheets().add("mapeditor/css/mv_style.css");
        promptForChangeDimensions.getStyleClass().add("cust");
        primaryStage.setScene(scene);

        primaryStage.show();

        ok.setOnAction(e -> primaryStage.close());
        cancel.setOnAction(e -> primaryStage.close());
    }

    @Override
    public AppComponentsBuilder makeAppBuilderHook() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}