package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(final Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/sample.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load(getClass().getResource("sample.fxml"));

        primaryStage.setScene(new Scene(root, 258.0,456.0));
        primaryStage.show();

        System.out.println();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
