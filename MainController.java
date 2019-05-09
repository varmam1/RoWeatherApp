package RoWeatherApp;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.util.Map;

public class MainController {

    private CityDataFinder weatherAPI = new CityDataFinder("Cambridge,UK");
    private Map<String, Double> weather = CityDataFinder.getCurrentWeather("Cambridge,UK");

    @FXML
    private Text info;

    @FXML
    private void initialize(){
        //This will get the temperature from the weather API and the feels like and display it in the info text
        info.setText(Math.round(CityDataFinder.getFeelsLikeTemperature(weather)) + "°" + "\n Actually: " +
                Math.round(CityDataFinder.getFeelsLikeTemperature(weather)) + "°");
    }

}
