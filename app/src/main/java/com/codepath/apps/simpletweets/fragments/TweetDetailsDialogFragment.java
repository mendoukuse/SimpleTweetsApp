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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class TweetDetailsDialogFragment extends DialogFragment {
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvScreenName) TextView tvScreenName;
    @BindView(R.id.tvDate) TextView tvRelativeDate;
    @BindView(R.id.tvBody) TextView tvBody;
    @BindView(R.id.tvReplies) TextView tvReplies;
    @BindView(R.id.tvRetweets) TextView tvRetweets;
    @BindView(R.id.tvFavorites) TextView tvFavorites;

    private Unbinder unbinder;

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
        View view = inflater.inflate(R.layout.fragment_tweet_details, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Tweet tweet = Parcels.unwrap(getArguments().getParcelable("tweet"));

        tvUserName.setText(tweet.getUser().getName());
        tvScreenName.setText("@" + tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvRelativeDate.setText(tweet.getFormattedTime());
        tvFavorites.setText(String.valueOf(tweet.getFavoriteCount()));
        tvRetweets.setText(String.valueOf(tweet.getRetweetCount()));

        Picasso.with(view.getContext()).load(tweet.getUser().getProfileImageUrl())
                .transform(new RoundedCornersTransformation(10, 10)).fit().centerCrop().into(ivProfileImage);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void cancel() {
        dismiss();
    }
}
