package RoWeatherApp;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class CityDataFinder {
    private static final String APIKEY = "e63d6d93d6cba955e0c5a04fe508c08f";
    private Map<String, Double> cityData = null;

    public CityDataFinder(String cityName) {
        cityData = new HashMap<>();
        String queryWebsite = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&APPID=" + APIKEY;
        try {
            BufferedInputStream in = new BufferedInputStream(new URL(queryWebsite).openStream());
            JSONParser jsonParser = new JSONParser();
            JSONObject collectedData = (JSONObject) jsonParser.parse(new InputStreamReader(in, "UTF-8"));

            System.out.println(collectedData);
            Map mainData = (Map) collectedData.get("main");
            for (Object entry : mainData.keySet()) {
                Object paired = mainData.get(entry);
                if (paired instanceof Double) cityData.put((String) entry, (double) mainData.get(entry));
                else if (paired instanceof Long) {
                    long value = (Long) paired;
                    double sameValue = value;
                    cityData.put((String) entry, sameValue);
                }
            }

            mainData = (Map) collectedData.get("wind");
            for (Object entry : mainData.keySet()) {
                Object paired = mainData.get(entry);
                if (paired instanceof Double) cityData.put((String) entry, (double) mainData.get(entry));
                else if (paired instanceof Long) {
                    long value = (Long) paired;
                    double sameValue = value;
                    cityData.put((String) entry, sameValue);
                }
            }

            System.out.println(cityData);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public double getHumidity() { //returned in 100 * %
        return cityData.getOrDefault("humidity", 90.0);
    }

    public double getTemperature() { //need to convert from kelvin to celsius
        return cityData.get("temp") - 273.15;
    }

    public double getWindSpeed() { //returned in m/s
        return cityData.getOrDefault("speed", 0.0);
    }

    public double getWindDirection() { //
        return cityData.getOrDefault("deg", 0.0);
    }

    public double getFeelsLikeTemperature() { //https://pl.wikipedia.org/wiki/Temperatura_odczuwalna
        double V = this.getWindSpeed() * 3.6;
        double T = this.getTemperature();
        return 13.12 + 0.6215 * T - 11.37*Math.pow(V, 0.16) + 0.3965 * T * Math.pow(V, 0.16);
    }

    public static void main(String[] args) throws Exception {
        CityDataFinder df = new CityDataFinder("Szczecin");
        System.out.println(df.getTemperature());
        System.out.println(df.getHumidity());
        System.out.println(df.getWindDirection());
        System.out.printf("Wind speed: %f\n", df.getWindSpeed());

        System.out.println(df.getFeelsLikeTemperature());
    }
}