package RoWeatherApp;

import javafx.application.Application;
import javafx.event.*;
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

    public Scene changeToSettings(){
        return new SettingsPage(true, false).getScene();
    }

    @Override
    public void start(final Stage primaryStage) throws IOException {

        //The following adds a settings button that switches to a currently empty settings page
        Button settingsButton = new Button();
        settingsButton.setText("[INSERT GEAR HERE]");
        settingsButton.setOnAction(e -> primaryStage.setScene(changeToSettings()));

        //This will add the settings button
        Group root = new Group();
        root.getChildren().add(settingsButton);

        //Adds a text which has the flag color
        Text flagColor = new Text(100, 300, "Flag Color: " + FlagGetter.getFlagColor());
        root.getChildren().add(flagColor);
        Scene scene = new Scene(root, 750/2.0, 1334/2.0);

        //Displays the stage
        primaryStage.setTitle("RoWeather App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
