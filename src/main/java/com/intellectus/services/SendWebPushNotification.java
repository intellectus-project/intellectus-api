package com.intellectus.services;

import com.intellectus.model.UserWebPushCredentials;
import com.intellectus.model.configuration.User;
import com.intellectus.repositories.UserWebPushCredentialsRepository;
import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.apache.http.HttpResponse;
import org.jose4j.lang.JoseException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.testcontainers.shaded.org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class SendWebPushNotification {
    @Value("${vapid.publicKey}")
    private String PUBLIC_KEY;

    @Value("${vapid.privateKey}")
    private String PRIVATE_KEY;

    @Autowired
    UserWebPushCredentialsRepository userWebPushCredentialsRepository;

    public SendWebPushNotification() {

    }

    public void breakTaken(User user, String fullName) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            PushService pushService = new PushService(PUBLIC_KEY, PRIVATE_KEY);

            Collection<UserWebPushCredentials> credentials = userWebPushCredentialsRepository.findAllByUser(user);

            credentials.forEach(cred -> {
                Subscription.Keys keys = new Subscription.Keys(cred.getP256dh(), cred.getAuth());

                Subscription subscription = new Subscription(cred.getEndpoint(), keys);

                Notification notification = null;
                try {
                    notification = new Notification(subscription, payload(fullName));
                    pushService.send(notification);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (JoseException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

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
