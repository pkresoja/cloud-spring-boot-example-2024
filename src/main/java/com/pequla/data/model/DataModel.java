package com.pequla.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataModel {

    private Integer id;
    private String uuid;
    private DiscordUser user;
    private DiscordGuild guild;
    private LocalDateTime createdAt;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiscordUser {
        private Integer id;
        private String discordId;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiscordGuild {
        private Integer id;
        private String discordId;
        private LocalDateTime createdAt;
    }
}
