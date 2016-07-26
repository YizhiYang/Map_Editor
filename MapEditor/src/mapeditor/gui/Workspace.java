/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapeditor.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import static javafx.scene.input.DataFormat.URL;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.Clip;
import mapeditor.MapEditorApp;
import mapeditor.data.DataManager;
import mapeditor.data.Subregion;
import mapeditor.workspaceController.WorkspaceController;
import properties_manager.PropertiesManager;
import saf.components.AppWorkspaceComponent;
import static saf.settings.AppPropertyType.NEW_ICON;
import static saf.settings.AppPropertyType.NEW_TOOLTIP;
import static saf.settings.AppPropertyType.CHANGEMAPNAME_TOOLTIP;
import static saf.settings.AppPropertyType.ADDIMAGETOMAP_TOOLTIP;
import static saf.settings.AppPropertyType.REMOVEIMAGEFROMMAP_TOOLTIP;
import static saf.settings.AppPropertyType.CHANGEMAPBACKGROUNDCOLOR_TOOLTIP;
import static saf.settings.AppPropertyType.CHANGEBORDERCOLOR_TOOLTIP;
import static saf.settings.AppPropertyType.CHANGEBORDERTHICKNESS_TOOLTIP;
import static saf.settings.AppPropertyType.REASSIGNMAPCOLORS_TOOLTIP;
import static saf.settings.AppPropertyType.PLAYSUBREGIONANTHEM_TOOLTIP;
import static saf.settings.AppPropertyType.ZOOMMAPINOUT_TOOLTIP;
import static saf.settings.AppPropertyType.CHANGEMAPDIMENSIONS_TOOLTIP;
import static saf.settings.AppPropertyType.CHANGE_MAP_NAME;
import static saf.settings.AppPropertyType.ADD_IMAGE_TO_MAP;
import static saf.settings.AppPropertyType.REMOVE_IMAGE_ON_MAP;
import static saf.settings.AppPropertyType.CHANGE_MAP_BACKGROUND_COLOR;
import static saf.settings.AppPropertyType.CHANGE_BORDER_COLOR;
import static saf.settings.AppPropertyType.CHANGE_BORDER_THICKNESS;
import static saf.settings.AppPropertyType.REASSIGN_MAP_COLORS;
import static saf.settings.AppPropertyType.PLAY_SUB_REGION_ANTHEM;
import static saf.settings.AppPropertyType.CHANGE_MAP_DIMENSIONS;
import static saf.settings.AppPropertyType.PAUSE_ANTHEM;
import static saf.settings.AppPropertyType.PAUSEANTHEM_TOOLTIP;

import saf.ui.AppGUI;

/**
 *
 * @author Yizhi Yang
 */
public class Workspace extends AppWorkspaceComponent {

    MapEditorApp app;

    AppGUI gui;

    Button changeMapName;
    Button addImageToMap;
    Button removeImageOnMap;
    Button changeMapBackgroundColor;
    Button changeBorderColor;
    Button changeBorderThickness;
    Button reassignMapColors;
    Button playSubregionAnthem;
    Button pauseAnthem;
    Slider thicknessSlider;
    Slider zoomSlider;
    Button changeMapDimensions;
    SplitPane splitPane;

    Label label;
    Label number;

    TableColumn nameColumn;
    TableColumn leaderColumn;
    TableColumn capitalColumn;

    StackPane left;
    TableView<Subregion> itemsTable;
    StackPane root;

    Group groupForMap;
    Group groupForImages;

    double orgSceneX;
    double orgSceneY;
    double orgTranslateX;
    double orgTranslateY;

    int translateX;
    int translateY;

    Rectangle rectangle;
    Rectangle rectangle2;
    Tooltip tooltip;
    
    

    public Workspace(MapEditorApp initApp) {

        root = new StackPane();

        app = initApp;

        gui = app.getGUI();

        workspace = new Pane();

        splitPane = new SplitPane();

//        left = new StackPane();
        itemsTable = new TableView<>();

        activateWorkspace(app.getGUI().getAppPane());

        layoutGUI();

        translateX = 0;

    }

    @Override
    public void reloadWorkspace() {

        DataManager data = (DataManager) app.getDataComponent();

        data.getImageViewsList().clear();
        WorkspaceController workspaceController = new WorkspaceController(app);

        for (int j = 0; j < data.getSubregions().size(); j++) {

            Subregion subregion = (Subregion) data.getSubregions().get(j);

            for (int i = 0; i < subregion.getPolygons().size(); i++) {

                groupForMap.getChildren().add(subregion.getPolygons().get(i));

                subregion.getPolygons().get(i).setOnMouseClicked(e -> {

                    if (e.getClickCount() == 2) {
                        this.reseTheColors();
                        Polygon clicked = (Polygon) e.getSource();
                        for (int k = 0; k < data.getSubregions().size(); k++) {

                            Subregion subregionForComparing = (Subregion) data.getSubregions().get(k);

                            for (int l = 0; l < subregionForComparing.getPolygons().size(); l++) {

                                if (subregionForComparing.getPolygons().get(l).equals(clicked)) {
                                    itemsTable.requestFocus();
                                    itemsTable.getSelectionModel().select(subregionForComparing);

                                    for (int innerCounter = 0; innerCounter < subregion.getPolygons().size(); innerCounter++) {

                                        //subregion.getPolygons().get(innerCounter).setFill(Color.LIGHTPINK);
                                    }

                                    workspaceController.editSubregion(subregionForComparing);
                                };

                            }
                        }
                    } else if (e.getClickCount() == 1) {

                        this.reseTheColors();
                        Polygon clicked = (Polygon) e.getSource();
                        for (int k = 0; k < data.getSubregions().size(); k++) {

                            Subregion subregionForComparing = (Subregion) data.getSubregions().get(k);

                            for (int l = 0; l < subregionForComparing.getPolygons().size(); l++) {

                                if (subregionForComparing.getPolygons().get(l).equals(clicked)) {
                                    itemsTable.requestFocus();
                                    itemsTable.getSelectionModel().select(subregionForComparing);

                                    for (int innerCounter = 0; innerCounter < subregion.getPolygons().size(); innerCounter++) {

                                        subregion.getPolygons().get(innerCounter).setFill(Color.LIGHTBLUE);
                                    }

                                };

                            }
                        }

                    }

                });
                tooltip = new Tooltip(subregion.getName());
                Tooltip.install(subregion.getPolygons().get(i), tooltip);
            }
        }

        String backgroundColor = ((DataManager) app.getDataComponent()).getBackgroundColor();

        groupForMap.setScaleX(Double.parseDouble(((DataManager) app.getDataComponent()).getZoomLevel()));
        groupForMap.setScaleY(Double.parseDouble(((DataManager) app.getDataComponent()).getZoomLevel()));
        thicknessSlider.setValue(Double.parseDouble(((DataManager) app.getDataComponent()).getBorderThickness()));
        zoomSlider.setValue(Double.parseDouble(((DataManager) app.getDataComponent()).getZoomLevel()));
        left.setStyle("-fx-background-color: " + backgroundColor);
        left.getChildren().add(groupForMap);
        changeMapName.setDisable(false);
        addImageToMap.setDisable(false);
        removeImageOnMap.setDisable(false);
        changeMapBackgroundColor.setDisable(false);
        changeBorderColor.setDisable(false);
        thicknessSlider.setDisable(false);
        reassignMapColors.setDisable(false);
        playSubregionAnthem.setDisable(false);
        zoomSlider.setDisable(false);
        changeMapDimensions.setDisable(false);

        number.setText(((DataManager) app.getDataComponent()).getSubregions().size() + "         ");
        label.setText(((DataManager) app.getDataComponent()).getName() + ".");

        for (int i = 0; i < data.getImagesList().size(); i++) {

            File file = new File(data.getImagesList().get(i).getPath());
            BufferedImage bufferedImage;
            try {

                bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                ImageView imageView = new ImageView(image);

                data.getImageViewsList().add(imageView);

                FlowPane flow = (FlowPane) app.getGUI().getAppPane().getTop();

                imageView.setTranslateX(data.getImagesHash().get(file.getPath()).getX() - ((Workspace) app.getWorkspaceComponent()).getLeft().getWidth() / 2 + 101);

                imageView.setTranslateY(data.getImagesHash().get(file.getPath()).getY() - ((Workspace) app.getWorkspaceComponent()).getLeft().getHeight() / 2 + flow.getHeight() + 14);

                left.getChildren().add(imageView);

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

                    Light.Point point = new Light.Point();
                    point.setX((imageView.localToParent(imageView.getBoundsInLocal())).getMinX());
                    point.setY((imageView.localToParent(imageView.getBoundsInLocal())).getMinY());
                    data.getImagesHash().put(file.getPath(), point);
                });

                Workspace workspaceForRemoveButton = (Workspace) app.getWorkspaceComponent();

                imageView.setOnMouseClicked(e -> {

                    for (int j = 0; j < data.getImageViewsList().size(); j++) {

                        data.getImageViewsList().get(j).setEffect(null);
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
                    Light.Point point = new Light.Point();

                    point.setX(X);
                    point.setY(Y);

                    workspaceForRemoveButton.getRemoveButton().setOnAction(eh -> {

                        for (Map.Entry entry : data.getImagesHash().entrySet()) {

                            if (point.getX() == ((Light.Point) entry.getValue()).getX() && point.getX() == ((Light.Point) entry.getValue()).getX()) {

                                Object key = (String) entry.getKey();

                                data.getImagesList().remove(new File((String) key));

                                data.getImagesHash().remove(key);
                            }

                        }

                        imageClick.setImage(null);

                    });
                });

            } catch (IOException ex) {
                Logger.getLogger(Workspace.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

//        rectangle.setWidth(left.getWidth());
//        rectangle.setHeight(left.getHeight());
//        System.out.println(left.getWidth());
//
//        left.setClip(rectangle);
    }

    @Override
    public void initStyle() {

    }

    private void layoutGUI() {

        left = new StackPane();

        groupForMap = new Group();

        groupForImages = new Group();

        HBox hbox = new HBox();

        hbox.setMinWidth(100);
        ((FlowPane) app.getGUI().getAppPane().getTop()).getChildren().add(hbox);

        Pane top = (Pane) app.getGUI().getAppPane().getTop();
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        Label nameOfTheApp = new Label("Regio Vinco Map Editor   ");
        nameOfTheApp.getStyleClass().add("cust2");
        top.getChildren().add(nameOfTheApp);

        label = new Label();
        Label label2 = new Label("                        Map name:  ");
        label2.getStyleClass().add("cust");
        label.setMinWidth(80);
        label.setText(((DataManager) app.getDataComponent()).getName());
        label.getStyleClass().add("cust");

        Label numberOfSubregions = new Label("    Number Of Subregions: ");

        number = new Label();
        number.setMinWidth(40);
        number.getStyleClass().add("cust");
        numberOfSubregions.getStyleClass().add("cust");
        number.setText(((DataManager) app.getDataComponent()).getSubregions().size() + "   ");

//        top.getChildren().add(label2);
//        top.getChildren().add(label);
//        top.getChildren().add(numberOfSubregions);
//        top.getChildren().add(number);
        
        changeMapName = app.getGUI().initChildButton(top, CHANGE_MAP_NAME.toString(), CHANGEMAPNAME_TOOLTIP.toString(), true);
        addImageToMap = app.getGUI().initChildButton(top, ADD_IMAGE_TO_MAP.toString(), ADDIMAGETOMAP_TOOLTIP.toString(), true);
        removeImageOnMap = app.getGUI().initChildButton(top, REMOVE_IMAGE_ON_MAP.toString(), REMOVEIMAGEFROMMAP_TOOLTIP.toString(), true);
        changeMapBackgroundColor = app.getGUI().initChildButton(top, CHANGE_MAP_BACKGROUND_COLOR.toString(), CHANGEMAPBACKGROUNDCOLOR_TOOLTIP.toString(), true);
        changeBorderColor = app.getGUI().initChildButton(top, CHANGE_BORDER_COLOR.toString(), CHANGEBORDERCOLOR_TOOLTIP.toString(), true);
        Label thickness = new Label("          Thickness:   ");
        thickness.getStyleClass().add("cust");
        top.getChildren().add(thickness);

        thicknessSlider = new Slider(0, 0.2, 0.1);
        thicknessSlider.setDisable(true);
        thicknessSlider.setShowTickLabels(true);
        thicknessSlider.setShowTickMarks(true);
        thicknessSlider.setMajorTickUnit(0.05f);
        thicknessSlider.setBlockIncrement(0.05f);
        top.getChildren().add(thicknessSlider);
        Tooltip buttonTooltip = new Tooltip(props.getProperty(CHANGEBORDERTHICKNESS_TOOLTIP.toString()));
        thicknessSlider.setTooltip(buttonTooltip);
        Label empty2 = new Label("      ");
        top.getChildren().add(empty2);
        reassignMapColors = app.getGUI().initChildButton(top, REASSIGN_MAP_COLORS.toString(), REASSIGNMAPCOLORS_TOOLTIP.toString(), true);
        playSubregionAnthem = app.getGUI().initChildButton(top, PLAY_SUB_REGION_ANTHEM.toString(), PLAYSUBREGIONANTHEM_TOOLTIP.toString(), true);
        pauseAnthem = app.getGUI().initChildButton(top, PAUSE_ANTHEM.toString(), PAUSEANTHEM_TOOLTIP.toString(), true);
        Label zoom = new Label("          Zoom:   ");
        zoom.getStyleClass().add("cust");
        top.getChildren().add(zoom);

        zoomSlider = new Slider(0, 512, 1);
        zoomSlider.setDisable(true);
        zoomSlider.setShowTickLabels(true);
        zoomSlider.setShowTickMarks(true);
        zoomSlider.setMajorTickUnit(64f);
        zoomSlider.setBlockIncrement(1f);
        top.getChildren().add(zoomSlider);
        Tooltip buttonTooltip2 = new Tooltip(props.getProperty(ZOOMMAPINOUT_TOOLTIP.toString()));
        zoomSlider.setTooltip(buttonTooltip2);
        Label empty = new Label("      ");
        top.getChildren().add(empty);
        changeMapDimensions = app.getGUI().initChildButton(top, CHANGE_MAP_DIMENSIONS.toString(), CHANGEMAPDIMENSIONS_TOOLTIP.toString(), true);

        WorkspaceController workspaceController = new WorkspaceController(app);

        changeMapName.setOnAction(e -> {
            this.reseTheColors();
            workspaceController.handlerChangeMapName();
            this.reseTheColors();
        });

        addImageToMap.setOnAction(e -> {
            try {
                this.reseTheColors();
                workspaceController.handlerAddImageToMap();
            } catch (IOException ex) {
                Logger.getLogger(Workspace.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        changeMapBackgroundColor.setOnAction(e -> {
            this.reseTheColors();
            workspaceController.changeBackgroundColor();
        });

        changeBorderColor.setOnAction(e -> {
            this.reseTheColors();
            workspaceController.changeBorderColor();

        });

        reassignMapColors.setOnAction(e -> {
            this.reseTheColors();
            workspaceController.reassignColorsHandler(((DataManager) app.getDataComponent()));
        });

        playSubregionAnthem.setOnAction(e -> {
            try {
                pauseAnthem.setDisable(false);
                workspaceController.playAnthemHandler();
                playSubregionAnthem.setDisable(true);

                pauseAnthem.setOnAction(eh -> {
                    workspaceController.pauseAnthemHandler();
                    pauseAnthem.setDisable(true);
                    playSubregionAnthem.setDisable(false);
                });

            } catch (InvalidMidiDataException ex) {
                Logger.getLogger(Workspace.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MidiUnavailableException ex) {
                Logger.getLogger(Workspace.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Workspace.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        pauseAnthem.setOnAction(eh -> {
            workspaceController.pauseAnthemHandler();
            pauseAnthem.setDisable(true);
            playSubregionAnthem.setDisable(false);
        });

        app.getGUI().getWindow().setOnCloseRequest(e -> workspaceController.pauseAnthemHandler());
        changeMapDimensions.setOnAction(e -> {
            this.reseTheColors();
            workspaceController.changeMapDimensions();
        });

        thicknessSlider.valueProperty().addListener(e -> {
            this.reseTheColors();
            workspaceController.thicknessHandler(thicknessSlider, (DataManager) app.getDataComponent());
        });

        zoomSlider.valueProperty().addListener(e -> {
            this.reseTheColors();
            workspaceController.zoomHandler(zoomSlider, (DataManager) app.getDataComponent());

        });

        itemsTable.setOnMousePressed(e -> {
            this.reseTheColors();

            if (e.getClickCount() == 2) {

                workspaceController.handlerTableSelected(itemsTable);
            } else {
                Subregion subregion = itemsTable.getSelectionModel().getSelectedItem();
                for (int i = 0; i < subregion.getPolygons().size(); i++) {

                    subregion.getPolygons().get(i).setFill(Color.LIGHTBLUE);
                }
            }
        });

        app.getGUI().getPrimaryScene().setOnKeyReleased(e -> {

            left.requestFocus();
            DataManager dataManager = (DataManager) app.getDataComponent();

            if (e.getCode().toString().equals("LEFT")) {
                groupForMap.setTranslateX(translateX + 10);
                translateX += 10;
                dataManager.setTranslateWidth(translateX);
                
            } else if (e.getCode().toString().equals("RIGHT")) {
                groupForMap.setTranslateX(translateX - 10);
                translateX -= 10;
                dataManager.setTranslateWidth(translateX);
            } else if (e.getCode().toString().equals("UP")) {
                groupForMap.setTranslateY(translateY + 10);
                translateY += 10;
                dataManager.setTranslateHeight(translateY);
                
            } else if (e.getCode().toString().equals("DOWN")) {
                groupForMap.setTranslateY(translateY - 10);
                translateY -= 10;
                dataManager.setTranslateHeight(translateY);
            }
        });

        nameColumn = new TableColumn("Name");
        leaderColumn = new TableColumn("Leader");
        capitalColumn = new TableColumn("Capital");

        nameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("name"));
        leaderColumn.setCellValueFactory(new PropertyValueFactory<String, String>("leader"));
        capitalColumn.setCellValueFactory(new PropertyValueFactory<String, String>("capital"));
        DataManager dataManager = (DataManager) app.getDataComponent();

        itemsTable.getColumns().add(nameColumn);
        itemsTable.getColumns().add(leaderColumn);
        itemsTable.getColumns().add(capitalColumn);

        itemsTable.setItems(dataManager.getSubregions());

        itemsTable.getStyleClass().add("cust");

        splitPane.setDividerPositions(0.75);

        left.getStyleClass().add("cust");

        itemsTable.getStyleClass().add("cust");

        splitPane.setMinSize(app.getGUI().getPrimaryScene().getWidth(), app.getGUI().getPrimaryScene().getHeight());

//        left.maxWidthProperty().bind(root.widthProperty());
//        left.minHeightProperty().bind(root.heightProperty());
//        left.minWidthProperty().bind(root.widthProperty());    
//        left.maxHeightProperty().bind(root.heightProperty());
        rectangle = new Rectangle();
        rectangle.setFill(Color.web("#CCFFFF"));

//        rectangle.setWidth(1436);
//        rectangle.setHeight(536);

//        left.getChildren().add(rectangle);
//        Rectangle rectangle2 = new Rectangle();

//        rectangle2.widthProperty().bind(rectangle.widthProperty());
//        rectangle2.heightProperty().bind(rectangle.heightProperty());
//
//        rectangle2.layoutXProperty().bind(rectangle.layoutXProperty());
//        rectangle2.layoutYProperty().bind(rectangle.layoutYProperty());

//        left.setClip(rectangle2);

        splitPane.getItems().addAll(left, itemsTable);

        //splitPane.getItems().addAll(rectangle, itemsTable);
        workspace.getChildren().add(splitPane);

        itemsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    public SplitPane getSplitPane() {

        return splitPane;
    }

    public StackPane getLeft() {

        return left;
    }

    public Group getGroupMap() {

        return groupForMap;
    }

    public Group getGroupImages() {

        return groupForImages;
    }

    public void resetWorkspace() {

        left.getChildren().clear();
    }

    public TableView<Subregion> getTable() {

        return itemsTable;
    }

    class Delta {

        double x, y;
    }

    public Button getRemoveButton() {

        return removeImageOnMap;
    }

    public void reseTheColors() {
        DataManager dataManager = (DataManager) app.getDataComponent();

        for (int i = 0; i < dataManager.getSubregions().size(); i++) {

            Subregion subregion = (Subregion) dataManager.getSubregions().get(i);

            int red = Integer.parseInt(((Subregion) dataManager.getSubregions().get(i)).getRed());
            int blue = Integer.parseInt(((Subregion) dataManager.getSubregions().get(i)).getBlue());
            int green = Integer.parseInt(((Subregion) dataManager.getSubregions().get(i)).getGreen());

            for (int j = 0; j < subregion.getPolygons().size(); j++) {

                ((Polygon) subregion.getPolygons().get(j)).setFill(Color.rgb(red, blue, green));
            }
        }
    }

    public void updataTooltip() {

        for (int i = 0; i < ((DataManager) app.getDataComponent()).getSubregions().size(); i++) {
            Subregion subregion = (Subregion) ((DataManager) app.getDataComponent()).getSubregions().get(i);
            tooltip = new Tooltip(subregion.getName());
            for (int j = 0; j < subregion.getPolygons().size(); j++) {
                Tooltip.install(subregion.getPolygons().get(j), tooltip);
            }
        }
    }

    public Rectangle getRectangle() {

        return rectangle;
    }

}
