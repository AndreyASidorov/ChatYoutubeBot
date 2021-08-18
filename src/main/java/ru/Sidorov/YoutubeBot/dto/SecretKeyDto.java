package ru.Sidorov.YoutubeBot.dto;

public class SecretKeyDto {
        public SecretKeyDto() {
        }

        public SecretKeyDto(String key) {
            this.key = key;
        }

        private String key;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
}
