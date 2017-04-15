package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.adapters.TweetsAdapter;
import com.codepath.apps.simpletweets.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.apps.simpletweets.models.Tweet;

import java.util.ArrayList;

/**
 * Created by christine_nguyen on 4/14/17.
 */

public abstract class TweetsListFragment extends Fragment {
    private ArrayList<Tweet> tweets;
    private TweetsAdapter adapter;
    private RecyclerView rvTweets;
    private EndlessRecyclerViewScrollListener scrollListener;
    private LinearLayoutManager layoutManager;

    private SwipeRefreshLayout swipeContainer;

    // For pagination
    Long maxId;
    Long sinceId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(getActivity(), tweets);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        initSwipeContainer(v);
        initRecyclerView(v);

        return v;
    }

    private void initSwipeContainer(View v) {
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    public void setSwipeListener(SwipeRefreshLayout.OnRefreshListener listener) {
        swipeContainer.setOnRefreshListener(listener);
    }

    private void initRecyclerView(View v) {
        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweets);
        rvTweets.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvTweets.setLayoutManager(layoutManager);
    }

    public void setScrollListener(EndlessRecyclerViewScrollListener listener) {
        scrollListener = listener;
        rvTweets.addOnScrollListener(scrollListener);
    }

    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void addTweetToTopOfTimeline(Tweet tweet) {
        tweets.add(0, tweet);
        adapter.notifyItemInserted(0);
        rvTweets.scrollToPosition(0);
    }

    public void handleRefreshSuccess(ArrayList<Tweet> newTweets) {
        if (newTweets.size() > 0) {
            clearAll();
            addAll(newTweets);
        }
        swipeContainer.setRefreshing(false);
    }

    public void handleRefreshFailure() {
        Snackbar.make(swipeContainer, R.string.timeline_refresh_error, Snackbar.LENGTH_LONG).show();
        swipeContainer.setRefreshing(false);
    }

    public void handleLoadFailure() {
        Snackbar.make(swipeContainer, R.string.populate_timeline_error, Snackbar.LENGTH_LONG).show();
    }

    public void handlePostFailure() {
        Snackbar.make(swipeContainer, R.string.post_tweet_error, Snackbar.LENGTH_LONG).show();
    }

    public void addAll(ArrayList<Tweet> tweets) {
        adapter.addAll(tweets);
    }

    public void clearAll() {
        adapter.clear();
    }

    public void resetPaginationParams() {
        sinceId = null;
        maxId = null;
    }

    public void updatePaginationParams(Long newestUid, Long oldestUid) {
        // Tweets are returned in order of recency (most recent is first)
        if (sinceId == null || newestUid > sinceId) {
            sinceId = newestUid;
        }
        if (maxId == null || oldestUid < maxId) {
            maxId = oldestUid - 1;
        }
    }

    protected abstract void populateTimeline();

    protected abstract void refreshTimeline();
}
