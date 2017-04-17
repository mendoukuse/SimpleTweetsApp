package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.simpletweets.TwitterApplication;
import com.codepath.apps.simpletweets.TwitterClient;
import com.codepath.apps.simpletweets.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.apps.simpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by christine_nguyen on 4/17/17.
 */

public class FollowingFragment extends UserListFragment {
    TwitterClient client;
    private String screenName;

    public static FollowingFragment newInstance(String screenName) {
        FollowingFragment frag = new FollowingFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenName = getArguments().getString("screen_name");
        client = TwitterApplication.getRestClient();
        fetchUserList(page);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        // set up scroll listener here
        setScrollListener(new EndlessRecyclerViewScrollListener(getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                fetchUserList(page);
            }
        });

        return v;
    }

    @Override
    public void fetchUserList(final int page) {
        if (nextCursor != null && nextCursor == 0) {
            // no more results if next_cursor = 0
            Log.d("DEBUG", "No more friends");
            return;
        }
        client.getUserFriends(screenName, nextCursor, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d("DEBUG", json.toString());

                try {
                    ArrayList<User> parsedUsers = User.fromJSONArray(json.getJSONArray("users"));

                    if (parsedUsers.size() > 0) {
                        updatePaginationParams(page,
                                json.getLong("previous_cursor"),
                                json.getLong("next_cursor"));
                        addAll(parsedUsers);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", responseString);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}
