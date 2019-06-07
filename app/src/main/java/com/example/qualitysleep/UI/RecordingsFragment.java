package com.example.qualitysleep.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qualitysleep.Model.MainViewModel;
import com.example.qualitysleep.Model.Recording;
import com.example.qualitysleep.R;

import java.util.ArrayList;
import java.util.List;

public class RecordingsFragment extends Fragment {
    private static final String TAG = "RecordingsFragment";

    RecyclerViewAdapter mAdapter;
    RecyclerView mRecyclerView;
    private List<Recording> mRecordings;
    private MainViewModel mMainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recordings_fragment, container, false);

        // Links recyclerView to layout
        mRecyclerView = v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecordings = new ArrayList<>();

        // Update the UI when the game list is changed
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.getRecordings().observe(this, new Observer<List<Recording>>() {
            @Override
            public void onChanged(@Nullable List<Recording> games) {
                mRecordings = games;
                updateUI();
            }
        });
        return v;
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new RecyclerViewAdapter(mRecordings);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.swapList(mRecordings);
        }
    }

}
