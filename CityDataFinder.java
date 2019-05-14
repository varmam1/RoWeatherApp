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
    //Class to find weather data for particular cities
    private static final String APIKEY = "e63d6d93d6cba955e0c5a04fe508c08f";
    private Map<String, Double> cityDataForParticularDT = null;
    private TreeMap<Long, Map<String, Double>> cityDataForTheWholeDay;



    private final static String[] numbersToDescriptionsMapping = {"Clouds", "Snow", "Extreme", "Clear", "Rain", "Thunderstorm", "Haze", "Drizzle", "Mist", "Dust", "Fog"};
    public CityDataFinder(String cityName) {
        //fetches data for a particular city
        //the data is available for hours being multiples of three, that is: 0:00, 3:00, 6:00, 9:00, ..., 21:00.
        //for other hours, the function returns the first available forecast in the future
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

                Map<String, Double> descriptionsToNumbersMapping = new HashMap<>();
                for (int iter = 0; iter < numbersToDescriptionsMapping.length; ++iter) {
                    descriptionsToNumbersMapping.put(numbersToDescriptionsMapping[iter], (double)iter);
                }

                List thisAPIisStupid = (List)tmpEntry.get("weather"); //why tf it returns a list of just one element
                mainData = (Map) thisAPIisStupid.get(0);
                for (Object entry : mainData.keySet()) {
                    Object paired = mainData.get(entry);
                    if (entry instanceof String && paired instanceof String && entry.equals("main")) {
                        cityDataForParticularDT.put("description", descriptionsToNumbersMapping.getOrDefault(paired, 0.0));
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

    public static Map<String, Double> getCurrentWeather(String cityName) {
        //returns current weather in the form of Map<String, Double> used for other functions
        //Parameters in the map:
        //"temp", "temp_min", "deg" (wind direction), "pressure", "temp_max", "speed" (wind speed)
        //Strongly advised to use CityDataFinder functions to extract a certain weather feature,
        //because by default some of these might be returned in counterintuitive units
        Map<String, Double> cityData = new HashMap<>();
        String queryWebsite = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&APPID=" + APIKEY;
        try {
            BufferedInputStream in = new BufferedInputStream(new URL(queryWebsite).openStream());
            JSONParser jsonParser = new JSONParser();
            JSONObject collectedData = (JSONObject) jsonParser.parse(new InputStreamReader(in, "UTF-8"));

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

            Map<String, Double> descriptionsToNumbersMapping = new HashMap<>();
            for (int iter = 0; iter < numbersToDescriptionsMapping.length; ++iter) {
                descriptionsToNumbersMapping.put(numbersToDescriptionsMapping[iter], (double)iter);
            }

            List thisAPIisStupid = (List)collectedData.get("weather"); //why tf it returns a list of just one element
            mainData = (Map) thisAPIisStupid.get(0);
            for (Object entry : mainData.keySet()) {
                Object paired = mainData.get(entry);
                if (entry instanceof String && paired instanceof String && entry.equals("main")) {
                    cityData.put("description", descriptionsToNumbersMapping.getOrDefault(paired, 0.0));
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityData;
    }

    public Map<String, Double> todayForecastInTimeT(long time) {
        //Input: time of the day for which we would like to get the data

        //returns current weather in the form of Map<String, Double> used for other functions
        //Parameters in the map:
        //"temp", "temp_min", "deg" (wind direction), "pressure", "temp_max", "speed" (wind speed)
        //Strongly advised to use CityDataFinder functions to extract a certain weather feature,
        //because by default some of these might be returned in counterintuitive units
        if (cityDataForTheWholeDay.higherEntry(time) == null) return cityDataForTheWholeDay.pollLastEntry().getValue();
        return new TreeMap<String, Double>(cityDataForTheWholeDay.higherEntry(time).getValue());
    }

    public static String getWeatherType(Map <String, Double> data) {
        String desc = "description";
        return numbersToDescriptionsMapping[(int)Math.round(data.getOrDefault(desc, 0.0))];
    }
    public static double getHumidity(Map <String, Double> data) { //returned in 100 * %
        return data.getOrDefault("humidity", 90.0);
    }

    public static double getTemperature(Map <String, Double> data) { //converts from kelvin to celsius
        return data.get("temp") - 273.15;
    }

    public static double getWindSpeed(Map <String, Double> data) { //returned in m/s
        return data.getOrDefault("speed", 0.0);
    }

    public static double getWindDirection(Map <String, Double> data) { //
        return data.getOrDefault("deg", 0.0);
    }

    public static double getFeelsLikeTemperature(Map <String, Double> data) { //https://pl.wikipedia.org/wiki/Temperatura_odczuwalna
        double V = getWindSpeed(data) * 3.6;
        double T = getTemperature(data);
        return 13.12 + 0.6215 * T - 11.37*Math.pow(V, 0.16) + 0.3965 * T * Math.pow(V, 0.16);
    }

    public static void main(String[] args) {
        CityDataFinder df = new CityDataFinder("Cambridge");
        //System.out.println(CityDataFinder.getCurrentWeather(""));
        System.out.println(getWeatherType(getCurrentWeather("Cambridge")));
    }
}