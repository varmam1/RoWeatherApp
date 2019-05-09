package RoWeatherApp;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SettingsPage extends Application {

    private boolean isCelsius;
    private boolean isColorBlind;

    public SettingsPage(boolean isCelsius, boolean isColorBlind){
        this.isCelsius = isCelsius;
        this.isColorBlind = isColorBlind;
    }

    public Scene getScene(Stage primaryStage, Scene main){
        Group root = new Group();
        Button back = new Button("[Back to Home]");
        back.setOnAction(e -> primaryStage.setScene(main));
        root.getChildren().add(back);
        Scene scene = new Scene(root, 750/2.0, 1334/2.0);
        return scene;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("RoWeather App");
//        primaryStage.setScene(getScene());
        primaryStage.show();
    }
}
