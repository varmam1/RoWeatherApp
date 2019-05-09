package RoWeatherApp;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.util.Map;

public class MainController {
    private CityDataFinder weatherAPI = new CityDataFinder("Cambridge,UK");
    private Map<String, Double> weather = CityDataFinder.getCurrentWeather("Cambridge,UK");

    @FXML
    private Text info;

//    @FXML
//    private ImageView flag;

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

//        try {
//            String f = FlagGetter.getFlagColor();
//            if (f.equals("Green")){
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
