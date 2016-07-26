/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapeditor.test_bed;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javafx.scene.paint.Color;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import mapeditor.data.DataManager;
import mapeditor.data.Subregion;
import mapeditor.file.FileManager;

/**
 *
 * @author Yizhi Yang
 */
public class TestSave {

    public static void main(String[] args) throws IOException {

        DataManager dataManager = new DataManager();
        TestSave.makeAndorra(dataManager);

        DataManager sanMarino = new DataManager();
        TestSave.makeSanMarino(sanMarino);
        
        DataManager slovakia = new DataManager();
        TestSave.makeSlovakia(slovakia);
  
        System.out.println("\n\n-------------------------THIS IS OUTPPUTS FROM THE JSON FILE------------------------------\n");

        FileManager fileManager = new FileManager();

        JsonObject json = fileManager.loadJSONFile("./work/Andorra.json");

        String rawData = json.getString("rawData");

     

        System.out.println("Name: " + json.getString("name"));
        System.out.println("RawData: " + json.getString("rawData"));
        System.out.println("Background color: " + json.getString("backgroundColor"));
        System.out.println("Border color: " + json.getString("borderColor"));
        System.out.println("Border thickness: " + json.getString("borderThickness"));
        System.out.println("Zoom Level: " + json.getString("zoomLevel"));
        System.out.println("X of national flag: " + json.getString("XPositionOfNationalFlag") + "  Y of national flag: " + json.getString("YPositionOfNationalFlag"));
        System.out.println("X of national logo: " + json.getString("XPositionOfNationalLogo") + "  Y of national logo: " + json.getString("YPositionOfNationalLogo"));
        System.out.println("Flag: " + json.getString("flagPath"));
        System.out.println("Logo: " + json.getString("logoPath"));
        System.out.println("Scroll location: " + json.getString("scrollPosition"));
        System.out.println("\n\n");
        
        int counter = json.getInt("numberOfRegions");

        JsonArray jsonItemArray = json.getJsonArray("regions");

        for (int i = 0; i < counter; i++) {
            JsonObject jsonItem = jsonItemArray.getJsonObject(i);
            System.out.println(jsonItem.getString("name"));
             System.out.println(jsonItem.getString("leaderName"));
             System.out.println(jsonItem.getString("capital"));
             System.out.println(jsonItem.getString("red"));
             System.out.println(jsonItem.getString("green"));
             System.out.println(jsonItem.getString("blue"));
             System.out.println(jsonItem.getString("leader"));
             System.out.println(jsonItem.getString("flag"));
             System.out.println("\n\n");
        }

    }

    public static void makeAndorra(DataManager dataManager) throws IOException {

        dataManager.setName("Andorra");
        dataManager.setBackground("#00BFFF");
        dataManager.setBorderColor("BLACK");
        dataManager.setBorderThickness("2");
        dataManager.setZoomLevel("1.0");

        dataManager.setRawData("./raw_map_data/Andorra.json");
        dataManager.setFlag("./work/FlagsAndLogos/Andorran_Flag.png");
        dataManager.setLogo("./work/FlagsAndLogos/Andorran_Logo.png");
        dataManager.setScrollLoaction("4");

        dataManager.setPositionOfNationalFlag("0", "0");
        dataManager.setPositionOfNationalLogo("100", "100");

        Subregion laVella = new Subregion("Ordino", "Ventura Espot", "Ordino (town)", "200", "200", "200", "./export/The World/Europe/Andorra/Ventura Espot.png", "./export/The World/Europe/Andorra/Ordino Flag.png");

        Subregion Canillo = new Subregion("Canillo", "Enric Casadevall Medrano", "Canillo (town)", "198", "198", "198", "./export/The World/Europe/Andorra/Enric Casadevall Medrano.png", "./export/The World/Europe/Andorra/Carillo Flag.png");

        Subregion Encamp = new Subregion("Encamp", "Miquel Alis Font", "Encamp (town)", "196", "196", "196", "./export/The World/Europe/Andorra/Miquel Alís Font.png", "./export/The World/Europe/Andorra/Encamp Flag.png");

        Subregion Escaldes_Engordany = new Subregion("Escaldes Engordany", "Montserrat Capdevila Pallares", "Escaldes-Engordany (town)", "194", "194", "194", "./export/The World/Europe/Andorra/Montserrat Capdevila Pallarés.png", "./export/The World/Europe/Andorra/Escaldes-Engordany Flag.png");

        Subregion LaMassana = new Subregion("La Massana", "Josep Areny", "La Massana (town)", "192", "192", "192", "./export/The World/Europe/Andorra/Josep Areny.png", "./export/The World/Europe/Andorra/La Massana Flag.png");

        Subregion Andorra_la_Vella = new Subregion("Andorra la Vella", "Maria Rosa Ferrer Obiols", "Andorra la Vella (city)", "190", "190", "190", "./export/The World/Europe/Andorra/Maria Rosa Ferrer Obiols.png", "./export/The World/Europe/Andorra/Andorra la Vella Flag.png");

        Subregion Sant_Julia_de_Loria = new Subregion("Sant Julia de Loria", "Josep Pintat Forne", "Sant Julia de Loria (town)", "188", "188", "188", "./export/The World/Europe/Andorra/Josep Pintat Forné.png", "./export/The World/Europe/Andorra/Sant Julia de Loria Flag.png");

        dataManager.add(laVella);
        dataManager.add(Canillo);
        dataManager.add(Encamp);
        dataManager.add(Escaldes_Engordany);
        dataManager.add(LaMassana);
        dataManager.add(Andorra_la_Vella);
        dataManager.add(Sant_Julia_de_Loria);

        FileManager fileManager = new FileManager();

        fileManager.saveFakeData("./work/Andorra.json", dataManager);

    }

    public static void makeSanMarino(DataManager dataManager) throws IOException {

        dataManager.setName("San Marino");
        dataManager.setBackground("#00BFFF");
        dataManager.setBorderColor("BLACK");
        dataManager.setBorderThickness("3");
        dataManager.setZoomLevel("2.0");

        dataManager.setRawData("./raw_map_data/San Marino.json");
        dataManager.setFlag("./work/FlagsAndLogos/San Marino_Flag.png");
        dataManager.setLogo("./work/FlagsAndLogos/San Marino_Logo.png");
        dataManager.setScrollLoaction("4");

        dataManager.setPositionOfNationalFlag("5", "5");
        dataManager.setPositionOfNationalLogo("105", "105");

        Subregion Acquaviva = new Subregion("Acquaviva", "Lucia Tamagnini", "", "225", "225", "225", "./export/The World/Europe/San Marino/Lucia Tamagnini.png", "./export/The World/Europe/San Marino/Acquaviva Flag.png");

        Subregion Borgo_Maggiore = new Subregion("Borgo Maggiore", "Sergio Nanni", "", "200", "200", "200", "./export/The World/Europe/San Marino/Sergio Nanni.png", "./export/The World/Europe/San Marino/Borgo Maggiore Flag.png");

        Subregion Chiesanuova = new Subregion("Chiesanuova", "Franco Santi", "", "175", "175", "175", "./export/The World/Europe/San Marino/Franco Santi.png", "./export/The World/Europe/San Marino/Chiesanuova Flag.png");

        Subregion Domagnano = new Subregion("Domagnano", "Gabriel Guidi", "", "150", "150", "150", "./export/The World/Europe/San Marino/Gabriel Guidi.png", "./export/The World/Europe/San Marino/Domagnano Flag.png");

        Subregion Faetano = new Subregion("Faetano", "Pier Mario Bedetti", "", "125", "125", "125", "./export/The World/Europe/San Marino/Pier Mario Bedetti.png", "./export/The World/Europe/San Marino/Faetano Flag.png");

        Subregion Fiorentino = new Subregion("Fiorentino", "Gerri Fabbri", "", "100", "100", "100", "./export/The World/Europe/San Marino/Gerri Fabbri.png", "./export/The World/Europe/San Marino/Fiorentino Flag.png");

        Subregion Montegiardino = new Subregion("Montegiardino", "Marta Fabbri", "", "75", "75", "75", "./export/The World/Europe/San Marino/Marta Fabbri.png", "./export/The World/Europe/San Marino/Montegiardino Flag.png");

        Subregion City_of_San_Marino = new Subregion("City of San Marino", "Maria Teresa Beccari", "", "50", "50", "50", "./export/The World/Europe/San Marino/Maria Teresa Beccari.png", "./export/The World/Europe/San Marino/City of San Marino Flag.png");

        Subregion Serravalle = new Subregion("Serravalle", "Leandro Maiani", "", "25", "25", "25", "./export/The World/Europe/San Marino/Leandro Maiani.png", "./export/The World/Europe/San Marino/Serravalle Flag.png");

        dataManager.add(Acquaviva);
        dataManager.add(Borgo_Maggiore);
        dataManager.add(Chiesanuova);
        dataManager.add(Domagnano);
        dataManager.add(Faetano);
        dataManager.add(Fiorentino);
        dataManager.add(Montegiardino);
        dataManager.add(City_of_San_Marino);
        dataManager.add(Serravalle);

        FileManager fileManager = new FileManager();

        fileManager.saveFakeData("./work/San Marino.json", dataManager);
    }

    public static void makeSlovakia(DataManager dataManager) throws IOException {

        dataManager.setName("Slovakia");
        dataManager.setBackground("#00BFFF");
        dataManager.setBorderColor("BLACK");
        dataManager.setBorderThickness("3");
        dataManager.setZoomLevel("2.0");

        dataManager.setRawData("./raw_map_data/Slovakia.json");
        dataManager.setFlag("./work/FlagsAndLogos/Slovakia_Flag.png");
        dataManager.setLogo("./work/FlagsAndLogos/Slovakia_Logo.png");

        dataManager.setPositionOfNationalFlag("5", "5");
        dataManager.setPositionOfNationalLogo("105", "105");
        dataManager.setScrollLoaction("4");

        Subregion Bratislava = new Subregion("Bratislava", "", "", "250", "250", "250", "", "");

        Subregion Trnava = new Subregion("Trnava", "", "", "249", "249", "249", "", "");

        Subregion Trencin = new Subregion("Trencin", "", "", "248", "248", "249", "", "");

        Subregion Nitra = new Subregion("Nitra", "", "", "247", "247", "247", "", "");

        Subregion Zilina = new Subregion("Zilina", "", "", "246", "246", "246", "", "");

        Subregion Banska_Bystrica = new Subregion("Banska Bystrica", "", "", "245", "245", "245", "", "");

        Subregion Presov = new Subregion("Presov", "", "", "244", "244", "244", "", "");

        Subregion Kosice = new Subregion("Kosice", "", "", "243", "243", "243", "", "");

        dataManager.add(Bratislava);
        dataManager.add(Trnava);
        dataManager.add(Trencin);
        dataManager.add(Nitra);
        dataManager.add(Zilina);
        dataManager.add(Banska_Bystrica);
        dataManager.add(Presov);
        dataManager.add(Kosice);

        FileManager fileManager = new FileManager();

        fileManager.saveFakeData("./work/Slovakia.json", dataManager);
    }
}
