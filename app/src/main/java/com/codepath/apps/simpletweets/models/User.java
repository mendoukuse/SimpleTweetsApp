package com.codepath.apps.simpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by christine_nguyen on 4/8/17.
 */

@Parcel
public class User {
    long uid;
    String name;
    String screenName;
    String profileImageUrl;
    String description;
    int followersCount;
    int friendsCount;

    public User() {}

    public long getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public static User fromJSON(JSONObject json) {
        User user = new User();

        try {
            user.uid = json.getLong("id");
            user.name = json.getString("name");
            user.profileImageUrl = json.getString("profile_image_url");
            user.screenName = json.getString("screen_name");
            user.description = json.getString("description");
            user.followersCount = json.getInt("followers_count");
            user.friendsCount = json.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static ArrayList<User> fromJSONArray(JSONArray json) {
        ArrayList<User> users = new ArrayList<>();

        for (int i = 0; i < json.length(); i++) {
            try {
                JSONObject userJSON = json.getJSONObject(i);
                User user = User.fromJSON(userJSON);

                if (user != null) {
                    users.add(user);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return users;
    }
}
