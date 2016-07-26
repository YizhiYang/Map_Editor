package mapeditor.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import mapeditor.data.DataManager;
import mapeditor.data.Subregion;
import mapeditor.gui.Workspace;
import saf.components.AppDataComponent;
import saf.components.AppFileComponent;
import static saf.settings.AppPropertyType.SAVE_UNSAVED_WORK_MESSAGE;
import static saf.settings.AppPropertyType.SAVE_UNSAVED_WORK_TITLE;
import saf.ui.AppYesNoCancelDialogSingleton;

/**
 *
 * @author Yizhi Yang
 */
public class FileManager implements AppFileComponent {

    int orig;
    double origBar;

    String actualDir;
    String name;

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {

        ((DataManager) data).reset();

        loadInfoData((DataManager) data, filePath);
        //loadAndRenderGeographicData(data, filePath);
    }

    public void loadFakeData(DataManager dataManager, String filePath) throws IOException {

        JsonObject json = loadJSONFile(filePath);
    }

    public double getDataAsDouble(JsonObject json, String dataName) {
        JsonValue value = json.get(dataName);
        JsonNumber number = (JsonNumber) value;
        return number.bigDecimalValue().doubleValue();
    }

    public int getDataAsInt(JsonObject json, String dataName) {
        JsonValue value = json.get(dataName);
        JsonNumber number = (JsonNumber) value;
        return number.bigIntegerValue().intValue();
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    public JsonObject loadJSONFile(String jsonFilePath) throws IOException {

        BufferedReader is = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFilePath), "UTF8"));

        //InputStream is = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {

        DataManager dataManager = (DataManager) data;
        File file = new File(filePath);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file);

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        ObservableList<Subregion> items = dataManager.getSubregions();

        for (Subregion item : items) {
            JsonObject itemJson = Json.createObjectBuilder()
                    .add("name", item.getName())
                    .add("leaderName", item.getLeader())
                    .add("capital", item.getCapital())
                    .add("red", item.getRed())
                    .add("green", item.getGreen())
                    .add("blue", item.getBlue())
                    .add("leader", item.getLeaderImagePath())
                    .add("flag", item.getFlagImagePath()).build();
            arrayBuilder.add(itemJson);
        }
        JsonArray itemsArray = arrayBuilder.build();

        //-------------------------------------------------
        JsonArrayBuilder arrayBuilderForKeys = Json.createArrayBuilder();
        ArrayList<File> images = dataManager.getImagesList();

        for (File fileOfImage : images) {

            JsonObject itemJson = Json.createObjectBuilder()
                    .add("name", fileOfImage.getPath()).build();
            arrayBuilderForKeys.add(itemJson);
        }
        JsonArray fileArray = arrayBuilderForKeys.build();

        //-------------------------------------------------
        JsonArrayBuilder arrayBuilderForValues = Json.createArrayBuilder();
        ConcurrentHashMap<String, Point> values = dataManager.getImagesHash();

        for (int i = 0; i < values.size(); i++) {

            Point point = values.get(images.get(i).getPath());

            JsonObject itemJson = Json.createObjectBuilder()
                    .add("X", point.getX())
                    .add("Y", point.getY()).build();
            arrayBuilderForValues.add(itemJson);
        }
        JsonArray pointsArray = arrayBuilderForValues.build();

        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add("name", dataManager.getName())
                .add("rawData", dataManager.getRawDataPath())
                .add("backgroundColor", dataManager.getBackgroundColor())
                .add("borderColor", dataManager.getBorderColor())
                .add("borderThickness", dataManager.getBorderThickness())
                .add("zoomLevel", dataManager.getZoomLevel())
                .add("XPositionOfNationalFlag", dataManager.getXPositionOfNationalFlag())
                .add("YPositionOfNationalFlag", dataManager.getYPositionOfNationalFlag())
                .add("XPositionOfNationalLogo", dataManager.getXPositionOfNationalLogo())
                .add("YPositionOfNationalLogo", dataManager.getYPositionOfNationalLogo())
                .add("scrollPosition", dataManager.getScrollLocation())
                .add("flagPath", dataManager.getFlagPath())
                .add("logoPath", dataManager.getLogoPath())
                .add("numberOfRegions", itemsArray.size())
                .add("regions", itemsArray)
                .add("files", fileArray)
                .add("points", pointsArray)
                .add("parentDir", dataManager.getParentDir())
                .build();

        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties = new HashMap<>(1);

        properties.put(JsonGenerator.PRETTY_PRINTING,
                true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);

        jsonWriter.writeObject(dataManagerJSO);

        jsonWriter.close();

        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);

        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(filePath);

        pw.write(prettyPrinted);

        pw.close();
    }

    public void saveFakeData(String filePath, DataManager dataManager) throws IOException {

        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file);

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        ObservableList<Subregion> items = dataManager.getSubregions();

        for (Subregion item : items) {

            JsonObject itemJson = Json.createObjectBuilder()
                    .add("name", item.getName())
                    .add("leaderName", item.getLeader())
                    .add("capital", item.getCapital())
                    .add("red", item.getRed())
                    .add("green", item.getGreen())
                    .add("blue", item.getBlue())
                    .add("leader", item.getLeaderImagePath())
                    .add("flag", item.getFlagImagePath()).build();
            arrayBuilder.add(itemJson);
        }
        JsonArray itemsArray = arrayBuilder.build();

        // THEN PUT IT ALL TOGETHER IN A JsonObject
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add("name", dataManager.getName())
                .add("rawData", dataManager.getRawDataPath())
                .add("backgroundColor", dataManager.getBackgroundColor())
                .add("borderColor", dataManager.getBorderColor())
                .add("borderThickness", dataManager.getBorderThickness())
                .add("zoomLevel", dataManager.getZoomLevel())
                .add("XPositionOfNationalFlag", dataManager.getXPositionOfNationalFlag())
                .add("YPositionOfNationalFlag", dataManager.getYPositionOfNationalFlag())
                .add("XPositionOfNationalLogo", dataManager.getXPositionOfNationalLogo())
                .add("YPositionOfNationalLogo", dataManager.getYPositionOfNationalLogo())
                .add("scrollPosition", dataManager.getScrollLocation())
                .add("flagPath", dataManager.getFlagPath())
                .add("logoPath", dataManager.getLogoPath())
                .add("numberOfRegions", itemsArray.size())
                .add("regions", itemsArray)
                .build();

        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();

        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(filePath);
        pw.write(prettyPrinted);
        pw.close();
    }

    @Override
    public void exportData(AppDataComponent dataManager) throws IOException {

        DataManager data2 = ((DataManager) dataManager);

        File file2 = new File(data2.getParentDir() + "/" + data2.getName() + ".rvm");

        file2.createNewFile();

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        ObservableList<Subregion> items = ((DataManager) dataManager).getSubregions();

        Boolean capitals = true;
        Boolean flags = true;
        Boolean leaders = true;

        for (int i = 0; i < ((DataManager) dataManager).getSubregions().size(); i++) {

            if (((Subregion) ((DataManager) dataManager).getSubregions().get(i)).getCapital().isEmpty()) {
                capitals = false;

            }
        }

        for (int i = 0; i < ((DataManager) dataManager).getSubregions().size(); i++) {

            //File file = new File(((Subregion) ((DataManager) dataManager).getSubregions().get(i)).getFlagImagePath());
            Subregion subregion = (Subregion) ((DataManager) dataManager).getSubregions().get(i);

            String path = ((DataManager) dataManager).getParentDir();

            File file = new File(path + "\\" + subregion.getName() + " Flag.png");

            if (!file.exists()) {

                flags = false;
            }

        }

        for (int i = 0; i < ((DataManager) dataManager).getSubregions().size(); i++) {

            Subregion subregion = (Subregion) ((DataManager) dataManager).getSubregions().get(i);

            String path = ((DataManager) dataManager).getParentDir();

            File file = new File(path + "\\" + subregion.getLeader() + ".png");

            if (!file.exists()) {

                leaders = false;
            }
        }

        for (Subregion item : items) {

            if (capitals == false && leaders == false) {
                JsonObject itemJson = Json.createObjectBuilder()
                        .add("name", item.getName())
                        .add("red", new Integer(item.getRed()))
                        .add("green", new Integer(item.getGreen()))
                        .add("blue", new Integer(item.getBlue())).build();
                arrayBuilder.add(itemJson);
            } else if (capitals == false && leaders == true) {
                JsonObject itemJson = Json.createObjectBuilder()
                        .add("name", item.getName())
                        .add("leader", item.getLeader())
                        .add("red", new Integer(item.getRed()))
                        .add("green", new Integer(item.getGreen()))
                        .add("blue", new Integer(item.getBlue())).build();
                arrayBuilder.add(itemJson);
            } else if (capitals == true && leaders == false) {
                JsonObject itemJson = Json.createObjectBuilder()
                        .add("name", item.getName())
                        .add("capital", item.getCapital())
                        .add("red", new Integer(item.getRed()))
                        .add("green", new Integer(item.getGreen()))
                        .add("blue", new Integer(item.getBlue())).build();
                arrayBuilder.add(itemJson);

            } else if (capitals == true && leaders == true) {

                JsonObject itemJson = Json.createObjectBuilder()
                        .add("name", item.getName())
                        .add("capital", item.getCapital())
                        .add("leader", item.getLeader())
                        .add("red", new Integer(item.getRed()))
                        .add("green", new Integer(item.getGreen()))
                        .add("blue", new Integer(item.getBlue())).build();
                arrayBuilder.add(itemJson);
            }
        }
        JsonArray itemsArray = arrayBuilder.build();

        // THEN PUT IT ALL TOGETHER IN A JsonObject
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add("name", ((DataManager) dataManager).getName())
                .add("subregions_have_capitals", capitals)
                .add("subregions_have_flags", flags)
                .add("subregions_have_leaders", leaders)
                .add("subregions", itemsArray)
                .build();

        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();

        OutputStream os = new FileOutputStream(file2.getPath());

        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(file2.getPath());
        pw.write(prettyPrinted);

        WritableImage snapshot = ((DataManager) dataManager).getStackPane().snapshot(new SnapshotParameters(), null);

        DataManager data = ((DataManager) dataManager);
        //File file = new File((DataManager) dataManager).getParentDir() + "\\" + ((DataManager) dataManager).getName() + ".png"));

        File file = new File(data.getParentDir() + "\\" + data.getName() + ".png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
            System.out.println("snapshot saved: " + file.getAbsolutePath());
        } catch (IOException ex) {

        }

        AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
        yesNoDialog.show("Export...", "Map exported successfully.");
        pw.close();

    }

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void loadAndRenderGeographicData(AppDataComponent data, String filePath) throws IOException {

        orig = 0;

        DataManager dataManager = ((DataManager) data);

        ProgressBar progress = new ProgressBar();
        progress.setPrefWidth(300.0d);
        Label updateLabel = new Label("Running: ");
        updateLabel.setPrefWidth(300.0d);

        VBox updatePane = new VBox();
        updatePane.setPadding(new Insets(10));
        updatePane.setSpacing(5.0d);
        updatePane.getChildren().addAll(updateLabel, progress);

        Stage taskUpdateStage = new Stage(StageStyle.UTILITY);
        taskUpdateStage.setScene(new Scene(updatePane));
        taskUpdateStage.show();

        Task loading;
        loading = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

//                DataManager dataManager = (DataManager) data;
//                dataManager.reset();
                System.out.println(filePath);
                JsonObject json = FileManager.this.loadJSONFile(filePath);

                int numberOfSubregions = Integer.parseInt(json.get("NUMBER_OF_SUBREGIONS").toString());

                int numberOfPolygons;

                JsonArray arrayOfSubregions = json.getJsonArray("SUBREGIONS");

                double interval = (1.0 / arrayOfSubregions.size()) * 100;

                origBar = interval;

                for (int i = 1; i <= arrayOfSubregions.size(); i++) {

                    JsonObject outterObject = arrayOfSubregions.getJsonObject(i - 1);

                    numberOfPolygons = Integer.parseInt(outterObject.get("NUMBER_OF_SUBREGION_POLYGONS").toString());

                    for (int j = 0; j < 1; j++) {

                        JsonArray middleArray = outterObject.getJsonArray("SUBREGION_POLYGONS");
                        int numberOfP = 0;

                        ArrayList<Polygon> polygonList = new ArrayList<Polygon>();        // new 

                        for (int k = 0; k < middleArray.size(); k++) {

                            Polygon polygon = new Polygon();

                            JsonArray innerArray = middleArray.getJsonArray(k);

                            for (int z = 0; z < innerArray.size(); z++) {

                                double x = FileManager.this.getDataAsDouble(innerArray.getJsonObject(z), "X");
                                double y = FileManager.this.getDataAsDouble(innerArray.getJsonObject(z), "Y");

                                double relativeX = ((DataManager) data).convertX(x);

                                double relativeY = ((DataManager) data).convertY(y);

                                polygon.getPoints().addAll(relativeX, relativeY);

                            }
                            polygonList.add(polygon);                                           // new 
                            numberOfP++;
                            DataManager dataManager = ((DataManager) data);

                            //polygon.setFill((Color.web((DataManager)data).getBackgroundColor()));
                            //polygon.setFill(Color.web(((DataManager)data).getBackgroundColor()));
                            polygon.setStroke(Color.web(((DataManager) data).getBorderColor()));
                            polygon.setStrokeWidth(Double.parseDouble(((DataManager) data).getBorderThickness()));
                            //((DataManager) data).addPolygonToList(polygon);

                        }
                        ((Subregion) ((DataManager) data).getSubregions().get(i - 1)).addSubregionPolygonList(polygonList);      // new 
                        ((DataManager) data).addToNumberOfP(new Integer(numberOfP));                // new 
                    }

                    updateProgress(i, arrayOfSubregions.size());
                    updateMessage("Loading the map: " + String.valueOf((int) origBar) + "%");

                    origBar += interval;

                    orig += 1;

                    Thread.sleep(100);
                }
                ArrayList<Integer> numberOfPoly = dataManager.getNumberofPolygons();

                for (int i = 0; i < ((DataManager) data).getSubregions().size(); i++) {

                    Subregion subregion = (Subregion) ((DataManager) data).getSubregions().get(i);

                    int red = Integer.parseInt(((Subregion) dataManager.getSubregions().get(i)).getRed());
                    int blue = Integer.parseInt(((Subregion) dataManager.getSubregions().get(i)).getBlue());
                    int green = Integer.parseInt(((Subregion) dataManager.getSubregions().get(i)).getGreen());

                    for (int j = 0; j < subregion.getPolygons().size(); j++) {

                        ((Polygon) subregion.getPolygons().get(j)).setFill(Color.rgb(red, blue, green));
                    }
                }

//                int i=0;
//                int k=0;
//                while(i < dataManager.getSubregions().size()) {
//
//                    int red = Integer.parseInt(((Subregion)dataManager.getSubregions().get(i)).getRed());
//                    int blue = Integer.parseInt(((Subregion)dataManager.getSubregions().get(i)).getBlue());
//                    int green = Integer.parseInt(((Subregion)dataManager.getSubregions().get(i)).getGreen());
//                    for(int j = 0; j<numberOfPoly.get(i); j++){
//                        dataManager.getPolygons().get(k).setFill(Color.rgb(red, blue, green));
//                        k++;
//                    }
//                    i++;
//                    
//                }
                return null;
            }
        };

        loading.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                taskUpdateStage.hide();
                ((DataManager) data).reloadWorkspace();

            }
        });
        progress.progressProperty().bind(loading.progressProperty());
        updateLabel.textProperty().bind(loading.messageProperty());

        taskUpdateStage.show();
        Thread thread = new Thread(loading);
        thread.start();
    }

    public void loadInfoData(DataManager dataManager, String filePath) throws IOException {

        JsonObject json = loadJSONFile(filePath);

        String rawData = json.getString("rawData");

//        loadAndRenderGeographicData(dataManager, rawData);
        dataManager.setName(json.getString("name"));
        dataManager.setRawData(json.getString("rawData"));
        dataManager.setBackground(json.getString("backgroundColor"));
        dataManager.setBorderColor(json.getString("borderColor"));
        dataManager.setBorderThickness(json.getString("borderThickness"));
        dataManager.setZoomLevel(json.getString("zoomLevel"));
        dataManager.setPositionOfNationalFlag(json.getString("XPositionOfNationalFlag"), json.getString("YPositionOfNationalFlag"));
        dataManager.setPositionOfNationalLogo(json.getString("XPositionOfNationalLogo"), json.getString("YPositionOfNationalLogo"));
        dataManager.setFlag(json.getString("flagPath"));
        dataManager.setLogo(json.getString("logoPath"));
        dataManager.setScrollLoaction(json.getString("scrollPosition"));
        dataManager.setParentDir(json.getString("parentDir"));

        int counter = json.getInt("numberOfRegions");

        JsonArray jsonItemArray = json.getJsonArray("regions");

        for (int i = 0; i < counter; i++) {
            JsonObject jsonItem = jsonItemArray.getJsonObject(i);
            Subregion item = loadItem(jsonItem);
            dataManager.add(item);
        }

        JsonArray fileArray = json.getJsonArray("files");

        dataManager.getImagesList().clear();                                                         // this is new to add. 
        for (int i = 0; i < fileArray.size(); i++) {

            JsonObject jsonItem = fileArray.getJsonObject(i);
            String name = jsonItem.getString("name");

            dataManager.getImagesList().add(new File(name));
        }

        JsonArray pointsArray = json.getJsonArray("points");

        for (int i = 0; i < pointsArray.size(); i++) {

            JsonObject jsonItem = pointsArray.getJsonObject(i);
            int X = jsonItem.getInt("X");
            int Y = jsonItem.getInt("Y");

            Point point = new Point();
            point.setX(X);
            point.setY(Y);

            dataManager.getImagesHash().put(dataManager.getImagesList().get(i).getPath(), point);
        }

        loadAndRenderGeographicData(dataManager, rawData);
    }

    public Subregion loadItem(JsonObject jsonItem) {

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

    // 212 253 254
    public void newMap(String name, AppDataComponent data, String geoPath, String parentPath) throws IOException {

        this.name = name;
        this.actualDir = parentPath + "\\" + name;

        DataManager dataManager = ((DataManager) data);

        dataManager.setParentDir(actualDir);
        dataManager.setRawData(geoPath);
        dataManager.setBackground("#CCFFFF");
        dataManager.setBorderColor("#000000");
        dataManager.setBorderThickness("0.1");
        dataManager.setName(name);
        dataManager.setFlag("");
        dataManager.setLogo("");
        dataManager.setPositionOfNationalFlag("", "");
        dataManager.setPositionOfNationalLogo("", "");
        dataManager.setScrollLoaction("");
        dataManager.setZoomLevel("20");

        JsonObject json = FileManager.this.loadJSONFile(geoPath);

        JsonArray arrayOfSubregions = json.getJsonArray("SUBREGIONS");

        int numberOfPolygons;

        for (int i = 1; i <= arrayOfSubregions.size(); i++) {                    // get the number of subregions

            Subregion subregion = new Subregion();

            subregion.setName("Subregion" + i);
            subregion.setColor("120", "120", "120");

            dataManager.add(subregion);

            JsonObject singleSubregion = arrayOfSubregions.getJsonObject(i - 1);

            numberOfPolygons = Integer.parseInt(singleSubregion.get("NUMBER_OF_SUBREGION_POLYGONS").toString());

            for (int j = 0; j < 1; j++) {

                JsonArray singlePolygon = singleSubregion.getJsonArray("SUBREGION_POLYGONS");
                int numberOfP = 0;

                ArrayList<Polygon> polygonList = new ArrayList<Polygon>();        // new 

                for (int k = 0; k < singlePolygon.size(); k++) {

                    Polygon polygon = new Polygon();

                    JsonArray singlePoint = singlePolygon.getJsonArray(k);

                    for (int z = 0; z < singlePoint.size(); z++) {

                        double x = FileManager.this.getDataAsDouble(singlePoint.getJsonObject(z), "X");
                        double y = FileManager.this.getDataAsDouble(singlePoint.getJsonObject(z), "Y");

                        double relativeX = ((DataManager) data).convertX(x);

                        double relativeY = ((DataManager) data).convertY(y);

                        polygon.getPoints().addAll(relativeX, relativeY);

                    }
                    polygonList.add(polygon);                                           // new 
                    numberOfP++;

                    polygon.setFill(Color.LIGHTGREEN);
                    polygon.setStroke(Color.BLACK);
                    polygon.setStrokeWidth(0.1);
                    //((DataManager) data).addPolygonToList(polygon);
                }
                ((Subregion) ((DataManager) data).getSubregions().get(i - 1)).addSubregionPolygonList(polygonList);
                ((DataManager) data).addToNumberOfP(new Integer(numberOfP));
            }

            for (int p = 0; p < ((DataManager) data).getSubregions().size(); p++) {

                Subregion subregionForColor = (Subregion) ((DataManager) data).getSubregions().get(p);

                int red = Integer.parseInt(((Subregion) dataManager.getSubregions().get(p)).getRed());
                int blue = Integer.parseInt(((Subregion) dataManager.getSubregions().get(p)).getBlue());
                int green = Integer.parseInt(((Subregion) dataManager.getSubregions().get(p)).getGreen());

                for (int j = 0; j < subregionForColor.getPolygons().size(); j++) {

                    ((Polygon) subregionForColor.getPolygons().get(j)).setFill(Color.rgb(red, blue, green));
                }
            }
        }
    }

    public String getName() {

        return name;
    }

    public String getActualDir() {

        return actualDir;
    }
}
