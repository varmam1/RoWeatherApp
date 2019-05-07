package RoWeatherApp;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import java.io.IOException;
import javafx.stage.Stage;


public class BasicJavaFX extends Application {

//    @Override
//    public void start(Stage stage) {
//        //Creating a Text object
//        Text text = new Text(100, 100, "Yooooooo");
//
////        //Setting the text to be added.
////        text.setText("Hello how are you");
////
////        //setting the position of the text
////        text.setX(50);
////        text.setY(50);
//
//        //Creating a Group object
//        StackPane root = new StackPane(text);
//
//        //Creating a scene object
//        Scene scene = new Scene(root, 600, 300);
//
//        //Setting title to the Stage
//        stage.setTitle("Sample Application");
//
//        //Adding scene to the stage
//        stage.setScene(scene);
//
//        //Displaying the contents of the stage
//        stage.show();
//    }

    @Override
    public void start(final Stage primaryStage) throws IOException {

        Button button = new Button();
        button.setText("Get Flag Color");

        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                Label secondLabel = null;
                try {
                    secondLabel = new Label(FlagGetter.getFlagColor());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                StackPane secondaryLayout = new StackPane();
                secondaryLayout.getChildren().add(secondLabel);

                Scene secondScene = new Scene(secondaryLayout, 230, 100);

                // New window (Stage)
                Stage newWindow = new Stage();
                newWindow.setTitle("Second Stage");
                newWindow.setScene(secondScene);

                // Set position of second window, related to primary window.
                newWindow.setX(primaryStage.getX() + 200);
                newWindow.setY(primaryStage.getY() + 100);

                newWindow.show();
            }
        });

        Group root = new Group();
        root.getChildren().add(button);

        Text t = new Text(100, 300, "Flag Color: " + FlagGetter.getFlagColor());
        root.getChildren().add(t);

        Scene scene = new Scene(root, 750/2.0, 1334/2.0);

        primaryStage.setTitle("RoWeather App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
