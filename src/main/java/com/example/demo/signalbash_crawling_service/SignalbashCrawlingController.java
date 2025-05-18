package com.example.demo.signalbash_crawling_service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class SignalbashCrawlingController {
    private static final String baseUrl = "https://api.signalbash.com/user/";
    private static final String userBaseUrlTemplate = baseUrl + "%s";

    private final RestTemplate restTemplate;
    private final String computeTimeScript;

    public SignalbashCrawlingController(
            RestTemplate restTemplate, 
            @Qualifier("computeTimeScript") String computeTimeScript
        ) {
        this.restTemplate = restTemplate;
        this.computeTimeScript = computeTimeScript;
    }

    private static final Logger logger = LoggerFactory.getLogger(SignalbashCrawlingController.class);

    @GetMapping("/signalbash/{username}")
    public EntityModel<Profile> getProfile(@PathVariable String username) {
        String userBaseUrl = String.format(userBaseUrlTemplate, username);
        
        Profile profile = restTemplate.getForObject(userBaseUrl, Profile.class);
        logger.info("profile: " + profile);
        return EntityModel.of(profile, 
        linkTo(methodOn(SignalbashCrawlingController.class).getProfile(username)).withSelfRel(),
        linkTo(methodOn(SignalbashCrawlingController.class).getActivity(username)).withRel("activity"));
    }

    @GetMapping("/signalbash/{username}/activity")
    public EntityModel<Activity> getActivity(@PathVariable String username) {
        String activityUrl = String.format(userBaseUrlTemplate, username) + "/activity";
        String rawActivity = restTemplate.getForObject(activityUrl, String.class);
        logger.info("rawActivity: " + rawActivity);

        Context graalvmContext = Context.create();
        Value computeTimeFunc = graalvmContext.eval("js", this.computeTimeScript);
        Value graalvmActivity = computeTimeFunc.execute(rawActivity);
        Activity activity = new Activity(
            graalvmActivity.getMember("dawTime").asString(), 
            graalvmActivity.getMember("streak").asInt());
        
        logger.info(activity.toString());
        
        return EntityModel.of(activity,
        linkTo(methodOn(SignalbashCrawlingController.class).getActivity(username)).withSelfRel(),
        linkTo(methodOn(SignalbashCrawlingController.class).getProfile(username)).withRel("profile"));
    }
}
