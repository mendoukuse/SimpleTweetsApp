package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by christine_nguyen on 4/15/17.
 */

public class TweetDetailsDialogFragment extends DialogFragment {
    private ImageView ivProfileImage;
    private TextView tvUserName;
    private TextView tvScreenName;
    private TextView tvRelativeDate;
    private TextView tvBody;
    private TextView tvReplies;
    private TextView tvRetweets;
    private TextView tvFavorites;

    public TweetDetailsDialogFragment() {}

    public static TweetDetailsDialogFragment newInstance(Tweet tweet) {
        TweetDetailsDialogFragment frag = new TweetDetailsDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable("tweet", Parcels.wrap(tweet));
        frag.setArguments(args);

        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tweet_details, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Tweet tweet = Parcels.unwrap(getArguments().getParcelable("tweet"));

        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        tvScreenName = (TextView) view.findViewById(R.id.tvScreenName);
        tvRelativeDate = (TextView) view.findViewById(R.id.tvDate);
        tvBody = (TextView) view.findViewById(R.id.tvBody);
        tvReplies = (TextView) view.findViewById(R.id.tvReplies);
        tvRetweets = (TextView) view.findViewById(R.id.tvRetweets);
        tvFavorites = (TextView) view.findViewById(R.id.tvFavorites);

        tvUserName.setText(tweet.getUser().getName());
        tvScreenName.setText("@" + tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvRelativeDate.setText(tweet.getRelativeTimeAgo());
        tvReplies.setText(tweet.getReplyCount());
        tvFavorites.setText(tweet.getFavoriteCount());
        tvRetweets.setText(tweet.getRetweetCount());

        Picasso.with(view.getContext()).load(tweet.getUser().getProfileImageUrl())
                .transform(new RoundedCornersTransformation(10, 10)).fit().centerCrop().into(ivProfileImage);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    public void cancel() {
        dismiss();
    }
}
