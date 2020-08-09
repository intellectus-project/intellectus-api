package com.intellectus.model;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Text;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "news_events")
public class NewsEvent extends AuditableEntity {

    public NewsEvent() { }

    public NewsEvent(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TITLE", length = 1000)
    private String title;

    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "url", length = 1000)
    private String url;
}