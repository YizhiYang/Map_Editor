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
import static saf.settings.AppPropertyType.BORDERS_COLOR;
import static saf.settings.AppPropertyType.CANCEL;
import static saf.settings.AppPropertyType.LOAD_WORK_TITLE;
import static saf.settings.AppPropertyType.OK;
import static saf.settings.AppPropertyType.PICK_A_BORDERS_COLOR;
import static saf.settings.AppStartupConstants.PATH_WORK;
import static saf.settings.AppStartupConstants.SIMPLE_APP_PROPERTIES_FILE_NAME;

/**
 *
 * @author Yizhi Yang
 */
public class ChangeBorderColor extends AppTemplate {

    public static void main(String[] args) {

        launch(args);
    }

    public void start(Stage primaryStage) {

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        loadProperties(SIMPLE_APP_PROPERTIES_FILE_NAME);
        
        
        String title = props.getProperty(PICK_A_BORDERS_COLOR);
        
        primaryStage.setTitle(title);
        
        Scene scene = new Scene(new GridPane(), 300, 300);
        GridPane gridPane =(GridPane) scene.getRoot();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(10);
            
             
        final ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(Color.CORNFLOWERBLUE);
        
        final Text text = new Text(props.getProperty(BORDERS_COLOR));
        text.setFont(Font.font ("Verdana", 15));
        text.setFill(colorPicker.getValue());
        text.getStyleClass().add("cust");
        
        colorPicker.setOnAction(e->
                
                text.setFill(colorPicker.getValue())              
            
        );
        
        Button ok = new Button(props.getProperty(OK));
        Button cancel = new Button(props.getProperty(CANCEL));
 
        //GridPane.getChildren().addAll(colorPicker, text);
        
        gridPane.add(colorPicker, 0, 0);
        gridPane.add(text, 0, 1);
        gridPane.add(ok, 0, 2);
        gridPane.add(cancel, 1, 2);
 
        scene.getStylesheets().add("mapeditor/css/mv_style.css");
        
        gridPane.getStyleClass().add("cust");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public AppComponentsBuilder makeAppBuilderHook() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
