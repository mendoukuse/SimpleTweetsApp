package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.User;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class UserHeaderFragment extends Fragment {
    TextView tvName;
    TextView tvTagline;
    TextView tvFollowers;
    TextView tvFollowing;
    ImageView ivProfileImage;

    public static UserHeaderFragment newInstance(User user) {
        UserHeaderFragment userHeader = new UserHeaderFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", Parcels.wrap(user));
        userHeader.setArguments(args);
        return userHeader;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_header, container, false);

        User user = Parcels.unwrap(getArguments().getParcelable("user"));

        tvName = (TextView) view.findViewById(R.id.tvName);
        tvTagline = (TextView) view.findViewById(R.id.tvTagline);
        tvFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        tvFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);

        tvName.setText(user.getName());
        tvTagline.setText(user.getDescription());
        tvFollowers.setText(String.format(getString(R.string.followers_count), user.getFollowersCount()));
        tvFollowing.setText(String.format(getString(R.string.friends_count), user.getFriendsCount()));

        Picasso.with(getContext()).load(user.getProfileImageUrl())
                .transform(new RoundedCornersTransformation(10, 10)).fit().centerCrop().into(ivProfileImage);

        return view;
    }
}
