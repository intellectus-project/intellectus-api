package com.intellectus.services.weather;

import com.intellectus.model.Weather;
import com.intellectus.repositories.WeatherRepository;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class WeatherService {

    @Value("${openWeather.baseUrl}")
    private String BASE_URL;

    @Value("${openWeather.apiKey}")
    private String API_KEY;

    private static final Integer BUENOS_AIRES_ID = 3433955;

    @Autowired
    WeatherRepository weatherRepository;

    private final OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public void fetch() {
        try {
            Request request = new Request.Builder()
                    .url(BASE_URL + "weather?id=" + BUENOS_AIRES_ID.toString() + "&appid=" + API_KEY + "&lang=es")
                    .build();

            Call call = client.newCall(request);
            Response response = call.execute();
            String jsonData = response.body().string();
            JSONObject jsonResponse = new JSONObject(jsonData);
            save(jsonResponse);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void save(JSONObject jsonResponse){
        try {
            Double temp = kelvinToCelsius(jsonResponse.getJSONObject("main").getDouble("temp"));
            String description = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            Weather weather = new Weather(description, temp);
            weatherRepository.save(weather);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Double kelvinToCelsius(Double kelvin){
        return kelvin - 273.15;
    }
}

