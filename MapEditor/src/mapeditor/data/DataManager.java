package mapeditor.data;

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.ImageView;
//import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javax.json.JsonValue;
import mapeditor.MapEditorApp;
import mapeditor.gui.Workspace;
import saf.components.AppDataComponent;

/**
 *
 * @author Yizhi Yang
 */
public class DataManager implements AppDataComponent {

    MapEditorApp app;

    ObservableList<Subregion> subregions;

    ArrayList<Integer> numberOfPolygons;

    String name;

    String zoomLevel;

    String backgroundColor;

    String borderColor;

    String borderThickness;

    File rawData;
    File flag;
    File logo;

    ArrayList<File> images;
    ArrayList<Double> positionsForXandY;
    ArrayList<ImageView> imageViewsList;

    ConcurrentHashMap<String, Point> imagesHash;

    String XPositionOfNationalFlag;
    String YPositionOfNationalFlag;

    String XPositionOfNationalLogo;
    String YPositionOfNationalLogo;

    String scrollLocation;
    
    String parentDir;
    
    int translateWidth;
    int translateHeight;

    public DataManager() {

        this.imagesHash = new ConcurrentHashMap<String, Point>();
        subregions = FXCollections.observableArrayList();
        numberOfPolygons = new ArrayList();
        //polygons = new ArrayList<Polygon>();

    }

    public DataManager(MapEditorApp initApp) {
        
        this.imagesHash = new ConcurrentHashMap<String, Point>();

        app = initApp;

        subregions = FXCollections.observableArrayList();
        numberOfPolygons = new ArrayList();
        //polygons = new ArrayList<Polygon>();
        images = new ArrayList<File>();
        imageViewsList = new ArrayList<ImageView>();
        positionsForXandY = new ArrayList<Double>();
        parentDir = "";
    }

    @Override
    public void reset() {

        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        this.XPositionOfNationalFlag = "";
        this.XPositionOfNationalLogo = "";
        this.YPositionOfNationalFlag = "";
        this.YPositionOfNationalLogo = "";

        this.backgroundColor = "";
        this.borderColor = "";
        this.borderThickness = "";
        this.flag = null;
        this.logo = null;
        this.name = "";

        for (int i = 0; i < subregions.size(); i++) {
            ((Subregion) subregions.get(i)).getPolygons().clear();
        }

        this.rawData = null;
        this.scrollLocation = "";
        this.subregions.clear();
        this.zoomLevel = "";

        workspace.getGroupMap().getChildren().clear();

    }

    public ObservableList getSubregions() {

        return subregions;
    }

    public ArrayList getNumberofPolygons() {
        return numberOfPolygons;
    }

    public void addToNumberOfP(Integer number) {
        numberOfPolygons.add(number);
    }

    public void add(Subregion subregion) {

        subregions.add(subregion);
    }

    public double convertX(double x) {

        Workspace pane = (Workspace) app.getWorkspaceComponent();

        Double width = ((Pane) ((Workspace) app.getWorkspaceComponent()).getSplitPane().getItems().get(0)).getWidth();

        double relativeX = (((width) / 360.0) * (180 + x));

        return relativeX;
    }

    public double convertY(double y) {

        Workspace pane = (Workspace) app.getWorkspaceComponent();

        FlowPane flow = (FlowPane) app.getGUI().getAppPane().getTop();

        double relativeY = (((app.getGUI().getPrimaryScene().getHeight() - flow.getHeight()) / 180) * (90 - y));

        return relativeY;
    }

    public void setZoomLevel(String zoomLevel) {

        this.zoomLevel = zoomLevel;
    }

    public void setBackground(String backgroundColor) {

        this.backgroundColor = backgroundColor;
    }

    public void setBorderColor(String borderColor) {

        this.borderColor = borderColor;
    }

    public void setBorderThickness(String thickness) {

        borderThickness = thickness;
    }

    public void setFlag(String path) {

        flag = new File(path);

    }

    public void setLogo(String path) {

        logo = new File(path);
    }

    public void setPositionOfNationalFlag(String X, String Y) {

        XPositionOfNationalFlag = X;
        YPositionOfNationalFlag = Y;
    }

    public void setPositionOfNationalLogo(String X, String Y) {

        XPositionOfNationalLogo = X;
        YPositionOfNationalLogo = Y;
    }

    public void setRawData(String path) {

        rawData = new File(path);
    }

    public String getBackgroundColor() {

        return backgroundColor;
    }

    public String getBorderColor() {

        return borderColor;
    }

    public String getBorderThickness() {

        return borderThickness;
    }

    public String getZoomLevel() {

        return zoomLevel;
    }

    public String getRawDataPath() {

        return rawData.getPath();
    }

    public String getFlagPath() {

        return flag.getPath();
    }

    public String getLogoPath() {

        return logo.getPath();
    }

    public String getXPositionOfNationalFlag() {

        return XPositionOfNationalFlag;
    }

    public String getYPositionOfNationalFlag() {

        return YPositionOfNationalFlag;
    }

    public String getXPositionOfNationalLogo() {

        return XPositionOfNationalLogo;
    }

    public String getYPositionOfNationalLogo() {

        return YPositionOfNationalLogo;
    }

    public void reloadWorkspace() {

        ((Workspace) app.getWorkspaceComponent()).resetWorkspace();
        app.getWorkspaceComponent().reloadWorkspace();
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScrollLoaction(String value) {

        scrollLocation = value;
    }

    public String getScrollLocation() {

        return scrollLocation;
    }

    public void addImage(File file) {

        images.add(file);
    }

    public ArrayList<File> getImagesList() {

        return images;
    }

    public ArrayList<Double> getPositionsForXandY() {

        return positionsForXandY;
    }

    public void addPositionsForXandY(double X, double Y) {

        positionsForXandY.add(X);
        positionsForXandY.add(Y);
    }

    public ConcurrentHashMap<String, Point> getImagesHash() {

        return imagesHash;
    }
    
    public void setParentDir(String dir){
        
        parentDir = dir;
    }
    
    public String getParentDir(){
        
        return parentDir;
    }
    
    public StackPane getStackPane(){
        
        return ((Workspace)app.getWorkspaceComponent()).getLeft();
    }
    
    public ArrayList<ImageView> getImageViewsList(){
        
        return imageViewsList;
    }
    
    public void addToImageViewsList(ImageView imageView){
        
        imageViewsList.add(imageView);
    }
    
    public int getTranslateWidth(){
        
        return translateWidth;
    }
    
    public int getTranslateLateHeight(){
        
        return translateHeight;
    }
    
    public void setTranslateWidth(int width){
        
        this.translateWidth = width;
    }
    
    public void setTranslateHeight(int height){
        
        this.translateHeight = height;
    }
}
