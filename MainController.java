import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainController {
    private Map<String, Double> weather = CityDataFinder.getCurrentWeather("Cambridge,UK");

    @FXML
    private Text info; //Temperature info text

    @FXML
    private ImageView weatherIcon; // Current weather icon

    @FXML
    private ImageView flagPic; //Picture of the flag

    @FXML
    private ToggleButton isFahrenheit; //Button to change between units

    @FXML
    private ToggleButton colourblind; //Button to change whether you're colorblind or not

    @FXML
    private Text colourblindText; //Will put flag color if you said you're colorblind

    @FXML
    private AnchorPane settingsPage; //Page with the settings (isFahrenheit and colourblind)

    @FXML
    private AnchorPane alarmSettings; //Page with the alarm

    @FXML
    private ImageView Day_Icon_Early; //Early morning weather icon for a day
    @FXML
    private ImageView Day_Icon_Morning; //Morning weather icon for a day
    @FXML
    private ImageView Day_Icon_Afternoon; //Afternoon weather icon for a day
    @FXML
    private ImageView Day_Icon_Evening; //Evening weather icon for a day

    @FXML
    private Text em_wind;
    @FXML
    private Text m_wind;
    @FXML
    private Text a_wind;
    @FXML
    private Text e_wind;

    @FXML
    private ComboBox mins;
    @FXML
    private ComboBox hours;
    @FXML
    private ToggleButton onOff;
    @FXML
    private Text alarmTime;

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

        //DailyBreakdown_Icons[0] = Day_Icon_Early;
        if (roundedActual == roundedFL) {
            info.setText(roundedFL + "°C");
        } else {
            info.setText(roundedFL + "°C" + "\nActual: " + roundedActual + "°C");
        }


        //This will set the forecast at the bottom correctly
        Current_BreakDown_Day = new Date();//automatically set to current system date on ini
        ForecastInfo = new CityDataFinder("Cambridge, UK");
        UpdateForecastBreakdown();

        try {
            //This will set the weather for the current time
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

                isFahrenheit.setText("Switch to Fahrenheit");
            } else {
                int fl = freedomUnitsConverter(CityDataFinder.getFeelsLikeTemperature(weather));
                int act = freedomUnitsConverter(CityDataFinder.getTemperature(weather));

                if (fl == act) {
                    info.setText(fl + "°F");
                } else {
                    info.setText(fl + "°F" + "\nActually: " + act + "°F");
                }
                isFahrenheit.setText("Switch to Celsius");
            }
        }
        ));

        //The following gives the colorblind button the event handler to say what the flag is or not
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

        List<String> minutesValues = new ArrayList<>();
        List<String> hourValues = new ArrayList<>();
        for (int i = 0; i < 59; i++){
            if (i < 10){
                minutesValues.add("0" + i);
                hourValues.add("0" + i);
            }
            else if (i < 24){
                minutesValues.add(i + "");
                hourValues.add(i + "");
            }
            else{
                minutesValues.add(i + "");
            }
        }

        mins.getItems().addAll(minutesValues);
        hours.getItems().addAll(hourValues);

        onOff.setOnAction((event -> {
            if (onOff.isSelected()){
                String min = (mins.getValue() != null) ? (String) mins.getValue() : "";
                String hour = (hours.getValue() != null) ? (String) hours.getValue() : "";
                if (min.length() != 2 || hour.length() != 2 ||
                    Integer.parseInt(hour) > 24 || Integer.parseInt(hour) < 0 || Integer.parseInt(min) > 60 || Integer.parseInt(min) < 0){
                    onOff.setSelected(false);
                }
                else {
                    onOff.setText("Turn Off");
                    alarmTime.setText(hour + ":" + min + " - ON");
                }
            }
            else{
                onOff.setText("Turn On");
                alarmTime.setText("OFF");
            }
        }));

    }

    // This will show the settings page or take it away depending on if it is there as well as if the alarm page is there
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

    // Settings page method but for the alarm page
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


    //Gets the long for an hour of the day
    public long getTimeForDayPoint(int hour){
        Date ToUpdate = (Date)Current_BreakDown_Day.clone();
        ToUpdate.setHours(hour);
        ToUpdate.setMinutes(0);
        ToUpdate.setSeconds(0);
        return ToUpdate.getTime();
    }

    //Updates forecast at the bottom of the screen
    private void UpdateForecastBreakdown() throws FileNotFoundException{ //Assumes an updated date to base the update off of.
        Map<String,Double> Early_Day_Forecast = ForecastInfo.todayForecastInTimeT(getTimeForDayPoint(7));
        Map<String,Double> Morning_Day_Forecast = ForecastInfo.todayForecastInTimeT(getTimeForDayPoint(7));
        Map<String,Double> Afternoon_Forecast = ForecastInfo.todayForecastInTimeT(getTimeForDayPoint(7));
        Map<String,Double> Eve_Day_Forecast = ForecastInfo.todayForecastInTimeT(getTimeForDayPoint(7));

        em_wind.setText(CityDataFinder.getWindSpeed(Early_Day_Forecast)+ " m/s");
        m_wind.setText(CityDataFinder.getWindSpeed(Morning_Day_Forecast)+ " m/s");
        a_wind.setText(CityDataFinder.getWindSpeed(Afternoon_Forecast)+ " m/s");
        e_wind.setText(CityDataFinder.getWindSpeed(Eve_Day_Forecast)+ " m/s");

        setWeatherPicture(Day_Icon_Early,CityDataFinder.getWeatherType(Early_Day_Forecast));
        setWeatherPicture(Day_Icon_Morning,CityDataFinder.getWeatherType(Morning_Day_Forecast));
        setWeatherPicture(Day_Icon_Afternoon,CityDataFinder.getWeatherType(Afternoon_Forecast));
        setWeatherPicture(Day_Icon_Evening,CityDataFinder.getWeatherType(Eve_Day_Forecast));
    }
}