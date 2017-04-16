package com.codepath.apps.simpletweets.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.activities.ProfileActivity;
import com.codepath.apps.simpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by christine_nguyen on 4/8/17.
 */

public class TweetsAdapter extends
        RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProfileImage;
        private TextView tvUserName;
        private TextView tvScreenName;
        private TextView tvRelativeDate;
        private TextView tvBody;

        public ViewHolder(View itemView) {
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
            tvRelativeDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
        }
    }

    private ArrayList<Tweet> tweets;
    private Context context;

    public TweetsAdapter(Context context, ArrayList<Tweet> tweets) {
        this.tweets = tweets;
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);

        ViewHolder viewHolder = new ViewHolder(tweetView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Tweet tweet = tweets.get(position);

        ImageView imageView = holder.ivProfileImage;
        TextView username = holder.tvUserName;
        TextView screenname = holder.tvScreenName;
        TextView relativeDate = holder.tvRelativeDate;
        TextView bodyText = holder.tvBody;

        username.setText(tweet.getUser().getName());
        screenname.setText('@' + tweet.getUser().getScreenName());
        relativeDate.setText(tweet.getRelativeTimeAgo());
        bodyText.setText(tweet.getBody());

        imageView.setImageResource(android.R.color.transparent);
        Picasso.with(context).load(tweet.getUser().getProfileImageUrl())
                .transform(new RoundedCornersTransformation(10, 10)).fit().centerCrop().into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ProfileActivity.class);
                i.putExtra("user", Parcels.wrap(tweet.getUser()));
                i.putExtra("screen_name", tweet.getUser().getScreenName());
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(ArrayList<Tweet> parsedTweets) {
        int start = tweets.size();
        tweets.addAll(parsedTweets);
        notifyItemRangeInserted(start, parsedTweets.size());
    }

}
