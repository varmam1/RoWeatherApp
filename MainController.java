import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class MainController {
    private Map<String, Double> weather = CityDataFinder.getCurrentWeather("Cambridge,UK");

    @FXML
    private Text info;

    @FXML
    private ImageView weatherIcon;

    @FXML
    private ImageView flagPic;

    @FXML
    private ToggleButton isFahrenheit;

    @FXML
    private ToggleButton colourblind;

    @FXML
    private Text colourblindText;

    @FXML
    private AnchorPane settingsPage;

    @FXML
    private AnchorPane alarmSettings;

    @FXML
    private ImageView Day_Icon_Early;

    @FXML
    private ImageView Day_Icon_Morning;

    @FXML
    private ImageView Day_Icon_Afternoon;

    @FXML
    private ImageView Day_Icon_Evening;

    private Date Current_BreakDown_Day; //THIS MUST REFER TO TODAY, OR LESS THAN 1 WEEK INTO THE FUTURE.

    private CityDataFinder ForecastInfo;

    //Will convert from science units to freedom units (Celsius to Fahrenheit)
    private int freedomUnitsConverter(double celsius){
        return (int)Math.round((1.8*celsius) + 32);
    }

    // Will set icon to the weatherType image on the condition that the image is the weatherType.png
    private void setWeatherPicture(ImageView icon, String weatherType) throws FileNotFoundException {
        FileInputStream input = new FileInputStream("icon_" + weatherType + ".png");
        Image image = new Image(input);
        icon.setImage(image);
    }

    @FXML
    private void initialize() throws FileNotFoundException {
        //This will get the temperature from the weather API and the feels like and display it in the info text

        double feelsLike = CityDataFinder.getFeelsLikeTemperature(weather);

        double actual = CityDataFinder.getTemperature(weather);


        int roundedFL = (int) Math.round(feelsLike);
        int roundedActual = (int) Math.round(actual);

        Current_BreakDown_Day = new Date();//automatically set to current system date on ini

        ForecastInfo = new CityDataFinder("Cambridge, UK");

        UpdateForecastBreakdown();


        //DailyBreakdown_Icons[0] = Day_Icon_Early;
        if (roundedActual == roundedFL) {
            info.setText(roundedFL + "°C");
        } else {
            info.setText(roundedFL + "°C" + "\nActual: " + roundedActual + "°C");
        }


        try {
            setWeatherPicture(weatherIcon, CityDataFinder.getWeatherType(weather));

            //This should take the flag and display the correct one
            FileInputStream input = new FileInputStream(FlagGetter.getFlagColor() + " Flag.png");
            Image image = new Image(input);
            flagPic.setImage(image);
//            flagPic.addEventHandler(MouseEvent.MOUSE_CLICKED, this::showAlarmSettings);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //The following gives the unit converter button an event handler to change units
        isFahrenheit.setOnAction((e -> {
            if (!isFahrenheit.isSelected()) {
                if (roundedActual == roundedFL) {
                    info.setText(roundedFL + "°C");
                } else {
                    info.setText(roundedFL + "°C" + "\nActual: " + roundedActual + "°C");
                }
            } else {
                int fl = freedomUnitsConverter(CityDataFinder.getFeelsLikeTemperature(weather));
                int act = freedomUnitsConverter(CityDataFinder.getTemperature(weather));

                if (fl == act) {
                    info.setText(fl + "°F");
                } else {
                    info.setText(fl + "°F" + "\nActually: " + act + "°F");
                }
            }
        }
        ));
        colourblind.setOnAction((e -> {
            if (colourblind.isSelected()) {
                try {
                    colourblindText.setText(FlagGetter.getFlagColor());
                } catch (IOException ioe) {
                    System.out.println("Couldn't get flag colour");
                }
            } else {
                colourblindText.setText("");
            }
        }
        ));
    }

    @FXML
    public void showSettings(Event event){
        if (alarmSettings.isVisible()){
            alarmSettings.setVisible(false);
            settingsPage.setVisible(true);
        }
        else {
            settingsPage.setVisible(!settingsPage.isVisible());
        }
    }

    @FXML
    public void showAlarmSettings(Event event){
        if (settingsPage.isVisible()){
            settingsPage.setVisible(false);
            alarmSettings.setVisible(true);
        }
        else {
            alarmSettings.setVisible(!alarmSettings.isVisible());
        }
    }

    public long getTimeForDayPoint(Date GivenDate, int hour){
        Date ToUpdate = (Date)GivenDate.clone();
        ToUpdate.setHours(hour);
        ToUpdate.setMinutes(0);
        ToUpdate.setSeconds(0);
        return ToUpdate.getTime();
    }

    public long getTimeForDayPoint( int hour){
        Date ToUpdate = (Date)Current_BreakDown_Day.clone();
        ToUpdate.setHours(hour);
        ToUpdate.setMinutes(0);
        ToUpdate.setSeconds(0);
        return ToUpdate.getTime();
    }

    private void UpdateForecastBreakdown ()throws FileNotFoundException{//Assumes an updated date to base the update off of.
        Map<String,Double> Early_Day_Forecast = ForecastInfo.todayForecastInTimeT(getTimeForDayPoint(7));
        Map<String,Double> Morning_Day_Forecast = ForecastInfo.todayForecastInTimeT(getTimeForDayPoint(7));
        Map<String,Double> Afternoon_Forecast = ForecastInfo.todayForecastInTimeT(getTimeForDayPoint(7));
        Map<String,Double> Eve_Day_Forecast = ForecastInfo.todayForecastInTimeT(getTimeForDayPoint(7));

        setWeatherPicture(Day_Icon_Early,CityDataFinder.getWeatherType(Early_Day_Forecast));
        setWeatherPicture(Day_Icon_Morning,CityDataFinder.getWeatherType(Morning_Day_Forecast));
        setWeatherPicture(Day_Icon_Afternoon,CityDataFinder.getWeatherType(Afternoon_Forecast));
        setWeatherPicture(Day_Icon_Evening,CityDataFinder.getWeatherType(Eve_Day_Forecast));
    }
}