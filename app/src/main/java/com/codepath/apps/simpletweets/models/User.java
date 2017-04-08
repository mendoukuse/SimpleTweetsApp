package com.codepath.apps.simpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by christine_nguyen on 4/8/17.
 */

public class User {
    private long uid;
    private String name;
    private String screenName;
    private String profileImageUrl;

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


    public static User fromJSON(JSONObject json) {
        User user = new User();

        try {
            user.uid = json.getLong("id");
            user.name = json.getString("name");
            user.profileImageUrl = json.getString("profile_image_url");
            user.screenName = json.getString("screen_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}
