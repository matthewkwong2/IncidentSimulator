package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void onGenerateIncident(ActionEvent actionEvent) {
        IncidentSimulation.getInstance().runIncidentAppearanceSimulationServiceNow();
    }

    @FXML
    private void onRemoveIncident(ActionEvent actionEvent) {
        IncidentSimulation.getInstance().runIncidentClearanceSimulationServiceNow();
    }
}
