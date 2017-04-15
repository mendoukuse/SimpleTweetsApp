package com.codepath.apps.simpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.adapters.TweetsPagerAdapter;

//implements ComposeTweetDialogFragment.ComposeTweetDialogListener

public class TimelineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // setup view pager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);

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

    public void onProfileView(MenuItem mi) {
        // launch profile view
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public void onComposeAction(MenuItem mi) {

    }

    // move out
//    public void onComposeAction(View view) {
//        showComposeTweetDialogFragment();
//    }

//    private void showComposeTweetDialogFragment() {
//        FragmentManager fm = getSupportFragmentManager();
//        User appUser = ((HomeTimelineFragment) fm.findFragmentById(R.id.fragment_timeline))
//                .getAppUser();
//        ComposeTweetDialogFragment tweetDialogFragment = ComposeTweetDialogFragment.newInstance(appUser);
//        tweetDialogFragment.show(fm, "compose_tweet_fragment");
//    }

//    public void onFinishedComposeDialog(String tweet) {
//        HomeTimelineFragment fragment = (HomeTimelineFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.fragment_timeline);
//        fragment.postTweet(tweet);
//    }

}
