package com.codepath.apps.simpletweets.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.apps.simpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.simpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.simpletweets.models.Tweet;

public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {
    private String tabTitles[] = { "Home", "Mentions" };

    public TweetsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeTimelineFragment();
        } else if (position == 1) {
            return new MentionsTimelineFragment();
        } else {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    public void handleNewTweet(Tweet tweet) {
        HomeTimelineFragment frag = (HomeTimelineFragment) getRegisteredFragment(0);
        frag.handleNewTweet(tweet);
    }
}
