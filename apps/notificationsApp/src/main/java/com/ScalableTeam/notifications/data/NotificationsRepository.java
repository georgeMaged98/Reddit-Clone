package com.ScalableTeam.notifications.data;

import com.ScalableTeam.notifications.constants.Collections;
import com.ScalableTeam.notifications.constants.Fields;
import com.ScalableTeam.notifications.exceptions.FirebaseCredentialsException;
import com.ScalableTeam.notifications.exceptions.FirebaseNotificationException;
import com.ScalableTeam.notifications.models.requests.DeviceTokenRequest;
import com.ScalableTeam.notifications.models.requests.NotificationReadRequest;
import com.ScalableTeam.notifications.models.requests.NotificationRequest;
import com.ScalableTeam.notifications.models.responses.NotificationResponse;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("unchecked")
@Repository
public class NotificationsRepository {

    private final Firestore firestore = FirestoreClient.getFirestore();

    public void registerDeviceToken(DeviceTokenRequest deviceToken) throws InterruptedException, ExecutionException {
        firestore.collection(Collections.USERS).document(deviceToken.getUserId()).update(Fields.TOKENS, FieldValue.arrayUnion(deviceToken.getDeviceToken())).get();
    }

    public void unregisterDeviceToken(DeviceTokenRequest deviceToken) throws InterruptedException, ExecutionException {
        firestore.collection(Collections.USERS).document(deviceToken.getUserId()).update(Fields.TOKENS, FieldValue.arrayRemove(deviceToken.getDeviceToken())).get();
    }

    public List<String> getDeviceTokens(List<String> users) throws InterruptedException, ExecutionException {
        List<String> tokens = new ArrayList<>();

        for (String user : users) {
            DocumentSnapshot document = firestore.collection(Collections.USERS).document(user).get().get();

            if (!document.exists()) {
                continue;
            }

            List<String> userTokens = (List<String>) document.get(Fields.TOKENS);

            if (userTokens == null) {
                continue;
            }

            tokens.addAll(userTokens);
        }

        return tokens;
    }

    public void sendNotification(NotificationRequest notification) throws FirebaseCredentialsException, FirebaseNotificationException, InterruptedException, ExecutionException {
        HashMap<String, Object> document = new HashMap<>();

        document.put(Fields.SENDER, notification.getSender());
        document.put(Fields.TITLE, notification.getTitle());
        document.put(Fields.BODY, notification.getBody());
        document.put(Fields.TIMESTAMP, FieldValue.serverTimestamp());
        document.put(Fields.IS_READ, false);

        // Store the notification.
        for (String receiver : notification.getReceivers()) {
            firestore.collection(Collections.USERS).document(receiver).collection(Collections.NOTIFICATIONS).add(document).get();
        }

        // Send the notification.
        NotificationEventHandler handler = NotificationEventHandler.getInstance();
        handler.notify(getDeviceTokens(notification.getReceivers()), notification.getTitle(), notification.getBody());
    }

    public List<NotificationResponse> getNotifications(String userId) throws InterruptedException, ExecutionException {
        List<NotificationResponse> notifications = new ArrayList<>();

        QuerySnapshot query = firestore.collection(Collections.USERS).document(userId).collection(Collections.NOTIFICATIONS).get().get();

        for (DocumentSnapshot document : query.getDocuments()) {
            String id = document.getId();
            String title = document.getString(Fields.TITLE);
            String body = document.getString(Fields.BODY);
            String sender = document.getString(Fields.SENDER);
            Date timestamp = document.getDate(Fields.TIMESTAMP);
            boolean isRead = Boolean.TRUE.equals(document.getBoolean(Fields.IS_READ));

            notifications.add(new NotificationResponse(id, title, body, sender, timestamp, isRead));
        }

        return notifications;
    }

    public void markNotificationAsRead(NotificationReadRequest notificationRead) throws InterruptedException, ExecutionException {
        firestore.collection(Collections.USERS).document(notificationRead.getUserId()).collection(Collections.NOTIFICATIONS).document(notificationRead.getNotificationId()).update(Fields.IS_READ, true).get();
    }
}