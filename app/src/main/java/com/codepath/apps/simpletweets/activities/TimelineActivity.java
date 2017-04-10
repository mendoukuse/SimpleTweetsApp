package com.codepath.apps.simpletweets.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.TwitterApplication;
import com.codepath.apps.simpletweets.TwitterClient;
import com.codepath.apps.simpletweets.adapters.TweetsAdapter;
import com.codepath.apps.simpletweets.fragments.ComposeTweetDialogFragment;
import com.codepath.apps.simpletweets.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class TimelineActivity extends AppCompatActivity implements ComposeTweetDialogFragment.ComposeTweetDialogListener {

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeContainer;

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsAdapter adapter;
    private RecyclerView rvTweets;
    private EndlessRecyclerViewScrollListener scrollListener;

    private User appUser;

    // For pagination
    int currentPage;
    Long maxId;
    Long sinceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        initToolbar();
        initSwipeContainer();
        initRecyclerView();

        // set pagination params;
        currentPage = 0;
        sinceId = null;
        maxId = null;

        client = TwitterApplication.getRestClient(); // singleton
        getUserInformation();
        populateTimeline();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initSwipeContainer() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTimeline();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    private void initRecyclerView() {
        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);

        rvTweets.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTweets.setLayoutManager(layoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                currentPage = page;
                populateTimeline();
            }
        };

        rvTweets.addOnScrollListener(scrollListener);
    }

    private void getUserInformation() {
        client.verifyCredentials(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                appUser = User.fromJSON(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("DEBUG", json.toString());

                ArrayList<Tweet> parsedTweets = Tweet.fromJSONArray(json);

                if (parsedTweets.size() > 0) {
                    updateParams(parsedTweets.get(0).getUid(),
                                 parsedTweets.get(parsedTweets.size() - 1).getUid());
                    // handle notification of adapter in adapter
                    adapter.addAll(parsedTweets);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                Snackbar.make(swipeContainer, R.string.populate_timeline_error, Snackbar.LENGTH_LONG).show();
            }
        }, maxId, null);
    }

    private void updateParams(Long newestUid, Long oldestUid) {
        // Tweets are returned in order of recency (most recent is first)
        if (sinceId == null || newestUid > sinceId) {
            sinceId = newestUid;
        }
        if (maxId == null || oldestUid < maxId) {
            maxId = oldestUid - 1;
        }
    }

    private void clearParams() {
        sinceId = null;
        maxId = null;
    }

    public void refreshTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("DEBUG", json.toString());
                ArrayList<Tweet> parsedTweets = Tweet.fromJSONArray(json);

                if (parsedTweets.size() > 0) {
                    adapter.clear();
                    clearParams();
                    updateParams(parsedTweets.get(0).getUid(),
                            parsedTweets.get(parsedTweets.size() - 1).getUid());
                    adapter.addAll(parsedTweets);
                }
                swipeContainer.setRefreshing(false);
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
                Snackbar.make(swipeContainer, R.string.timeline_refresh_error, Snackbar.LENGTH_LONG).show();
                swipeContainer.setRefreshing(false);
            }
        }, null, null);
    }

    private void postTweet(String status) {
        client.postNewTweet(status, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                Tweet tweet = Tweet.fromJSON(response);

                if (tweet != null) {
                    tweets.add(0, tweet);
                    // TODO (do we want to fetch all the new tweets at this time?)
                    adapter.notifyItemInserted(0);
                    rvTweets.scrollToPosition(0);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                Snackbar.make(swipeContainer, R.string.post_tweet_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onComposeAction(View view) {
        showComposeTweetDialogFragment();
    }

    private void showComposeTweetDialogFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetDialogFragment tweetDialogFragment = ComposeTweetDialogFragment.newInstance(appUser);
        tweetDialogFragment.show(fm, "compose_tweet_fragment");
    }

    @Override
    public void onFinishedComposeDialog(String tweet) {
        postTweet(tweet);
    }

}
