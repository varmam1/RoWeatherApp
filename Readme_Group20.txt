Running the code:

To run our application, we used an executable .jar file.

Libraries:

To build our "RoWeather" App, we used the JavaFX library. More specifically, we used .xml files (javafx.fxml) to build the visuals of our application. Features we used include are the scenes and  animations from the JavaFX library (javafx.animation and javafx.scene).

For the weather data in our running app, we used the OpenWeatherMap API (openweathermap.org/api). In order to manipulate the data inputs we queried from the API we used a JSON Parser (org.json.simple.parser) to give us the JSON objects. We then read this data into HashMaps so that we could access the weather data with each timestamp.

For the Alarm Clock integrated in our app we used the Java Sound Library (java.sound).