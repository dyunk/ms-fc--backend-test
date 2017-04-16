package com.scmspain.configuration;

import javax.persistence.EntityManager;

import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.scmspain.controller.TweetController;
import com.scmspain.daos.TweetDao;
import com.scmspain.services.TweetService;

@Configuration
public class TweetConfiguration {
    @Bean
    public TweetDao getTweetDao(EntityManager entityManager) {
	return new TweetDao(entityManager);
    }

    @Bean
    public TweetService getTweetService(TweetDao tweetDao, MetricWriter metricWriter) {
	return new TweetService(tweetDao, metricWriter);
    }

    @Bean
    public TweetController getTweetConfiguration(TweetService tweetService) {
	return new TweetController(tweetService);
    }
}
