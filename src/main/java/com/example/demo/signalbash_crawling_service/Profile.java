package com.example.demo.signalbash_crawling_service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Profile(
    @JsonProperty("user_display_name") String name, 
    @JsonProperty("bio") String bio, 
    @JsonProperty("currently_making") String currentlyMaking
    ) {
        @Override
        public final String toString() {
            return String.format("Profile [name=%s, bio=%s, currentlyMaking=%s]", name, bio, currentlyMaking);
        }
    }