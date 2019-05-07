package RoWeatherApp;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class CityDataFinder {
    private static final String APIKEY = "e63d6d93d6cba955e0c5a04fe508c08f";
    private Map<String, Double> cityDataForParticularDT = null;
    private Map<Long, Map<String, Double>> cityDataForTheWholeDay;

    public CityDataFinder(String cityName) {
        cityDataForTheWholeDay = new TreeMap<>();
        String queryWebsite = "https://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "&APPID=" + APIKEY;
        try {
            BufferedInputStream in = new BufferedInputStream(new URL(queryWebsite).openStream());
            JSONParser jsonParser = new JSONParser();
            JSONObject collectedData = (JSONObject) jsonParser.parse(new InputStreamReader(in, "UTF-8"));
            List ListOfPredictions = (List)collectedData.get("list");
            for (Object u : ListOfPredictions) {
                cityDataForParticularDT = new HashMap<>();
                Map tmpEntry = (Map) u;

                long predictionTime = (long) tmpEntry.get("dt");
                Map mainData = (Map) tmpEntry.get("main");
                for (Object entry : mainData.keySet()) {
                    Object paired = mainData.get(entry);
                    if (paired instanceof Double)
                        cityDataForParticularDT.put((String) entry, (double) mainData.get(entry));
                    else if (paired instanceof Long) {
                        long value = (Long) paired;
                        double sameValue = value;
                        cityDataForParticularDT.put((String) entry, sameValue);
                    }
                }

                mainData = (Map) tmpEntry.get("wind");
                for (Object entry : mainData.keySet()) {
                    Object paired = mainData.get(entry);
                    if (paired instanceof Double)
                        cityDataForParticularDT.put((String) entry, (double) mainData.get(entry));
                    else if (paired instanceof Long) {
                        long value = (Long) paired;
                        double sameValue = value;
                        cityDataForParticularDT.put((String) entry, sameValue);
                    }
                }

                cityDataForTheWholeDay.put(predictionTime, cityDataForParticularDT);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Double> todayForecastInTimeT(long time) {
        return new HashMap<String, Double>(cityDataForTheWholeDay.get(time));
    }

    public double getHumidity(Map <String, Double> data) { //returned in 100 * %
        return data.getOrDefault("humidity", 90.0);
    }

    public double getTemperature(Map <String, Double> data) { //need to convert from kelvin to celsius
        return data.get("temp") - 273.15;
    }

    public double getWindSpeed(Map <String, Double> data) { //returned in m/s
        return data.getOrDefault("speed", 0.0);
    }

    public double getWindDirection(Map <String, Double> data) { //
        return data.getOrDefault("deg", 0.0);
    }

    public double getFeelsLikeTemperature(Map <String, Double> data) { //https://pl.wikipedia.org/wiki/Temperatura_odczuwalna
        double V = this.getWindSpeed(data) * 3.6;
        double T = this.getTemperature(data);
        return 13.12 + 0.6215 * T - 11.37*Math.pow(V, 0.16) + 0.3965 * T * Math.pow(V, 0.16);
    }

    public static void main(String[] args) throws Exception {
        CityDataFinder df = new CityDataFinder("Cambridge");
    }
}