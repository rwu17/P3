package dk.aau.cs.ds308e18.gui.controllers;

import dk.aau.cs.ds308e18.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class TourListController {
    @FXML
    private void orderListButtonAction(ActionEvent event) throws Exception{
        Main.gui.changeView("OrderList");
    }

    @FXML
    private void backButtonAction(ActionEvent event) throws Exception{
        Main.gui.changeView("MainMenu");
    }

    @FXML
    private void createEmptyTourButtonAction(ActionEvent event) throws Exception{

    }

    @FXML
    private void editTourButtonAction(ActionEvent event) throws Exception{

    }

    @FXML
    private void deleteTourButtonAction(ActionEvent event) throws Exception{

    }

    @FXML
    private void printTourButtonAction(ActionEvent event) throws Exception{

    }

    @FXML
    private void releaseTourButtonAction(ActionEvent event) throws Exception{

    }
}
