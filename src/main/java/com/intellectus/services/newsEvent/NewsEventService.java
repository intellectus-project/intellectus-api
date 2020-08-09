package com.intellectus.services.newsEvent;

import com.intellectus.controllers.model.NewsEventDto;
import com.intellectus.model.NewsEvent;
import com.intellectus.repositories.NewsEventRepository;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class NewsEventService {

    @Value("${newsEvent.baseUrl}")
    private String BASE_URL;

    @Value("${newsEvent.apiKey}")
    private String API_KEY;
    
    private NewsEventRepository newsEventRepository;

    private final OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    @Autowired
    public NewsEventService(NewsEventRepository newsEventRepository) {
        this.newsEventRepository = newsEventRepository;
    }

    public void fetch() {
        try {
            Request request = new Request.Builder()
                    .url(BASE_URL + "top-headlines?country=ar&ApiKey=" + API_KEY)
                    .build();

            Call call = client.newCall(request);
            Response response = call.execute();
            String jsonData = response.body().string();
            JSONObject jsonResponse = new JSONObject(jsonData);
            process(jsonResponse.getJSONArray("articles"));
        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
        }
    }

    public void process(JSONArray jsonResponse){
        try {
            for (int i=0; i < jsonResponse.length(); i++) {
                JSONObject news = jsonResponse.getJSONObject(i);
                String url = news.getString("url");
                boolean toSave = newsEventRepository.findByUrl(url).isEmpty();
                if (toSave) {
                    NewsEvent newsEvent = new NewsEvent(news.getString("title"),
                            news.getString("description"),
                            url);
                    newsEventRepository.save(newsEvent);
                }
            }
        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
        }
    }

    public List<NewsEventDto> fetchByDate(LocalDate dateFrom, LocalDate dateTo) {
        return newsEventRepository.findAllByCreatedBetweenOrderByIdDesc(dateFrom.atStartOfDay(), dateTo.atTime(LocalTime.MAX));
    }
}
