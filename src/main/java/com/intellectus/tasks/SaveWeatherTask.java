package com.intellectus.tasks;

import ch.qos.logback.core.util.FixedDelay;
import com.intellectus.services.weather.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SaveWeatherTask {

    WeatherService weatherService;

    @Autowired
    SaveWeatherTask(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Scheduled(fixedDelay = 3600)
    //@Scheduled(cron = "0 * * * * ?")
    public void perform() {
        weatherService.fetch();
    }

}
