/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapeditor.data;

import java.io.File;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.shape.Polygon;

/**
 *
 * @author Yizhi Yang
 */
public class Subregion {

    ArrayList<Polygon> polygons;

    private final SimpleStringProperty name;
    private final SimpleStringProperty leader;
    private final SimpleStringProperty capital;

    private int numberOfPolygons;
    private String red;
    private String green;
    private String blue;

    File leaderImage;
    File flagImage;

    public Subregion(String name, String leader, String capital, String red, String green, String blue, String leaderImage, String flagImage) {

        this.name = new SimpleStringProperty(name);
        this.leader = new SimpleStringProperty(leader);
        this.capital = new SimpleStringProperty(capital);
        this.red = red;
        this.green = green;
        this.blue = blue;

        this.leaderImage = new File(leaderImage);
        this.flagImage = new File(flagImage);

        polygons = new ArrayList<Polygon>();
    }

    public Subregion() {
        
        this.name = new SimpleStringProperty("");
        this.leader = new SimpleStringProperty("");
        this.capital = new SimpleStringProperty("");

        this.red = "";
        this.green = "";
        this.blue = "";

        this.leaderImage = new File("");
        this.flagImage = new File("");

        polygons = new ArrayList<Polygon>();
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getLeader() {
        return leader.get();
    }

    public void setLeader(String leader) {
        this.leader.set(leader);
    }

    public StringProperty leaderProperty() {
        return leader;
    }

    public String getCapital() {
        return capital.get();
    }

    public void setCapital(String capital) {
        this.capital.set(capital);
    }

    public StringProperty capitalProperty() {
        return capital;
    }

    public void setColor(String red, String green, String blue) {

        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public void setFlagFile(String path) {

        flagImage = new File(path);
    }

    public void setLeaderFile(String path) {

        leaderImage = new File(path);
    }

    public String getRed() {

        return red;
    }

    public String getGreen() {

        return green;
    }

    public String getBlue() {

        return blue;
    }

    public String getLeaderImagePath() {

        return leaderImage.getPath();
    }

    public String getFlagImagePath() {

        return flagImage.getPath();
    }

    public void setNumberOfPolygons(int value) {

        numberOfPolygons = value;
    }

    public int getNumberOfPolygons() {

        return numberOfPolygons;
    }

    public void addSubregionPolygonList(ArrayList<Polygon> polygonList) {

        polygons = polygonList;
    }

    public ArrayList<Polygon> getPolygons() {

        return polygons;
    }
}
