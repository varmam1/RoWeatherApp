Running the code:

To run the app, use the folder as the project root and run BasicJavaFX.java.

Instructions in IntelliJ:
  1. New Project From Existing Sources
  2. Select this folder as the project root.
  3. Next, next, next, etc. until Finish (should have the json parser jar file in the project)
  4. Run BasicJavaFX.java


Libraries:

To build our "RoWeather" App, we used the JavaFX library. More specifically, we used .xml files (javafx.fxml) to build the visuals of our application. Features we used include are the scenes and animations from the JavaFX library (javafx.animation and javafx.scene).

For the weather data in our running app, we used the OpenWeatherMap API (openweathermap.org/api). In order to manipulate the data inputs we queried from the API we used a JSON Parser (org.json.simple.parser) to give us the JSON objects. We then read this data into HashMaps so that we could access the weather data with each timestamp.

For the Alarm Clock integrated in our app we used the Java Sound Library (java.sound).


OS that it works on:

The application seems to run on Windows and Mac but not Linux since JavaFX seems to give errors only on Linux. 
