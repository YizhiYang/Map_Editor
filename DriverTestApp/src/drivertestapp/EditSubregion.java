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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import static saf.settings.AppPropertyType.BACKGROUND_COLOR;
import static saf.settings.AppPropertyType.CANCEL;
import static saf.settings.AppPropertyType.CAPITAL;
import static saf.settings.AppPropertyType.EDIT_MAP;
import static saf.settings.AppPropertyType.LEADER;
import static saf.settings.AppPropertyType.LOAD_WORK_TITLE;
import static saf.settings.AppPropertyType.NAME;
import static saf.settings.AppPropertyType.OK;
import static saf.settings.AppPropertyType.PICK_A_BACKGROUND_COLOR;
import static saf.settings.AppStartupConstants.PATH_WORK;
import static saf.settings.AppStartupConstants.SIMPLE_APP_PROPERTIES_FILE_NAME;

/**
 *
 * @author Yizhi Yang
 */
public class EditSubregion extends AppTemplate {

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        loadProperties(SIMPLE_APP_PROPERTIES_FILE_NAME);
        GridPane editSubregion = new GridPane();

        editSubregion.setAlignment(Pos.CENTER);
        editSubregion.setVgap(5.5);

        TextField name = new TextField();
        editSubregion.add(new Label(props.getProperty(NAME)), 1, 1);

        editSubregion.add(name, 2, 1);

        TextField capital = new TextField();
        editSubregion.add(new Label(props.getProperty(CAPITAL)), 1, 2);
        editSubregion.add(capital, 2, 2);

        TextField leader = new TextField();
        editSubregion.add(new Label(props.getProperty(LEADER)), 1, 3);
        editSubregion.add(leader, 2, 3);

        //Button previous = new Button(props.getProperty(NEXT));
        //Button next = new Button(props.getProperty(PREVIOUS));
        Button previous = new Button();

        previous.setGraphic(new ImageView(new Image("file:./images/Previous.png")));

        Button next = new Button();
        next.setGraphic(new ImageView(new Image("file:./images/Next.png")));

        editSubregion.add(previous, 1, 5);
        editSubregion.add(next, 4, 5);

        Button ok = new Button(props.getProperty(OK));
        Button cancel = new Button(props.getProperty(CANCEL));

        editSubregion.add(ok, 1, 7);
        editSubregion.add(cancel, 4, 7);

        Scene scene = new Scene(editSubregion, 700, 700);

        Image img = new Image("file:./images/flag.png");

        ImageView imgView = new ImageView(img);

        editSubregion.add(imgView, 0, 0);

        Image img2 = new Image("file:./images/leader.png");

        imgView = new ImageView(img2);

        editSubregion.add(imgView, 4, 0);

        scene.getStylesheets().add("mapeditor/css/mv_style.css");
        editSubregion.getStyleClass().add("cust");

        primaryStage.setTitle(props.getProperty(EDIT_MAP));

        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public AppComponentsBuilder makeAppBuilderHook() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
