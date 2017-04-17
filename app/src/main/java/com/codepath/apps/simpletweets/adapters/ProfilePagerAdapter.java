package com.codepath.apps.simpletweets.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.simpletweets.fragments.FollowersFragment;
import com.codepath.apps.simpletweets.fragments.FollowingFragment;
import com.codepath.apps.simpletweets.fragments.UserTimelineFragment;

/**
 * Created by christine_nguyen on 4/16/17.
 */

public class ProfilePagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = { "Tweets", "Following", "Followers" };
    private String currentUserScreenname;

    public ProfilePagerAdapter(FragmentManager fm, String screenname) {
        super(fm);
        currentUserScreenname = screenname;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return UserTimelineFragment.newInstance(currentUserScreenname);
        } else if (position == 1) {
            return FollowingFragment.newInstance(currentUserScreenname);
        } else if (position == 2) {
            return FollowersFragment.newInstance(currentUserScreenname);
        } else {
            return null;
        }
    }
}
