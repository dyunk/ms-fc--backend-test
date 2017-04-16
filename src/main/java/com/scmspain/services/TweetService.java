package com.scmspain.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.scmspain.daos.TweetDao;
import com.scmspain.entities.Tweet;

@Service
@Transactional
public class TweetService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TweetService.class);

    private TweetDao tweetDao;
    private MetricWriter metricWriter;

    private static final String URL_PATTERN = "((http://|https://)\\S*\\s)";
    private static final Integer TWEET_MAX_LENGTH = 140;

    public TweetService(TweetDao tweetDao, MetricWriter metricWriter) {
	this.tweetDao = tweetDao;
	this.metricWriter = metricWriter;
    }

    /**
     * Push tweet to repository
     * 
     * @param publisher
     *            - creator of the Tweet
     * @param text
     *            - Content of the Tweet
     */
    public void publishTweet(String publisher, String text) {
	LOGGER.info("publishTweet {}, {} ", publisher, text);
	Assert.hasLength(publisher, "Publisher must not be empty");
	Assert.hasLength(text, "Tweet must not be empty");
	Assert.isTrue(tweetExcedesLength(text), "Tweet must not be greater than 140 characters");

	Tweet tweet = new Tweet();
	tweet.setTweet(text);
	tweet.setPublisher(publisher);

	this.metricWriter.increment(new Delta<Number>("published-tweets", 1));
	this.tweetDao.insert(tweet);
    }

    /**
     * Recover tweet from repository Parameter
     * 
     * @param id-
     *            id of the Tweet to retrieve
     * @return retrieved Tweet
     */
    public Tweet getTweet(Long id) {
	LOGGER.info("getTweet {} ", id);
	this.metricWriter.increment(new Delta<Number>("times-queried-tweets", 1));
	return this.tweetDao.findById(id);
    }

    /**
     * Recover all tweets from repository
     * 
     * @return list with all tweets
     */
    public List<Tweet> listAllTweets() {
	LOGGER.info("listAllTweets");
	this.metricWriter.increment(new Delta<Number>("times-queried-tweets", 1));
	return this.tweetDao.listAll();
    }

    /**
     * Discard a tweet
     * 
     * @param tweet
     *            id of the tweet to discard
     */
    public void discardTweet(Long tweet) {
	LOGGER.info("discardTweet {}", tweet);
	if (this.getTweet(tweet) == null) {
	    throw new EntityNotFoundException(String.format("Tweet not found with id %d", tweet));
	}
	this.metricWriter.increment(new Delta<Number>("discarded-tweets", 1));
	this.tweetDao.discardTweet(tweet);
    }

    /**
     * List all discarded tweets
     * 
     * @return
     */
    public List<Tweet> listDiscardedTweets() {
	LOGGER.info("listDiscardedTweets");
	this.metricWriter.increment(new Delta<Number>("times-queried-tweets", 1));
	return this.tweetDao.listAllDiscarded();
    }

    /**
     * Check if the tweet is greater than 140 chars without links.
     * 
     * @param text
     * @return
     */
    private boolean tweetExcedesLength(String text) {
	String valuableText = text.replaceAll(URL_PATTERN, "");
	return valuableText.length() < TWEET_MAX_LENGTH;
    }
}
