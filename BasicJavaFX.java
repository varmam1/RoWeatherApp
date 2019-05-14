import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.*;
import java.io.IOException;
import javafx.stage.*;


public class BasicJavaFX extends Application {

    @Override
    public void start(final Stage primaryStage) throws IOException {
        // The following code loads in an FXML doc:

        AnchorPane root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        Scene scene = new Scene(root, 400, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
