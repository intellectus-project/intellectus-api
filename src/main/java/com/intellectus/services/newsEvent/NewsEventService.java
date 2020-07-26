package com.intellectus.services.newsEvent;

import com.google.gson.JsonObject;
import com.intellectus.model.NewsEvent;
import com.intellectus.model.Weather;
import com.intellectus.repositories.NewsEventRepository;
import com.intellectus.repositories.WeatherRepository;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.repository.init.ResourceReader.Type.JSON;

@Service
public class NewsEventService {

    @Value("${newsEvent.baseUrl}")
    private String BASE_URL;

    @Value("${newsEvent.apiKey}")
    private String API_KEY;

    private static final Integer BUENOS_AIRES_ID = 3433955;

    @Autowired
    NewsEventRepository newsEventRepository;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    private final OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

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
            System.out.println(e);
        }
    }

    public void process(JSONArray jsonResponse){
        try {
            for (int i=0; i < jsonResponse.length(); i++) {
                JSONObject news = jsonResponse.getJSONObject(i);
                String url = news.getString("url");
                SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
                Session sess = sessionFactory.openSession();
                boolean exists = sess.createQuery("from NewsEvent where url = '" + url + "'")
                                     .uniqueResult() != null;
                if (exists) return;
                NewsEvent newsEvent = new NewsEvent(news.getString("title"),
                                                    news.getString("description"),
                                                    url);
                newsEventRepository.save(newsEvent);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
