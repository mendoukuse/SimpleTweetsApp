package com.codepath.apps.simpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.TwitterApplication;
import com.codepath.apps.simpletweets.TwitterClient;
import com.codepath.apps.simpletweets.adapters.TweetsPagerAdapter;
import com.codepath.apps.simpletweets.fragments.ComposeTweetDialogFragment;
import com.codepath.apps.simpletweets.fragments.TweetDetailsDialogFragment;
import com.codepath.apps.simpletweets.fragments.TweetsListFragment;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity
        implements ComposeTweetDialogFragment.ComposeTweetDialogListener, TweetsListFragment.OnItemClickedListener {
    TwitterClient client;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;
    User appUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

         ButterKnife.bind(this);

        // setup view pager
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        getApplicationUser();

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

    private void getApplicationUser() {
        client = TwitterApplication.getRestClient();
        client.getUserInfo(new JsonHttpResponseHandler() {
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

    public void onProfileView(MenuItem mi) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public void onComposeAction(MenuItem mi) {
        showComposeTweetDialogFragment();
    }

    private void showComposeTweetDialogFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetDialogFragment tweetDialogFragment = ComposeTweetDialogFragment.newInstance(appUser);
        tweetDialogFragment.show(fm, "fragment_compose_tweet");
    }

    public void onFinishedComposeDialog(String tweet) {
        postTweet(tweet);
    }

    public void postTweet(String status) {
        client.postNewTweet(status, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                Tweet tweet = Tweet.fromJSON(response);

                if (tweet != null) {
                    ((TweetsPagerAdapter) viewPager.getAdapter()).handleNewTweet(tweet);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                handlePostFailure();
            }
        });
    }

    public void handlePostFailure() {
        Snackbar.make(viewPager, R.string.post_tweet_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onTweetClicked(Tweet tweet) {
        FragmentManager fm = getSupportFragmentManager();
        TweetDetailsDialogFragment frag = TweetDetailsDialogFragment.newInstance(tweet);
        frag.show(fm, "fragment_tweet_details");
    }
}
