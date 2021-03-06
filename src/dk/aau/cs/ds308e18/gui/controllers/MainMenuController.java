package dk.aau.cs.ds308e18.gui.controllers;

import dk.aau.cs.ds308e18.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.prefs.Preferences;

/*
The main menu, where the sub-menus can be accessed.
*/
public class MainMenuController {
    @FXML
    public void initialize() {
        //Checks if dark mode is enabled in the user preferences and enables dark mode
        Preferences prefs = Preferences.userNodeForPackage(dk.aau.cs.ds308e18.Main.class);
        Main.gui.setDarkMode(prefs.getBoolean("darkMode", false));
    }

    @FXML
    private void toursButtonAction(ActionEvent event) throws IOException {
        Main.gui.changeView("TourList");
    }

    @FXML
    private void ordersButtonAction(ActionEvent event) throws IOException{
        Main.gui.changeView("OrderList");
    }

    @FXML
    private void waresButtonAction(ActionEvent event) throws IOException{
        Main.gui.changeView("WareList");
    }

    @FXML
    private void settingsButtonAction(ActionEvent event) throws IOException{
        Main.gui.changeView("Settings");
    }
}
