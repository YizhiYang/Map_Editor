/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapeditor.workspaceController;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import mapeditor.MapEditorApp;
import mapeditor.data.DataManager;
import mapeditor.data.Subregion;
import mapeditor.file.FileManager;
import mapeditor.gui.Workspace;
import properties_manager.PropertiesManager;
import static saf.settings.AppPropertyType.BACKGROUND_COLOR;
import static saf.settings.AppPropertyType.BORDERS_COLOR;
import static saf.settings.AppPropertyType.CANCEL;
import static saf.settings.AppPropertyType.CAPITAL;
import static saf.settings.AppPropertyType.CHANGEMAPNAME_TOOLTIP;
import static saf.settings.AppPropertyType.CHANGE_MAPS_DIMENSIONS;
import static saf.settings.AppPropertyType.CHANGE_MAP_NAME;
import static saf.settings.AppPropertyType.EDIT_MAP;
import static saf.settings.AppPropertyType.LOAD_WORK_TITLE;
import static saf.settings.AppPropertyType.ENTER_A_NAME_FOR_THE_MAP;
import static saf.settings.AppPropertyType.LEADER;
import static saf.settings.AppPropertyType.NAME_OF_THE_MAP;
import static saf.settings.AppPropertyType.OK;
import static saf.settings.AppPropertyType.NAME;
import static saf.settings.AppPropertyType.NEXT;
import static saf.settings.AppPropertyType.PICK_A_BACKGROUND_COLOR;
import static saf.settings.AppPropertyType.PICK_A_BORDERS_COLOR;
import static saf.settings.AppPropertyType.PREVIOUS;
import static saf.settings.AppStartupConstants.PATH_WORK;
import static saf.settings.AppPropertyType.SAVE_WORK_TITLE;
import static saf.settings.AppPropertyType.X_COORDINATE;
import static saf.settings.AppPropertyType.Y_COORDINATE;
import static saf.settings.AppStartupConstants.PATH_WORK;

/**
 *
 * @author Yizhi Yang
 */
public class WorkspaceController {

    MapEditorApp app;

    double orgSceneX;
    double orgSceneY;
    double orgTranslateX;
    double orgTranslateY;

    int previousIndex;
    int nextIndex;
    int i = 0;

    public WorkspaceController() {

    }

    public WorkspaceController(MapEditorApp app) {

        this.app = app;
    }

    public void handlerChangeMapName() {

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        DataManager dataManager = (DataManager) app.getDataComponent();

        GridPane promptForChangeName = new GridPane();

        promptForChangeName.setAlignment(Pos.CENTER);
        promptForChangeName.setVgap(5.5);

        TextField name = new TextField();
        name.setText(dataManager.getName());
        promptForChangeName.add(new Label(props.getProperty(ENTER_A_NAME_FOR_THE_MAP)), 0, 0);
        promptForChangeName.add(name, 1, 0);

        Button ok = new Button(props.getProperty(OK));
        Button cancel = new Button(props.getProperty(CANCEL));

        promptForChangeName.add(ok, 0, 7);
        promptForChangeName.add(cancel, 1, 7);

        Stage stage = new Stage();

        Scene scene = new Scene(promptForChangeName, 350, 200);

        scene.getStylesheets().add("mapeditor/css/mv_style.css");
        promptForChangeName.getStyleClass().add("cust");

        stage.setTitle(props.getProperty(NAME_OF_THE_MAP));

        stage.setScene(scene);

        stage.show();

        DataManager data = (DataManager) app.getDataComponent();
        ok.setOnAction(e -> {

            data.setName(name.getText());

            stage.close();
        }
        );
        cancel.setOnAction(e -> stage.close());
    }

    public void handlerAddImageToMap() throws IOException {

        PropertiesManager props = PropertiesManager.getPropertiesManager();

        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_WORK));
        fc.setTitle(props.getProperty(LOAD_WORK_TITLE));
        File selectedFile = fc.showOpenDialog(app.getGUI().getWindow());

        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        Pane left = workspace.getLeft();
        // ONLY OPEN A NEW IMAGE IF THE USER SAYS OK
        if (selectedFile != null) {

            DataManager dataManager = (DataManager) app.getDataComponent();
            //--------------------------//
            dataManager.addImage(selectedFile);

            //--------------------------//
            BufferedImage bufferedImage = ImageIO.read(selectedFile);

            Image image = SwingFXUtils.toFXImage(bufferedImage, null);

            ImageView imageView = new ImageView(image);
            
            dataManager.getImageViewsList().add(imageView);

            workspace.getLeft().getChildren().add(imageView);

            imageView.setOnMousePressed(e -> {

                orgSceneX = e.getSceneX();
                orgSceneY = e.getSceneY();
                orgTranslateX = ((ImageView) (e.getSource())).getTranslateX();
                orgTranslateY = ((ImageView) (e.getSource())).getTranslateY();
            });

            imageView.setOnMouseDragged(e -> {

                double offsetX = e.getSceneX() - orgSceneX;
                double offsetY = e.getSceneY() - orgSceneY;
                double newTranslateX = orgTranslateX + offsetX;
                double newTranslateY = orgTranslateY + offsetY;

                ((ImageView) (e.getSource())).setTranslateX(newTranslateX);
                ((ImageView) (e.getSource())).setTranslateY(newTranslateY);

                imageView.setEffect(null);
                Point point = new Point();
                point.setX((imageView.localToParent(imageView.getBoundsInLocal())).getMinX());
                point.setY((imageView.localToParent(imageView.getBoundsInLocal())).getMinY());
                dataManager.getImagesHash().put(selectedFile.getPath(), point);
            });

            Workspace workspaceForRemoveButton = (Workspace) app.getWorkspaceComponent();

            imageView.setOnMouseClicked(e -> {
                
                //DataManager dataManager = (DataManager) app.getDataComponent();
                
                for(int i = 0; i < dataManager.getImageViewsList().size(); i++){
                    
                    dataManager.getImageViewsList().get(i).setEffect(null);
                }
                
                ImageView imageClick = (ImageView) e.getSource();
                imageClick.requestFocus();
                imageClick.setCursor(Cursor.CLOSED_HAND);

                DropShadow dropShadow = new DropShadow();
                dropShadow.setWidth(imageClick.getFitWidth());
                dropShadow.setHeight(imageClick.getFitHeight());
                dropShadow.setColor(Color.BLACK);
                
                dropShadow.setOffsetX(3.0);
                dropShadow.setOffsetY(3.0);

                imageView.setEffect(dropShadow);

                double X = ((imageClick.localToParent(imageClick.getBoundsInLocal())).getMinX());
                double Y = ((imageClick.localToParent(imageClick.getBoundsInLocal())).getMinY());
                Point point = new Point();

                point.setX(X);
                point.setY(Y);

                workspaceForRemoveButton.getRemoveButton().setOnAction(eh -> {

                    for (Map.Entry entry : dataManager.getImagesHash().entrySet()) {

                        if (point.getX() == ((Point) entry.getValue()).getX() && point.getX() == ((Point) entry.getValue()).getX()) {

                            Object key = (String) entry.getKey();

                            dataManager.getImagesList().remove(new File((String) key));

                            dataManager.getImagesHash().remove(key);
                        }

                    }

                    imageClick.setImage(null);

                });
            });

            Point point = new Point();
            point.setX((imageView.localToParent(imageView.getBoundsInLocal())).getMinX());
            point.setY((imageView.localToParent(imageView.getBoundsInLocal())).getMinY());
            dataManager.getImagesHash().put(selectedFile.getPath(), point);
        }
    }

    public void changeBackgroundColor() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        Stage stage = new Stage();
        stage.setTitle(props.getProperty(PICK_A_BACKGROUND_COLOR));
        Scene scene = new Scene(new GridPane(), 300, 300);
        GridPane gridPane = (GridPane) scene.getRoot();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(10);

        final ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(Color.CORNFLOWERBLUE);

        final Text text = new Text(props.getProperty(BACKGROUND_COLOR));
        text.setFont(Font.font("Verdana", 15));
        text.setFill(colorPicker.getValue());

        text.getStyleClass().add("cust");
        Button ok = new Button(props.getProperty(OK));
        Button cancel = new Button(props.getProperty(CANCEL));

        DataManager dataManager = (DataManager) app.getDataComponent();

        colorPicker.setOnAction(e -> {
            text.setFill(colorPicker.getValue());
            ok.setOnAction(ex -> {
                Workspace workspace = (Workspace) app.getWorkspaceComponent();

                Pane left = workspace.getLeft();

                left.setBackground(new Background(new BackgroundFill(colorPicker.getValue(), CornerRadii.EMPTY, Insets.EMPTY)));

                String hex = String.format("#%02X%02X%02X",
                        (int) (colorPicker.getValue().getRed() * 255),
                        (int) (colorPicker.getValue().getGreen() * 255),
                        (int) (colorPicker.getValue().getBlue() * 255));
                
                dataManager.setBackground(hex);
                 workspace.getRectangle().setFill(Color.web(hex));
                
                stage.close();
            });
        });

        ok.setOnAction(ex -> {
            Workspace workspace = (Workspace) app.getWorkspaceComponent();

            Pane left = workspace.getLeft();

            String hex = String.format("#%02X%02X%02X",
                    (int) (colorPicker.getValue().getRed() * 255),
                    (int) (colorPicker.getValue().getGreen() * 255),
                    (int) (colorPicker.getValue().getBlue() * 255));
            
            dataManager.setBackground(hex);
            
//            left.setBackground(new Background(new BackgroundFill(colorPicker.getValue(), CornerRadii.EMPTY, Insets.EMPTY)));
//            left.setStyle("-fx-background-color: " + dataManager.getBackgroundColor());
             workspace.getRectangle().setFill(Color.web(hex));

        //workspace.getRectangle().setFill(Color.web(dataManager.getBackgroundColor()));
            
            stage.close();
        });

        cancel.setOnAction(e -> stage.close());

        gridPane.add(colorPicker, 0, 0);
        gridPane.add(text, 0, 1);
        gridPane.add(ok, 0, 2);
        gridPane.add(cancel, 1, 2);

        scene.getStylesheets().add("mapeditor/css/mv_style.css");
        gridPane.getStyleClass().add("cust");

        stage.setScene(scene);
        stage.show();
    }

    public void changeBorderColor() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        Stage stage = new Stage();

        String title = props.getProperty(PICK_A_BORDERS_COLOR);

        stage.setTitle(title);

        Scene scene = new Scene(new GridPane(), 300, 300);
        GridPane gridPane = (GridPane) scene.getRoot();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(10);

        final ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(Color.CORAL);

        final Text text = new Text(props.getProperty(BORDERS_COLOR));
        text.setFont(Font.font("Verdana", 15));
        text.setFill(colorPicker.getValue());
        text.getStyleClass().add("cust");

        colorPicker.setOnAction(e
                -> text.setFill(colorPicker.getValue())
        );

        Button ok = new Button(props.getProperty(OK));
        Button cancel = new Button(props.getProperty(CANCEL));

        //GridPane.getChildren().addAll(colorPicker, text);
        gridPane.add(colorPicker, 0, 0);
        gridPane.add(text, 0, 1);
        gridPane.add(ok, 0, 2);
        gridPane.add(cancel, 1, 2);

        DataManager dataManager = (DataManager) app.getDataComponent();

        colorPicker.setOnAction(e -> {
            text.setFill(colorPicker.getValue());
            ok.setOnAction(ex -> {
                Workspace workspace = (Workspace) app.getWorkspaceComponent();

                for (int i = 0; i < dataManager.getSubregions().size(); i++) {

                    Subregion subregion = (Subregion) dataManager.getSubregions().get(i);

                    for (int j = 0; j < subregion.getPolygons().size(); j++) {

                        subregion.getPolygons().get(j).setStroke(colorPicker.getValue());
                    }
                }

                String hex = String.format("#%02X%02X%02X",
                        (int) (colorPicker.getValue().getRed() * 255),
                        (int) (colorPicker.getValue().getGreen() * 255),
                        (int) (colorPicker.getValue().getBlue() * 255));

                dataManager.setBorderColor(hex);
                stage.close();
            });
        });

        ok.setOnAction(ex -> {

            for (int i = 0; i < dataManager.getSubregions().size(); i++) {

                Subregion subregion = (Subregion) dataManager.getSubregions().get(i);

                for (int j = 0; j < subregion.getPolygons().size(); j++) {

                    subregion.getPolygons().get(j).setStroke(colorPicker.getValue());
                }
            }

            String hex = String.format("#%02X%02X%02X",
                    (int) (colorPicker.getValue().getRed() * 255),
                    (int) (colorPicker.getValue().getGreen() * 255),
                    (int) (colorPicker.getValue().getBlue() * 255));
            dataManager.setBorderColor(hex);
            stage.close();
        });

        cancel.setOnAction(e -> stage.close());

        scene.getStylesheets().add("mapeditor/css/mv_style.css");
        gridPane.getStyleClass().add("cust");
        stage.setScene(scene);
        stage.show();
    }

    public void changeMapDimensions() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

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

        Stage stage = new Stage();

        Scene scene = new Scene(promptForChangeDimensions, 350, 200);

        stage.setTitle(props.getProperty(CHANGE_MAPS_DIMENSIONS));

        scene.getStylesheets().add("mapeditor/css/mv_style.css");
        promptForChangeDimensions.getStyleClass().add("cust");
        stage.setScene(scene);

        stage.show();

        ok.setOnAction(e -> {

            DataManager dataManager = (DataManager) app.getDataComponent();
            Workspace workspace = (Workspace) app.getWorkspaceComponent();

            workspace.getRectangle().setWidth(Integer.parseInt(X.getText()));
            workspace.getRectangle().setHeight(Integer.parseInt(Y.getText()));
            workspace.getLeft().getChildren().remove(workspace.getRectangle());
            workspace.getLeft().getChildren().add(0, workspace.getRectangle());
            
            workspace.getRectangle().setFill(Color.web(dataManager.getBackgroundColor()));
            stage.close();
            
            System.out.println(workspace.getLeft().getWidth());
            System.out.println(workspace.getLeft().getHeight());

        });
        cancel.setOnAction(e -> stage.close());
    }

    public void thicknessHandler(Slider slider, DataManager dataManager) {

        for (int i = 0; i < dataManager.getSubregions().size(); i++) {

            Subregion subregion = (Subregion) dataManager.getSubregions().get(i);

            for (int j = 0; j < subregion.getPolygons().size(); j++) {

                subregion.getPolygons().get(j).setStrokeWidth(slider.getValue());
            }
        }

        dataManager.setBorderThickness(Double.toString(slider.getValue()));
    }

    public void zoomHandler(Slider zoom, DataManager dataManager) {

        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        Group map = workspace.getGroupMap();

        map.setScaleX(zoom.getValue());
        map.setScaleY(zoom.getValue());

        dataManager.setZoomLevel(Double.toString(zoom.getValue()));
    }

    public void handlerTableSelected(TableView itemsTable) {

        Subregion subregion = (Subregion) itemsTable.getSelectionModel().getSelectedItem();

        this.editSubregion(subregion);

    }

    public void editSubregion(Subregion subregion) {

        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();

        Stage stage = new Stage();
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        GridPane editSubregion = new GridPane();

        editSubregion.setAlignment(Pos.CENTER);
        editSubregion.setVgap(5.5);

        TextField name = new TextField();
        name.setText(subregion.getName());
        editSubregion.add(new Label(props.getProperty(NAME)), 1, 1);

        editSubregion.add(name, 2, 1);

        TextField capital = new TextField();
        capital.setText(subregion.getCapital());
        editSubregion.add(new Label(props.getProperty(CAPITAL)), 1, 2);
        editSubregion.add(capital, 2, 2);

        TextField leader = new TextField();
        leader.setText(subregion.getLeader());
        editSubregion.add(new Label(props.getProperty(LEADER)), 1, 3);
        editSubregion.add(leader, 2, 3);

        Button previous = new Button();
        previous.setGraphic(new ImageView(new Image("file:./images/Previous.png")));

        if (dataManager.getSubregions().indexOf(subregion) == 0) {
            previous.setDisable(true);
        }

        previous.setOnAction(e -> {

            int i = 0;
            while (!dataManager.getSubregions().get(i).equals(subregion)) {

                i++;
            }
            i--;

            workspace.reseTheColors();

            this.editSubregion((Subregion) dataManager.getSubregions().get(i));

            stage.close();
        });

        Button next = new Button();

        if (dataManager.getSubregions().indexOf(subregion) == dataManager.getSubregions().size() - 1) {
            next.setDisable(true);
        }
        next.setGraphic(new ImageView(new Image("file:./images/Next.png")));
        next.setOnAction(e -> {

            int i = 0;
            while (!dataManager.getSubregions().get(i).equals(subregion)) {

                i++;
            }
            i++;

            workspace.reseTheColors();

            this.editSubregion((Subregion) dataManager.getSubregions().get(i));

            stage.close();
        });

        editSubregion.add(previous, 1, 5);
        editSubregion.add(next, 4, 5);

        Button ok = new Button(props.getProperty(OK));
        Button cancel = new Button(props.getProperty(CANCEL));

        editSubregion.add(ok, 1, 7);
        editSubregion.add(cancel, 4, 7);

        Scene scene = new Scene(editSubregion, 700, 700);

        String flagPath = (dataManager.getParentDir() + "\\" + subregion.getName());
        String leaderPath = (dataManager.getParentDir() + "\\" + subregion.getLeader());
        File imageFile = new File(flagPath + " Flag.png");
        File leaderFile = new File(leaderPath + ".png");

        System.out.println(imageFile.getPath());
        System.out.println(leaderFile.getPath());

        try {
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            Image img = SwingFXUtils.toFXImage(bufferedImage, null);
            ImageView imgView = new ImageView(img);

            imgView.setFitHeight(200);
            imgView.setFitWidth(125);
            imgView.setPreserveRatio(true);
            editSubregion.add(imgView, 0, 0);
        } catch (IOException e) {

        }
        try {
            BufferedImage bufferedImage2 = ImageIO.read(leaderFile);
            Image img2 = SwingFXUtils.toFXImage(bufferedImage2, null);
            ImageView imgView2 = new ImageView(img2);

            imgView2.setFitHeight(200);
            imgView2.setFitWidth(125);
            imgView2.setPreserveRatio(true);
            editSubregion.add(imgView2, 4, 0);
        } catch (IOException eh) {
        }

        scene.getStylesheets()
                .add("mapeditor/css/mv_style.css");
        editSubregion.getStyleClass()
                .add("cust");

        stage.setTitle(props.getProperty(EDIT_MAP));

        stage.setScene(scene);

        stage.setX(
                ((app.getGUI().getPrimaryScene().getWidth()) / 2) + 250);
        stage.setY(
                30);

        stage.show();

        for (int i = 0; i < subregion.getPolygons().size(); i++) {

            subregion.getPolygons().get(i).setFill(Color.LIGHTPINK);
        }

        ok.setOnAction(e
                -> {
            //DataManager dataManager = (DataManager) app.getDataComponent();

            subregion.setName(name.getText());
            subregion.setCapital(capital.getText());
            subregion.setLeader(leader.getText());

            int red = Integer.parseInt(subregion.getRed());
            int blue = Integer.parseInt(subregion.getBlue());
            int green = Integer.parseInt(subregion.getGreen());

            for (int i = 0; i < subregion.getPolygons().size(); i++) {

                subregion.getPolygons().get(i).setFill(Color.rgb(red, green, blue));
            }

            int index = dataManager.getSubregions().indexOf(subregion);
            workspace.getTable().getSelectionModel().select(index);
            workspace.updataTooltip();
            stage.close();
        }
        );

        cancel.setOnAction(e -> stage.close());

        stage.setOnCloseRequest(e -> {
            int index = dataManager.getSubregions().indexOf(subregion);
            workspace.getTable().getSelectionModel().select(index);
            workspace.reseTheColors();
        });
    }

    public void reassignColorsHandler(DataManager dataManager) {

        int randomNumber = (int) (Math.random() * 254);

        int counter = 254 / dataManager.getSubregions().size();

        int interval = 0;

        ArrayList<Integer> numbers = new ArrayList();

        for (int i = 0; i < dataManager.getSubregions().size(); i++) {

            interval += counter;
            numbers.add(interval);
        }

        Collections.shuffle(numbers);

        for (int i = 0; i < numbers.size(); i++) {

            Subregion subregion = (Subregion) dataManager.getSubregions().get(i);

            for (int j = 0; j < subregion.getPolygons().size(); j++) {
                subregion.getPolygons().get(j).setFill(Color.rgb(numbers.get(i), numbers.get(i), numbers.get(i)));
            }

            subregion.setColor(Integer.toString(numbers.get(i)), Integer.toString(numbers.get(i)), Integer.toString(numbers.get(i)));
        }

    }

    public void previous(Subregion subregion) {

        DataManager dataManager = (DataManager) app.getDataComponent();

        int i = 0;

        while ((!((Subregion) dataManager.getSubregions().get(i)).equals(subregion)) && i > 0) {
            i++;
        }
    }

    public void testInfo(DataManager dataManager) {

    }

    Sequence sequence;
    Sequencer sequencer;

    public void playAnthemHandler() throws InvalidMidiDataException, MidiUnavailableException, IOException {

        DataManager dataManager = (DataManager) app.getDataComponent();

        File MidiFile = new File(dataManager.getParentDir() + "//" + dataManager.getName() + " National Anthem.mid");

        sequence = MidiSystem.getSequence(MidiFile);
        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.setSequence(sequence);
        sequencer.start();
    }

    public void pauseAnthemHandler() {

        if (sequencer != null) {
            sequencer.stop();
        }
    }

}
