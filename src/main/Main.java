package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Incident Simulator");
        primaryStage.setScene(new Scene(root, 300, 200));
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> {
            IncidentSimulation.getInstance().cancel();
        });
        primaryStage.show();

        IncidentSimulation.getInstance().start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
