package regiovincodatatester;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.stream.JsonParsingException;

/**
 *
 */
public class RegioVincoDataTester extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    File exportDirectory = new File("./export/The World/Europe/");
    BorderPane appPane = new BorderPane();
    FlowPane toolbar = new FlowPane();
    Button testButton = new Button("Test Exported File");
    TextArea resultsTextArea = new TextArea();
    ScrollPane scrollPane = new ScrollPane(resultsTextArea);
    File regionDirectory;
    ArrayList<String> subregionNames = new ArrayList();
    ArrayList<String> subregionCapitals = new ArrayList();
    ArrayList<String> subregionLeaders = new ArrayList();

    @Override
    public void start(Stage primaryStage) {
        testButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(exportDirectory);
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                try {
                    loadData(selectedFile);
                } catch (JsonParsingException | IOException exception) {
                    Alert alertBox = new Alert(AlertType.ERROR);
                    alertBox.setTitle("Error in Format of Exported File");
                    alertBox.setHeaderText("Format Error");
                    alertBox.setContentText("The Exported Regio Vinco Map File has a formatting error inside");
                    alertBox.showAndWait();
                }
            }
        });
        toolbar.getChildren().add(testButton);
        appPane.setTop(toolbar);
        appPane.setCenter(scrollPane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(appPane, 800, 500);
        primaryStage.setTitle("Regio Vinco Data Tester");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    static String JSON_SUBREGIONS_HAVE_CAPITALS = "subregions_have_capitals";
    static String JSON_SUBREGIONS_HAVE_FLAGS = "subregions_have_flags";
    static String JSON_SUBREGIONS_HAVE_LEADERS = "subregions_have_leaders";
    static String JSON_SUBREGIONS_COUNT = "subregions_count";
    static String JSON_SUBREGIONS = "subregions";
    static String JSON_NAME = "name";
    static String JSON_CAPITAL = "capital";
    static String JSON_LEADER = "leader";
    static String JSON_RED = "red";
    static String JSON_GREEN = "green";
    static String JSON_BLUE = "blue";

    public void loadData(File exportedFile) throws IOException, JsonParsingException {
        // CLEAR THE TEXT AREA
        resultsTextArea.setText("");

        // GET THE REGION DIRECTORY
        regionDirectory = exportedFile.getParentFile();

        // CLEAR OLD SUBREGION DATA
        subregionNames.clear();
        subregionCapitals.clear();
        subregionLeaders.clear();

        // GET THE FILE PATH
        String filePath = exportedFile.getAbsolutePath();

        // LOAD THE JSON FILE WITH ALL THE DATA
        JsonObject json = loadJSONFile(filePath);

        // NOW LOAD THE DATA ONE AT A TIME
        String name = json.getString(JSON_NAME);
        resultsTextArea.appendText("loaded " + JSON_NAME + ": " + name + "\n");

        boolean actualSubregionsHaveCapitals = json.getBoolean(JSON_SUBREGIONS_HAVE_CAPITALS);
        resultsTextArea.appendText("loaded " + JSON_SUBREGIONS_HAVE_CAPITALS + ": " + actualSubregionsHaveCapitals + "\n");

        boolean actualSubregionsHaveFlags = json.getBoolean(JSON_SUBREGIONS_HAVE_FLAGS);
        resultsTextArea.appendText("loaded " + JSON_SUBREGIONS_HAVE_FLAGS + ": " + actualSubregionsHaveFlags + "\n");

        boolean actualSubregionsHaveLeaders = json.getBoolean(JSON_SUBREGIONS_HAVE_LEADERS);
        resultsTextArea.appendText("loaded " + JSON_SUBREGIONS_HAVE_LEADERS + ": " + actualSubregionsHaveLeaders + "\n");

        JsonArray subregionsArray = json.getJsonArray(JSON_SUBREGIONS);
        resultsTextArea.appendText("loaded " + JSON_SUBREGIONS + ":\n");

        for (int i = 0; i < subregionsArray.size(); i++) {
            JsonObject subregion = subregionsArray.getJsonObject(i);
            String subregionName = "";
            if (subregion.containsKey(JSON_NAME))
                subregionName = subregion.getString(JSON_NAME);
            subregionNames.add(subregionName);
            String subregionCapital = "";
            if (subregion.containsKey(JSON_CAPITAL))
                subregionCapital = subregion.getString(JSON_CAPITAL);
            subregionCapitals.add(subregionCapital);
            String subregionLeader = "";
            if (subregion.containsKey(JSON_LEADER))
                subregionLeader = subregion.getString(JSON_LEADER);
            subregionLeaders.add(subregionLeader);
            int subregionRed = subregion.getInt(JSON_RED);
            int subregionGreen = subregion.getInt(JSON_GREEN);
            int subregionBlue = subregion.getInt(JSON_BLUE);
            String subText = "\t-loading "
                    + subregionName + ", capital: "
                    + subregionCapital + ", leader: "
                    + subregionLeader + ", color: ("
                    + subregionRed + ","
                    + subregionGreen + ","
                    + subregionBlue + ")\n";
            resultsTextArea.appendText(subText);
        }
        
        boolean expectedCapitalsGamePlayable = testSubregionCapitals();
        boolean expectedFlagsGamePlayable = testSubregionFlags();
        boolean expectedLeadersGamePlayable = testSubregionLeaders();

        if (actualSubregionsHaveCapitals != expectedCapitalsGamePlayable) {
            resultsTextArea.appendText("actualSubregionsHaveCapitals ERROR!\n");
        } else {
            resultsTextArea.appendText("actualSubregionsHaveCapitals value is CORRECT\n");
        }

        if (actualSubregionsHaveFlags != expectedFlagsGamePlayable) {
            resultsTextArea.appendText("actualSubregionsHaveFlags ERROR!\n");
        } else {
            resultsTextArea.appendText("actualSubregionsHaveFlags value is CORRECT\n");
        }

        if (actualSubregionsHaveLeaders != expectedLeadersGamePlayable) {
            resultsTextArea.appendText("actualSubregionsHaveLeaders ERROR!\n");
        } else {
            resultsTextArea.appendText("actualSubregionsHaveLeaders value is CORRECT\n");
        }
    }

    public boolean testSubregionFlags() {
        for (String subregionName : subregionNames) {
            File imageFile = new File(regionDirectory.getAbsoluteFile() + "/" + subregionName + " Flag.png");
            if (!imageFile.exists()) {
                return false;
            }
        }
        return true;
    }

    public boolean testSubregionCapitals() {
        for (String capital : subregionCapitals) {
            if (capital.equals("")) {
                return false;
            }
        }
        return true;
    }

    public boolean testSubregionLeaders() {
        for (String subregionLeader : subregionLeaders) {
            File imageFile = new File(regionDirectory.getAbsoluteFile() + "/" + subregionLeader + ".png");
            if (!imageFile.exists()) {
                return false;
            }
        }
        return true;
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

    public boolean getDataAsBoolean(JsonObject json, String dataName) {
        JsonValue value = json.get(dataName);
        ValueType type = value.getValueType();
        resultsTextArea.appendText(type.toString());
        return true;
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        InputStream is = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }
}
