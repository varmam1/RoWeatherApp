package RoWeatherApp;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class MainController {
    private Map<String, Double> weather = CityDataFinder.getCurrentWeather("Cambridge,UK");

    @FXML
    private Text info;

    @FXML
    private ImageView flagPic;

    @FXML
    private void initialize(){
        //This will get the temperature from the weather API and the feels like and display it in the info text

        int feelsLike = (int) Math.round(CityDataFinder.getFeelsLikeTemperature(weather));
        int actual = (int) Math.round(CityDataFinder.getTemperature(weather));

        if (feelsLike == actual){
            info.setText(feelsLike + "°");
        }
        else {
            info.setText(feelsLike + "°" + "\n Actually: " + actual + "°");
        }

        try {
            String f = FlagGetter.getFlagColor();
            if (f.equals("Green")){
                FileInputStream input = new FileInputStream("src/RoWeatherApp/Green Flag.png"); //Root is Project
                Image image = new Image(input);
                flagPic.setImage(image);
            }
            else if (f.equals("Yellow")){
                FileInputStream input = new FileInputStream("src/RoWeatherApp/Yellow Flag.png"); //Root is Project
                Image image = new Image(input);
                flagPic.setImage(image);
            }
            else{
                FileInputStream input = new FileInputStream("src/RoWeatherApp/Red Flag.png"); //Root is Project
                Image image = new Image(input);
                flagPic.setImage(image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
