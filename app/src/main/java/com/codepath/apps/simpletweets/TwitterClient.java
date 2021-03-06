package com.codepath.apps.simpletweets;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = "VRmaZTRsC79RnO8w86541hv4O";
	public static final String REST_CONSUMER_SECRET = "MHQtdxsbxHzyHdhnJxKhib6chLHOXj7yPMfls3kYZDEzSLj3EH";
	public static final String REST_CALLBACK_URL = "oauth://cptweetstream";

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */

	// DEFINE METHODS for different API endpoints here
	public void getHomeTimeline(AsyncHttpResponseHandler handler, Long maxId, Long sinceId) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();

        params.put("count", 25);

        if (sinceId != null) {
            params.put("since_id", sinceId);
        } else {
            params.put("since_id", 1);
        }

        if (maxId != null) {
            params.put("max_id", maxId);
        }

        getClient().get(apiUrl, params, handler);
	}

	public void getUserInfo(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        getClient().get(apiUrl, null, handler);
    }

	public void postNewTweet(String status, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", status);
        getClient().post(apiUrl, params, handler);
    }

    public void getMentionsTimeline(AsyncHttpResponseHandler handler, Long maxId) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();

        params.put("count", 25);

        if (maxId != null) {
            params.put("max_id", maxId);
        }

        getClient().get(apiUrl, params, handler);
    }

    public void getUserTimeline(String screenname, Long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();

        params.put("count", 25);

        if (screenname != null) {
            params.put("screen_name", screenname);
        }
        if (maxId != null) {
            params.put("max_id", maxId);
        }

        getClient().get(apiUrl, params, handler);
    }

    public void getUserFollowers(String screenName, Long nextCursor, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("followers/list.json");
        RequestParams params = new RequestParams();

        params.put("count", 20);

        if (screenName != null) {
            params.put("screen_name", screenName);
        }
        if (nextCursor == null) {
            params.put("cursor", -1);
        } else {
            params.put("cursor", nextCursor);
        }

        getClient().get(apiUrl, params, handler);
    }

    public void getUserFriends(String screenName, Long nextCursor, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("friends/list.json");
        RequestParams params = new RequestParams();

        params.put("count", 20);

        if (screenName != null) {
            params.put("screen_name", screenName);
        }
        if (nextCursor == null) {
            params.put("cursor", -1);
        } else {
            params.put("cursor", nextCursor);
        }

        getClient().get(apiUrl, params, handler);
    }
}
