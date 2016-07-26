/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapeditor.test_bed;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import mapeditor.data.DataManager;
import mapeditor.data.Subregion;

/**
 *
 * @author Yizhi Yang
 */
public class TestLoad {

    public static void main(String[] args) throws IOException {

        DataManager dataManager = new DataManager();

        TestSave.makeAndorra(dataManager);
        
        DataManager dataManagerWithLoadData = new DataManager();
        
        
        TestLoad.loadData(dataManagerWithLoadData, "./work/Andorra.json");

        System.out.println("\n\n---------------------------------VERIFIED OUTPUT-------------------------------------------------");
        
        System.out.println("Name: " + dataManagerWithLoadData.getName());
        System.out.println("RawData: " + dataManagerWithLoadData.getRawDataPath());
        System.out.println("Background color: " + dataManagerWithLoadData.getBackgroundColor());
        System.out.println("Border color: " + dataManagerWithLoadData.getBorderColor());
        System.out.println("Border thickness: " + dataManagerWithLoadData.getBorderThickness());
        System.out.println("Zoom Level: " + dataManagerWithLoadData.getZoomLevel());
        System.out.println("X of national flag: " + dataManagerWithLoadData.getXPositionOfNationalFlag());
        System.out.println("Y of national flag: " + dataManagerWithLoadData.getYPositionOfNationalFlag());
        System.out.println("X of national logo: " + dataManagerWithLoadData.getXPositionOfNationalLogo());
        System.out.println("Y of national logo: " + dataManagerWithLoadData.getYPositionOfNationalLogo());
        System.out.println("Scroll location: " + dataManagerWithLoadData.getScrollLocation());
        System.out.println("Flag: " + dataManagerWithLoadData.getFlagPath());
        System.out.println("Logo: " + dataManagerWithLoadData.getLogoPath());
        System.out.println("\n\n");
        
        for (int i = 0; i < dataManager.getSubregions().size(); i++) {

            System.out.println(((Subregion) dataManagerWithLoadData.getSubregions().get(i)).getName());
            System.out.println(((Subregion) dataManagerWithLoadData.getSubregions().get(i)).getLeader());
            System.out.println(((Subregion) dataManagerWithLoadData.getSubregions().get(i)).getCapital());
            System.out.println(((Subregion) dataManagerWithLoadData.getSubregions().get(i)).getRed());
            System.out.println(((Subregion) dataManagerWithLoadData.getSubregions().get(i)).getGreen());
            System.out.println(((Subregion) dataManagerWithLoadData.getSubregions().get(i)).getBlue());
            System.out.println(((Subregion) dataManagerWithLoadData.getSubregions().get(i)).getLeaderImagePath());
            System.out.println(((Subregion) dataManagerWithLoadData.getSubregions().get(i)).getFlagImagePath());
            System.out.println(" ");
            System.out.println(" ");
        }
    }

    public static void loadData(DataManager dataManager, String filePath) throws IOException {

        JsonObject json = loadJSONFile(filePath);
        
        dataManager.setName(json.getString("name"));
        String rawData = json.getString("rawData");

        dataManager.setRawData(json.getString("rawData"));
        dataManager.setBackground(json.getString("backgroundColor"));
        dataManager.setBorderColor(json.getString("borderColor"));
        dataManager.setBorderThickness(json.getString("borderThickness"));
        dataManager.setZoomLevel(json.getString("zoomLevel"));
        dataManager.setPositionOfNationalFlag(json.getString("XPositionOfNationalFlag"), json.getString("YPositionOfNationalFlag"));
        dataManager.setPositionOfNationalLogo(json.getString("XPositionOfNationalLogo"), json.getString("YPositionOfNationalLogo"));
        dataManager.setScrollLoaction(json.getString("scrollPosition"));
        dataManager.setFlag(json.getString("flagPath"));
        dataManager.setLogo(json.getString("logoPath"));
        
        int counter = json.getInt("numberOfRegions");

        JsonArray jsonItemArray = json.getJsonArray("regions");
        
        for (int i = 0; i < counter; i++) {
            JsonObject jsonItem = jsonItemArray.getJsonObject(i);
            Subregion item = loadItem(jsonItem);
            dataManager.add(item);
        }


    }

    private static JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        InputStream is = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }

    private static Subregion loadItem(JsonObject jsonItem) {

        String name = jsonItem.getString("name");
        String leaderName = jsonItem.getString("leaderName");
        String capital = jsonItem.getString("capital");
        String red = jsonItem.getString("red");
        String green = jsonItem.getString("green");
        String blue = jsonItem.getString("blue");
        String leader = jsonItem.getString("leader");
        String flag = jsonItem.getString("flag");

        Subregion subregion = new Subregion(name, leaderName, capital, red, green, blue, leader, flag);

        return subregion;
    }
}
