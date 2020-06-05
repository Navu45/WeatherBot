import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class WeatherParser {
    private final static String API_KEY = "&appid=1171a69442732b0d76517a3474f65183";
    private final static String API_CALL_TEMPLATE = "api.openweathermap.org/data/2.5/weather?q=";
    JsonNode city;
    String city_name;

    String getCurrentWeather(String city) {
        String res;
        try{
            String forecast = getWeather(getJsonWeatherData(city));
            res = String.format("%s:%s%s", city, System.lineSeparator(),forecast);
        }
        catch(IllegalArgumentException e){
            return "Can't find this city. Try another one";
        } catch (Exception e)
        {
            e.printStackTrace();
            return "Bot crashed";
        }
        return res;
    }

    private static String getJsonWeatherData(String city) throws Exception {
        String urlString = API_CALL_TEMPLATE + city + API_KEY;
        URL urlObj = new URL(urlString);

        HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = connection.getResponseCode();
        if(responseCode == 404){
            throw new IllegalArgumentException();
        }

        InputStreamReader stream = new InputStreamReader(connection.getInputStream());
        BufferedReader in = new BufferedReader(stream);
        String input_line;
        StringBuffer response = new StringBuffer();
        while ((input_line = in.readLine()) != null)
        {
            response.append(input_line);
        }
        in.close();
        return response.toString();
    }

    private static String getWeather(String data) throws Exception {
        List<String> weatherList = new ArrayList<>();
        String[] list_template = {"temp", "description", "speed", "humidity"};
        String[] nodes_names = {"main", "weather", "wind"};
        JsonNode arrNode;


        for (int i = 0; i < list_template.length; i++)
        {
            arrNode = new ObjectMapper().readTree(data).get(nodes_names[i % 3]);
            String characteristic = arrNode.get(list_template[i]).asText();
            weatherList.add(characteristic);
        }

        return String.format("%s\n%s\n%s\n%s",
                weatherList.get(0), weatherList.get(1), weatherList.get(2), weatherList.get(3));
    }

}
