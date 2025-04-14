package com.godlife.godlifegram.user.ui.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nl.martijndwars.webpush.Subscription;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class NotificationDto extends Subscription {
    private Long id;
}
