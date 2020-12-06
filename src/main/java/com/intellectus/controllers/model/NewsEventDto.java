package com.intellectus.controllers.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;

@Getter
@Setter
public class NewsEventDto{
    private String title;
    private String url;
    private LocalDate created;

    public NewsEventDto(String title, String url, LocalDateTime created)
    {
        this.title = title;
        this.url = url;
        this.created = created.toLocalDate();
    }
}
