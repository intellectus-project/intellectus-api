package com.intellectus.services.weather;

import com.intellectus.controllers.model.WeatherDto;
import com.intellectus.model.Weather;
import com.intellectus.model.WeatherImage;
import com.intellectus.repositories.WeatherRepository;
import com.intellectus.services.WeatherImageService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WeatherService {

    public String[] DESCRIPTION_EXAMPLES = {
            "Niebla",
            "Nubes rotas",
            "Nubes",
            "Cielo claro",
            "Algo de nubes",
            "Bruma",
            "Nubes dispersas",
            "Llovizna ligera",
            "Lluvia ligera"
    };

    @Value("${openWeather.baseUrl}")
    private String BASE_URL;

    @Value("${openWeather.apiKey}")
    private String API_KEY;

    private static final Integer BUENOS_AIRES_ID = 3433955;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository, WeatherImageService weatherImageService) {
        this.weatherRepository = weatherRepository;
        this.weatherImageService = weatherImageService;
    }

    private WeatherRepository weatherRepository;
    private WeatherImageService weatherImageService;

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
            log.error(e.getStackTrace().toString());
        }
    }

    private void save(JSONObject jsonResponse) {
        try {
            Double temp = kelvinToCelsius(jsonResponse.getJSONObject("main").getDouble("temp"));
            String description = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            LocalDateTime now = LocalDateTime.now();
            Weather weather = new Weather(capitalize(description), temp, now);
            weatherRepository.save(weather);
        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
        }
    }

    public Weather getWeatherAt(LocalDateTime dateTime) {
        List<Weather> weathers = weatherRepository.findByTimeAfterAndTimeBeforeOrderByTimeDesc(dateTime.minusDays(1), dateTime);
        return weathers.stream().findFirst().orElse(null);
    }

    private Double kelvinToCelsius(Double kelvin) {
        return kelvin - 273.15;
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public WeatherDto getDayWeatherInfo(LocalDate date) {
        List<Weather> weathers = weatherRepository.findByTimeAfterAndTimeBeforeOrderByTimeDesc(date.atStartOfDay(), date.atTime(LocalTime.MAX));
        Weather currentWeather = weathers.stream().findFirst().get();
        double maxTemperature = Collections.max(weathers, Comparator.comparing(w -> w.getTemperature())).getRoundedTemperature() ;
        double minTemperature = Collections.min(weathers, Comparator.comparing(w -> w.getTemperature())).getRoundedTemperature();
        Optional<WeatherImage> image = weatherImageService.findByDescriptionAndCurrentHour(currentWeather.getDescription());
        String imageName;
        if(image.isPresent())
            imageName = image.get().getImage();
        else
            imageName = "nube.png";
        return WeatherDto.builder()
                .currentTemperature(currentWeather.getRoundedTemperature())
                .maxTemperature(maxTemperature)
                .minTemperature(minTemperature)
                .description(currentWeather.getDescription())
                .image(imageName)
                .build();
    }
}

