package com.example.qualitysleep.UI;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qualitysleep.Model.Recording;
import com.example.qualitysleep.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Recording> mData;

    RecyclerViewAdapter(List<Recording> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final Recording recording = mData.get(position);
        viewHolder.tvID.setText(String.valueOf(recording.getId()));
        viewHolder.tvLength.setText(String.valueOf(recording.getMLength()));
        viewHolder.tvDateTime.setText(recording.getMDate());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void swapList(List<Recording> newList) {
        mData = newList;
        if (newList != null) {
            this.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvID;
        TextView tvLength;
        TextView tvDateTime;

        public ViewHolder(View v) {
            super(v);

            tvID = itemView.findViewById(R.id.text_id);
            tvLength = itemView.findViewById(R.id.text_length);
            tvDateTime = itemView.findViewById(R.id.text_date_time);
        }
    }
}
