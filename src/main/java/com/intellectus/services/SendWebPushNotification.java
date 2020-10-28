package com.intellectus.services;

import com.intellectus.model.configuration.User;
import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.testcontainers.shaded.org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.Security;

@Service
@Slf4j
public class SendWebPushNotification {
    @Value("${vapid.publicKey}")
    private String PUBLIC_KEY;

    @Value("${vapid.privateKey}")
    private String PRIVATE_KEY;

    @Autowired
    public SendWebPushNotification() {

    }

    public void breakTaken(User user, String fullName) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            PushService pushService = new PushService(PUBLIC_KEY, PRIVATE_KEY);

            Subscription.Keys keys = new Subscription.Keys(user.getP256dh(), user.getAuth());

            Subscription subscription = new Subscription(user.getEndpoint(), keys);

            Notification notification = new Notification(subscription, payload(fullName));

            pushService.send(notification);

        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
        }
    }

    private String payload(String fullName) {
        try {
            JSONObject json = new JSONObject();
            json.put("text", "El operador " + fullName + " se tomó un descanso");
            return json.toString();
        }
        catch (Exception e){
            return "El operador se tomó un descanso";
        }
    }
}
