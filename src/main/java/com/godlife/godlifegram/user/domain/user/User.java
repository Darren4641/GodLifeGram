package com.godlife.godlifegram.user.domain.user;

import com.godlife.godlifegram.common.domain.BaseEntity;
import com.godlife.godlifegram.user.ui.dto.request.NotificationDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(name = "end_point", columnDefinition = "text")
    private String endPoint;

    @Column(name = "p256dh", columnDefinition = "text")
    private String p256dh;

    @Column(name = "auth", columnDefinition = "text")
    private String auth;

    @Column(name = "is_push_enabled", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isPushEnabled = false;

    public static User signup(String email, String nickname, String password) {
        User user = new User();
        user.email = email;
        user.nickname = nickname;
        user.password = password;
        return user;
    }

    public void subscribe(NotificationDto notificationDto) {
        this.endPoint = notificationDto.endpoint;
        this.p256dh = notificationDto.keys.p256dh;
        this.auth = notificationDto.keys.auth;
        this.isPushEnabled = true;
    }

    public void switchPushEnable() {
        this.isPushEnabled = !isPushEnabled;
    }

}