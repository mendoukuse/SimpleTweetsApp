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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class UserHeaderFragment extends Fragment {
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvTagline) TextView tvTagline;
    @BindView(R.id.tvFollowers) TextView tvFollowers;
    @BindView(R.id.tvFollowing) TextView tvFollowing;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;

    private Unbinder unbinder;

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
        unbinder = ButterKnife.bind(this, view);

        User user = Parcels.unwrap(getArguments().getParcelable("user"));

        tvName.setText(user.getName());
        tvTagline.setText(user.getDescription());
        tvFollowers.setText(String.format(getString(R.string.followers_count), user.getFollowersCount()));
        tvFollowing.setText(String.format(getString(R.string.friends_count), user.getFriendsCount()));

        Picasso.with(getContext()).load(user.getProfileImageUrl())
                .transform(new RoundedCornersTransformation(10, 10)).fit().centerCrop().into(ivProfileImage);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
