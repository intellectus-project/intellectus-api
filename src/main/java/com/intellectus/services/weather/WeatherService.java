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
import org.apache.tomcat.jni.Local;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.*;
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
    private static final String LAT = "-34.587873";
    private static final String LON = "-58.456468";

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
            normalizeAndSave(jsonResponse);
        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
        }
    }

    public void fetchFiveDaysHistorical() {
        LocalDateTime dt = LocalDateTime.now().minusDays(4);
        for(int i = 0; i<=4; i++){
            fetchHistorical(dt.plusDays(i));
        }
    }

    public void fetchHistorical(LocalDateTime dt) {
        try {
            Request request = new Request.Builder()
                    .url(BASE_URL + "onecall/timemachine?lat=" + LAT + "&lon=" + LON + "&appid=" + API_KEY
                            + "&dt=" + unixTimestamp(dt) + "&lang=es")
                    .build();

            Call call = client.newCall(request);
            Response response = call.execute();
            String jsonData = response.body().string();
            JSONObject jsonResponse = new JSONObject(jsonData);
            JSONObject current = jsonResponse.getJSONObject("current");
            findOrSave(getDescription(current), current.getDouble("temp"), unixToLocalDateTime(current.getString("dt")));
            JSONArray array = jsonResponse.getJSONArray("hourly");
            if (array != null) {
                Object obj = new Object();
                int len = array.length();
                for (int i=0;i<len;i++){
                    obj = array.get(i);
                    findOrSave(getDescription((JSONObject)obj), ((JSONObject) obj).getDouble("temp"), unixToLocalDateTime(((JSONObject) obj).getString("dt")));
                }
            }
        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
        }
    }

    private String unixTimestamp(LocalDateTime dt) {
        return String.valueOf(dt.toEpochSecond(ZoneOffset.UTC));
    }

    private LocalDateTime unixToLocalDateTime(String unix) {
        return LocalDateTime.ofEpochSecond(Long.valueOf(unix), 0, ZoneOffset.UTC);
    }

    private void normalizeAndSave(JSONObject jsonResponse) {
        try {
            Double temp = jsonResponse.getJSONObject("main").getDouble("temp");
            String description = getDescription(jsonResponse);
            LocalDateTime now = LocalDateTime.now();
            findOrSave(capitalize(description), temp, now);
        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
        }
    }

    private String getDescription(JSONObject obj) throws JSONException {
        return obj.getJSONArray("weather").getJSONObject(0).getString("description");
    }

    private void findOrSave(String description, double temp, LocalDateTime dt) {
        if (!weatherRepository.findByHour(dt).isEmpty()) {
            return;
        }
        Weather weather = new Weather(capitalize(description), kelvinToCelsius(temp), dt);
        weatherRepository.save(weather);
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

    public Optional<WeatherImage> getWeatherImage(String description, int hour){
        return weatherImageService.findByDescriptionAndHour(description, hour);
    }
}

