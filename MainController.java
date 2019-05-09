package RoWeatherApp;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.util.Map;

public class MainController {

    private CityDataFinder weatherAPI = new CityDataFinder("Cambridge,UK");
    private Map<String, Double> weather = CityDataFinder.getCurrentForecast("Cambridge,UK");

    @FXML
    private Text info;

    @FXML
    private void initialize(){
        info.setText(weatherAPI.getTemperature(weather) + "°" + " and feels like: " +
                weatherAPI.getFeelsLikeTemperature(weather) + "°");
    }

}
