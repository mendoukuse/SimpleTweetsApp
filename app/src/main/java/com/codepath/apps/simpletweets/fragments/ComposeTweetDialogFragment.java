package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.User;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by christine_nguyen on 4/8/17.
 */

public class ComposeTweetDialogFragment extends DialogFragment {

    private ImageView ivProfileImage;
    private EditText etTweet;
    private TextView tvUserName;
    private TextView tvScreenName;
    private TextView tvCounter;
    private Button btnClose;
    private Button btnTweet;
    private Integer countLimit = 140;

    public ComposeTweetDialogFragment() {}

    public interface ComposeTweetDialogListener {
        void onFinishedComposeDialog(String status);
    }

    public static ComposeTweetDialogFragment newInstance(User user) {
        ComposeTweetDialogFragment frag = new ComposeTweetDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable("user", Parcels.wrap(user));
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User user = Parcels.unwrap(getArguments().getParcelable("user"));

        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        etTweet = (EditText) view.findViewById(R.id.etTweet);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        tvScreenName = (TextView) view.findViewById(R.id.tvScreenName);
        tvCounter = (TextView) view.findViewById(R.id.tvCounter);

        btnClose = (Button) view.findViewById(R.id.btnClose);
        btnTweet = (Button) view.findViewById(R.id.btnTweet);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBackResult();
            }
        });

        tvUserName.setText(user.getName());
        tvScreenName.setText("@" + user.getScreenName());
        tvCounter.setText(countLimit.toString());

        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Integer limit = 140;
                Integer charCountLeft = limit - s.toString().length();
                if (charCountLeft <= 0) {
                    tvCounter.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    btnTweet.setClickable(false);
                } else {
                    tvCounter.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    btnTweet.setClickable(true);
                }
                tvCounter.setText(charCountLeft.toString());
            }
        });

        Picasso.with(view.getContext()).load(user.getProfileImageUrl())
                .transform(new RoundedCornersTransformation(10, 10)).fit().centerCrop().into(ivProfileImage);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    public void cancel() {
        dismiss();
    }

    public void sendBackResult() {
        ComposeTweetDialogListener listener = (ComposeTweetDialogListener) getActivity();
        String status = etTweet.getText().toString();
        listener.onFinishedComposeDialog(status);
        dismiss();
    }
}
