package com.scmspain.services;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;

import com.scmspain.daos.TweetDao;
import com.scmspain.entities.Tweet;

public class TweetServiceTest {
    private TweetDao tweetDao;
    private MetricWriter metricWriter;
    private TweetService tweetService;

    @Before
    public void setUp() throws Exception {
	this.tweetDao = mock(TweetDao.class);
	this.metricWriter = mock(MetricWriter.class);

	this.tweetService = new TweetService(tweetDao, metricWriter);
    }

    @Test
    public void shouldInsertANewTweet() throws Exception {
	tweetService.publishTweet("Guybrush Threepwood", "I am Guybrush Threepwood, mighty pirate.");

	verify(tweetDao).insert(any(Tweet.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenTweetLengthIsInvalid() throws Exception {
	tweetService.publishTweet("Pirate",
		"LeChuck? He's the guy that went to the Governor's for dinner and never wanted to leave. He fell for her in a big way, but she told him to drop dead. So he did. Then things really got ugly.");
    }

    @Test
    public void shouldListAllTweets() throws Exception {
	List<Tweet> expected = new ArrayList<>();
	expected.add(new Tweet());
	when(tweetDao.listAll()).thenReturn(expected);
	List<Tweet> list = tweetService.listAllTweets();
	Assert.assertEquals(expected, list);
    }

    @Test
    public void shouldReturnATweet() throws Exception {
	Tweet expected = new Tweet();
	expected.setId(1L);
	expected.setPublisher("Me");
	expected.setTweet(
		"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam varius et augue ultrices cursus.");
	when(tweetDao.findById(1L)).thenReturn(expected);
	Tweet result = tweetService.getTweet(1L);
	Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldDiscardATweet() throws Exception {
	Tweet tweet = new Tweet();
	tweet.setId(1L);
	when(tweetDao.findById(1L)).thenReturn(tweet);
	tweetService.discardTweet(1L);

	verify(tweetDao).discardTweet(any(Long.class));
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowAnExceptionWhenDiscardingANonExistingTweet() throws Exception {
	when(tweetDao.findById(1L)).thenThrow(new EntityNotFoundException());
	tweetService.discardTweet(1L);
    }

    @Test
    public void shouldListAllDiscardedTweets() throws Exception {
	List<Tweet> expected = new ArrayList<>();
	expected.add(new Tweet());
	when(tweetDao.listAllDiscarded()).thenReturn(expected);
	List<Tweet> list = tweetService.listDiscardedTweets();
	Assert.assertEquals(expected, list);
    }
}
