package com.scmspain.daos;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.scmspain.entities.Tweet;

@Component
public class TweetDao {

    private EntityManager entityManager;

    public TweetDao(EntityManager entityManager) {
	this.entityManager = entityManager;
    }

    /**
     * Persist a new tweet object
     * 
     * @param tweet
     */
    public void insert(Tweet tweet) {
	entityManager.persist(tweet);
    }

    /**
     * Find a tweet by id
     * 
     * @param id
     *            tweet primary key
     * @return the tweet
     */
    public Tweet findById(Long id) {
	return this.entityManager.find(Tweet.class, id);
    }

    /**
     * List all tweets
     * 
     * @return list with all tweets
     */
    public List<Tweet> listAll() {
	TypedQuery<Tweet> query = this.entityManager.createQuery(
		"FROM Tweet AS tweetId WHERE pre2015MigrationStatus<>99 and discardedDate IS NULL ORDER BY id DESC",
		Tweet.class);
	return query.getResultList();
    }

    /**
     * Discard a tweet
     * 
     * @param id
     *            tweet primary key
     */
    public void discardTweet(Long id) {
	this.entityManager.createQuery("UPDATE Tweet SET discardedDate = :now WHERE id = :id").setParameter("id", id)
		.setParameter("now", new Date()).executeUpdate();
    }

    /**
     * List all discarded tweets
     * 
     * @return list with all discarded tweets
     */
    public List<Tweet> listAllDiscarded() {
	TypedQuery<Tweet> query = this.entityManager.createQuery(
		"FROM Tweet AS tweetId WHERE pre2015MigrationStatus<>99 and discardedDate IS NOT NULL ORDER BY discardedDate DESC",
		Tweet.class);
	return query.getResultList();
    }

}
