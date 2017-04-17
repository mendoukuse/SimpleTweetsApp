package com.codepath.apps.simpletweets.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by christine_nguyen on 4/17/17.
 */

public class UsersAdapter extends
        RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @BindView(R.id.tvUserName) TextView tvUserName;
        @BindView(R.id.tvScreenName) TextView tvScreenName;
        @BindView(R.id.tvTagline) TextView tvTagline;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private ArrayList<User> users;
    private Context context;

    public UsersAdapter(Context context, ArrayList<User> users) {
        this.users = users;
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View userRowView = inflater.inflate(R.layout.item_user_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(userRowView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User user = users.get(position);

        ImageView imageView = holder.ivProfileImage;
        TextView username = holder.tvUserName;
        TextView screenname = holder.tvScreenName;
        TextView tagline = holder.tvTagline;

        username.setText(user.getName());
        screenname.setText('@' + user.getScreenName());
        tagline.setText(user.getDescription());

        imageView.setImageResource(android.R.color.transparent);
        Picasso.with(context).load(user.getProfileImageUrl())
                .transform(new RoundedCornersTransformation(10, 10)).fit().centerCrop().into(imageView);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<User> parsedUsers) {
        int start = users.size();
        users.addAll(parsedUsers);
        notifyItemRangeInserted(start, parsedUsers.size());
    }

}
