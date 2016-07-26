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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import mapeditor.workspaceController.WorkspaceController;
import properties_manager.PropertiesManager;
import saf.AppTemplate;
import saf.components.AppComponentsBuilder;
import static saf.settings.AppPropertyType.CANCEL;
import static saf.settings.AppPropertyType.ENTER_A_NAME_FOR_THE_MAP;
import static saf.settings.AppPropertyType.NAME_OF_THE_MAP;
import static saf.settings.AppPropertyType.OK;
import static saf.settings.AppStartupConstants.SIMPLE_APP_PROPERTIES_FILE_NAME;
import static saf.settings.AppStartupConstants.WORKSPACE_PROPERTIES_FILE_NAME;

/**
 *
 * @author Yizhi Yang
 */
public class ChangeMapName extends AppTemplate {

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage){

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        loadProperties(SIMPLE_APP_PROPERTIES_FILE_NAME);

        GridPane promptForChangeName = new GridPane();

        promptForChangeName.setAlignment(Pos.CENTER);
        promptForChangeName.setVgap(5.5);

        TextField category = new TextField();
        promptForChangeName.add(new Label(props.getProperty(ENTER_A_NAME_FOR_THE_MAP)), 0, 0);
        promptForChangeName.add(category, 1, 0);

        Button ok = new Button(props.getProperty(OK));
        Button cancel = new Button(props.getProperty(CANCEL));

        promptForChangeName.add(ok, 0, 7);
        promptForChangeName.add(cancel, 1, 7);


        Scene scene = new Scene(promptForChangeName, 350, 200);

        scene.getStylesheets().add("mapeditor/css/mv_style.css");
        promptForChangeName.getStyleClass().add("cust");

        primaryStage.setTitle(props.getProperty(NAME_OF_THE_MAP));

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
