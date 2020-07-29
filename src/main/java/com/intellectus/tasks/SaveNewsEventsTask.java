package com.intellectus.tasks;

import com.intellectus.services.newsEvent.NewsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SaveNewsEventsTask {

    NewsEventService newsEventService;

    @Autowired
    SaveNewsEventsTask(NewsEventService newsEventService) {
        this.newsEventService = newsEventService;
    }

    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
    public void perform() {
        newsEventService.fetch();
    }

}
