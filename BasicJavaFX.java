package RoWeatherApp;

import javafx.application.Application;
import javafx.event.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.io.IOException;
import javafx.stage.*;


public class BasicJavaFX extends Application {

    public void backToMain(){

    }

    public void changeToAlarm(){

    }

    public Scene changeToSettings(Stage primaryStage, Scene main){
        return new SettingsPage(true, false).getScene(primaryStage, main);
    }

    @Override
    public void start(final Stage primaryStage) throws IOException {
//
//        //The following adds a settings button that switches to a currently empty settings page
//        Button settingsButton = new Button();
//        settingsButton.setText("[INSERT GEAR HERE]");
////
////        Button alarmButton = new Button();
////        alarmButton.setText("[INSERT CLOCK HERE]");
//
//
//        //This will add the settings button
//        Group root = new Group();
//        root.getChildren().add(settingsButton);
////        root.getChildren().add(alarmButton);
//
//        //Adds a text which has the flag color
//        Text flagColor = new Text(100, 300, "Flag Color: " + FlagGetter.getFlagColor());
//        root.getChildren().add(flagColor);
//        Scene scene = new Scene(root, 750/2.0, 1334/2.0);
//        settingsButton.setOnAction(e -> primaryStage.setScene(changeToSettings(primaryStage, scene)));
//
//        System.out.println(root.getChildren());
//
//        //Displays the stage
//        primaryStage.setTitle("RoWeather App");
//        primaryStage.setScene(scene);
//        primaryStage.show();

        // The following code loads in an FXML doc:
//
        Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        Scene scene = new Scene(root, 750/2.0, 1334/2.0);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
