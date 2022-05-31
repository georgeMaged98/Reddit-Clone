package com.ScalableTeam.models.notifications.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDeleteRequest {

    private String userId;
    private String notificationId;
}
