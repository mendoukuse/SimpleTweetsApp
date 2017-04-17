package com.codepath.apps.simpletweets.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.activities.ProfileActivity;
import com.codepath.apps.simpletweets.adapters.UsersAdapter;
import com.codepath.apps.simpletweets.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.apps.simpletweets.listeners.ItemClickSupport;
import com.codepath.apps.simpletweets.models.User;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class UserListFragment extends Fragment {
    private ArrayList<User> users;
    private UsersAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private LinearLayoutManager layoutManager;
    private Unbinder unbinder;

    // for pagination
    int page = 0;
    Long prevCursor;
    Long nextCursor;

    @BindView(R.id.rvUsers) RecyclerView rvUsers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        users = new ArrayList<>();
        adapter = new UsersAdapter(getActivity(), users);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRecyclerView(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initRecyclerView(View v) {
        rvUsers.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvUsers.setLayoutManager(layoutManager);

        ItemClickSupport.addTo(rvUsers).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                User user = users.get(position);

                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("user", Parcels.wrap(user));
                i.putExtra("screen_name", user.getScreenName());
                getContext().startActivity(i);
            }
        });
    }

    public void setScrollListener(EndlessRecyclerViewScrollListener listener) {
        scrollListener = listener;
        rvUsers.addOnScrollListener(scrollListener);
    }

    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void addAll(ArrayList<User> users) {
        adapter.addAll(users);
    }

    public void clearAll() {
        adapter.clear();
    }

    public void updatePaginationParams(int page, Long prevCursor, Long nextCursor) {
        this.page = page;
        this.prevCursor = prevCursor;
        this.nextCursor = nextCursor;
    }

    public void resetPaginationParams() {
        this.page = 0;
        this.prevCursor = null;
        this.nextCursor = null;
    }

    public abstract void fetchUserList(int page);

}
