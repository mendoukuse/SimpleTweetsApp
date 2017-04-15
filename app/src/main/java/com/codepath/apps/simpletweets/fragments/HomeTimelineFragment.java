package com.codepath.apps.simpletweets.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.simpletweets.TwitterApplication;
import com.codepath.apps.simpletweets.TwitterClient;
import com.codepath.apps.simpletweets.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.apps.simpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class HomeTimelineFragment extends TweetsListFragment {
    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateTimeline();
        resetPaginationParams();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        // Setup refresh listener which triggers new data loading
        setSwipeListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTimeline();
            }
        });

        // Setup scroll listener for infinite scroll
        setScrollListener(new EndlessRecyclerViewScrollListener(getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                populateTimeline();
            }
        });

        return v;
    }

    protected void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("DEBUG", json.toString());

                ArrayList<Tweet> parsedTweets = Tweet.fromJSONArray(json);

                if (parsedTweets.size() > 0) {
                    updatePaginationParams(
                            parsedTweets.get(0).getUid(),
                            parsedTweets.get(parsedTweets.size() - 1).getUid());

                    addAll(parsedTweets);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                handleLoadFailure();
            }
        }, maxId, null);
    }

    protected void refreshTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("DEBUG", json.toString());
                ArrayList<Tweet> parsedTweets = Tweet.fromJSONArray(json);
                if (parsedTweets.size() > 0) {
                    updatePaginationParams(
                            parsedTweets.get(0).getUid(),
                            parsedTweets.get(parsedTweets.size() - 1).getUid());
                }
                handleRefreshSuccess(parsedTweets);
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
                handleRefreshFailure();
            }
        }, null, null);
    }
}
