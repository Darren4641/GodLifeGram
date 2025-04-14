package com.godlife.godlifegram.post.application.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PushNotificationService {
    private final PushService pushService;
    private final ObjectMapper objectMapper;

    public void sendPushNotificationFromLike(Subscription subscription, String serverBaseUrl, Long postId) {
        Map<String, String> payloadData = new HashMap<>();
        payloadData.put("title", "갓생하루");
        payloadData.put("body", "게시물에 좋아요를 받았어요!");
        payloadData.put("icon", serverBaseUrl + "/icon/icon.png");
        payloadData.put("url", serverBaseUrl + "/post-detail?id=" + postId);

        try {
            String payloadJson = objectMapper.writeValueAsString(payloadData);
            Notification notification = new Notification(
                    subscription.endpoint,
                    subscription.keys.p256dh,
                    subscription.keys.auth,
                    payloadJson
            );

            HttpResponse response = pushService.send(notification);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 201) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendPushNotificationFromComment(Subscription subscription, String serverBaseUrl, Long postId, String commentWriter) {
        Map<String, String> payloadData = new HashMap<>();
        payloadData.put("title", "갓생하루");
        payloadData.put("body", commentWriter + "님이 내 게시물에 댓글을 남겼어요!");
        payloadData.put("icon", serverBaseUrl + "/icon/icon.png");
        payloadData.put("url", serverBaseUrl + "/post-detail?id=" + postId);

        try {
            String payloadJson = objectMapper.writeValueAsString(payloadData);
            Notification notification = new Notification(
                    subscription.endpoint,
                    subscription.keys.p256dh,
                    subscription.keys.auth,
                    payloadJson
            );

            HttpResponse response = pushService.send(notification);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 201) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
