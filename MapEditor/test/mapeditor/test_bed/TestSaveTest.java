/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapeditor.test_bed;

import java.io.IOException;
import mapeditor.data.DataManager;
import mapeditor.data.Subregion;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Yizhi Yang
 */
public class TestSaveTest {

    public TestSaveTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testMakeAndorra() throws Exception {

        DataManager dataManagerWithMakeUpData = new DataManager();

        TestSave.makeAndorra(dataManagerWithMakeUpData);

        DataManager dataManager = new DataManager();

        TestLoad.loadData(dataManager, "./work/Andorra.json");

        assertEquals(dataManagerWithMakeUpData.getName(), dataManager.getName());
        assertEquals(dataManagerWithMakeUpData.getBackgroundColor(), dataManager.getBackgroundColor());
        assertEquals(dataManagerWithMakeUpData.getBorderColor(), dataManager.getBorderColor());
        assertEquals(dataManagerWithMakeUpData.getBorderThickness(), dataManager.getBorderThickness());
        assertEquals(dataManagerWithMakeUpData.getZoomLevel(), dataManager.getZoomLevel());
        assertEquals(dataManagerWithMakeUpData.getFlagPath(), dataManager.getFlagPath());
        assertEquals(dataManagerWithMakeUpData.getLogoPath(), dataManager.getLogoPath());
        assertEquals(dataManagerWithMakeUpData.getRawDataPath(), dataManager.getRawDataPath());
        assertEquals(dataManagerWithMakeUpData.getXPositionOfNationalFlag(), dataManager.getXPositionOfNationalFlag());
        assertEquals(dataManagerWithMakeUpData.getYPositionOfNationalFlag(), dataManager.getYPositionOfNationalFlag());
        assertEquals(dataManagerWithMakeUpData.getXPositionOfNationalLogo(), dataManager.getYPositionOfNationalLogo());
        assertEquals(dataManagerWithMakeUpData.getYPositionOfNationalLogo(), dataManager.getYPositionOfNationalLogo());

        for (int i = 0; i < dataManagerWithMakeUpData.getSubregions().size(); i++) {
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getName(), ((Subregion) dataManager.getSubregions().get(i)).getName());
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getLeader(), ((Subregion) dataManager.getSubregions().get(i)).getLeader());
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getCapital(), ((Subregion) dataManager.getSubregions().get(i)).getCapital());
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getRed(), ((Subregion) dataManager.getSubregions().get(i)).getRed());
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getGreen(), ((Subregion) dataManager.getSubregions().get(i)).getGreen());
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getBlue(), ((Subregion) dataManager.getSubregions().get(i)).getBlue());
            
        }
    }
    /**
     * Test of makeSanMarino method, of class TestSave.
     */
    @Test
    public void testMakeSanMarino() throws Exception {

        DataManager dataManagerWithMakeUpData = new DataManager();

        TestSave.makeSanMarino(dataManagerWithMakeUpData);

        DataManager dataManager = new DataManager();

        TestLoad.loadData(dataManager, "./work/San Marino.json");

        assertEquals(dataManagerWithMakeUpData.getName(), dataManager.getName());
        assertEquals(dataManagerWithMakeUpData.getBackgroundColor(), dataManager.getBackgroundColor());
        assertEquals(dataManagerWithMakeUpData.getBorderColor(), dataManager.getBorderColor());
        assertEquals(dataManagerWithMakeUpData.getBorderThickness(), dataManager.getBorderThickness());
        assertEquals(dataManagerWithMakeUpData.getZoomLevel(), dataManager.getZoomLevel());
        assertEquals(dataManagerWithMakeUpData.getFlagPath(), dataManager.getFlagPath());
        assertEquals(dataManagerWithMakeUpData.getLogoPath(), dataManager.getLogoPath());
        assertEquals(dataManagerWithMakeUpData.getRawDataPath(), dataManager.getRawDataPath());
        assertEquals(dataManagerWithMakeUpData.getXPositionOfNationalFlag(), dataManager.getXPositionOfNationalFlag());
        assertEquals(dataManagerWithMakeUpData.getYPositionOfNationalFlag(), dataManager.getYPositionOfNationalFlag());
        assertEquals(dataManagerWithMakeUpData.getXPositionOfNationalLogo(), dataManager.getYPositionOfNationalLogo());
        assertEquals(dataManagerWithMakeUpData.getYPositionOfNationalLogo(), dataManager.getYPositionOfNationalLogo());

        for (int i = 0; i < dataManagerWithMakeUpData.getSubregions().size(); i++) {
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getName(), ((Subregion) dataManager.getSubregions().get(i)).getName());
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getLeader(), ((Subregion) dataManager.getSubregions().get(i)).getLeader());
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getCapital(), ((Subregion) dataManager.getSubregions().get(i)).getCapital());
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getRed(), ((Subregion) dataManager.getSubregions().get(i)).getRed());
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getGreen(), ((Subregion) dataManager.getSubregions().get(i)).getGreen());
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getBlue(), ((Subregion) dataManager.getSubregions().get(i)).getBlue());
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getFlagImagePath(), ((Subregion) dataManager.getSubregions().get(i)).getFlagImagePath());
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getLeaderImagePath(), ((Subregion) dataManager.getSubregions().get(i)).getLeaderImagePath());
        }
    }

    /**
     * Test of makeSlovakia method, of class TestSave.
     */
    @Test
    public void testMakeSlovakia() throws IOException {

        DataManager dataManagerWithMakeUpData = new DataManager();

        TestSave.makeSlovakia(dataManagerWithMakeUpData);

        DataManager dataManager = new DataManager();

        TestLoad.loadData(dataManager, "./work/Slovakia.json");

        assertEquals(dataManagerWithMakeUpData.getName(), dataManager.getName());
        assertEquals(dataManagerWithMakeUpData.getBackgroundColor(), dataManager.getBackgroundColor());
        assertEquals(dataManagerWithMakeUpData.getBorderColor(), dataManager.getBorderColor());
        assertEquals(dataManagerWithMakeUpData.getBorderThickness(), dataManager.getBorderThickness());
        assertEquals(dataManagerWithMakeUpData.getZoomLevel(), dataManager.getZoomLevel());
        assertEquals(dataManagerWithMakeUpData.getFlagPath(), dataManager.getFlagPath());
        assertEquals(dataManagerWithMakeUpData.getLogoPath(), dataManager.getLogoPath());
        assertEquals(dataManagerWithMakeUpData.getRawDataPath(), dataManager.getRawDataPath());
        assertEquals(dataManagerWithMakeUpData.getXPositionOfNationalFlag(), dataManager.getXPositionOfNationalFlag());
        assertEquals(dataManagerWithMakeUpData.getYPositionOfNationalFlag(), dataManager.getYPositionOfNationalFlag());
        assertEquals(dataManagerWithMakeUpData.getXPositionOfNationalLogo(), dataManager.getYPositionOfNationalLogo());
        assertEquals(dataManagerWithMakeUpData.getYPositionOfNationalLogo(), dataManager.getYPositionOfNationalLogo());

        for (int i = 0; i < dataManagerWithMakeUpData.getSubregions().size(); i++) {
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getName(), ((Subregion) dataManager.getSubregions().get(i)).getName());
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getLeader(), ((Subregion) dataManager.getSubregions().get(i)).getLeader());
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getCapital(), ((Subregion) dataManager.getSubregions().get(i)).getCapital());
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getRed(), ((Subregion) dataManager.getSubregions().get(i)).getRed());
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getGreen(), ((Subregion) dataManager.getSubregions().get(i)).getGreen());
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getBlue(), ((Subregion) dataManager.getSubregions().get(i)).getBlue());
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getFlagImagePath(), ((Subregion) dataManager.getSubregions().get(i)).getFlagImagePath());
            assertEquals(((Subregion) dataManagerWithMakeUpData.getSubregions().get(i)).getLeaderImagePath(), ((Subregion) dataManager.getSubregions().get(i)).getLeaderImagePath());
        }
    }
}
