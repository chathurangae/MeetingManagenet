package com.meeting.management.view.Events;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meeting.management.R;
import com.meeting.management.model.Meetings;

import java.util.List;

public class UpcomingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Meetings> meetingsList;

    public UpcomingAdapter(List<Meetings> meetingsList) {

        this.meetingsList = meetingsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_upcoming, parent, false);

        return new MeetingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MeetingViewHolder meetingHolder = (MeetingViewHolder) holder;
        meetingHolder.purpose.setText(meetingsList.get(position).getPurpose());


    }


    class MeetingViewHolder extends RecyclerView.ViewHolder {

        TextView purpose;


        MeetingViewHolder(View view) {
            super(view);
            purpose = view.findViewById(R.id.text_purpose);


        }
    }


    @Override
    public int getItemCount() {
        return meetingsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
