package com.scmspain.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.scmspain.controller.command.DiscardTweetCommand;
import com.scmspain.controller.command.PublishTweetCommand;
import com.scmspain.entities.Tweet;
import com.scmspain.services.TweetService;

@RestController
public class TweetController {

    private final static Logger LOGGER = LoggerFactory.getLogger(TweetController.class);

    private TweetService tweetService;

    public TweetController(TweetService tweetService) {
	this.tweetService = tweetService;
    }

    @GetMapping("/tweet")
    public List<Tweet> listAllTweets() {
	LOGGER.info("GET /tweet");
	return this.tweetService.listAllTweets();
    }

    @PostMapping("/tweet")
    @ResponseStatus(CREATED)
    public void publishTweet(@RequestBody PublishTweetCommand publishTweetCommand) {
	LOGGER.info("POST /tweet", publishTweetCommand);
	this.tweetService.publishTweet(publishTweetCommand.getPublisher(), publishTweetCommand.getTweet());
    }

    @PostMapping("/discarded")
    @ResponseStatus(NO_CONTENT)
    public void discardTweet(@RequestBody DiscardTweetCommand discardTweetCommand) {
	LOGGER.info("POST /discarded", discardTweetCommand);
	this.tweetService.discardTweet(discardTweetCommand.getTweet());
    }

    @GetMapping("/discarded")
    public List<Tweet> listDiscardedTweets() {
	LOGGER.info("GET /discarded");
	return this.tweetService.listDiscardedTweets();
    }
}
