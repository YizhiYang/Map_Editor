/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drivertestapp;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import mapeditor.workspaceController.WorkspaceController;
import properties_manager.PropertiesManager;
import saf.AppTemplate;
import saf.components.AppComponentsBuilder;
import saf.controller.AppFileController;
import static saf.settings.AppPropertyType.CANCEL;
import static saf.settings.AppPropertyType.CAPITAL;
import static saf.settings.AppPropertyType.CHOOSE_A_DATA_FILE;
import static saf.settings.AppPropertyType.CHOOSE_A_PARENT_DIRECTORY;
import static saf.settings.AppPropertyType.EDIT_MAP;
import static saf.settings.AppPropertyType.ENTER_A_NAME_FOR_THE_MAP;
import static saf.settings.AppPropertyType.LEADER;
import static saf.settings.AppPropertyType.NAME;
import static saf.settings.AppPropertyType.NEW_MAP;
import static saf.settings.AppPropertyType.OK;
import static saf.settings.AppStartupConstants.SIMPLE_APP_PROPERTIES_FILE_NAME;

/**
 *
 * @author Yizhi Yang
 */
public class NewMap extends AppTemplate{


    public static void main(String[] args) {
        
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        
        AppFileController appFile = new AppFileController();
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        loadProperties(SIMPLE_APP_PROPERTIES_FILE_NAME);

        GridPane newMap = new GridPane();
        
        newMap.setAlignment(Pos.CENTER);
        newMap.setVgap(5.5);

        TextField name = new TextField();
        newMap.add(new Label(props.getProperty(ENTER_A_NAME_FOR_THE_MAP)), 0, 0);
        newMap.add(name, 1, 0);
        
        newMap.add(new Label(props.getProperty(CHOOSE_A_DATA_FILE)), 0, 1);
        Button button1 = new Button();
        button1.setGraphic(new ImageView(new Image("file:./images/folder.png")));
        newMap.add(button1, 1, 1);
        
        newMap.add(new Label(props.getProperty(CHOOSE_A_PARENT_DIRECTORY)), 0, 2);
        Button button2 = new Button();
        button2.setGraphic(new ImageView(new Image("file:./images/folder.png")));
        newMap.add(button2, 1, 2);
        
        Button ok = new Button(props.getProperty(OK));
        Button cancel = new Button(props.getProperty(CANCEL));
        
        newMap.add(ok, 0, 7);
        newMap.add(cancel, 1, 7);

        Scene scene = new Scene(newMap, 350, 200);
        
        scene.getStylesheets().add("mapeditor/css/mv_style.css");
        newMap.getStyleClass().add("cust");
        
        primaryStage.setTitle(props.getProperty(NEW_MAP));

        primaryStage.setScene(scene);

        primaryStage.show();
        
        ok.setOnAction(e -> {
            
            primaryStage.close();});
        cancel.setOnAction(e -> primaryStage.close());
    }

    @Override
    public AppComponentsBuilder makeAppBuilderHook() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
