import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainController {
    private Map<String, Double> weather = CityDataFinder.getCurrentWeather("Cambridge,UK");

    @FXML
    private ImageView LArrow; //left arrow to change the current day

    @FXML
    private ImageView RArrow; //right arrow to change the current day

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
    private Text DayText; //The current day in the forecast

    private int DaysAhead; //How many days ahead of today it is

    @FXML
    private Text em_wind; //The early morning wind speed
    @FXML
    private Text m_wind; //Morning wind speed
    @FXML
    private Text a_wind; //Afternoon wind speed
    @FXML
    private Text e_wind; //Evening wind speed

    @FXML
    private Text em_temp; //Early morning temperature
    @FXML
    private Text m_temp; //Morning temperature
    @FXML
    private Text a_temp; //Afternoon temperature
    @FXML
    private Text e_temp; //Evening temperature

    @FXML
    private ComboBox mins; //Minutes box to get the alarm time
    @FXML
    private ComboBox hours; //Hours box to get the alarm time
    @FXML
    private ToggleButton onOff; // Button to turn alarm on and off (won't turn on if not a correct time)
    @FXML
    private Text alarmTime; // Text to indicate whether alarm is on or not and if it is on, what time its set to

    private Date Current_BreakDown_Day; //THIS MUST REFER TO TODAY, OR LESS THAN 1 WEEK INTO THE FUTURE.

    private CityDataFinder ForecastInfo;

    private String flagColour; //Stores the current flag colour so that it doesn't need to be queried again when setting to colourblind mode

    private Timeline alarmPlayer; //The timeline which can be initialized and stopped later

    private final int maxDaysAhead = 4; //stores the maximum number of days we want to go ahead

    //Will convert from science units to freedom units (Celsius to Fahrenheit)
    private int freedomUnitsConverter(double celsius) {
        return (int) Math.round((1.8 * celsius) + 32);
    }

    // Will set icon to the weatherType image on the condition that the image is the weatherType.png
    private void setWeatherPicture(ImageView icon, String weatherType){
        Image image = new Image(this.getClass().getResourceAsStream("icon_" + weatherType + ".png"));
        icon.setImage(image);
    }

    @FXML
    private void initialize() throws FileNotFoundException {
        //This will get the temperature from the weather API and the feels like and display it in the info text
        double feelsLike = CityDataFinder.getFeelsLikeTemperature(weather);
        double actual = CityDataFinder.getTemperature(weather);

        int roundedFL = (int) Math.round(feelsLike);
        int roundedActual = (int) Math.round(actual);

        if (roundedActual == roundedFL) {
            info.setText(roundedFL + "°C");
        } else {
            info.setText(roundedFL + "°C" + "\nActual: " + roundedActual + "°C");
        }

        //This will set the forecast at the bottom correctly
        Current_BreakDown_Day = new Date();//automatically set to current system date on ini
        DaysAhead = 0;
        ForecastInfo = new CityDataFinder("Cambridge, UK");
        UpdateForecastBreakdown();

        try {
            //This will set the weather for the current time
            setWeatherPicture(weatherIcon, CityDataFinder.getWeatherType(weather));

            //This should take the flag and display the correct one
            flagColour = FlagGetter.getFlagColor();
            Image image = new Image(this.getClass().getResourceAsStream(flagColour + " Flag.png"));
            flagPic.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //The following gives the unit converter button an event handler to change units
        isFahrenheit.setOnAction((e -> {
            try {
                UpdateForecastBreakdown();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
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
                    info.setText(fl + "°F" + "\nActual: " + act + "°F");
                }
                isFahrenheit.setText("Switch to Celsius");
            }
        }
        ));

        //The following gives the colorblind button the event handler to say what the flag is or not
        colourblind.setOnAction((e -> {
            if (colourblind.isSelected()) {
                colourblindText.setText(flagColour);
            } else {
                colourblindText.setText("");
            }
        }
        ));

        //This would get all the possible values the hours and minutes could be and make them in a dropdown box
        List<String> minutesValues = new ArrayList<>();
        List<String> hourValues = new ArrayList<>();
        for (int i = 0; i < 59; i++) {
            if (i < 10) {
                minutesValues.add("0" + i);
                hourValues.add("0" + i);
            } else if (i < 24) {
                minutesValues.add(i + "");
                hourValues.add(i + "");
            } else {
                minutesValues.add(i + "");
            }
        }

        mins.getItems().addAll(minutesValues);
        hours.getItems().addAll(hourValues);

        //Sets onOff button for alarm to turn on or off the alarm
        onOff.setOnAction((event -> {
            if (onOff.isSelected()) {
                //If there is something in the inputs, then it'll take it, otherwise empty string
                String min = (mins.getValue() != null) ? (String) mins.getValue() : "";
                String hour = (hours.getValue() != null) ? (String) hours.getValue() : "";

                //If the length of the string is not 2 or the input is out of range, then it won't let it work
                if (min.length() != 2 || hour.length() != 2 ||
                        Integer.parseInt(hour) > 24 || Integer.parseInt(hour) < 0 || Integer.parseInt(min) > 60 || Integer.parseInt(min) < 0) {
                    onOff.setSelected(false);
                } else {
                    //Change the text on the button and main screen
                    onOff.setText("Turn Off");
                    alarmTime.setText(hour + ":" + min + " - ON");

                    //Gets the current time and input time and calculates the difference
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    Calendar cal = Calendar.getInstance();
                    String now = dateFormat.format(cal.getTime());
                    String then = hour + ":" + min + ":00";
                    long difference = 0;
                    try {
                        difference = dateFormat.parse(then).getTime() - dateFormat.parse(now).getTime();
                        if (difference < 0) {
                            long oneDay = 24 * 60 * 60 * 1000;
                            difference = oneDay + difference;
                        }
                        difference = difference / 1000;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //Sets a song to play the difference far into the future
                    alarmPlayer = new Timeline(new KeyFrame(Duration.seconds(difference), (e -> {
                        try {
                            if (!FlagGetter.getFlagColor().equals("Red")) {
                                AlarmPlayer.playAlarm();
                                alarmTime.setText("OFF");
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    })));
                    alarmPlayer.play();
                }
            } else {
                // If the button is turned off then mark as so and stop the player
                onOff.setText("Turn On");
                alarmTime.setText("OFF");
                alarmPlayer.stop();
            }
        }));
    }

    // This will show the settings page or take it away depending on if it is there as well as if the alarm page is there
    @FXML
    public void showSettings() {
        if (alarmSettings.isVisible()) {
            alarmSettings.setVisible(false);
            settingsPage.setVisible(true);
        } else {
            settingsPage.setVisible(!settingsPage.isVisible());
        }
    }

    // Settings page method but for the alarm page
    @FXML
    public void showAlarmSettings() {
        if (settingsPage.isVisible()) {
            settingsPage.setVisible(false);
            alarmSettings.setVisible(true);
        } else {
            alarmSettings.setVisible(!alarmSettings.isVisible());
        }
    }

    //This would get the day in a certain number of days ahead
    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }


    //This will update the necessary fields and update the forecast if you go a day in advance
    public void Increment_Breakdown_Day() throws DateOutOfRangeException, FileNotFoundException {
        //on pressing the button to increase the day
        if (DaysAhead == maxDaysAhead) {
            throw new DateOutOfRangeException("Incremented past available range");
        } else {
            Current_BreakDown_Day = addDays(Current_BreakDown_Day, 1);
            DaysAhead += 1;

            if (DaysAhead == 1) {
                DayText.setText("Tomorrow");
                LArrow.setVisible(true);
            } else if (DaysAhead >= maxDaysAhead) {
                DaysAhead = maxDaysAhead;//in case of button spam, reset this information to protect against future issues
                Current_BreakDown_Day = addDays(new Date(), maxDaysAhead);
                RArrow.setVisible(false);
                DayText.setText(parseDate(Current_BreakDown_Day));
            } else {
                DayText.setText(parseDate(Current_BreakDown_Day));
            }
            UpdateForecastBreakdown();
        }
    }

    //This will update the necessary fields and update the forecast if you go a day behind
    public void Decrement_Breakdown_Day() throws DateOutOfRangeException, FileNotFoundException {
        // Decrease the day for the weather breakdown
        if (DaysAhead == 0) {
            throw new DateOutOfRangeException("Decrementing past available range.");
        } else {
            Current_BreakDown_Day = addDays(Current_BreakDown_Day, -1);
            DaysAhead -= 1;
            if (DaysAhead <= 0) {
                DaysAhead = 0;//in case of button spam, reset this information to protect against future issues
                Current_BreakDown_Day = new Date();
                DayText.setText("Today");
                LArrow.setVisible(false);
            } else if (DaysAhead == maxDaysAhead - 1) {
                RArrow.setVisible(true);
                DayText.setText(parseDate(Current_BreakDown_Day));
            } else {
                if (DaysAhead == 1) {//if the date has now rolled back to being the day after today...
                    DayText.setText("Tomorrow");
                } else {
                    DayText.setText(parseDate(Current_BreakDown_Day));
                }
            }
            UpdateForecastBreakdown();
        }
    }

    private String parseDate(Date day) {
        DateFormat timeFormat = new SimpleDateFormat("dd/MM");
        return timeFormat.format(day);
    }

    //Gets the long for an hour of the day
    public long getTimeForDayPoint(int hour) {
        Date ToUpdate = (Date) Current_BreakDown_Day.clone();
        ToUpdate.setHours(hour);
        ToUpdate.setMinutes(0);
        ToUpdate.setSeconds(0);
        long milli = ToUpdate.getTime();//number of milliseconds since 0:00 Jan 1, 1970
        return milli / 1000;//to give it in seconds instead, for the API.
    }

    //Updates forecast at the bottom of the screen
    private void UpdateForecastBreakdown() throws FileNotFoundException { //Assumes an updated date to base the update off of.
        Map<String, Double> Early_Day_Forecast = ForecastInfo.todayForecastInTimeT(getTimeForDayPoint(7));
        Map<String, Double> Morning_Day_Forecast = ForecastInfo.todayForecastInTimeT(getTimeForDayPoint(10));
        Map<String, Double> Afternoon_Forecast = ForecastInfo.todayForecastInTimeT(getTimeForDayPoint(14));
        Map<String, Double> Eve_Day_Forecast = ForecastInfo.todayForecastInTimeT(getTimeForDayPoint(18));

        em_wind.setText(CityDataFinder.getWindSpeed(Early_Day_Forecast) + " m/s");
        m_wind.setText(CityDataFinder.getWindSpeed(Morning_Day_Forecast) + " m/s");
        a_wind.setText(CityDataFinder.getWindSpeed(Afternoon_Forecast) + " m/s");
        e_wind.setText(CityDataFinder.getWindSpeed(Eve_Day_Forecast) + " m/s");

        double earlyMornTemp = CityDataFinder.getFeelsLikeTemperature(Early_Day_Forecast);
        double mornTemp = CityDataFinder.getFeelsLikeTemperature(Morning_Day_Forecast);
        double afterTemp = CityDataFinder.getFeelsLikeTemperature(Afternoon_Forecast);
        double eveTemp = CityDataFinder.getFeelsLikeTemperature(Eve_Day_Forecast);

        if (isFahrenheit.isSelected()) {
            e_temp.setText(freedomUnitsConverter(eveTemp) + "°F");
            a_temp.setText(freedomUnitsConverter(afterTemp) + "°F");
            m_temp.setText(freedomUnitsConverter(mornTemp) + "°F");
            em_temp.setText(freedomUnitsConverter(earlyMornTemp) + "°F");
        } else {
            e_temp.setText(Math.round(eveTemp) + "°C");
            a_temp.setText(Math.round(afterTemp) + "°C");
            m_temp.setText(Math.round(mornTemp) + "°C");
            em_temp.setText(Math.round(earlyMornTemp) + "°C");
        }

        setWeatherPicture(Day_Icon_Early, CityDataFinder.getWeatherType(Early_Day_Forecast));
        setWeatherPicture(Day_Icon_Morning, CityDataFinder.getWeatherType(Morning_Day_Forecast));
        setWeatherPicture(Day_Icon_Afternoon, CityDataFinder.getWeatherType(Afternoon_Forecast));
        setWeatherPicture(Day_Icon_Evening, CityDataFinder.getWeatherType(Eve_Day_Forecast));

        if (DaysAhead == 0) {
            DateFormat dateFormat = new SimpleDateFormat("HH");
            Calendar cal = Calendar.getInstance();
            int currentHour = Integer.parseInt(dateFormat.format(cal.getTime()));

            if (currentHour > 7) {
                Day_Icon_Early.setImage(null);
                em_wind.setText("In the past");
                em_temp.setText("");
            }
            if (currentHour > 10) {
                Day_Icon_Morning.setImage(null);
                m_wind.setText("In the past");
                m_temp.setText("");
            }
            if (currentHour > 14) {
                Day_Icon_Afternoon.setImage(null);
                a_wind.setText("In the past");
                a_temp.setText("");
            }
            if (currentHour > 18) {
                Day_Icon_Evening.setImage(null);
                e_wind.setText("In the past");
                e_temp.setText("");
            }
        }
    }
}