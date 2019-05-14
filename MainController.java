import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

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
    private ToggleButton isFahrenheit;

    @FXML
    private AnchorPane settingsPage;

    @FXML
    private AnchorPane alarmSettings;

    //Will convert from science units to freedom units (Celsius to Fahrenheit)
    private int freedomUnitsConverter(double celsius){
        return (int)Math.round((1.8*celsius) + 32);
    }

    @FXML
    private void initialize(){
        //This will get the temperature from the weather API and the feels like and display it in the info text

        double feelsLike = CityDataFinder.getFeelsLikeTemperature(weather);
        double actual = CityDataFinder.getTemperature(weather);

        int roundedFL = (int) Math.round(feelsLike);
        int roundedActual = (int) Math.round(actual);

        if (roundedActual == roundedFL) {
            info.setText(roundedFL + "°");
        } else {
            info.setText(roundedFL + "°" + "\nActual: " + roundedActual + "°");
        }

        //This should take the flag and display the correct one
        try {
            String f = FlagGetter.getFlagColor();
            if (f.equals("Green")){
                FileInputStream input = new FileInputStream("Green Flag.png"); //Root is Project
                Image image = new Image(input);
                flagPic.setImage(image);
            }
            else if (f.equals("Yellow")){
                FileInputStream input = new FileInputStream("Yellow Flag.png"); //Root is Project
                Image image = new Image(input);
                flagPic.setImage(image);
            }
            else{
                FileInputStream input = new FileInputStream("Red Flag.png"); //Root is Project
                Image image = new Image(input);
                flagPic.setImage(image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //The following gives the unit converter button an event handler to change units
        isFahrenheit.setOnAction((e -> {
                if (!isFahrenheit.isSelected()){
                    if (roundedActual == roundedFL) {
                        info.setText(roundedFL + "°");
                    } else {
                        info.setText(roundedFL + "°" + "\nActually: " + roundedActual + "°");
                    }
                }
                else {
                    int fl = freedomUnitsConverter(CityDataFinder.getFeelsLikeTemperature(weather));
                    int act = freedomUnitsConverter(CityDataFinder.getTemperature(weather));

                    if (fl == act) {
                        info.setText(fl + "°");
                    } else {
                        info.setText(fl + "°" + "\nActually: " + act + "°");
                    }
                }
            }
        ));
    }

    @FXML
    public void showSettings(ActionEvent event){
        if (alarmSettings.isVisible()){
            alarmSettings.setVisible(false);
            settingsPage.setVisible(true);
        }
        else {
            settingsPage.setVisible(!settingsPage.isVisible());
        }
    }

    @FXML
    public void showAlarmSettings(ActionEvent event){
        if (settingsPage.isVisible()){
            settingsPage.setVisible(false);
            alarmSettings.setVisible(true);
        }
        else {
            alarmSettings.setVisible(!alarmSettings.isVisible());
        }
    }
}
